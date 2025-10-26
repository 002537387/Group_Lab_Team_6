# Digital University System

## Project Overview

The Digital University System is a comprehensive desktop application designed to manage various aspects of a university's operations. It provides functionalities for administrators, faculty, students, and registrars to handle user accounts, course management, student registration, financial records, and analytics.

## Features

### Admin Role
- **User Management:** Register new persons (Admin, Faculty, Student, Registrar) and manage their user accounts (create, update, delete).
- **Profile Management:** Manage faculty, student, and registrar profiles.
- **System Analytics:** View university-level analytics, including user statistics, courses offered, enrollment per course, and tuition revenue.
- **Personal Profile:** Update administrator's own profile information.

### Faculty Role
- Course Management
- Student Management
- Personal Profile Management

### Student Role
- Course Registration
- Coursework Management
- Financial Management
- Graduation Audit
- Transcript Review
- Personal Profile Management

### Registrar Role
- Course Creation
- Student Registration Management
- Tuition Reconciliation
- Personal Profile Management

## Technologies Used

- **Language:** Java
- **UI Framework:** Java Swing
- **Build Tool:** Ant (based on `build.xml`)

## Setup Instructions

To set up the project locally, follow these steps:

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/Group_Lab_Team_6/DigitalUniversitySystem.git
    cd DigitalUniversitySystem
    ```

2.  **Ensure Java Development Kit (JDK) is installed:**
    This project requires JDK 8 or higher. You can download it from the official Oracle website or use a package manager.

3.  **Set up your IDE (e.g., Apache NetBeans, IntelliJ IDEA, Eclipse):**
    *   **NetBeans:** Open the project directly using `File > Open Project` and navigate to the `DigitalUniversitySystem` directory.
    *   **IntelliJ IDEA/Eclipse:** Import the project as an existing Ant project or a general Java project. You might need to configure the JDK path manually.

## How to Run

### From IDE

1.  Open the project in your IDE.
2.  Locate the main class (e.g., `Business.Configure.java` or `Business.ProfileWorkAreaMainFrame.java` if it contains the `main` method for application startup).
3.  Run the main class directly from your IDE.

### From Command Line (using Ant)

1.  Navigate to the project root directory in your terminal:
    ```bash
    cd DigitalUniversitySystem
    ```
2.  Compile the project:
    ```bash
    ant compile
    ```
3.  Build the JAR file:
    ```bash
    ant jar
    ```
4.  Run the application (assuming `manifest.mf` correctly points to the main class):
    ```bash
    java -jar dist/DigitalUniversitySystem.jar
    ```
    *(Note: The exact JAR name and main class might vary based on `build.xml` and `manifest.mf` configurations.)*

## Usage

Upon launching the application, you will be presented with a login screen. Use the appropriate credentials for each role to access different functionalities:

-   **Admin:** (Credentials will be provided or can be configured in `Business.Configure.java`)
-   **Faculty:** (Credentials will be provided or can be configured)
-   **Student:** (Credentials will be provided or can be configured)
-   **Registrar:** (Credentials will be provided or can be configured)

Navigate through the system using the provided buttons and tabbed interfaces to perform various tasks relevant to your role.

## Contributing

If you wish to contribute to this project, please follow these steps:

1.  Fork the repository.
2.  Create a new branch for your feature or bug fix.
3.  Make your changes and ensure the code adheres to the existing style.
4.  Write appropriate tests for your changes.
5.  Submit a pull request with a clear description of your changes.

## License

[Specify your project's license here, e.g., MIT, Apache 2.0, etc. If no license is chosen, state "All Rights Reserved."]
