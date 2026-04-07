# 👗 The Closet Select

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
- Context-aware signals (e.g. user profile data)

Instead of generic suggestions, the app generates **highly personalized outfit combinations using your own wardrobe**.

---

## 🧱 Architecture

The project follows a **Clean Architecture approach** with clear separation of concerns:
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

## 🚀 Features

- 🔐 Firebase Authentication (Email/Password + Google Sign-In via Credential Manager)
- 👤 User profile with automatic zodiac calculation
- 🌤️ Real-time weather using OpenWeather API
- 🤖 AI-powered outfit generation (Gemini 2.5 Flash)
- 👗 Personal wardrobe archive with photo capture (CameraX)
- 🎨 Personalized color palette (Aura Palette)
- ✨ Daily context screen with confidence indicator
- 🔄 Real-time sync with Firestore
- 🌑 Premium dark UI with gold accents

---

## 🛠️ Tech Stack

### Core
- Kotlin
- Jetpack Compose
- MVVM + StateFlow
- Clean Architecture

### Backend & Services
- Firebase Authentication
- Firebase Firestore
- Firebase Storage

### APIs & AI
- Google Gemini (AI)
- OpenWeather API
- Google Location Services

### Libraries
- Navigation Compose
- Coil (image loading)
- CameraX
- OkHttp
- Kotlinx Serialization
- Accompanist Permissions

---

## 📸 Screens

<p align="center">
  <img src="screenshots/login.png" width="180"/>
  <img src="screenshots/home.png" width="180"/>
  <img src="screenshots/daily_energy.png" width="180"/>
  <img src="screenshots/gerar_look.png" width="180"/>
</p>

---

## 🔑 Environment Setup

Sensitive keys are **not hardcoded** and must be provided via:

- `local.properties` (local dev)
- environment variables (CI/CD)

## ▶️ Running the Project

1. Clone the repository  
2. Add required API keys to `local.properties`  
3. Sync Gradle  
4. Run on emulator or device  

---

## 📊 Code Quality

- Static analysis with **SonarQube / SonarCloud**
- Android Lint integrated
- Modular separation of concerns
- Consistent UI state management

---

## 🔐 Security

- Firebase Auth for secure authentication  
- Firestore rules with user-based access  
- API keys managed via environment variables  
- No sensitive data committed to repository  

---

## 💡 Why This Project Stands Out

This project demonstrates:

- Real-world app architecture (not a toy project)
- Integration of multiple APIs and services
- AI-driven feature design
- Strong UI/UX consistency with Compose
- End-to-end product thinking (from onboarding to core feature)

---

## 📄 License

MIT License
