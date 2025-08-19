# PadUp 🏓
**Padel Court Reservation App for Tunisia**

PadUp is a mobile application designed to streamline padel court reservations across Tunisia. Built with modern Android technologies, it provides players with an easy way to find, book, and manage padel court reservations.

## 🌟 Features

### For Players
- **Court Discovery**: Find padel courts near you across Tunisia
- **Real-time Availability**: Check court availability in real-time
- **Easy Booking**: Simple reservation process with instant confirmation
- **Player Matching**: Connect with other players looking for partners
- **Booking History**: Track your past and upcoming reservations
- **Club Information**: Detailed information about courts, facilities, and amenities
- **Multi-language Support**: Arabic, French, and English language options

### For Court Owners
- **Court Management**: Manage multiple courts and their schedules
- **Booking Management**: Accept, decline, or modify reservations
- **Revenue Tracking**: Monitor bookings and revenue analytics
- **Customer Communication**: Direct messaging with players
- **Pricing Management**: Set flexible pricing for different time slots

## 🚀 Technology Stack

- **Platform**: Android (Kotlin/Java)
- **Architecture**: MVVM (Model-View-ViewModel)
- **Database**: Room Database with SQLite
- **Networking**: Retrofit for API calls
- **Authentication**: Firebase Authentication
- **Maps**: Google Maps API for location services
- **Payment**: Integrated payment gateway for secure transactions
- **Push Notifications**: Firebase Cloud Messaging

## 📱 Screenshots

*Coming soon - Add your app screenshots here*

## 🛠️ Installation & Setup

### Prerequisites
- Android Studio Arctic Fox (2020.3.1) or later
- Android SDK API level 21 or higher
- Google Play Services
- Firebase account for authentication and messaging

### Clone the Repository
```bash
git clone https://github.com/ahmed12112000/PadeliumMarhaba.git
cd PadeliumMarhaba
```

### Setup Firebase
1. Create a new Firebase project at [Firebase Console](https://console.firebase.google.com/)
2. Add your Android app to the Firebase project
3. Download `google-services.json` and place it in the `app/` directory
4. Enable Authentication and Cloud Messaging in Firebase console

### API Configuration
1. Copy `config.example.properties` to `config.properties`
2. Add your API endpoints and keys:
```properties
BASE_URL=https://your-api-endpoint.com/
GOOGLE_MAPS_API_KEY=your_google_maps_key
PAYMENT_API_KEY=your_payment_gateway_key
```

### Build and Run
```bash
./gradlew assembleDebug
```

## 🏗️ Project Structure

```
app/
├── src/main/
│   ├── java/com/padup/
│   │   ├── ui/              # UI components and activities
│   │   ├── data/            # Repository, database, and API
│   │   ├── domain/          # Business logic, use cases, and domain models
│   │   ├── model/           # Data models
│   │   ├── utils/           # Utility classes
│   │   └── di/              # Dependency injection
│   ├── res/                 # Resources (layouts, strings, etc.)
│   └── AndroidManifest.xml
├── build.gradle
└── proguard-rules.pro
```

## 🌍 Supported Regions

Currently serving padel courts in:
- **Tunis** - Greater Tunis area
- **Sousse** - Sousse and surrounding areas  
- **Sfax** - Sfax metropolitan area
- **Monastir** - Monastir region
- **Hammamet** - Hammamet and Nabeul

*Expanding to more cities across Tunisia*

## 🔧 Key Dependencies

```gradle
dependencies {
    implementation 'androidx.navigation:navigation-fragment-ktx:2.7.5'
    implementation 'com.google.android.material:material:1.10.0'
    implementation 'androidx.room:room-runtime:2.6.1'
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.google.firebase:firebase-auth:22.3.0'
    implementation 'com.google.android.gms:play-services-maps:18.2.0'
    implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0'
}
```

## 🤝 Contributing

We welcome contributions to improve PadUp! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Code Style
- Follow Android Kotlin style guidelines
- Use meaningful variable and function names
- Add comments for complex logic
- Ensure all new features have appropriate tests

## 📄 API Documentation

The app connects to our REST API for data synchronization. Key endpoints:

```
GET /api/courts              # Get available courts
POST /api/bookings           # Create new booking
GET /api/bookings/{userId}   # Get user bookings
PUT /api/bookings/{id}       # Update booking
DELETE /api/bookings/{id}    # Cancel booking
```

## 🔐 Security & Privacy

- All user data is encrypted in transit and at rest
- Payment information is processed through secure, PCI-compliant gateways
- Location data is only used for court discovery and is not stored permanently
- Users can delete their accounts and all associated data at any time

## 📋 Roadmap

### Version 2.0
- [ ] Tournament organization features
- [ ] Advanced player statistics
- [ ] Social features and player communities
- [ ] Integration with fitness tracking apps
- [ ] Loyalty program for frequent players

### Version 2.1
- [ ] AI-powered court recommendations
- [ ] Weather integration for outdoor courts
- [ ] Group booking features
- [ ] Video coaching integration

## 🐛 Known Issues

- Occasional GPS accuracy issues in dense urban areas
- Payment processing may take up to 24 hours for bank transfers
- Push notifications require app to be running in background

## 📞 Support

- **Email**: support@padup.tn
- **Phone**: +216 XX XXX XXX
- **Website**: www.padup.tn

For technical issues, please create an issue in this repository.

## 📜 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Team

- **Lead Developer**: Ahmed Ben Salem
- **UI/UX Designer**: [Your Designer Name]
- **Backend Developer**: [Backend Dev Name]
- **Product Manager**: [PM Name]

## 🙏 Acknowledgments

- Thanks to the Tunisian Padel Federation for their support
- Special thanks to all the padel clubs who partnered with us
- Icons provided by [Icons8](https://icons8.com)
- Maps powered by Google Maps Platform

---

**Made with ❤️ in Tunisia for the Padel community**

*For business inquiries and partnerships: business@padup.tn*
