# 🚀 Oppam Launcher - Quick Start Guide

## What You've Built

An Android launcher app for elderly users in Kerala with:
- ✅ Simple 3-button interface
- ✅ Malayalam voice assistance
- ✅ Simulated scam call protection
- ✅ Caregiver dashboard

---

## 📱 How to Run the App

### Option 1: Android Studio (Recommended)

1. **Open Android Studio**
   - Launch Android Studio
   - Click "Open" → Navigate to the `oppam` folder
   - Click OK

2. **Wait for Gradle Sync**
   - Android Studio will automatically sync Gradle dependencies
   - This may take 2-5 minutes on first run
   - Watch the progress bar at the bottom

3. **Run the App**
   - Click the green ▶️ Play button (or press Shift+F10)
   - Select a device:
     - Physical Android device (USB debugging enabled)
     - OR Android Emulator (API 26+)
   - Wait for the app to build and install

### Option 2: Command Line

```bash
cd oppam

# Windows
gradlew.bat assembleDebug

# Mac/Linux
./gradlew assembleDebug
```

APK location: `app/build/outputs/apk/debug/app-debug.apk`

---

## 🎯 Testing the Features

### 1. **Home Screen**
- You'll see 3 large buttons:
  - 📞 Call Family (coming soon)
  - ❤️ Health Log (coming soon)
  - 🛡️ Protection Mode ✅

### 2. **Voice Assistant**
- When you press any button, you'll hear voice feedback
- If Malayalam TTS is not available, it will fall back to English
- Example: Press Protection Mode → Hear "Protection Mode"

### 3. **Protection Mode Demo**
1. Tap "Protection Mode" on home screen
2. Tap "Simulate Scam Call"
3. See the fake incoming call screen
4. Tap "Detect as Scam"
5. Watch the RED ALERT animation
6. Hear Malayalam warning: "സൂക്ഷിക്കുക! വഞ്ചന വിളി"
7. See "Alert sent to caregiver" message

### 4. **Caregiver Dashboard**
1. From home screen, tap "Caregiver Dashboard" at the bottom
2. View:
   - Current risk level (HIGH/MEDIUM/LOW)
   - Last alert time
   - 7-day behavior graph (bars)
   - Recent alerts list

---

## ⚙️ Set App as Default Launcher (Optional)

To test it as an actual launcher:

1. Go to Android Settings
2. Navigate to: Apps → Default Apps → Home App
3. Select "Oppam"
4. Press the Home button → Should show Oppam launcher

To revert: Repeat steps and select your original launcher

---

## 🐛 Common Issues

### Issue: "Cannot resolve symbol R"
**Fix:** Build → Clean Project → Rebuild Project

### Issue: Gradle sync fails
**Fix:** 
- Check internet connection
- File → Invalidate Caches → Restart

### Issue: No voice output
**Fix:**
- Check device volume
- Go to Settings → Accessibility → Text-to-Speech
- Install Malayalam language pack if available

### Issue: App crashes on launch
**Fix:**
- Check Logcat in Android Studio
- Ensure minimum API level is 26 (Android 8.0)

---

## 📂 Key Files to Explore

| File | Purpose | Line Count |
|------|---------|------------|
| `MainActivity.kt` | Navigation setup | ~80 |
| `LauncherScreen.kt` | Main home screen | ~160 |
| `VoiceAssistant.kt` | TTS integration | ~130 |
| `RakshaShieldScreen.kt` | Protection mode | ~250 |
| `CaregiverDashboard.kt` | Dashboard UI | ~350 |

---

## 🎨 Customization Tips

### Change Colors:
Edit `app/src/main/java/com/oppam/launcher/ui/theme/Color.kt`

### Change Button Text:
Edit `app/src/main/res/values/strings.xml`

### Add More Voice Responses:
Edit `app/src/main/java/com/oppam/launcher/ui/voice/MalayalamResponses.kt`

### Adjust Font Sizes:
Edit `app/src/main/java/com/oppam/launcher/ui/theme/Type.kt`

---

## 📊 App Statistics

- **Total Kotlin Files**: 16
- **Total Lines of Code**: ~2,000+
- **Screens**: 3 main screens
- **Components**: 5 reusable components
- **Minimum Android Version**: API 26 (Android 8.0)
- **Target Android Version**: API 34 (Android 14)

---

## 🔄 Next Development Steps

### Immediate:
1. Test on real device with elderly user
2. Gather feedback on button size and contrast
3. Test Malayalam TTS on different devices

### Short-term:
1. Implement "Call Family" with speed dial
2. Add Health Log screen
3. Add settings screen
4. Create onboarding tutorial

### Long-term:
1. Add actual emergency contact integration
2. Implement medicine reminders
3. Add activity logging
4. Consider backend for real caregiver alerts

---

## 📱 Device Requirements

**Minimum:**
- Android 8.0 (API 26)
- 2GB RAM
- 100MB storage

**Recommended:**
- Android 10+ (API 29+)
- 4GB RAM
- Larger screen (6"+) for better visibility

---

## 🎓 Learning Resources

- [Jetpack Compose Tutorial](https://developer.android.com/jetpack/compose/tutorial)
- [Material Design 3](https://m3.material.io/)
- [Android Accessibility](https://developer.android.com/guide/topics/ui/accessibility)

---

## ⚠️ Important Disclaimers

1. **This is a DEMO app** - All features are simulated
2. **No real call interception** - Not possible without root access
3. **No backend** - All data is local and temporary
4. **No real alerts** - Caregiver notifications are simulated

---

## 🎉 You're Ready!

Your Oppam launcher is ready to run. Press that green play button and see it in action!

**Happy Coding! 🚀**
