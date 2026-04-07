# 👗 The Closet Select

![Repo Views](https://komarev.com/ghpvc/?username=crlsribeiro&repo=the-closet-select&color=gold)

An Android app that delivers **context-aware outfit recommendations** by combining personal wardrobe data, weather conditions, color analysis, and AI.

---

## 🔍 Overview

- 📱 Android app built with Kotlin + Jetpack Compose
- 🧠 AI-powered outfit generation (Google Gemini)
- 🌤️ Real-time weather integration
- 🎨 Personalized color palette system
- 🔐 Firebase Authentication & Firestore backend
- 🧱 Clean Architecture (Data / Domain / Presentation)

---

## 💡 Concept

Getting dressed is often repetitive and disconnected from context.

**The Closet Select** transforms your wardrobe into an intelligent system that considers:

- Your available clothes
- Current weather conditions
- Personalized color palette
- Context-aware signals (user profile + preferences)

Instead of generic suggestions, the app generates **highly personalized outfit combinations using your own wardrobe**.

---

## 🧱 Architecture

The project follows a **Clean Architecture** approach:

```text
data/
├── remote/         (API services)
├── model/          (DTOs)
└── repository/     (implementations)

domain/
├── model/          (business models)
├── repository/     (interfaces)
└── usecase/        (business logic)

presentation/
├── screens/        (feature-based UI)
├── navigation/
└── components/
```

**Principles:**

- UI driven by `StateFlow`
- Business logic isolated in use cases
- Repository pattern
- Clear separation of concerns

---

## 🚀 Features

- 🔐 Firebase Authentication (Email/Password + Google Sign-In)
- 👤 User profile with automatic zodiac calculation
- 🌤️ Real-time weather (OpenWeather API)
- 🤖 AI-powered outfit generation (Gemini 2.5 Flash)
- 👗 Personal wardrobe archive with CameraX
- 🎨 Aura Palette (personalized color system)
- ✨ Daily context screen with confidence indicator
- 🔄 Real-time sync with Firestore
- 🌑 Premium dark UI with gold accents

---

## 🛠️ Tech Stack

### Core

| Technology | Role |
|---|---|
| Kotlin | Primary language |
| Jetpack Compose | UI framework |
| MVVM + StateFlow | State management |
| Clean Architecture | Project structure |

### Backend & Services

| Service | Role |
|---|---|
| Firebase Authentication | User auth |
| Firebase Firestore | Database |
| Firebase Storage | Media storage |

### APIs & AI

| Service | Role |
|---|---|
| Google Gemini 2.5 Flash | AI outfit generation |
| OpenWeather API | Real-time weather |
| Google Location Services | Device location |

### Libraries

| Library | Role |
|---|---|
| Navigation Compose | In-app navigation |
| Coil | Image loading |
| CameraX | Wardrobe photo capture |
| OkHttp | HTTP client |
| Kotlinx Serialization | JSON parsing |
| Accompanist Permissions | Runtime permissions |

---

## 📸 Screens

<p align="center">
  <img src="app/screenshots/01_Splash.png" width="180" alt="Splash Screen"/>
  <img src="app/screenshots/02_CreateAccount.png" width="180" alt="Create Account Screen"/>
  <img src="app/screenshots/03_ForgotPassword.png" width="180" alt="Forgot Password Screen"/>
  <img src="app/screenshots/04_Home.png" width="180" alt="Home Screen"/>
  <img src="app/screenshots/05_TheArchive.png" width="180" alt="Archive Screen"/>
</p>

---

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog or later
- Android SDK 26+
- A Firebase project configured

### Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/crlsribeiro/the-closet-select.git
   ```

2. Open the project in Android Studio.

3. Add your `google-services.json` to the `app/` directory.

4. Add your API keys to `local.properties`:
   ```properties
   GEMINI_API_KEY=your_gemini_key
   OPENWEATHER_API_KEY=your_openweather_key
   ```

5. Build and run on an emulator or physical device.

---

## 📄 License

```
MIT License — feel free to use, modify, and distribute.
```
