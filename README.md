# Snapchat Clone - Android App

This project is an Android application that emulates Snapchat, allowing users to send and receive photos using email IDs. It uses Firebase for backend services, ensuring real-time updates and secure storage.

## Features

- **Send Photos**: Share photos with friends via email.
- **Receive Photos**: View photos sent by friends.
- **User Authentication**: Sign up and log in to manage your account and photos.
- **Real-time Updates**: Utilizes Firebase for instant photo delivery.
- **Scalable Backend**: Uses Firebase Firestore and Firebase Storage.

## Technologies Used

- **Android Development**: Java/Kotlin and Android Studio
- **Backend**: Firebase (Firestore and Storage)
- **Authentication**: Firebase Authentication
- **Real-time Database**: Firebase Firestore
- **Storage**: Firebase Storage

## Getting Started

### Prerequisites

- **Android Studio** installed on your development machine.
- **Java Development Kit (JDK)** installed.
- **Firebase Account**: Set up a Firebase project and enable Firestore and Storage.

### Setup Instructions

#### 1. Clone the Repository

Clone the project from GitHub to your local machine.

```bash
git clone https://github.com/kanak27/Snapchat.git
cd Snapchat
```


#### 2. Open the Project in Android Studio

- Open **Android Studio**.
- Select **Open an existing Android Studio project**.
- Navigate to the cloned repository and select the project folder.

#### 3. Configure Firebase

- Set up a Firebase project at [Firebase Console](https://console.firebase.google.com/).
- Enable **Firebase Authentication**, **Firestore**, and **Storage**.
- Download `google-services.json` file from Firebase and place it in the `app` directory of your project.

#### 4. Build and Run the Application

- Connect your Android device or start an emulator.
- Click on the **Run** button in Android Studio to build and install the app on your device.

## Usage

- **Sign Up / Log In**: Create a new account or log in with existing credentials using Firebase Authentication.
- **Send Photos**: Select a photo from your gallery and send it to a friend's email ID.
- **Receive Photos**: View photos sent by friends directly in the app.

## Deployment

To deploy the app to users:

1. Ensure all Firebase configurations are correctly set up for production.
2. Generate a release build of the app.
3. Distribute the APK file through various channels or publish it to the Google Play Store.

## Contributing

We welcome contributions! To contribute:

1. Fork the repository.
2. Create a new branch (`git checkout -b feature/your-feature-name`).
3. Make your changes and commit them (`git commit -m 'Add some feature'`).
4. Push to the branch (`git push origin feature/your-feature-name`).
5. Open a pull request.

Please make sure your code follows the project's coding standards and includes relevant tests.

## Contact

For any questions, issues, or suggestions, please open an issue or contact the project maintainer at kanaklohiya.lohiya@gmail.com.
