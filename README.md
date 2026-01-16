# Oppam - AI-Powered Launcher for Elderly Users

An Android launcher designed specifically for elderly users in Kerala, featuring:

- **Simple Home Screen**: Large buttons with high contrast
- **Malayalam Support**: Voice responses in Malayalam
- **Protection Mode**: Simulated scam call detection
- **Caregiver Dashboard**: Monitor user safety

## Tech Stack

- Kotlin
- Jetpack Compose
- Android TextToSpeech
- Material Design 3

## Project Structure

```
app/
├── ui/
│   ├── launcher/         # Main launcher screen
│   ├── voice/           # Voice assistant (TTS)
│   ├── protection/      # Scam protection simulation
│   ├── caregiver/       # Caregiver dashboard
│   └── components/      # Reusable UI components
├── data/                # Data layer (fake repository)
└── MainApplication.kt   # App initialization
```

## Features

### 1. Launcher Screen
- 3 large, accessible buttons
- Elderly-friendly font sizes
- High contrast colors

### 2. Voice Assistant
- Android TextToSpeech integration
- Predefined Malayalam responses
- Helpful voice guidance

### 3. Protection Mode (Simulated)
- Fake incoming call screen
- Visual and audio alerts
- Caregiver notification simulation

### 4. Caregiver Dashboard
- Risk level indicators
- Behavior change graphs (dummy data)
- Alert history

## Build Instructions

```bash
./gradlew assembleDebug
```

## Note

This is a simulated app for demonstration purposes. It does NOT:
- Intercept real calls
- Record conversations
- Access real user data
- Send real alerts

All functionality is simulated for UI/UX demonstration.
