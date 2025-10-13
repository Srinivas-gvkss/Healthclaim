# 📥 Installation Instructions

## Prerequisites

Make sure you have these installed:

1. **Node.js** (v14 or higher)
   - Download: https://nodejs.org/
   - Check version: `node --version`

2. **npm** (comes with Node.js)
   - Check version: `npm --version`

3. **Expo CLI**
   ```bash
   npm install -g expo-cli
   ```

4. **Expo Go App** (on your phone)
   - iOS: [App Store](https://apps.apple.com/app/expo-go/id982107779)
   - Android: [Google Play](https://play.google.com/store/apps/details?id=host.exp.exponent)

---

## Step 1: Navigate to Project

```bash
cd Hfontend
```

---

## Step 2: Install Dependencies

```bash
npm install
```

This will install all required packages:
- React Native
- React Navigation
- Formik & Yup (for forms)
- Axios (for API calls)
- AsyncStorage (for data storage)
- And more...

**Expected time:** 2-3 minutes

---

## Step 3: Start the App

```bash
npm start
```

Or alternatively:
```bash
expo start
```

This will:
1. Start the Metro bundler
2. Open Expo DevTools in your browser
3. Show a QR code

---

## Step 4: Run on Your Device

### Option A: Physical Device (Easiest)

**For iOS:**
1. Open **Camera** app
2. Point at the QR code
3. Tap the notification
4. App opens in Expo Go

**For Android:**
1. Open **Expo Go** app
2. Tap "Scan QR Code"
3. Point at the QR code
4. App loads automatically

### Option B: Emulator/Simulator

**Android Emulator:**
```bash
# In the terminal where expo is running
Press 'a'
```

**iOS Simulator (Mac only):**
```bash
# In the terminal where expo is running
Press 'i'
```

**Web Browser:**
```bash
# In the terminal where expo is running
Press 'w'
```

---

## 🎉 Success!

You should now see:
1. **Welcome Screen** with Healthcare app branding
2. **Login** button
3. **Create Account** button

---

## 🧪 Testing

### Test Login
```
Email: demo@healthcare.com
Password: Demo@123
```

### Test Signup
1. Click "Create Account"
2. Fill in your details
3. Select a role (Patient, Doctor, Admin, or Insurance Provider)
4. Complete registration

---

## 📁 Project Structure

After installation, your project looks like this:

```
Hfontend/
├── node_modules/          ← Installed packages (2-3 GB)
├── src/
│   ├── components/        ← Reusable UI components
│   ├── screens/           ← App screens
│   ├── navigation/        ← Navigation setup
│   ├── services/          ← API services
│   └── utils/             ← Helper functions
├── App.js                 ← Main entry point
├── package.json           ← Dependencies list
└── Documentation files
```

---

## 🔧 Troubleshooting

### Problem: "expo: command not found"
**Solution:**
```bash
npm install -g expo-cli
```

### Problem: "Cannot find module"
**Solution:**
```bash
rm -rf node_modules
npm install
```

### Problem: Metro bundler issues
**Solution:**
```bash
expo start --clear
# or
npm start -- --clear
```

### Problem: Port already in use
**Solution:**
```bash
# Kill the process on port 19000
npx kill-port 19000

# Then start again
npm start
```

### Problem: Expo Go app won't connect
**Solution:**
- Ensure phone and computer are on same WiFi network
- Disable VPN if active
- Try using "Tunnel" connection instead of "LAN" in Expo DevTools

### Problem: iOS Simulator won't open
**Solution:**
- Install Xcode from Mac App Store (Mac only)
- Open Xcode → Preferences → Locations → Set Command Line Tools

### Problem: Android Emulator won't open
**Solution:**
- Install Android Studio
- Create an AVD (Android Virtual Device)
- Start the emulator before running `npm start`

---

## 📱 Device Setup

### For iOS (iPhone/iPad)
1. Install "Expo Go" from App Store
2. Connect to same WiFi as your computer
3. Open Camera app
4. Scan QR code

### For Android
1. Install "Expo Go" from Google Play
2. Connect to same WiFi as your computer
3. Open Expo Go app
4. Tap "Scan QR Code"
5. Scan the QR code

---

## 🚀 Development Commands

```bash
# Start development server
npm start

# Start with cache clearing
npm start -- --clear

# Run on Android
npm run android

# Run on iOS
npm run ios

# Run on web
npm run web
```

---

## 🔄 Update Dependencies

If you need to update packages:

```bash
# Update all dependencies
npm update

# Update specific package
npm update react-native

# Check for outdated packages
npm outdated
```

---

## 📦 Package Installation Issues

If you encounter issues during `npm install`:

### Windows Users
```bash
# Run as administrator
npm install --legacy-peer-deps
```

### Mac/Linux Users
```bash
# Use sudo if needed
sudo npm install
```

### Slow Installation?
```bash
# Try yarn instead of npm
npm install -g yarn
yarn install
```

---

## 🌐 Network Configuration

### Using LAN (default)
- Fastest option
- Requires same WiFi network

### Using Tunnel
- Works across different networks
- Slower but more reliable
- Use if LAN doesn't work

**To switch:**
In Expo DevTools (browser), change connection type from "LAN" to "Tunnel"

---

## ✅ Installation Checklist

Before you start:

- [ ] Node.js installed (v14+)
- [ ] npm installed
- [ ] Expo CLI installed globally
- [ ] Expo Go app on phone
- [ ] Computer and phone on same WiFi
- [ ] Navigate to Hfontend folder
- [ ] Run `npm install`
- [ ] Run `npm start`
- [ ] Scan QR code
- [ ] App loads successfully

---

## 📞 Still Having Issues?

1. ✅ Check error message in terminal
2. ✅ Try clearing cache: `expo start -c`
3. ✅ Reinstall node_modules
4. ✅ Restart computer
5. ✅ Check firewall settings
6. ✅ Review troubleshooting section above
7. ✅ Search error on Google
8. ✅ Check Expo documentation

---

## 📚 Additional Resources

- **Expo Setup:** https://docs.expo.dev/get-started/installation/
- **React Native Setup:** https://reactnative.dev/docs/environment-setup
- **Troubleshooting:** https://docs.expo.dev/troubleshooting/
- **Community:** https://forums.expo.dev/

---

## 🎯 What's Next?

After successful installation:

1. ✅ Test the Welcome screen
2. ✅ Test Login functionality
3. ✅ Test Signup flow
4. ✅ Explore the code
5. ✅ Read documentation files
6. ✅ Start building new features!

---

**Installation Time:** ~5-10 minutes  
**Difficulty:** Easy  
**Support:** See SETUP_GUIDE.md for more details

---

**Ready to build amazing healthcare features! 🏥💙**

