# TDM Insight – Therapeutic Drug Monitoring Calculator

## 📚 Course Information
- **Course Code**: CDE2313 – Mobile Application Development  
- **Programme**: Bachelor in Data Science  
- **Semester**: 3 (2025/2026)  
- **Lecturer**: Ts. Mohd Zulkifli Mohd Zaki  

## 👥 Group Members
- Student Name (ID)  
- Student Name (ID)  
- Student Name (ID)  

## 📝 Case Study Overview
The project is based on the **TDM Insight** case study.  
A hospital pharmacy requires a native Android application to perform **Therapeutic Drug Monitoring (TDM)** calculations for Vancomycin. The app collects patient and medication parameters, validates inputs, runs pharmacokinetic calculations, and explains results in a clear, structured way.

### Problem Domain
Manual TDM calculations are error-prone and inefficient. Pharmacists need a reliable, mobile tool to streamline workflows.  

### Implemented Solution
We developed **TDM Insight**, a native Android app using Kotlin and Jetpack Compose/XML. It provides dynamic input forms, validation, a dedicated calculation engine, and explainable results for Vancomycin workflows.

## ✨ Key Features
- Vancomycin workflows: **Pre**, **Post**, **Pre + Post**  
- Dynamic input forms based on workflow selection  
- Input validation (required fields, ranges, cross-field logic)  
- Pharmacokinetic outputs: Ke, half-life, Vd, clearance, AUC  
- Explainable results: inputs → intermediate → final  
- Material 3 UI design for clarity and usability  

## 🛠️ Technology Stack & Architecture
- **Language**: Kotlin  
- **IDE**: Android Studio  
- **UI**: Jetpack Compose / XML  
- **Architecture**: MVVM with a separate calculation engine  

## 📥 Installation Guide
1. Clone the repository:  
   ```bash
   git clone <GitHub Repository URL>
Open in Android Studio.

Sync Gradle and build the project.

Run on emulator or physical device (Android 8.0+).

## 🔨 How to Build
Use Android Studio’s Build → Build APK option.

The release APK is available in /apk/app-release.apk.

## 📱 APK Download
[Looks like the result wasn't safe to show. Let's switch things up and try something else!]

## 🖼️ Screenshots
Screenshots are available in the /screenshots/ folder.

## 📂 Repository Structure
plaintext
## MobileAppProject/
├── README.md
├── LICENCE
├── .gitignore
├── app/                # Android Studio Project
├── gradle/             # Gradle configuration
├── screenshots/        # App screenshots
├── docs/               # Documentation
│   ├── Case_Study_Analysis.md
│   ├── wireframe/
│   └── diagrams/
├── apk/                # Release APK
│   └── app-release.apk
├── presentation/       # Project presentation
│   ├── Presentation.pptx
│   └── Presentation.pdf
├── ai/                 # AI usage declaration
│   └── AI_Usage_Log.pdf
└── assets/             # Supporting resources
## 🙏 Acknowledgements
Albukhary International University

Lecturer Ts. Mohd Zulkifli Mohd Zaki

Reference calculators: myTDM Calculator, PhIS TDM Calculator

## 📜 License
This project is licensed under the MIT License – see the [LICENSE](LICENSE) file for details.

## 📖 References
Malaysian Pharmacy Information System (PhIS) TDM Calculator Manual

myTDM Calculator (https://www.mytdmcalculator.com/)

Vancomycin TDM clinical guidance (authoritative sources approved by lecturer)
