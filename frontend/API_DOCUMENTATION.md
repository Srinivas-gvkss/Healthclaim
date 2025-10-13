# API Documentation

## Base URL
```
https://your-api-endpoint.com/api
```

## Authentication

All authenticated endpoints require a JWT token in the header:
```
Authorization: Bearer <token>
```

---

## Endpoints

### Authentication

#### 1. User Registration (Signup)

**POST** `/auth/register`

Register a new user with role-specific information.

**Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phone": "1234567890",
  "password": "SecurePass@123",
  "role": "patient",
  
  // Conditional fields based on role:
  
  // For patients:
  "insurancePolicyNumber": "POL123456789",
  
  // For doctors:
  "medicalLicenseNumber": "MED987654321",
  "specialty": "Cardiology",
  
  // For admins: (no additional fields required)
  
  // For insurance providers:
  "companyName": "HealthCare Insurance Co."
}
```

**Success Response (201 Created):**
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
      "id": "user_123",
      "firstName": "John",
      "lastName": "Doe",
      "email": "john.doe@example.com",
      "phone": "1234567890",
      "role": "patient",
      "status": "active",
      "createdAt": "2024-01-15T10:30:00Z"
    }
  }
}
```

**Error Response (400 Bad Request):**
```json
{
  "success": false,
  "message": "Validation error",
  "errors": [
    {
      "field": "email",
      "message": "Email already exists"
    }
  ]
}
```

---

#### 2. User Login

**POST** `/auth/login`

Authenticate user and receive access token.

**Request Body:**
```json
{
  "email": "john.doe@example.com",
  "password": "SecurePass@123"
}
```

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
      "id": "user_123",
      "firstName": "John",
      "lastName": "Doe",
      "email": "john.doe@example.com",
      "role": "patient",
      "status": "active"
    }
  }
}
```

**Error Response (401 Unauthorized):**
```json
{
  "success": false,
  "message": "Invalid email or password"
}
```

---

#### 3. Forgot Password

**POST** `/auth/forgot-password`

Request password reset link/OTP.

**Request Body:**
```json
{
  "email": "john.doe@example.com"
}
```

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "Password reset instructions sent to your email"
}
```

---

#### 4. Reset Password

**POST** `/auth/reset-password`

Reset password using OTP or token.

**Request Body:**
```json
{
  "email": "john.doe@example.com",
  "otp": "123456",
  "newPassword": "NewSecurePass@123"
}
```

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "Password reset successful"
}
```

---

#### 5. Verify OTP

**POST** `/auth/verify-otp`

Verify OTP sent during registration or password reset.

**Request Body:**
```json
{
  "email": "john.doe@example.com",
  "otp": "123456"
}
```

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "OTP verified successfully"
}
```

---

#### 6. Refresh Token

**POST** `/auth/refresh-token`

Get new access token using refresh token.

**Request Body:**
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Success Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
}
```

---

#### 7. Logout

**POST** `/auth/logout`

Logout user and invalidate tokens.

**Headers:**
```
Authorization: Bearer <token>
```

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "Logged out successfully"
}
```

---

## User Profile

#### 8. Get Current User Profile

**GET** `/user/profile`

Get authenticated user's profile.

**Headers:**
```
Authorization: Bearer <token>
```

**Success Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "id": "user_123",
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "phone": "1234567890",
    "role": "patient",
    "status": "active",
    "profilePicture": "https://...",
    "createdAt": "2024-01-15T10:30:00Z",
    
    // Role-specific data
    "patientProfile": {
      "insurancePolicyNumber": "POL123456789",
      "dateOfBirth": "1990-05-15",
      "address": "123 Main St, City, State 12345"
    }
  }
}
```

---

## Error Codes

| Code | Description |
|------|-------------|
| 200  | Success |
| 201  | Created |
| 400  | Bad Request (validation error) |
| 401  | Unauthorized (invalid credentials) |
| 403  | Forbidden (insufficient permissions) |
| 404  | Not Found |
| 409  | Conflict (duplicate resource) |
| 422  | Unprocessable Entity |
| 500  | Internal Server Error |

---

## Validation Rules

### Email
- Must be valid email format
- Unique in system

### Password
- Minimum 8 characters
- Must contain:
  - At least one uppercase letter
  - At least one lowercase letter
  - At least one number
  - At least one special character (@$!%*?&)

### Phone
- 10 digits
- Numeric only

### Names
- Minimum 2 characters
- Letters and spaces only

### Medical License Number (for doctors)
- Required for doctor role
- Alphanumeric
- Unique

### Insurance Policy Number (for patients)
- Required for patient role
- Alphanumeric

---

## Rate Limiting

- **Authentication endpoints**: 5 requests per minute per IP
- **General endpoints**: 100 requests per minute per user
- **Upload endpoints**: 10 requests per minute per user

---

## Webhook Events (Future)

Events that can be subscribed to:

- `user.created` - New user registered
- `claim.submitted` - New claim submitted
- `claim.approved` - Claim approved
- `claim.rejected` - Claim rejected
- `payment.processed` - Payment completed

---

## Testing

### Base URL (Development)
```
https://dev-api.healthcare-app.com/api
```

### Base URL (Production)
```
https://api.healthcare-app.com/api
```

### Test Credentials
```json
{
  "email": "demo@healthcare.com",
  "password": "Demo@123"
}
```

---

## Notes

1. All timestamps are in ISO 8601 format (UTC)
2. All requests must include `Content-Type: application/json`
3. File uploads use `Content-Type: multipart/form-data`
4. Tokens expire after 24 hours
5. Refresh tokens expire after 30 days

---

**API Version:** v1.0  
**Last Updated:** October 2024

