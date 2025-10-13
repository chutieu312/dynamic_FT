# Orders Management React UI

A modern React-based UI for managing orders, built with Vite for fast development and optimal performance.

## 🚀 Features

- **Complete CRUD Operations**: Create, Read, Update, Delete orders
- **Real-time API Integration**: Connects to Spring Boot Orders API (port 7011)
- **Responsive Design**: Works seamlessly on desktop and mobile devices
- **Modern UI/UX**: Clean, professional interface with gradient themes
- **Form Validation**: Client-side validation for order data
- **Status Management**: Support for all order statuses (Pending, Confirmed, Shipped, Delivered, Cancelled)
- **Error Handling**: Comprehensive error handling with user-friendly messages
- **Loading States**: Visual feedback during API operations

## 🛠️ Technology Stack

- **Frontend Framework**: React 19.1.1
- **Build Tool**: Vite 7.1.7
- **Language**: JavaScript (ES6+)
- **Styling**: CSS3 with Flexbox/Grid
- **HTTP Client**: Fetch API
- **Development Server**: Vite Dev Server

## 🚀 Getting Started

### Prerequisites
- Node.js (v18 or higher)
- Spring Boot Orders API running on port 7011

### Quick Start

1. **Install dependencies:**
   ```bash
   npm install
   ```

2. **Start development server:**
   ```bash
   npm run dev
   ```

3. **Open in browser:**
   - Navigate to `http://localhost:4011`
   - Ensure Spring Boot API is running on `http://localhost:7011`

## 📡 API Integration

Connects to Spring Boot Orders API endpoints:
- `GET /api/v1/orders` - Fetch all orders
- `POST /api/v1/orders` - Create new order
- `PUT /api/v1/orders/{id}` - Update order
- `DELETE /api/v1/orders/{id}` - Delete order

## 🧪 Full-Stack Testing

1. **Start Spring Boot API** (separate terminal):
   ```bash
   cd ../../../dynamic_BE/springboot_java_t01-orders_s1
   gradle bootRun
   ```

2. **Start React UI** (this directory):
   ```bash
   npm run dev
   ```

3. **Test Integration**: Create, edit, and delete orders via the UI

## 📝 Configuration

- **Environment**: `.env.local` contains `VITE_API_URL=http://localhost:7011`
- **Vite Config**: `vite.config.js` sets dev server to port 4011
- **CORS**: Spring Boot API configured for localhost:4011

Part of the Dynamic FT Microservices Learning Project.
