# Healthcare Insurance App - Setup Guide

## Quick Start Guide

This guide will help you set up and run the Healthcare Insurance Claim app on your local machine.

## Prerequisites Checklist

- [ ] Node.js (v14 or higher) installed
- [ ] npm or yarn package manager
- [ ] Git installed
- [ ] Code editor (VS Code recommended)
- [ ] Expo CLI installed globally
- [ ] Expo Go app on your mobile device

## Step-by-Step Installation

### 1. Install Node.js
```bash
# Check if Node.js is installed
node --version

# If not installed, download from: https://nodejs.org/
```

### 2. Install Expo CLI
```bash
npm install -g expo-cli

# Verify installation
expo --version
```

### 3. Install Project Dependencies
```bash
# Navigate to project directory
cd Hfontend

# Install all dependencies
npm install

# Or if you use yarn
yarn install
```

### 4. Start the Development Server
```bash
# Start Expo
npm start

# Or
expo start
```

### 5. Run on Your Device

#### Option A: Physical Device (Recommended)
1. Install **Expo Go** app from:
   - iOS: App Store
   - Android: Google Play Store
2. Scan the QR code shown in terminal/browser
3. App will load on your device

#### Option B: Android Emulator
```bash
# Press 'a' in terminal after starting expo
# Or click "Run on Android device/emulator" in browser
```

#### Option C: iOS Simulator (Mac only)
```bash
# Press 'i' in terminal after starting expo
# Or click "Run on iOS simulator" in browser
```

## Project Configuration

### Environment Setup

Create a `.env` file in the root directory:
```env
API_BASE_URL=https://your-api-endpoint.com/api
NODE_ENV=development
```

### API Configuration

Update `src/services/authService.js`:
```javascript
const API_BASE_URL = process.env.API_BASE_URL || 'https://your-api-endpoint.com/api';
```

## Testing the App

### 1. Welcome Screen
- You should see a blue welcome screen with the app logo
- Two buttons: "Login" and "Create Account"

### 2. Login
- Use demo credentials:
  - Email: `demo@healthcare.com`
  - Password: `Demo@123`

### 3. Signup
- Click "Create Account"
- Fill in Step 1 (Basic Information)
- Click "Next"
- Select a role (Patient, Doctor, Admin, or Insurance Provider)
- Fill in role-specific fields
- Click "Sign Up"

## Common Issues & Solutions

### Issue 1: "Command not found: expo"
**Solution:**
```bash
npm install -g expo-cli
```

### Issue 2: "Module not found" errors
**Solution:**
```bash
# Clear cache and reinstall
rm -rf node_modules
npm install
```

### Issue 3: Metro bundler issues
**Solution:**
```bash
# Clear Expo cache
expo start -c
```

### Issue 4: iOS simulator not opening
**Solution:**
- Ensure Xcode is installed (Mac only)
- Open Xcode → Preferences → Locations → Command Line Tools is set

### Issue 5: Android emulator not detected
**Solution:**
- Ensure Android Studio is installed
- Create and start an AVD (Android Virtual Device)
- Enable Developer mode on your phone if using physical device

## Development Tips

### 1. Hot Reload
- Shake your device to open developer menu
- Enable "Fast Refresh" for instant updates

### 2. Debugging
```bash
# Open React Native debugger
# In app: Shake device → Debug Remote JS
```

### 3. View Logs
```bash
# Terminal will show logs automatically
# Or use React Native Debugger
```

### 4. Component Preview
- Install React Native Debugger for better development experience
- Use React DevTools for component inspection

## File Structure Overview

```
Hfontend/
├── App.js                     # Entry point
├── package.json              # Dependencies
├── app.json                  # App configuration
│
├── src/
│   ├── components/           # Reusable UI components
│   │   ├── Button.js
│   │   ├── Input.js
│   │   └── RoleCard.js
│   │
│   ├── screens/              # App screens
│   │   ├── WelcomeScreen.js
│   │   ├── LoginScreen.js
│   │   └── SignupScreen.js
│   │
│   ├── navigation/           # Navigation config
│   │   └── AuthNavigator.js
│   │
│   ├── services/             # API services
│   │   └── authService.js
│   │
│   └── utils/                # Helper functions
│       ├── constants.js
│       └── validation.js
│
└── assets/                   # Images, fonts, icons
```

## Next Steps

After successful setup:

1. ✅ Test all authentication screens
2. ✅ Verify form validation
3. ✅ Test role selection
4. ✅ Try different user roles
5. 📝 Connect to backend API (optional)
6. 🚀 Start building additional features

## Available Scripts

```bash
# Start development server
npm start

# Start on Android
npm run android

# Start on iOS
npm run ios

# Start web version
npm run web
```

## Backend Integration Checklist

When ready to connect to a real backend:

- [ ] Update API_BASE_URL in authService.js
- [ ] Uncomment actual API calls
- [ ] Comment out mock responses
- [ ] Test login endpoint
- [ ] Test signup endpoint
- [ ] Implement token storage
- [ ] Add error handling
- [ ] Test with real data

## Resources

- [React Native Documentation](https://reactnative.dev/)
- [Expo Documentation](https://docs.expo.dev/)
- [React Navigation](https://reactnavigation.org/)
- [Formik Documentation](https://formik.org/)
- [Yup Validation](https://github.com/jquense/yup)

## Support

If you encounter any issues:
1. Check the console for error messages
2. Review this setup guide
3. Check the README.md for additional information
4. Contact the development team

---

**Happy Coding! 🚀**

