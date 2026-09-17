# TDM Insight — Native Android Therapeutic Drug Monitoring Calculator

## Course Information

**Course:** CDE2313 Mobile Application Development  
**Programme:** Bachelor in Data Science  
**Academic Session:** 2025/2026  
**Semester:** Semester 3  
**Project Type:** Group Project  
**Platform:** Native Android

---

## 👥 Group Members

| Student Name | Student ID |
|---|---|
| 1. Afifah Binti Mohd Shukri  | AIU24102306 |
| 2. Nur Khairunnisa binti Ramli | AIU24102156 |
| 3. Ummasalma Jamil  | AIU24102319|


---

## Project Overview

TDM Insight is a native Android application prototype developed for the Therapeutic Drug Monitoring (TDM) of Vancomycin.

The application is designed to support a structured TDM calculation workflow by collecting patient and dosing information, allowing the user to select a Vancomycin calculation workflow, validating the required inputs, performing pharmacokinetic calculations, and presenting the results together with intermediate calculation steps.

The project is developed as an academic software prototype based on the requirements provided in the CDE2313 project case study.

---

## Problem Statement

Therapeutic Drug Monitoring requires appropriate patient, medication, concentration, and sampling information to perform pharmacokinetic calculations.

A mobile application can help organize these inputs and provide a structured calculation workflow. However, the application must handle different calculation methods, validate inputs, separate calculation logic from the user interface, and provide understandable results.

TDM Insight addresses this problem through a native Android interface connected to a dedicated Vancomycin calculation engine.

---

## Objectives

The main objectives of TDM Insight are:

- Develop a native Android application using Kotlin.
- Provide Vancomycin TDM calculation workflows.
- Provide dynamic input fields based on the selected workflow.
- Validate required user inputs.
- Separate calculation logic from the user interface.
- Calculate pharmacokinetic parameters.
- Display intermediate and final calculation results.
- Provide an understandable calculation trail for the user.

---

## Target Users

The case study focuses on a hospital pharmacy department performing Therapeutic Drug Monitoring calculations.

The application is developed as an academic prototype for demonstration and educational purposes.

---

## Implemented Features

The current implementation includes:

### 1. Vancomycin Workflow Selection

The user can select one of three workflows:

- Pre
- Post
- Pre + Post

### 2. Dynamic Input Forms

The application changes the workflow-specific input fields according to the selected Vancomycin method.

Common patient and dosing information includes:

- Weight
- Age
- Serum creatinine
- Sex
- Dose
- Dosing interval
- Infusion duration

Workflow-specific information is displayed according to the selected method.

### 3. Input Validation

The application validates required inputs before calculation.

Invalid inputs generate an error message and prevent the calculation from continuing.

### 4. Vancomycin Calculation Engine

The calculation logic is separated from the user interface in a dedicated calculation engine.

The engine supports:

- Pre-dose workflow
- Post-dose workflow
- Pre + Post workflow

### 5. Pharmacokinetic Results

Depending on the selected workflow, the application calculates relevant pharmacokinetic values such as:

- Elimination rate constant (Ke)
- Half-life
- Volume of distribution (Vd)
- Clearance
- AUC (24 hr)
- New maintenance dose
- Expected concentration values

### 6. Explainable Results

The result screen presents the calculation in stages:

1. Input Values
2. Intermediate Values
3. Pharmacokinetic Parameters
4. Final Result

The calculation steps include the formula used and the calculated value.

### 7. Calculation Warnings

The application provides warnings when certain calculation conditions are invalid, such as invalid concentration values, timing issues, or calculation conditions that may result in invalid values.

---

## Technology Stack

| Technology | Purpose |
|---|---|
| Kotlin | Application programming language |
| Android Studio | Android development environment |
| Jetpack Compose | User interface development |
| Material 3 | UI components and design |
| Gradle | Project build system |
| JUnit | Unit testing |

---

## Application Architecture

The application follows a separation between the user interface, validation, data models, and calculation engine.

```text
User
  ↓
TDM Input Screen
  ↓
Input State
  ↓
Validation
  ↓
Vancomycin Calculation Engine
  ↓
TDM Result Model
  ↓
Results Screen
The calculation logic is kept outside the Composable UI functions to improve separation of concerns and maintainability.

### Main Application Flow
Open TDM Insight
        ↓
Select Vancomycin Workflow
        ↓
Enter Patient & Dose Parameters
        ↓
Enter Workflow-Specific Inputs
        ↓
Validate Inputs
        ↓
Run TDM Calculation
        ↓
Display Intermediate Calculations
        ↓
Display Pharmacokinetic Parameters
        ↓
Display Final Results

### Project Structure
MobileApplicationProjects-/

###│
├── README.md
├── LICENSE
├── .gitignore
│
├── app/
│   ├── src/
│   │   ├── main/
│   │   ├── test/
│   │   └── androidTest/
│   └── build.gradle.kts
│
├── gradle/
│
├── screenshots/
│
├── docs/
│   ├── Case_Study_Analysis.md
│   ├── wireframe/
│   └── diagrams/
│
├── apk/
│   └── app-release.apk
│
├── presentation/
│   ├── Presentation.pptx
│   └── Presentation.pdf
│
├── ai/
│   └── AI_Usage_Log.pdf
│
└── assets/ ###

### Documentation
Case Study Analysis
Wireframes
Diagrams
Screenshots
AI Usage Log

APK

The release APK will be provided in the following location:

apk/app-release.apk

Download TDM Insight APK

Installation
Requirements
Android Studio
Android SDK
Android device or Android Emulator
JDK compatible with the project's Gradle configuration
Using the APK
Download the release APK from the apk folder.
Transfer the APK to an Android device if necessary.
Allow installation from the appropriate source when prompted.
Install and launch TDM Insight.
Running from Android Studio
Open the project in Android Studio.
Open the repository/project root.
Allow Gradle to synchronize.
Connect an Android device or start an emulator.
Run the application.
Screenshots

Application screenshots are stored in:

screenshots/

View Application Screenshots

Testing

The project contains Android unit/instrumented test directories under:

app/src/test/
app/src/androidTest/

Testing is used to verify the calculation engine and application behaviour.

Final testing results will be updated based on the completed application testing.

Individual Contributions

Each group member contributes to different parts of the project, including:

Requirements and case study analysis
UI and wireframe planning
System diagrams
Android implementation
Calculation engine
Validation
Testing and debugging
Documentation
GitHub repository management
Presentation

Individual contributions can be verified through the GitHub commit history and project work.

AI Usage

Generative AI was used as a supplementary learning and troubleshooting resource during the project.

The project team remained responsible for:

Requirements analysis
Design decisions
Technical implementation
Testing
Debugging
Verification
Final project decisions

Details of AI usage are documented in the AI Usage Log.

View AI Usage Log

### Academic and Clinical Disclaimer

TDM Insight is an academic software prototype developed for educational, software development, and demonstration purposes.

The application is not clinically validated and is not intended to replace professional clinical judgment, prescribing decisions, diagnosis, or authoritative clinical guidance.

All demonstration cases are fictional.

Clinical equations, assumptions, units, and reference values should be verified against appropriate authoritative sources.

### Acknowledgements

Albukhary International University

Lecturer Ts. Mohd Zulkifli Mohd Zaki

Reference calculators: myTDM Calculator, PhIS TDM Calculator

The team acknowledges the course case study and the relevant authoritative references used to understand Vancomycin Therapeutic Drug Monitoring.

### References
CDE2313 Mobile Application Development Project Case Study.
CDE2313 Project Assessment Instructions.
myTDM Calculator.
Malaysian Pharmacy Information System (PhIS) TDM Calculator documentation.
Current authoritative Vancomycin Therapeutic Drug Monitoring guidance.
License

This project is provided under the TDM Insight Academic Non-Commercial License.

The project is intended for academic, educational, research, and demonstration purposes and is not intended for commercial use or clinical deployment.
