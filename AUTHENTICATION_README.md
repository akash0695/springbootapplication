# Authentication & Dashboard System

## Overview
This system provides user registration, login, and a dashboard for managing contact form submissions. Company owners can register accounts and access an internal dashboard to view all form submissions.

## Features

### 🔐 Authentication System
- **User Registration**: Company owners can create accounts with detailed information
- **User Login**: Secure login with JWT token authentication
- **Dashboard Access**: Protected dashboard for viewing form submissions
- **Session Management**: JWT-based stateless authentication

### 📊 Dashboard Features
- **Statistics Overview**: Total submissions, today's submissions, unique emails
- **Submission List**: View all contact form submissions
- **Search Functionality**: Search submissions by name or email
- **Real-time Updates**: Refresh data with one click
- **Responsive Design**: Works on desktop and mobile

## Pages Created

### 1. Registration Page (`/register.html`)
- **URL**: `http://localhost:8181/register.html`
- **Purpose**: Company owners can create new accounts
- **Fields**:
  - Username (unique)
  - Email Address
  - Password (with confirmation)
  - Company Name
  - Phone Number

### 2. Login Page (`/login.html`)
- **URL**: `http://localhost:8181/login.html`
- **Purpose**: Authenticated users can login
- **Features**:
  - Password visibility toggle
  - Form validation
  - JWT token storage
  - Auto-redirect to dashboard

### 3. Dashboard (`/dashboard.html`)
- **URL**: `http://localhost:8181/dashboard.html`
- **Purpose**: View and manage form submissions
- **Features**:
  - Statistics cards
  - Submission list with search
  - User session management
  - Logout functionality

## Database Schema

### User Table (`user`)
```sql
CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    company_name VARCHAR(255),
    phone VARCHAR(255)
);
```

### Form Data Table (`form_data`)
```sql
CREATE TABLE form_data (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    message TEXT NOT NULL
);
```

## API Endpoints

### Authentication
- **POST** `/register` - Create new user account
- **POST** `/authenticate` - Login and get JWT token

### Form Management
- **POST** `/form` - Submit contact form (public)
- **GET** `/form-submissions` - Get all submissions (authenticated)

## Security Configuration

### Public Access (No Authentication Required)
- `/` - Main website
- `/index.html` - Main website
- `/login.html` - Login page
- `/register.html` - Registration page
- `/dashboard.html` - Dashboard page (redirects to login if not authenticated)
- `/form` - Form submission endpoint
- `/form-submissions` - Form submissions API
- Static resources (`/css/**`, `/js/**`, `/images/**`, `/fonts/**`)

### Protected Access (Authentication Required)
- All other endpoints require valid JWT token

## How to Use

### 1. Register a New Account
1. Go to `http://localhost:8181/register.html`
2. Fill in all required fields:
   - Username (must be unique)
   - Email address
   - Password (minimum 6 characters)
   - Confirm password
   - Company name
   - Phone number
3. Click "Create Account"
4. You'll be redirected to login page

### 2. Login to Dashboard
1. Go to `http://localhost:8181/login.html`
2. Enter your username and password
3. Click "Login"
4. You'll be redirected to the dashboard

### 3. Access Dashboard
1. After successful login, you'll see the dashboard
2. View statistics at the top:
   - Total submissions
   - Today's submissions
   - Unique email addresses
3. Browse all form submissions below
4. Use the search box to filter submissions
5. Click "Refresh" to update data
6. Click "Logout" to end session

### 4. Test Form Submissions
1. Go to main website: `http://localhost:8181`
2. Scroll to Contact section
3. Fill and submit the contact form
4. Check dashboard to see the new submission

## Technical Implementation

### Frontend
- **jQuery**: AJAX requests and DOM manipulation
- **Bootstrap**: Responsive UI components
- **Bootstrap Icons**: Icon library
- **Local Storage**: JWT token storage

### Backend
- **Spring Security**: Authentication and authorization
- **JWT**: Token-based authentication
- **JPA/Hibernate**: Database operations
- **MySQL**: Database storage

### Security Features
- **Password Hashing**: BCrypt password encryption
- **JWT Tokens**: Stateless authentication
- **CORS Configuration**: Cross-origin resource sharing
- **CSRF Protection**: Disabled for API endpoints

## File Structure

```
src/main/resources/static/
├── index.html              # Main website with contact form
├── register.html           # User registration page
├── login.html             # User login page
├── dashboard.html         # Admin dashboard
├── view-submissions.html  # Public submissions view
├── test-form.html         # Test form page
└── js/
    ├── form-handler.js    # Contact form submission
    └── custom.js          # General JavaScript

src/main/java/com/springboot/springboot/
├── controller/
│   ├── JwtAuthenticationController.java  # Auth endpoints
│   └── MyController.java                # Form endpoints
├── model/
│   ├── DAOUser.java       # User entity
│   ├── UserDTO.java       # User DTO
│   ├── JwtRequest.java    # Login request
│   └── JwtResponse.java   # Login response
├── entities/
│   └── FormData.java      # Form submission entity
├── services/
│   ├── JwtUserDetailsService.java  # User service
│   ├── FormService.java            # Form service
│   └── FormServiceImpl.java        # Form service impl
├── dao/
│   ├── UserDao.java       # User data access
│   └── FormDao.java       # Form data access
└── config/
    ├── WebSecurityConfig.java      # Security config
    ├── JwtAuthenticationEntryPoint.java
    ├── JwtRequestFilter.java
    └── JwtTokenUtil.java
```

## Testing

### 1. Test Registration
```bash
# Register a new user
curl -X POST http://localhost:8181/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "password123",
    "email": "admin@company.com",
    "companyName": "Test Company",
    "phone": "1234567890"
  }'
```

### 2. Test Login
```bash
# Login and get token
curl -X POST http://localhost:8181/authenticate \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "password123"
  }'
```

### 3. Test Form Submission
```bash
# Submit contact form
curl -X POST http://localhost:8181/form \
  -F "name=John Doe" \
  -F "email=john@example.com" \
  -F "message=Test message"
```

### 4. Test Dashboard Access
```bash
# Get form submissions (with token)
curl -X GET http://localhost:8181/form-submissions \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## Troubleshooting

### Common Issues

1. **JAVA_HOME not set**
   - Set JAVA_HOME environment variable
   - Example: `set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_xxx`

2. **MySQL Connection Issues**
   - Ensure MySQL is running
   - Verify database `akash` exists
   - Check credentials in `application.properties`

3. **Registration Fails**
   - Check if username already exists
   - Ensure all required fields are filled
   - Verify password meets minimum requirements

4. **Login Fails**
   - Verify username and password
   - Check if user exists in database
   - Ensure password was hashed correctly

5. **Dashboard Not Loading**
   - Check if JWT token is valid
   - Verify token is stored in localStorage
   - Check browser console for errors

6. **Form Submissions Not Showing**
   - Verify database connection
   - Check if form submissions are being saved
   - Ensure `/form-submissions` endpoint is accessible

## Security Notes

- JWT tokens are stored in browser localStorage
- Passwords are hashed using BCrypt
- All sensitive endpoints require authentication
- CORS is configured for cross-origin requests
- CSRF protection is disabled for API endpoints

## Future Enhancements

1. **User Management**
   - User profile editing
   - Password reset functionality
   - User roles and permissions

2. **Dashboard Features**
   - Export submissions to CSV/PDF
   - Email notifications for new submissions
   - Advanced filtering and sorting

3. **Security Improvements**
   - Token refresh mechanism
   - Rate limiting
   - Audit logging

4. **UI Enhancements**
   - Dark mode toggle
   - Real-time notifications
   - Mobile app version 