# Healthcare Insurance Mobile App

A React Native mobile application for managing healthcare insurance claims built with Expo.

## Features

- 🔐 **Multi-role Authentication** - Supports Patient, Doctor, Admin, and Insurance Provider roles
- 📱 **Role-based Dashboards** - Customized experience for each user type
- 🏥 **Claims Management** - File, track, and manage insurance claims
- 💳 **Insurance Integration** - Connect with insurance providers
- 👨‍⚕️ **Doctor Support** - Healthcare providers can support patient claims
- 📊 **Admin Portal** - Comprehensive management tools for administrators

## Prerequisites

Before you begin, ensure you have the following installed:
- [Node.js](https://nodejs.org/) (v14 or higher)
- [npm](https://www.npmjs.com/) or [yarn](https://yarnpkg.com/)
- [Expo CLI](https://docs.expo.dev/get-started/installation/)

For mobile development:
- **Android**: [Android Studio](https://developer.android.com/studio) with Android SDK
- **iOS**: [Xcode](https://developer.apple.com/xcode/) (Mac only)

## Installation

1. **Clone the repository**
   ```bash
   cd frontend
   ```

2. **Install dependencies**
   ```bash
   npm install
   # or
   yarn install
   ```

3. **Configure API endpoint**
   - The app automatically detects your platform and uses the correct API URL
   - For Android Emulator: Uses `10.0.2.2:8080`
   - For iOS Simulator: Uses `localhost:8080`
   - For Physical Device: Update `PHYSICAL_DEVICE_API_URL` in `src/config/environment.js` with your computer's IP address

   To find your IP address:
   - **Windows**: Run `ipconfig` in Command Prompt
   - **Mac/Linux**: Run `ifconfig` or `ip addr` in Terminal

## Running the App

1. **Start the development server**
   ```bash
   npm start
   # or
   yarn start
   ```

2. **Run on Android**
   ```bash
   npm run android
   # or
   yarn android
   ```

3. **Run on iOS** (Mac only)
   ```bash
   npm run ios
   # or
   yarn ios
   ```

4. **Run on Web**
   ```bash
   npm run web
   # or
   yarn web
   ```

## Backend Setup

Ensure the backend services are running:

1. **Start Service Discovery** (Port 8761)
   ```bash
   cd backend/service-discovery
   mvn spring-boot:run
   ```

2. **Start User Service** (Port 8080)
   ```bash
   cd backend/user-service
   mvn spring-boot:run
   ```

3. **Start API Gateway** (Port 8060)
   ```bash
   cd backend/api-gateway
   mvn spring-boot:run
   ```

## User Roles

The app supports four different user roles:

### 1. Patient
- File insurance claims
- Track claim status
- View medical records
- Manage insurance policy details

### 2. Doctor
- View patient list
- Support patient claims
- Manage appointments
- Access medical records

### 3. Admin
- Manage users and roles
- View all claims
- Generate reports
- System configuration

### 4. Insurance Provider
- Review pending claims
- Approve/reject claims
- Manage policies
- View claim statistics

## Project Structure

```
frontend/
├── src/
│   ├── components/        # Reusable UI components
│   │   ├── Button.js
│   │   ├── Input.js
│   │   └── RoleCard.js
│   ├── config/           # Configuration files
│   │   └── environment.js
│   ├── contexts/         # React Context providers
│   │   └── AuthContext.js
│   ├── navigation/       # Navigation setup
│   │   ├── AuthNavigator.js
│   │   ├── AppNavigator.js
│   │   └── RootNavigator.js
│   ├── screens/          # Screen components
│   │   ├── Auth/
│   │   │   ├── LoginScreen.js
│   │   │   ├── SignupScreen.js
│   │   │   └── WelcomeScreen.js
│   │   └── Dashboard/
│   │       ├── PatientDashboard.js
│   │       ├── DoctorDashboard.js
│   │       ├── AdminDashboard.js
│   │       └── InsuranceProviderDashboard.js
│   ├── services/         # API services
│   │   └── authService.js
│   └── utils/            # Utility functions
│       ├── constants.js
│       └── validation.js
├── App.js               # App entry point
├── package.json
└── README.md
```

## Authentication

The app uses JWT-based authentication with:
- **Access Token**: Short-lived token for API requests
- **Refresh Token**: Long-lived token for obtaining new access tokens
- **Automatic Token Refresh**: Handles token expiration seamlessly

## API Integration

The app connects to the backend User Service at:
- **Android Emulator**: `http://10.0.2.2:8080/api`
- **iOS Simulator**: `http://localhost:8080/api`
- **Physical Device**: Configure your IP in `src/config/environment.js`

### API Endpoints

- `POST /auth/login` - User login
- `POST /auth/signup` - User registration
- `POST /auth/logout` - User logout
- `POST /auth/refresh` - Refresh access token
- `GET /auth/me` - Get current user

## Troubleshooting

### Cannot connect to backend

1. **Check backend is running**: Ensure all backend services are started
2. **Check IP address**: For physical devices, verify your computer's IP address is correct
3. **Check firewall**: Ensure your firewall allows connections on port 8080
4. **Network connection**: Ensure your device is on the same network as your computer

### App crashes on startup

1. **Clear cache**: Run `expo start -c`
2. **Reinstall dependencies**: Delete `node_modules` and run `npm install` again
3. **Check React Native compatibility**: Ensure all packages are compatible

### Login/Signup fails

1. **Check API URL**: Verify the correct API URL is configured
2. **Check backend logs**: Look for errors in the backend console
3. **Verify network request**: Use React Native Debugger to inspect API calls

## Development

### Adding new screens

1. Create screen component in `src/screens/`
2. Add to navigation in `src/navigation/`
3. Update routes as needed

### Adding new API calls

1. Add endpoint to `src/config/environment.js`
2. Create service method in `src/services/`
3. Use in components via hooks

## Testing

```bash
# Run tests (when implemented)
npm test
```

## Building for Production

### Android
```bash
expo build:android
```

### iOS
```bash
expo build:ios
```

## Technologies Used

- **React Native** - Mobile framework
- **Expo** - Development platform
- **React Navigation** - Navigation library
- **Formik** - Form management
- **Yup** - Schema validation
- **Axios** - HTTP client
- **AsyncStorage** - Local storage

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License.

## Support

For support, please contact the development team or open an issue in the repository.

## Acknowledgments

- Healthcare provider integration
- Insurance policy management
- Secure authentication system
