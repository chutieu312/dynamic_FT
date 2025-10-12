springboot_java_t01-orders_s1

Lightweight Spring Boot orders demo (in-memory storage). This README explains how to run the project either by (A) building with Docker (uses a Gradle builder image) or (B) installing Gradle locally via `run_setup.sh` (SDKMAN) which also creates the Gradle wrapper.

Prerequisites
- For option B (`run_setup.sh`): a POSIX shell (bash), and either `curl` or `wget`. On Windows use WSL, Git Bash, or other bash environment.
- For option A (Docker): Docker Engine installed and running.

Files added
- `build.gradle`, `settings.gradle` — Gradle build files
- `src/main/java/...` — Java sources (controller, service, DTOs, config)
- `src/main/resources/application.properties` — server.port 7011
- `Dockerfile` — multi-stage build using Gradle image
- `.dockerignore` — useful ignores
- `run_setup.sh` — bash script to install SDKMAN + Gradle and create Gradle wrapper

Option B — Install Gradle via SDKMAN and create wrapper (bash)

1) Run the setup script (idempotent):

```bash
cd C:/Users/canng/dynamic_FT/dynamic_BE/springboot_java_t01-orders_s1
bash run_setup.sh
```

Notes:
- The script installs SDKMAN into ~/.sdkman if missing, installs Gradle ${GRADLE_VERSION} and then runs `gradle wrapper` to create `gradlew`/`gradlew.bat`.
- On Windows prefer running the script inside WSL or Git Bash.

2) Start the app with the generated wrapper (Unix):

```bash
./gradlew bootRun
```

Or on Windows (cmd.exe / PowerShell):

```cmd
gradlew.bat bootRun
```

The app will listen on port 7011.

Option A — Build & run with Docker (no Gradle required locally)

From the project directory run:

```bash
docker build -t orders-inmem:latest .
docker run --rm -p 7011:7011 orders-inmem:latest
```

Endpoints
- Actuator health: http://localhost:7011/actuator/health
- Demo health: http://localhost:7011/api/v1/orders/health
- Orders API base: http://localhost:7011/api/v1/orders

Quick curl examples (POSIX shell):

List (empty):
```bash
curl http://localhost:7011/api/v1/orders
```

Create:
```bash
curl -X POST http://localhost:7011/api/v1/orders -H "Content-Type: application/json" -d '{"item":"Pen","price":2.50}'
```

Get one:
```bash
curl http://localhost:7011/api/v1/orders/1
```

Update:
```bash
curl -X PUT http://localhost:7011/api/v1/orders/1 -H "Content-Type: application/json" -d '{"status":"CONFIRMED","price":2.75}'
```

Delete:
```bash
curl -X DELETE http://localhost:7011/api/v1/orders/1
```

Next suggestions
- Add the Gradle wrapper files to the repo (the `run_setup.sh` will generate them for you locally).
- Add persistence (JPA + datasource) for production-like behavior.
- Add unit/integration tests and CI pipeline.

If you'd like, I can now:
- Add the Gradle wrapper files (I can provide a script to download the wrapper jar if you want it staged), or
- Create a small `docker-compose.yml` to run the app with other services (DB) for later migration.

Tell me which of these you'd like next.
