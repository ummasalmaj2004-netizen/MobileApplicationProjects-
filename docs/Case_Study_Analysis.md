# Case Study Analysis – TDM Insight Mobile Application

## 1. Problem Overview
Hospital pharmacy departments perform **Therapeutic Drug Monitoring (TDM)** calculations for medicines such as Vancomycin. These calculations require patient information, medication dose, dosing interval, drug concentration, sampling time, and laboratory data.  
Manual calculations are error-prone, time-consuming, and difficult to explain to non-technical users. Pharmacists need a reliable, mobile tool to streamline workflows and reduce calculation errors.

## 2. Current Challenges
- Lack of a structured mobile interface for TDM calculations.  
- Risk of calculation errors due to manual processes.  
- Difficulty in validating inputs and ensuring logical consistency.  
- Limited ability to explain intermediate pharmacokinetic results to users.  

## 3. Target Users
- Hospital pharmacists and pharmacy students.  
- Clinical staff performing Vancomycin monitoring.  
- Academic users learning pharmacokinetics in a mobile environment.  

## 4. Functional Requirements
- Support for **Vancomycin workflows**: Pre, Post, Pre + Post.  
- Dynamic input forms based on workflow selection.  
- Input validation (required fields, numeric ranges, cross-field logic).  
- Dedicated calculation engine separated from UI.  
- Display of intermediate and final pharmacokinetic results.  
- Explainable results with step-by-step breakdown.  

## 5. Non-Functional Requirements
- Native Android app using Kotlin and Android Studio.  
- Clean, intuitive Material 3 UI design.  
- Reliable performance with error handling (e.g., division by zero).  
- Usability across Android devices (minimum Android 8.0).  

## 6. Business Rules
- All demonstration cases must be fictional.  
- Clinical equations and reference values must be based on authoritative sources approved by the lecturer.  
- The app must not be presented as a clinically validated prescribing or diagnostic tool.  

## 7. Expected Application Features
- Pharmacokinetic outputs: elimination rate constant (Ke), half-life, volume of distribution (Vd), clearance, AUC values.  
- Dynamic forms that adapt to workflow selection.  
- Clear error messages and validation feedback.  
- Results screen showing inputs → intermediate values → final outputs.  

## 8. Optional Enhancements
- Local calculation history.  
- Camera capture of fictional lab reports or medication labels.  
- OCR-assisted extraction of selected values (with user confirmation).  
- Scenario simulation (“what-if” analysis).  
- Simple concentration-time graph.  
- Export or sharing of calculation summary.  

## 9. Summary of Implemented Solution
We developed **TDM Insight**, a native Android application that integrates a calculation engine with a structured mobile interface.  
The app provides dynamic input forms, robust validation, and explainable pharmacokinetic results for Vancomycin workflows.  
By separating the calculation engine from the UI, the solution ensures maintainability, clarity, and reliability.  

## 10. Technology Stack & Architecture
- **Language**: Kotlin  
- **IDE**: Android Studio  
- **UI Framework**: Jetpack Compose / XML  
- **Architecture**: MVVM pattern with a dedicated calculation engine module  
- **Design**: Material 3 principles  

## 11. Limitations
- Focused only on Vancomycin workflows (no multi-drug modules).  
- No cloud backend or user authentication.  
- Prototype intended for academic demonstration, not clinical deployment.  

