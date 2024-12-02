# LiveChatApplication - Real-time Chat Application
<div align= "center">
<img src="https://github.com/user-attachments/assets/b05c1d05-fcb3-4db0-a315-009990425c66" width="100" position/>
</div>

LiveChatApplication is a modern Android chat application built with **Jetpack Compose**, offering real-time messaging, status sharing, and profile management. It leverages **Firebase** for authentication and data storage, delivering a seamless chatting experience.

---

## Features

### Authentication
- User registration with email and password.

- Secure login system.
- Profile management with customizable avatars.

### Chat
- Real-time messaging for one-on-one conversations.
- Message status indicators.
- Chat history preservation.

### Status Updates
- Share status updates with images.
- 24-hour visibility for statuses.
- View others' status updates with timestamps.

### Profile Management
- Customizable user profiles.
- Profile picture uploads.
- Update name and contact information.
- Secure logout functionality.



## Technical Stack

- **Frontend**: Jetpack Compose  
- **Backend**: Firebase (Authentication, Firestore, Storage)  
- **Architecture**: MVVM  
- **Dependency Injection**: Dagger Hilt  
- **Image Loading**: Coil  
- **Additional Storage**: Supabase  




## Screenshots

Add screenshots for:
1. Login/Registration screens
<p align="center">
  <img src="https://github.com/user-attachments/assets/a18009c9-4cbf-4fce-8f16-b1649936e609" width="200"/>
  &nbsp;&nbsp;&nbsp;&nbsp; <!-- Adds a gap -->
  <img src="https://github.com/user-attachments/assets/2f74d074-61fc-4938-8e7b-d9b7d5ad349d" width="200"/>
</p>
3. Chat list view
 <p align="center">
  <img src="https://github.com/user-attachments/assets/86354108-7b3d-42c3-bebd-f91b7e82ed23" width="200"/>
</p>
5. Individual chat conversation  
  <p align="center">
  <img src="https://github.com/user-attachments/assets/bef629ca-01f4-42e9-8773-e160d9f6a2af" width="200"/>
</p>
6. Status updates  
 <p align="center">
  <img src="https://github.com/user-attachments/assets/6e81b764-836c-4aad-a59d-6af170f45a3b" width="200"/>
    <img src="https://github.com/user-attachments/assets/f6db73dd-e25e-4ed2-b354-c6042d81f084" width="200"/>
</p>
7. Profile management  
 <p align="center">
  <img src="https://github.com/user-attachments/assets/c0300ffe-7537-4f98-a401-17b710b2ed65" width="200"/>
</p>
---

## Installation

### Step 1: Clone the Repository

git clone https://github.com/noor-mustafa123/chattingapplication.git

### Step 2: Open in Android Studio
Import the project into Android Studio.

### Step 3: Configure Firebase
Create a Firebase project.
Add your google-services.json file to the app/ directory.
Enable Authentication and Firestore in your Firebase console.

### Step 4: Build and Run
Build and run the application from Android Studio.

### Project Structure

Copy code
```bash
chattingapplication/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/chattingapplication/
│   │   │   │   ├── Events/
│   │   │   │   │   └── Event.kt
│   │   │   │   ├── Models/
│   │   │   │   │   └── UserProfileModel.kt
│   │   │   │   ├── Screens/
│   │   │   │   │   ├── AllChatsScreen.kt
│   │   │   │   │   ├── LoginScreen.kt
│   │   │   │   │   ├── SignUpScreen.kt
│   │   │   │   │   ├── SingleChatScreen.kt
│   │   │   │   │   ├── SingleStatusScreen.kt
│   │   │   │   │   └── UserProfileScreen.kt
│   │   │   │   ├── Utilities/
│   │   │   │   │   ├── Constants/
│   │   │   │   │   │   └── DatabaseConstants.kt
│   │   │   │   │   ├── UtilityComposables/
│   │   │   │   │   │   └── UtilityComposables.kt
│   │   │   │   │   └── hiltComponents/
│   │   │   │   │       └── DaggerComponents.kt
│   │   │   │   ├── ViewModels/
│   │   │   │   │   └── ApplicationViewModel.kt
│   │   │   │   └── MainActivity.kt
│   │   │   └── res/
│   │   │       ├── drawable/
│   │   │       ├── mipmap/
│   │   │       ├── values/
│   │   │       └── xml/
│   │   ├── androidTest/
│   │   └── test/
│   ├── build.gradle.kts
│   └── google-services.json
├── gradle/
│   ├── libs.versions.toml
│   └── wrapper/
├── build.gradle.kts
└── gradle.properties
```

### Key Components
Main Activity
MainActivity.kt serves as the entry point, managing navigation and screen states using Jetpack Compose.

### ViewModel
ApplicationViewModel is responsible for:

User authentication.
Chat management.
Status updates.
Profile operations.
Screens
AllChatsScreen: Displays a list of active chats.
SingleChatScreen: Shows individual chat conversations.
UserProfileScreen: Manages user profile settings.
Data Models
UserProfileModel

kotlin
```
Copy code
data class UserProfileModel(
    val userId: String? = "",
    val name: String? = "",
    val number: String? = "",
    val imageUrl: String? = ""
)
ChatData

kotlin
Copy code
data class ChatData(
    val chatId: String? = "",
    val user1: ChatUser = ChatUser(),
    val user2: ChatUser = ChatUser()
)
Dependencies
kotlin
Copy code
dependencies {
    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:${Versions.firebase}"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-storage-ktx")

    // Jetpack Compose
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")

    // Dependency Injection
    implementation("com.google.dagger:hilt-android:2.52")
    ksp("com.google.dagger:hilt-compiler:2.52")
}
```
### Contributing
Fork the repository.
#### -Create your feature branch:
```bash
Copy code
git checkout -b feature/AmazingFeature
```
#### -Commit your changes:
```bash
Copy code
git commit -m 'Add some AmazingFeature'
```
#### -Push to the branch:
```bash
Copy code
git push origin feature/AmazingFeature
```
#### -Open a Pull Request.

###  License
This project is licensed under the MIT License - see the LICENSE.md file for details.

### Acknowledgments
Firebase: Backend services.
Jetpack Compose: Modern UI development.
Supabase: Additional storage solutions.

### Learning Experience
---
Developing LiveChatApplication was a comprehensive learning journey into modern Android development. Key takeaways include:

Jetpack Compose: Building declarative UIs, chat interfaces, and status updates.
Firebase Integration: Implementing authentication, real-time database operations, and cloud storage.
Architecture Patterns: Mastering MVVM and ViewModel-based state management.
Dependency Injection: Enhancing code organization and testability with Dagger Hilt.
Real-time Features: Implementing real-time messaging and status updates.
State Management: Handling complex application states effectively.
Modern Android Development: Adopting best practices and tools for scalable apps.
This project showcases a production-ready Android application built with cutting-edge technologies and architectural patterns.
