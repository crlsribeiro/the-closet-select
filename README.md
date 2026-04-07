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

The project follows a **Clean Architecture approach**:

```text
data/
├── remote (API services)
├── model (DTOs)
├── repository (implementations)

domain/
├── model (business models)
├── repository (interfaces)
├── usecase (business logic)

presentation/
├── screens (feature-based UI)
├── navigation
├── components

Principles:
UI driven by StateFlow
Business logic isolated in use cases
Repository pattern
Clear separation of concerns
🚀 Features
🔐 Firebase Authentication (Email/Password + Google Sign-In)
👤 User profile with automatic zodiac calculation
🌤️ Real-time weather (OpenWeather API)
🤖 AI-powered outfit generation (Gemini 2.5 Flash)
👗 Personal wardrobe archive with CameraX
🎨 Aura Palette (personalized color system)
✨ Daily context screen with confidence indicator
🔄 Real-time sync with Firestore
🌑 Premium dark UI with gold accents
🛠️ Tech Stack
Core
Kotlin
Jetpack Compose
MVVM + StateFlow
Clean Architecture
Backend & Services
Firebase Authentication
Firebase Firestore
Firebase Storage
APIs & AI
Google Gemini (AI)
OpenWeather API
Google Location Services
Libraries
Navigation Compose
Coil
CameraX
OkHttp
Kotlinx Serialization
Accompanist Permissions
📸 Screens
<p align="center"> <img src="app/screenshots/01_Splash.png" width="180"/> <img src="app/screenshots/02_CreateAccount.png" width="180"/> <img src="app/screenshots/03_ForgotPassword.png" width="180"/> <img src="app/screenshots/04_Home.png" width="180"/> <img src="app/screenshots/05_TheArchive.png" width="180"/> </p>
🔑 Firebase Setup

Add your own google-services.json file inside the app/ directory.

This file is not included in the repository for security reasons.

🔑 Environment Setup

Sensitive keys must be provided via:

local.properties
environment variables (CI/CD)
GOOGLE_WEB_CLIENT_ID=...
GEMINI_API_KEY=...
WEATHER_API_KEY=...
▶️ Running the Project
Clone the repository
Add API keys to local.properties
Add your google-services.json
Sync Gradle
Run on emulator or device
📊 Code Quality
SonarQube / SonarCloud integration
Android Lint
Clean architecture enforcement
State-driven UI
🔐 Security
Firebase Auth for authentication
Firestore rules per user
API keys outside version control
No sensitive data committed
💡 Why This Project Stands Out
Real-world architecture (not tutorial-level)
AI-powered feature with real use case
Multiple integrations (Firebase, Weather, AI)
Strong UI/UX consistency
End-to-end product thinking
