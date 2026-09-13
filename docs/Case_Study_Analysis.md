# Case Study Analysis – TDM Insight

## 1. Case Study Overview

### Project Title
TDM Insight – Native Android Therapeutic Drug Monitoring Calculator

### Course Information
- Course Code: CDE2313
- Course Name: Mobile Application Development
- Platform: Native Android
- Programming Language: Kotlin
- UI Technology: Jetpack Compose / XML
- Design System: Material 3

TDM Insight is an academic Android application designed to support a structured Therapeutic Drug Monitoring (TDM) calculation workflow. The application focuses on Vancomycin TDM and provides users with a clear process for entering patient and laboratory information, validating the inputs, performing calculations, and understanding the resulting pharmacokinetic parameters.

The project is based on a hospital pharmacy TDM scenario where calculations may require patient information, medication dose, dosing interval, drug concentration, sampling time, and laboratory information.

---

## 2. Problem Statement

Hospital pharmacy departments may need to perform TDM calculations using several patient, medication, laboratory, concentration, and timing parameters.

The main challenge is not simply performing a mathematical calculation. The application needs to transform a non-trivial TDM workflow into a reliable and understandable mobile application.

A poorly structured calculation interface could display unnecessary fields, allow incomplete or invalid information to be entered, or provide a final result without explaining how it was obtained.

Therefore, TDM Insight is designed to provide a structured workflow that guides the user from data entry and validation through calculation and explainable results.

---

## 3. Target Users

The case study is based on a hospital pharmacy environment. The application is designed as an academic software prototype for demonstrating a TDM calculation workflow.

The main intended users are:

- Pharmacy-related users working with TDM calculations.
- Students learning about mobile application development and TDM workflows.
- Users demonstrating fictional TDM calculation cases for academic purposes.

All demonstration cases used in the application should be fictional.

---

## 4. Project Scope

The main objective of TDM Insight is to develop a native Android TDM Calculator with a reliable calculation engine at its core.

The application focuses on:

- Vancomycin TDM calculations.
- Dynamic input forms.
- Input and cross-field validation.
- A dedicated calculation engine.
- Intermediate and final pharmacokinetic results.
- Explainable calculation results.
- A clean and intuitive Material 3 interface.

The application does not require:

- User authentication.
- A cloud backend or online services.
- Analytics dashboards.
- A complex patient-management system.
- Multiple medication modules.
- Large educational-content modules.

---

## 5. Functional Requirements

The application should provide the following core functionality.

### FR1 – Create a Fictional Case

The user should be able to create a fictional TDM case for calculation and demonstration purposes.

### FR2 – Enter Patient Parameters

The application should allow the user to enter the patient parameters required by the selected calculation workflow.

### FR3 – Select Medication

The user should be able to select Vancomycin as the medication used for the TDM calculation.

### FR4 – Select Calculation Workflow

The application should provide three Vancomycin workflows:

1. Vancomycin Pre
2. Vancomycin Post
3. Vancomycin Pre + Post

### FR5 – Display Dynamic Input Fields

The input form should change according to the selected workflow.

The application should not display every possible input field on one screen.

### FR6 – Validate Inputs

The application should validate entered information before calculations are performed.

Validation should include:

- Required-field validation.
- Numeric and unit validation.
- Range validation where specified.
- Cross-field validation.
- Detection of missing workflow-specific values.
- Protection against mathematical errors.

### FR7 – Review Calculation Inputs

The user should be able to review the entered information before running the calculation.

### FR8 – Perform TDM Calculation

The application should process the structured input through a dedicated calculation engine.

### FR9 – Display Intermediate Results

The application should display relevant intermediate pharmacokinetic results instead of showing only the final result.

### FR10 – Display Final Results

The application should present the final pharmacokinetic results produced by the selected calculation workflow.

### FR11 – Explain Calculation Results

The user should be able to open an explanation showing how the result was obtained.

The explanation should follow the sequence:

Input Values → Intermediate Values → Pharmacokinetic Parameters → Final Result

---

## 6. Non-Functional Requirements

### NFR1 – Usability

The application should provide a clean, intuitive, and understandable user interface.

### NFR2 – Reliability

The calculation process should handle valid inputs consistently and protect against invalid mathematical operations.

### NFR3 – Validation

The system should provide clear validation messages and distinguish between actual errors and information that requires review.

### NFR4 – Maintainability

The calculation logic should be separated from the user interface to make the application easier to maintain and modify.

### NFR5 – Understandability

Results should be explainable rather than presenting only a final numerical value.

### NFR6 – Platform Compatibility

The application should be developed as a native Android application using Kotlin and Android Studio.

### NFR7 – User Interface Consistency

The application should follow an appropriate Material 3 design approach.

---

## 7. Business Rules

The following rules are derived from the case study:

1. The application focuses on Vancomycin TDM.
2. The user must select a calculation workflow before entering workflow-specific information.
3. The available input fields should depend on the selected workflow.
4. The three supported workflows are Pre, Post, and Pre + Post.
5. Required values must be provided before a calculation can be performed.
6. Input values must pass appropriate validation.
7. Timing relationships must be logically valid where required.
8. Mathematical errors such as division by zero and invalid logarithmic operations must be prevented.
9. The calculation engine must be separated from the user interface.
10. Results should include intermediate and final values.
11. Users should be able to view an explanation of the calculation.
12. Demonstration cases must be fictional.
13. Clinical equations, units, assumptions, and reference values must come from lecturer-approved authoritative sources.
14. The application must not be presented as a clinically validated prescribing, diagnostic, or autonomous treatment-decision system.

---

## 8. Main Calculation Workflows

TDM Insight focuses on three Vancomycin workflows.

### 8.1 Vancomycin Pre

This workflow uses a pre-dose concentration.

The input form should display only the information required for the Pre workflow.

### 8.2 Vancomycin Post

This workflow uses a post-dose concentration together with relevant sampling information.

The input form should display the information required for the Post workflow.

### 8.3 Vancomycin Pre + Post

This workflow uses both pre-dose and post-dose concentrations together with relevant timing information.

The input form should display the additional fields required for the combined workflow.

The selected workflow determines which fields are displayed to the user.

---

## 9. Calculation Engine

The calculation engine is the main technical component of TDM Insight.

The application should not place calculation logic directly inside the UI or Composable functions.

The conceptual processing flow is:

UI
↓
Input State
↓
Validation
↓
TDM Calculation Engine
↓
Result Model
↓
Results UI

The calculation engine receives structured input data, validates the information, performs the appropriate calculation pathway, and returns structured results.

---

## 10. Pharmacokinetic Outputs

Depending on the selected workflow and the lecturer-approved calculation specification, the application may calculate:

- Elimination rate constant (Ke)
- Elimination half-life
- Volume of distribution (Vd)
- Clearance
- Concentration-related values
- AUC-related values
- Other parameters required by the selected TDM method

The exact clinical equations, units, assumptions, and reference values should be based on lecturer-approved authoritative sources.

---

## 11. Validation Requirements

Validation should go beyond checking whether a field is empty.

The application should support:

### Required-field Validation
Checks that all required fields for the selected workflow have been completed.

### Numeric and Unit Validation
Checks that numerical values and their associated units are valid.

### Range Validation
Checks values against specified valid ranges where applicable.

### Cross-field Validation
Checks relationships between different fields, such as logical timing relationships.

### Workflow-specific Validation
Checks that all values required by the selected workflow have been provided.

### Mathematical Validation
Protects the application against mathematical problems such as:

- Division by zero.
- Invalid logarithmic operations.
- Other invalid calculation conditions.

The application should display clear messages when an error occurs.

---

## 12. Explainable Results

The results screen should not display only a final number.

Users should be able to open a calculation explanation that presents:

1. Input Values
2. Intermediate Values
3. Pharmacokinetic Parameters
4. Final Result

This allows users to understand the calculation process rather than receiving an unexplained result.

---

## 13. Proposed Solution

TDM Insight addresses the identified problem by providing a structured mobile TDM workflow.

The proposed solution consists of:

- A Material 3 Android interface.
- Dynamic input forms based on the selected Vancomycin workflow.
- Input and cross-field validation.
- A separate TDM calculation engine.
- Structured result models.
- Intermediate and final calculation results.
- An explanation screen for understanding the calculation.

The overall solution can be summarized as:

Real-World TDM Problem
↓
Dynamic Mobile Input
↓
Validation
↓
Calculation Engine
↓
Intermediate Results
↓
Final Results
↓
Explainable Results

---

## 14. User Flow

The main user flow is:

1. Open TDM Insight.
2. Create a fictional case.
3. Enter the required patient parameters.
4. Select Vancomycin.
5. Select Pre, Post, or Pre + Post.
6. Enter the values required by the selected workflow.
7. Validate the information.
8. Review the calculation inputs.
9. Run the TDM calculation.
10. Display intermediate results.
11. Display the final results.
12. Open the calculation explanation.

---

## 15. Optional Enhancements

The following features are outside the core scope but may be added as enhancements:

- Local calculation history.
- Camera capture of a fictional laboratory report or medication label.
- OCR-assisted extraction of a selected value with mandatory user confirmation.
- What-if or scenario simulation.
- Additional TDM medication module.
- Simple concentration-time graph.
- Export or sharing of a calculation summary.

If the camera/OCR feature is implemented, the intended flow is:

Camera
↓
Capture
↓
Review
↓
Confirm Value
↓
Use in Calculation

---

## 16. Requirements-to-Feature Mapping

| Requirement | Proposed Feature |
|---|---|
| Create fictional case | New Case screen |
| Enter patient parameters | Patient Information screen |
| Select Vancomycin | Medication selection |
| Select TDM workflow | Pre / Post / Pre + Post selection |
| Dynamic inputs | Workflow-specific input forms |
| Input validation | Validation system |
| Review information | Review screen |
| TDM calculation | Dedicated calculation engine |
| Intermediate results | Calculation Results screen |
| Final results | Results summary |
| Explain calculation | Calculation Explanation screen |
| Material 3 interface | Material 3 UI components |

---

## 17. Clinical and Academic Disclaimer

TDM Insight is an academic software prototype intended only for educational and software development purposes.

It must not be presented as a clinically validated prescribing, diagnostic, or autonomous treatment-decision system.

All demonstration cases must be fictional, and clinical equations and reference values must be supported by appropriate authoritative sources.

---

## 18. References

The following sources were identified in the case study as starting points for domain understanding and reference checking:

- myTDM Calculator
- Malaysian Pharmacy Information System (PhIS) TDM Calculator documentation
- Current authoritative Vancomycin TDM clinical guidance

Clinical equations, target values, units, and assumptions should be verified using appropriate authoritative clinical sources before implementation. 

