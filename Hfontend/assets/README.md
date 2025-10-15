# Assets Directory

This directory contains all static assets for the Healthcare Insurance App.

## Required Assets

### App Icons
- **icon.png** - 1024x1024px - Main app icon
- **adaptive-icon.png** - 1024x1024px - Android adaptive icon
- **favicon.png** - 48x48px - Web favicon

### Splash Screen
- **splash.png** - 1242x2436px - App splash screen
  - Background:rgb(0, 110, 255) (Primary blue)
  - Should include app logo/icon

## Current Status

✅ **Assets are Optional**

The app.json has been configured to work without assets for development. You can add them later:

1. Create a simple app icon (medical/healthcare themed)
2. Create a splash screen with the app logo
3. Generate adaptive icon for Android

## Generating Assets

You can use these tools to generate app assets:

### Option 1: Online Tools
- [App Icon Generator](https://www.appicon.co/)
- [Expo Asset Generator](https://github.com/expo/expo-cli)

### Option 2: Expo CLI
```bash
# Generate icons automatically
expo optimize
```

### Option 3: Manual Creation
Use design tools like:
- Figma
- Adobe Illustrator
- Sketch
- Canva

## Asset Guidelines

### App Icon
- Simple and recognizable
- Works well at small sizes
- Healthcare/medical theme
- Colors: Use primary blue (#007AFF)
- Include: Medical cross, heartbeat, or health symbol

### Splash Screen
- Clean and professional
- Brand colors
- App name/logo centered
- Quick loading experience

## Temporary Solution

For development, you can:
1. Use simple colored squares as placeholders
2. Generate basic icons using online tools
3. Use Expo's default assets temporarily

## Asset Checklist

- [ ] icon.png (1024x1024)
- [ ] adaptive-icon.png (1024x1024)
- [ ] favicon.png (48x48)
- [ ] splash.png (1242x2436)
- [ ] Logo variations (light/dark)
- [ ] Onboarding images (future)
- [ ] Empty state illustrations (future)

---

**Note:** Assets should be optimized for mobile to reduce app size.

