# TDM Insight
### Native Android Therapeutic Drug Monitoring Calculator

TDM Insight is a native Android academic software prototype developed for the
CDE2313 Mobile Application Development course.

The application focuses on Therapeutic Drug Monitoring (TDM) calculations for
Vancomycin. It provides a structured workflow for entering patient and
laboratory information, validating inputs, performing calculations, displaying
intermediate and final pharmacokinetic results, and explaining how the results
were obtained.

---

## 📚 Course Information

| Information | Details |
|---|---|
| Course Code | CDE2313 |
| Course Name | Mobile Application Development |
| Programme | Bachelor in Data Science |
| Academic Session | 2025/2026 |
| Semester | Semester 3 |
| Development Platform | Native Android |
| Programming Language | Kotlin |
| UI Technology | Jetpack Compose / XML |
| Design System | Material 3 |

---

## 👥 Group Members

| Student Name | Student ID |
|---|---|
| 1. Afifah Binti Mohd Shukri  | AIU24102306 |
| 2. Nur Khairunnisa binti Ramli | AIU24102156 |
| 3. Ummasalma Jamil  | AIU24102319|

---

## 📌 Case Study

### Problem Overview

A hospital pharmacy department performs Therapeutic Drug Monitoring (TDM)
calculations for selected medicines. These calculations may require patient
information, medication dose, dosing interval, drug concentration, sampling
time, and laboratory information.

The main challenge is to transform a non-trivial TDM calculation workflow
into a reliable, understandable, and user-friendly mobile application.

### Proposed Solution

TDM Insight provides a structured Android-based TDM calculation workflow that
guides the user through:

1. Creating a fictional case.
2. Entering patient parameters.
3. Selecting Vancomycin.
4. Selecting the required TDM workflow.
5. Entering workflow-specific values.
6. Validating the information.
7. Reviewing the calculation inputs.
8. Running the TDM calculation.
9. Viewing intermediate and final results.
10. Opening an explanation of the calculation.

The case study focuses on three Vancomycin workflows:

- Vancomycin Pre
- Vancomycin Post
- Vancomycin Pre + Post

The selected workflow determines which input fields are displayed.

---

## ✨ Key Features

### Vancomycin TDM Workflows

The application supports the following workflow options:

- Pre-dose concentration workflow
- Post-dose concentration workflow
- Pre + Post concentration workflow

### Dynamic Input Forms

The application displays input fields according to the selected workflow
instead of displaying every possible field on a single screen.

### Input Validation

The application validates entered information before calculations are
performed.

Validation includes:

- Required-field validation
- Numeric and unit validation
- Range validation where applicable
- Cross-field validation
- Workflow-specific validation
- Mathematical error protection

### TDM Calculation Engine

The calculation logic is separated from the user interface through a dedicated
calculation engine.

### Intermediate Results

The application displays relevant intermediate pharmacokinetic values instead
of presenting only a final number.

### Explainable Results

Users can open a calculation explanation showing the calculation process:

Input Values
→ Intermediate Values
→ Pharmacokinetic Parameters
→ Final Result

### Material 3 Interface

The application uses a clean mobile interface based on Material 3 design
principles.

---


## 🛠️ Technology Stack

| Technology | Purpose |
|---|---|
| Kotlin | Application programming language |
| Android Studio | Android development environment |
| Jetpack Compose / XML | User interface development |
| Material 3 | User interface design |
| Gradle | Project build system |
| Git | Version control |
| GitHub | Source code and project repository |

---


## 🏗️ Application Architecture

TDM Insight separates the user interface from the calculation logic.

The main conceptual architecture is:

```text
┌─────────────────────────┐
│       User Interface    │
│     Material 3 UI       │
└────────────┬────────────┘
             │
             ▼
┌─────────────────────────┐
│       Input State       │
│   Patient / TDM Data    │
└────────────┬────────────┘
             │
             ▼
┌─────────────────────────┐
│       Validation        │
│ Required / Range /      │
│ Cross-field Validation  │
└────────────┬────────────┘
             │
             ▼
┌─────────────────────────┐
│   TDM Calculation       │
│        Engine           │
└────────────┬────────────┘
             │
             ▼
┌─────────────────────────┐
│     Result Model        │
│ Intermediate / Final    │
│ Pharmacokinetic Results │
└────────────┬────────────┘
             │
             ▼
┌─────────────────────────┐
│    Results & Explanation│
└─────────────────────────┘
The calculation engine is kept separate from UI functions so that calculation
logic can be maintained independently of the interface.



## 📱 Main User Flow

Open TDM Insight
       ↓
Create Fictional Case
       ↓
Enter Patient Parameters
       ↓
Select Vancomycin
       ↓
Select Workflow
       ↓
┌───────────────┬───────────────┬─────────────────┐
│   Vancomycin  │   Vancomycin  │   Vancomycin    │
│      Pre      │      Post     │    Pre + Post   │
└───────────────┴───────────────┴─────────────────┘
       ↓
Enter Required Values
       ↓
Validate Information
       ↓
Review Calculation Inputs
       ↓
Run TDM Calculation
       ↓
Display Intermediate Results
       ↓
Display Final Results
       ↓
Open Calculation Explanation



## 📥 Installation Guide

Requirements

Before installing or building the application, ensure that you have:

Android Studio installed.
A compatible Android SDK.
Kotlin support enabled.
An Android device or Android Emulator.
Clone the Repository
git clone [GITHUB_REPOSITORY_URL]
Navigate into the project directory:
cd [PROJECT_FOLDER_NAME]
Open the project in Android Studio.

Allow Android Studio to synchronize the Gradle files and complete the required
project setup.


## 🔨 How to Build the Project

Using Android Studio
Clone or download the repository.
Open the project in Android Studio.
Allow Gradle synchronization to complete.
Connect an Android device or start an Android Emulator.
Select the application configuration.
Click Run to build and launch the application.
Generate APK

To generate a release APK, use Android Studio's build tools or run the
appropriate Gradle build command.

The release APK should be placed in:
apk/app-release.apk

## 📦 APK Download

The release-ready APK is available in the repository:
- [Download APK](./apk/app-release.apk)


## 📸 Screenshots

Screenshots demonstrating the application interface are stored in:
- [Screenshots](./screenshots/)

## 📂 GitHub Repository Structure

## TDM-Insight/
│
├── README.md
├── LICENCE
├── .gitignore
│
├── app/
│   └── Android Studio application files
│
├── gradle/
│   └── Gradle configuration
│
├── screenshots/
│   └── Application screenshots
│
├── docs/
│   ├── Case_Study_Analysis.md
│   │
│   ├── wireframe/
│   │   └── Wireframe images
│   │
│   └── diagrams/
│       └── System diagrams
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
└── assets/
    └── Supporting resources ##


## 📄 Project Documentation

Additional project documentation can be found in the docs/ directory.

Case Study Analysis
docs/Case_Study_Analysis.md

This document contains the analysis of:

Problem statement
Target users
Project scope
Functional requirements
Non-functional requirements
Business rules
TDM workflows
Proposed solution
User flow
Requirements-to-feature mapping

Wireframes
- [Wireframes](./docs/wireframe/)
Contains the interface wireframes developed during the design stage.

Diagrams
- [Diagrams](./docs/diagrams/)

Contains the software engineering and system design diagrams for the project.

## 🧪 Testing

Functional testing should be performed before submission to verify that the
application behaves correctly.

Testing should cover:

Navigation between screens.
Workflow selection.
Dynamic input fields.
Required-field validation.
Invalid input handling.
Cross-field validation.
Mathematical error handling.
TDM calculations.
Intermediate results.
Final results.
Calculation explanation.

Testing results and relevant evidence should be included in the project
documentation where applicable.

## 🔐 Clinical and Academic Disclaimer

TDM Insight is an academic software prototype intended only for educational
and software development purposes.

It must not be presented as a clinically validated prescribing, diagnostic, or
autonomous treatment-decision system.

All demonstration cases must be fictional.

Clinical equations, reference values, units, and assumptions must be supported
by appropriate authoritative sources.

## 🤖 AI Usage

Artificial Intelligence tools may have been used during the development
process for permitted purposes such as learning, explanation, debugging,
documentation support, and design discussion.

A record of AI usage is provided in:

## AI Usage

- [AI Usage Log](./ai/AI_Usage_Log.pdf)
The AI Usage Log documents the relevant AI tools, purposes, prompts where

applicable, suggestions adopted or rejected, modifications made by the
students, and reflections on the use of AI.

## 🙏 Acknowledgements

We would like to acknowledge:

Lecturer Ts. Mohd Zulkifli Mohd Zaki
Albukhary International University.
The resources and references provided in the project case study.
All sources used to understand Therapeutic Drug Monitoring and Vancomycin
pharmacokinetic concepts.


## 📚 References

The following resources were identified in the case study as starting points
for domain understanding and reference checking:

myTDM Calculator
Malaysian Pharmacy Information System (PhIS) TDM Calculator documentation
Current authoritative Vancomycin TDM clinical guidance

Clinical equations and reference values used in the final application should
be verified against appropriate authoritative clinical sources.



