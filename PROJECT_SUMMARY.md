# Oppam - Project Summary

## 🎯 Project Overview

**Oppam** is an AI-powered Android launcher designed specifically for elderly users in Kerala. The app provides:
- Simple, large-button interface
- Malayalam voice assistance
- Simulated scam call protection
- Caregiver monitoring dashboard

---

## 📁 Project Structure

```
oppam/
├── app/
│   ├── src/main/
│   │   ├── java/com/oppam/launcher/
│   │   │   ├── MainActivity.kt                 # Main entry point with navigation
│   │   │   ├── MainApplication.kt              # App initialization
│   │   │   │
│   │   │   ├── ui/
│   │   │   │   ├── theme/                      # App theming (colors, typography)
│   │   │   │   │   ├── Color.kt
│   │   │   │   │   ├── Theme.kt
│   │   │   │   │   └── Type.kt
│   │   │   │   │
│   │   │   │   ├── launcher/                   # Main launcher screen
│   │   │   │   │   ├── LauncherScreen.kt       # 3 large buttons UI
│   │   │   │   │   └── LauncherViewModel.kt    # Screen state management
│   │   │   │   │
│   │   │   │   ├── voice/                      # Voice assistant module
│   │   │   │   │   ├── VoiceAssistant.kt       # TextToSpeech integration
│   │   │   │   │   └── MalayalamResponses.kt   # Malayalam phrases
│   │   │   │   │
│   │   │   │   ├── protection/                 # Scam protection features
│   │   │   │   │   ├── RakshaShieldScreen.kt   # Protection mode UI
│   │   │   │   │   ├── FakeCallActivity.kt     # Simulated call activity
│   │   │   │   │   └── AlertOverlay.kt         # Scam alert overlay
│   │   │   │   │
│   │   │   │   ├── caregiver/                  # Dashboard for caregivers
│   │   │   │   │   └── CaregiverDashboard.kt   # Risk monitoring UI
│   │   │   │   │
│   │   │   │   └── components/                 # Reusable UI components
│   │   │   │       ├── LargeButton.kt          # Elderly-friendly button
│   │   │   │       └── ElderText.kt            # Large text components
│   │   │   │
│   │   │   └── data/                           # Data layer
│   │   │       ├── Models.kt                   # Data models (Alert, RiskLevel, etc.)
│   │   │       └── FakeBehaviorRepository.kt   # Simulated data source
│   │   │
│   │   ├── res/
│   │   │   ├── values/
│   │   │   │   ├── strings.xml                 # String resources
│   │   │   │   ├── colors.xml                  # Color definitions
│   │   │   │   └── themes.xml                  # Theme definitions
│   │   │   └── xml/
│   │   │       ├── backup_rules.xml
│   │   │       └── data_extraction_rules.xml
│   │   │
│   │   └── AndroidManifest.xml                 # App configuration
│   │
│   ├── build.gradle.kts                        # App-level build config
│   └── proguard-rules.pro
│
├── build.gradle.kts                            # Project-level build config
├── settings.gradle.kts                         # Gradle settings
├── gradle.properties                           # Gradle properties
├── .gitignore
└── README.md
```

---

## ✨ Features Implemented

### 1. **Launcher Screen** (`ui/launcher`)
- ✅ 3 large, high-contrast buttons
- ✅ Greeting message based on time of day
- ✅ Voice feedback when buttons are pressed
- ✅ Navigation to other screens
- ✅ Elderly-friendly design (48sp+ fonts)

### 2. **Voice Assistant** (`ui/voice`)
- ✅ Android TextToSpeech integration
- ✅ Malayalam language support
- ✅ Predefined responses (welcome, warnings, confirmations)
- ✅ Slower speech rate (0.8x) for elderly users
- ✅ Example phrase: "പേടിക്കണ്ട, ഞാൻ ഇവിടെ ഉണ്ട്" (Don't be afraid, I am here)

### 3. **Protection Mode** (`ui/protection`)
- ✅ Simulate scam call button
- ✅ Fake incoming call screen
- ✅ Scam detection with visual + audio alerts
- ✅ Red pulsing animation for alerts
- ✅ Malayalam warning messages
- ✅ "Alert sent to caregiver" simulation

### 4. **Caregiver Dashboard** (`ui/caregiver`)
- ✅ Risk level indicator (HIGH/MEDIUM/LOW)
- ✅ Recent alerts list
- ✅ 7-day behavior trend graph
- ✅ Last alert timestamp
- ✅ Color-coded severity badges

### 5. **Reusable Components** (`ui/components`)
- ✅ `LargeButton` - 120dp height buttons with icons
- ✅ `LargeOutlinedButton` - Alternative button style
- ✅ `ElderText` - Large text (32sp default)
- ✅ `ElderHeading` - Extra large headings (48sp)
- ✅ `AlertOverlay` - Animated warning overlay

### 6. **Data Layer** (`data`)
- ✅ Fake data repository for demo purposes
- ✅ Alert models (type, severity, timestamp)
- ✅ Behavior tracking data points
- ✅ Risk calculation logic

### 7. **Theme & Design**
- ✅ High-contrast color scheme (green primary)
- ✅ Extra-large typography (all text 20sp+)
- ✅ Elderly-optimized spacing and padding
- ✅ Material Design 3 components

---

## 🛠️ Tech Stack

| Component | Technology |
|-----------|-----------|
| Language | Kotlin |
| UI Framework | Jetpack Compose |
| Navigation | Compose Navigation |
| State Management | ViewModel + StateFlow |
| Voice | Android TextToSpeech |
| Design System | Material Design 3 |
| Build Tool | Gradle (Kotlin DSL) |

---

## 🚀 Next Steps (Future Enhancements)

### Not Implemented Yet (Mentioned in Requirements):
1. **Call Family** - Actual phone call integration
2. **Health Log** - Health tracking screen
3. **Real Call Interception** - ⚠️ Not possible without system permissions
4. **Actual Caregiver Alerts** - Would require backend integration

### Suggested Improvements:
- [ ] Add Settings screen
- [ ] Implement Health Log feature
- [ ] Add quick dial contacts
- [ ] Medicine reminder functionality
- [ ] Emergency SOS button
- [ ] Tutorial/onboarding for first-time users
- [ ] Accessibility services integration
- [ ] Local database for persistent storage

---

## 📋 Build Instructions

### Prerequisites:
- Android Studio (latest version)
- JDK 17
- Android SDK 34
- Gradle 8.2+

### Steps:
1. Open Android Studio
2. File → Open → Select the `oppam` folder
3. Wait for Gradle sync to complete
4. Click Run (Shift+F10) or use the green play button
5. Select a device/emulator (API 26+)

### Command Line:
```bash
cd oppam
./gradlew assembleDebug
```

The APK will be generated at:
`app/build/outputs/apk/debug/app-debug.apk`

---

## ⚠️ Important Notes

### Simulation Only:
This app does **NOT** actually:
- Intercept real phone calls
- Access call logs
- Record conversations
- Send real alerts to caregivers
- Access real user data

All functionality is **simulated** for UI/UX demonstration purposes only.

### Permissions:
Currently only requests:
- `INTERNET` - For future features
- `SET_WALLPAPER` - To act as launcher

Does NOT request call-related permissions.

---

## 🎨 Design Principles

1. **Large Everything** - Buttons, text, icons all oversized
2. **High Contrast** - Dark text on light backgrounds
3. **Minimal Options** - Only 3 main actions on home screen
4. **Voice Feedback** - Audio confirmation for actions
5. **Malayalam Support** - Native language for Kerala users
6. **Safety First** - Prominent scam warnings

---

## 📝 File Checklist

- [x] `build.gradle.kts` (project)
- [x] `build.gradle.kts` (app)
- [x] `settings.gradle.kts`
- [x] `gradle.properties`
- [x] `AndroidManifest.xml`
- [x] `MainActivity.kt`
- [x] `MainApplication.kt`
- [x] Theme files (Color.kt, Theme.kt, Type.kt)
- [x] LauncherScreen.kt + ViewModel
- [x] VoiceAssistant.kt + MalayalamResponses.kt
- [x] Protection screens (3 files)
- [x] CaregiverDashboard.kt
- [x] UI Components (2 files)
- [x] Data layer (2 files)
- [x] Resource files (strings.xml, colors.xml, themes.xml)
- [x] README.md
- [x] .gitignore

---

## 🎓 Learning Resources

If you want to extend this project:
- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Material Design 3](https://m3.material.io/)
- [TextToSpeech API](https://developer.android.com/reference/android/speech/tts/TextToSpeech)
- [Compose Navigation](https://developer.android.com/jetpack/compose/navigation)

---

**Project Status**: ✅ Core Features Complete

**Last Updated**: January 2026
