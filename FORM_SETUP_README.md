# Contact Form Setup Instructions

## Overview
The contact form in the static HTML page has been configured to submit data to the Spring Boot backend and store it in the MySQL database.

## Changes Made

### 1. HTML Form Updates (`src/main/resources/static/index.html`)
- Added `id="contactForm"` to the form element
- Added `id="submitBtn"` to the submit button
- Added `required="required"` to the message textarea
- Removed the old `target="_Blank"` attribute

### 2. JavaScript Form Handler (`src/main/resources/static/js/form-handler.js`)
- Created new file to handle form submission
- Validates form data before submission
- Prevents double submission
- Shows success/error messages
- Resets form after successful submission

### 3. Security Configuration (`src/main/java/com/springboot/springboot/config/WebSecurityConfig.java`)
- Added `/form` endpoint to permitted URLs
- Added static resources (`/css/**`, `/js/**`, `/images/**`, `/fonts/**`) to permitted URLs
- Added root path `/` and `/index.html` to permitted URLs

### 4. Backend API (Already Exists)
- **Controller**: `MyController.java` - handles POST requests to `/form`
- **Service**: `FormService.java` and `FormServiceImpl.java` - business logic
- **DAO**: `FormDao.java` - data access layer
- **Entity**: `FormData.java` - database entity

## Database Configuration
- **Database**: MySQL database named `akash`
- **Table**: `form_data` (auto-created by JPA)
- **Fields**: 
  - `id` (Long, Auto-generated Primary Key)
  - `name` (String, Required)
  - `email` (String, Required)
  - `message` (String, Required)

## How to Test

### 1. Start the Application
```bash
# Make sure MySQL is running and database 'akash' exists
# Start the Spring Boot application
./mvnw spring-boot:run
```

### 2. Access the Website
- Open browser and go to: `http://localhost:8181`
- Navigate to the Contact section (section 5)

### 3. Test Form Submission
1. Fill in the contact form:
   - **Name**: Your name
   - **Email**: Your email address
   - **Message**: Your message
2. Click "Submit" button
3. You should see a success message: "Thank you for your interest! We will connect with you shortly."

### 4. Verify Database Storage
```sql
-- Connect to MySQL and check the data
USE akash;
SELECT * FROM form_data;
```

### 5. View All Submissions via API
- Access: `http://localhost:8181/form-submissions`
- Returns JSON array of all form submissions

## API Endpoint Details

**POST** `/form`
- **Content-Type**: `multipart/form-data`
- **Parameters**:
  - `name` (String, required)
  - `email` (String, required)
  - `message` (String, required)
- **Response**: Success message or error

**GET** `/form-submissions`
- **Response**: JSON array of all form submissions
- **Use Case**: View all contact form submissions

## Troubleshooting

### Common Issues:

1. **JAVA_HOME not set**
   - Set JAVA_HOME environment variable to your JDK installation
   - Example: `set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_xxx`

2. **MySQL Connection Issues**
   - Ensure MySQL is running
   - Verify database `akash` exists
   - Check username/password in `application.properties`

3. **Form not submitting**
   - Check browser console for JavaScript errors
   - Verify all required fields are filled
   - Check network tab for API call status

4. **Security Issues**
   - Ensure `/form` endpoint is in permitted URLs
   - Check if CSRF is properly disabled

## Files Modified/Created:
- `src/main/resources/static/index.html` - Updated form
- `src/main/resources/static/js/form-handler.js` - New file
- `src/main/java/com/springboot/springboot/config/WebSecurityConfig.java` - Security config
- `src/main/java/com/springboot/springboot/entities/FormData.java` - Added ID field
- `src/main/java/com/springboot/springboot/dao/FormDao.java` - Updated ID type
- `src/main/java/com/springboot/springboot/services/FormService.java` - Added getAllSubmissions method
- `src/main/java/com/springboot/springboot/services/FormServiceImpl.java` - Implemented getAllSubmissions
- `src/main/java/com/springboot/springboot/controller/MyController.java` - Added GET endpoint

## Existing Files (No Changes Needed):
- `src/main/java/com/springboot/springboot/controller/MyController.java` - API endpoint
- `src/main/java/com/springboot/springboot/services/FormService.java` - Service interface
- `src/main/java/com/springboot/springboot/services/FormServiceImpl.java` - Service implementation
- `src/main/java/com/springboot/springboot/dao/FormDao.java` - Data access
- `src/main/java/com/springboot/springboot/entities/FormData.java` - Entity class 