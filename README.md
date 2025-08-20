# ProFinder Mobile Client

## 📖 Table of Contents

- [Project Overview](#-project-overview)
- [Technology Stack](#-technology-stack)
- [Architecture](#-architecture)

## 🚀 Project Overview

**ProFinder Mobile** is the mobile client for the specialist finding platform, providing users with a convenient interface to interact with the [backend system](https://github.com/Strogolsky/proFinder_backend). The application enables clients to find professionals for various tasks and allows specialists to manage their orders and communicate with clients.

## 🛠 Technology Stack

### Core Technologies
- Kotlin
- Jetpack Compose
- Android SDK

### Networking
- Retrofit
- Gson
- OkHttp

### Asynchronous Operations
- Kotlin Coroutines
- Flow

### Additional Components
- Coil
- DataStore
- Navigation Component

## 🏗 Architecture
The application is built on **MVVM + Clean Architecture** principles with layer separation:
- Data Layer - network operations and data management
- Domain Layer - business logic and use cases
- Presentation Layer - UI components and ViewModels
