<#
set-java21-and-wrapper.ps1

Safely set JAVA_HOME to Temurin JDK21, prepend its bin to the MACHINE Path
(backing up the old Path), remove Oracle javapath/jdk-18 entries, and create
the Gradle wrapper in the project. Run this script from an elevated PowerShell
(Admin) to apply system-level changes.

Usage (Admin PowerShell):
  cd C:\Users\canng\dynamic_FT\dynamic_BE\springboot_java_t01-orders_s1\scripts
  .\set-java21-and-wrapper.ps1

Optional parameters:
  -JdkPath  : path to JDK21 installation (default uses detected Adoptium path)
  -GradleVersion : Gradle wrapper version to create (default 8.6)
  -RunWrapperOnly : if set, only runs `gradle wrapper` and does not modify PATH/JAVA_HOME
#>
[CmdletBinding()]
param(
    [string]$JdkPath = 'C:\Program Files\Eclipse Adoptium\jdk-21.0.8.9-hotspot',
    [string]$GradleVersion = '8.6',
    [switch]$RunWrapperOnly
)

Set-StrictMode -Version Latest
Write-Host "==> Script started: set-java21-and-wrapper.ps1"

# Resolve script/project directories
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Definition
$projectDir = Resolve-Path (Join-Path $scriptDir '..')
Write-Host "Project directory: $projectDir"

if ($RunWrapperOnly) {
    Write-Host "RunWrapperOnly specified: will only attempt to create the Gradle wrapper (no env changes)."
} else {
    # Validate JDK path exists
    $javaExe = Join-Path $JdkPath 'bin\java.exe'
    if (-not (Test-Path $javaExe)) {
        Write-Warning "JDK not found at expected path: $JdkPath"
        Write-Host "Please install JDK21 to this path or re-run with -JdkPath pointing to your JDK21 installation."
        exit 1
    }

    # Backup current machine PATH
    try {
        $timestamp = Get-Date -Format "yyyyMMddHHmmss"
        $backupFile = Join-Path $env:USERPROFILE "path-machine-backup-$timestamp.txt"
        [Environment]::GetEnvironmentVariable('Path','Machine') | Out-File -FilePath $backupFile -Encoding utf8
        Write-Host "Machine PATH backed up to: $backupFile"
    } catch {
        Write-Warning "Failed to backup machine PATH: $_"
    }

    # Read and filter existing machine PATH entries
    $machinePath = [Environment]::GetEnvironmentVariable('Path','Machine')
    $parts = $machinePath -split ';' | Where-Object { $_ -ne '' }

    $filtered = $parts | Where-Object {
        ($_ -notmatch '\\Common Files\\Oracle\\Java\\javapath') -and
        ($_ -notmatch 'Program Files \(x86\)\\Common Files\\Oracle\\Java\\javapath') -and
        ($_ -notmatch 'jdk-18')
    }

    # Prepend new JDK bin
    $jdkBin = Join-Path $JdkPath 'bin'
    if ($filtered -contains $jdkBin) {
        # move to front
        $filtered = $filtered | Where-Object { $_ -ne $jdkBin }
    }
    $newParts = ,$jdkBin + $filtered
    $newPath = ($newParts -join ';')

    # Apply machine-level changes
    try {
        [Environment]::SetEnvironmentVariable('Path', $newPath, 'Machine')
        [Environment]::SetEnvironmentVariable('JAVA_HOME', $JdkPath, 'Machine')
        Write-Host "Set JAVA_HOME to: $JdkPath"
        Write-Host "Prepended $jdkBin to machine PATH and removed Oracle/jdk-18 entries (if present)."
    } catch {
        Write-Error "Failed to update machine environment variables: $_"
        exit 1
    }

    Write-Host "Note: You must open a NEW command prompt (cmd or PowerShell) to pick up the changes."
}

# Attempt to create the Gradle wrapper
# Use the gradle executable available in the environment (system-level gradle), else try Chocolatey path
Write-Host "Creating Gradle wrapper (version $GradleVersion) in project: $projectDir"
Push-Location $projectDir
try {
    $gradleCmd = Get-Command gradle -ErrorAction SilentlyContinue | Select-Object -First 1 -ExpandProperty Source -ErrorAction SilentlyContinue
    if (-not $gradleCmd) {
        $chocoGradle = 'C:\ProgramData\chocolatey\bin\gradle.bat'
        if (Test-Path $chocoGradle) { $gradleCmd = $chocoGradle }
    }

    if (-not $gradleCmd) {
        Write-Warning "No 'gradle' command found in this session. If you run this script from a shell where Gradle is available, the wrapper will be created."
        Write-Host "You can open a NEW elevated PowerShell (or cmd) and run this script again, or run 'gradle wrapper --gradle-version $GradleVersion' manually in the project folder."
        Pop-Location
        exit 0
    }

    Write-Host "Using gradle command: $gradleCmd"
    & $gradleCmd wrapper --gradle-version $GradleVersion
    if ($LASTEXITCODE -ne 0) {
        Write-Warning "'gradle wrapper' returned exit code $LASTEXITCODE"
    } else {
        Write-Host "Gradle wrapper created. You should now have 'gradlew' and 'gradlew.bat' in the project root."
    }
} catch {
    Write-Warning "Failed to run 'gradle wrapper': $_"
} finally {
    Pop-Location
}

Write-Host "Done. After you open a new shell, run 'gradlew.bat bootRun' (cmd/PowerShell) or './gradlew bootRun' (Git Bash) to start the app."

