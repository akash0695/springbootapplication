# Admin System with User Approval

## Overview
This system includes an admin dashboard for managing user registrations and form submissions. New users need admin approval before they can access the system.

## Features

### 🔐 User Registration & Approval
- **Registration**: Users can register with company details
- **Admin Approval**: New users require admin approval
- **User Management**: Admins can approve/reject users
- **Email Field**: Added email field to registration form

### 📊 Admin Dashboard
- **User Management**: View pending and approved users
- **Form Submissions**: View all contact form submissions
- **Statistics**: Dashboard with user and submission stats
- **Approval Actions**: Approve or reject pending users

## Database Schema Updates

### User Table (`user`)
```sql
CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    company_name VARCHAR(255),
    phone VARCHAR(255),
    is_admin BOOLEAN DEFAULT FALSE,
    is_approved BOOLEAN DEFAULT FALSE
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

## Admin Setup

### 1. Create Admin User via Database
```sql
-- Connect to MySQL and run these commands
USE akash;

-- Insert admin user (password: admin123)
INSERT INTO user (username, password, email, company_name, phone, is_admin, is_approved) 
VALUES ('admin', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6', 'admin@akashcreations.com', 'Akash Creations', '1234567890', true, true);
```

### 2. Admin Login Credentials
- **Username**: `admin`
- **Password**: `admin123`
- **Access**: Admin dashboard at `/admin-dashboard.html`

## API Endpoints

### Admin Endpoints (Require Authentication)
- **GET** `/admin/pending-users` - Get all pending users
- **GET** `/admin/approved-users` - Get all approved users
- **POST** `/admin/approve-user/{userId}` - Approve a user
- **POST** `/admin/reject-user/{userId}` - Reject a user
- **GET** `/admin/form-submissions` - Get all form submissions
- **GET** `/admin/dashboard-stats` - Get admin dashboard statistics

### Public Endpoints
- **POST** `/register` - User registration
- **POST** `/authenticate` - User login
- **POST** `/form` - Contact form submission

## User Flow

### 1. User Registration
```
User fills registration form
    ↓
Data saved to database (is_approved = false)
    ↓
User cannot login until approved
```

### 2. Admin Approval Process
```
Admin logs in to admin dashboard
    ↓
Views pending users
    ↓
Approves or rejects users
    ↓
Approved users can now login
```

### 3. User Login After Approval
```
User tries to login
    ↓
System checks is_approved flag
    ↓
If approved: Access granted
    ↓
If not approved: Access denied
```

## Pages Created

### 1. Admin Dashboard (`/admin-dashboard.html`)
- **URL**: `http://localhost:8181/admin-dashboard.html`
- **Features**:
  - Statistics overview
  - Pending users management
  - Approved users list
  - Form submissions view
  - Approve/reject actions

### 2. Updated Registration (`/register.html`)
- **URL**: `http://localhost:8181/register.html`
- **New Fields**:
  - Email address
  - Company name
  - Phone number
- **Behavior**: Creates user with `is_approved = false`

## Security Configuration

### Public Access
- `/register.html` - User registration
- `/login.html` - User login
- `/form` - Contact form submission
- `/admin-dashboard.html` - Admin dashboard (frontend auth check)

### Protected Access
- `/admin/**` - All admin API endpoints
- `/form-submissions` - Form submissions API

## Admin Dashboard Features

### Statistics Cards
- **Total Users**: All registered users
- **Pending Users**: Users awaiting approval
- **Approved Users**: Users who can login
- **Form Submissions**: Total contact form submissions

### User Management
- **Pending Users Tab**: View and manage pending registrations
- **Approved Users Tab**: View all approved users
- **Form Submissions Tab**: View all contact form submissions

### Actions
- **Approve User**: Approves user registration
- **Reject User**: Deletes user registration
- **Refresh Data**: Updates dashboard data

## Database Queries

### Create Admin User
```sql
INSERT INTO user (username, password, email, company_name, phone, is_admin, is_approved) 
VALUES ('admin', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6', 'admin@akashcreations.com', 'Akash Creations', '1234567890', true, true);
```

### View Pending Users
```sql
SELECT * FROM user WHERE is_approved = false;
```

### View Approved Users
```sql
SELECT * FROM user WHERE is_approved = true;
```

### Approve a User
```sql
UPDATE user SET is_approved = true WHERE id = ?;
```

### Delete a User (Reject)
```sql
DELETE FROM user WHERE id = ?;
```

## Testing

### 1. Create Admin User
```bash
# Connect to MySQL and run the admin creation query
mysql -u root -p akash
```

### 2. Test User Registration
1. Go to `http://localhost:8181/register.html`
2. Fill in all fields (including email, company, phone)
3. Submit registration
4. User should be created with `is_approved = false`

### 3. Test Admin Login
1. Go to `http://localhost:8181/login.html`
2. Login with admin credentials
3. Access admin dashboard

### 4. Test User Approval
1. Login as admin
2. Go to admin dashboard
3. View pending users
4. Approve a user
5. User should now be able to login

### 5. Test User Login After Approval
1. Login with approved user credentials
2. Should access regular dashboard

## File Structure

```
src/main/java/com/springboot/springboot/
├── controller/
│   ├── AdminController.java          # Admin API endpoints
│   ├── JwtAuthenticationController.java
│   └── MyController.java
├── model/
│   ├── DAOUser.java                 # Updated with admin fields
│   ├── UserDTO.java                 # Updated with additional fields
│   └── ...
├── services/
│   ├── JwtUserDetailsService.java   # Updated with admin methods
│   └── ...
├── dao/
│   ├── UserDao.java                 # Updated with admin queries
│   └── ...
└── entities/
    └── FormData.java

src/main/resources/static/
├── admin-dashboard.html             # Admin dashboard
├── register.html                    # Updated registration
├── login.html                       # Updated login
└── ...
```

## Security Notes

- **Admin Creation**: Admin users must be created directly in database
- **User Approval**: New users cannot login until approved by admin
- **JWT Authentication**: All admin endpoints require valid JWT token
- **Frontend Security**: Admin dashboard checks authentication in frontend

## Future Enhancements

1. **Email Notifications**
   - Notify admin of new registrations
   - Notify users when approved/rejected

2. **User Roles**
   - Multiple admin levels
   - User permissions

3. **Audit Logging**
   - Track approval/rejection actions
   - User activity logging

4. **Advanced Admin Features**
   - Bulk user operations
   - User search and filtering
   - Export user data

## Troubleshooting

### Common Issues

1. **Admin Login Fails**
   - Verify admin user exists in database
   - Check password hash is correct
   - Ensure `is_admin = true` and `is_approved = true`

2. **User Cannot Login After Registration**
   - Check if user is approved (`is_approved = true`)
   - Verify user exists in database

3. **Admin Dashboard Not Loading**
   - Check JWT token is valid
   - Verify admin endpoints are accessible
   - Check browser console for errors

4. **Registration Form Not Saving All Fields**
   - Verify UserDTO has all required fields
   - Check JwtUserDetailsService.save() method
   - Ensure database table has all columns

The admin system is now complete with user approval functionality and comprehensive admin dashboard! 