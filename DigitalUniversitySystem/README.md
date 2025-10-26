# Digital University System

## 1. Project Title

Digital University System

## 2. Team Information

| Name | Role | Responsibilities | NUID |
| --- | --- | --- | --- |
| Ing-Ruei | Admin Role Lead | Admin role development, UI/UX optimization, bug fixing, documentation. | [002537387 Ing-Ruei] |
| Hao-Chun | Student Role Lead | Student role development, feature implementation, testing. | [002536970 Hao-Chun] |
| Shuai | Faculty Role Lead | Faculty role development, feature implementation, testing. | [002030535 Shuai] |
| Wanyu | Registrar Role Lead | Registrar role development, feature implementation, testing. | [002501820 Wanyu] |

## 3. Project Overview

The Digital University System is a comprehensive, Java-based desktop application designed to streamline and manage the administrative and academic operations of a university. The system provides a centralized platform for different user roles—Admin, Faculty, and Student—to perform their respective tasks efficiently. The primary objectives of this project are to reduce manual workload, improve data accuracy, and provide a seamless user experience for all stakeholders in the university ecosystem.

### Key Features:
- Role-based access control for tailored user experiences.
- Comprehensive user and profile management.
- Course and semester management.
- Student enrollment and registration system.
- Financial tracking and tuition reconciliation.
- In-depth analytics and reporting dashboard.

## 4. Installation & Setup Instructions

### Prerequisites
- **Java Development Kit (JDK):** Version 8 or higher
- **IDE:** Apache NetBeans, IntelliJ IDEA, or Eclipse
- **Build Tool:** Apache Ant

### Step-by-Step Setup

1.  **Clone the Repository:**
    ```bash
    git clone https://github.com/Group_Lab_Team_6/DigitalUniversitySystem.git
    cd DigitalUniversitySystem
    ```

2.  **Open in IDE:**
    -   **NetBeans:** Use `File > Open Project` and select the cloned directory.
    -   **IntelliJ/Eclipse:** Import the project as a Java project with existing sources and configure it to use the Ant build script (`build.xml`).

3.  **Run the Project:**
    -   Locate the `Business.ProfileWorkAreaMainFrame.java` file.
    -   Right-click and select `Run File` or use your IDE’s run configuration to execute the main method.

## 5. Authentication & Access Control

### Authentication
The system uses a username and password-based authentication mechanism. Upon launching the application, users are presented with a login screen where they must enter their credentials. The system validates these credentials against the stored user accounts to grant or deny access.

### Authorization
Authorization is role-based, meaning that the functionalities a user can access depend on their assigned role. The system supports the following roles:

-   **Admin:** Has full access to all system functionalities, including user management, profile management, and system-wide analytics.
-   **Faculty:** Can manage their assigned courses, view student information for their courses, and manage their own profile.
-   **Student:** Can register for courses, view their coursework and grades, manage their finances, and update their personal profile.
-   **Registrar:** Can create new courses, manage student registration, reconcile tuition, and manage their own profile.

## 6. Features Implemented

| Feature | Implemented By | Description |
| --- | --- | --- |
| **User & Profile Management** | Ing-Ruei | Admins can register new users, create and update user accounts, and manage profiles for all roles. | 
| **Course & Semester Management** | Shuai, Wanyu | Faculty can manage course offerings, and registrars can create new courses. | 
| **Student Enrollment System** | Hao-Chun | Students can register for available courses each semester. | 
| **Financial Management** | Hao-Chun, Wanyu | Students can view their tuition and payment history. Registrars can reconcile tuition payments. | 
| **Analytics Dashboard** | Ing-Ruei | Admins can view analytics on user roles, course enrollment, and tuition revenue. | 
| **UI/UX Enhancements** | Ing-Ruei | Implemented a modern, tabbed navigation for the admin panel and improved overall UI consistency. | 

## 7. Usage Instructions

1.  **Login:** Launch the application and log in with your assigned credentials.
2.  **Navigate:** Based on your role, you will be directed to a dashboard with accessible features.
    -   **Admins:** Use the tabbed navigation to switch between User Management, Profile Management, and System analytics.
    -   **Faculty/Students/Registrar:** Use the buttons on your dashboard to access your respective functionalities.
3.  **Perform Tasks:** Follow the on-screen instructions to perform tasks such as registering a person, managing courses, or viewing your transcript.

### Example Scenarios
-   **Admin:** To add a new student, navigate to `User Management > Register Person`, fill in the details with the "Student" role, then go to `Manage User Accounts` to create a login for that student.
-   **Student:** To register for a course, log in, go to `Course Registration`, select a semester, and enroll in an available course.

## 8. Testing Guide

1.  **Authentication:**
    -   Attempt to log in with valid and invalid credentials for each role.
    -   Verify that incorrect credentials result in an error message and access is denied.
2.  **Authorization:**
    -   Log in as each role (Admin, Faculty, Student, Registrar) and verify that only the permitted functionalities are visible and accessible.
    -   Attempt to access unauthorized features (e.g., a student trying to access the admin analytics dashboard). Access should be denied.
3.  **Functionality Testing:**
    -   **Admin:** Create, update, and delete a user for each role. Verify that the changes are reflected in the user tables.
    -   **Student:** Register for a course and verify that it appears in your coursework.

## 9. Challenges & Solutions

-   **Challenge:** Significant code duplication in the UI panels for managing different user types (Faculty, Student, Registrar).
    -   **Solution:** Refactored the duplicated panels into a single, generic `ManageUsersByTypeJPanel` and `UpdateAccountInfoJPanel`, which dynamically handle different user roles. This reduced code redundancy and improved maintainability.
-   **Challenge:** The user interface felt outdated and was not intuitive to navigate.
    -   **Solution:** Redesigned the main admin dashboard using a `JTabbedPane` to group related functionalities, creating a more modern and user-friendly navigation experience.
-   **Challenge:** Potential for data loss when an admin changes a user's role.
    -   **Solution:** Implemented a confirmation dialog to warn the admin that changing a role will erase the existing profile data, preventing accidental data loss.

## 10. Future Enhancements

-   **Database Integration:** Replace the in-memory data structures with a persistent database (e.g., MySQL, PostgreSQL) to ensure data is saved between sessions.
-   **Web-Based Interface:** Develop a web-based version of the application to make it accessible from any device.
-   **Enhanced Security:** Implement more robust security measures, such as password hashing and encryption of sensitive data.
-   **More Detailed Analytics:** Expand the analytics dashboard with more detailed reports and visualizations.

## 11. Contribution Breakdown

| Team Member | Contributions |
| --- | --- |
| Ing-Ruei | - Project management and coordination.<br>- Admin role development and UI/UX optimization.<br>- Bug fixing and documentation. |
| Hao-Chun | - Student role development.<br>- Implemented student enrollment and financial management features.<br>- Contributed to testing. |
| Shuai | - Faculty role development.<br>- Implemented course and semester management features.<br>- Contributed to testing. |
| Wanyu | - Registrar role development.<br>- Implemented tuition reconciliation and course creation features.<br>- Contributed to testing. |
