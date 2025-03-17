# Incident Management System

## Overview
This is a JavaFX-based Incident Management System developed using **Java** and **JavaFX**. It provides a user-friendly interface for managing various community services, including facility maintenance, transport, incident reporting, and more.

## Features
- **Facility Maintenance Management**
- **Transport Service Management**
- **Community Service Tracking**
- **User Profiling**
- **Surveys and Feedback Collection**
- **Incident Reporting System**
- **Hyperlinks for External Resources (About, Contact, Help)**

## Technologies Used
- **Java 11+** (Programming Language)
- **JavaFX** (User Interface Development)
- **FXML** (Layout Design)
- **Scene Builder** (FXML Editor)
- **IntelliJ IDEA** (IDE for development)

## Installation & Setup
### Prerequisites
Ensure you have the following installed:
- [Java Development Kit (JDK) 11+](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- [JavaFX SDK](https://gluonhq.com/products/javafx/)
- [IntelliJ IDEA](https://www.jetbrains.com/idea/)
- [Scene Builder](https://gluonhq.com/products/scene-builder/)

### Steps to Run the Project
1. **Clone the repository:**
   ```sh
   git clone https://github.com/emkay3002/PMIS.git
   cd PMIS/backup
   ```
2. **Open the project in IntelliJ IDEA.**
3. **Configure JavaFX SDK:**
   - Go to **File → Project Structure → Libraries**
   - Add the **JavaFX SDK path**
4. **Run the Application:**
   - Open `Main.java` file
   - Click **Run** or use `Shift + F10`

## Project Structure
```
backup/
│── src/
│   ├── com/example/backup/
│   │   ├── Controller.java  # JavaFX Controller
│   │   ├── Main.java        # Application Entry Point
│   │   ├── Facility.fxml    # Facility Maintenance UI
│   │   ├── Transport.fxml   # Transport UI
│   │   ├── CommunityService.fxml # Community UI
│   │   ├── incident.fxml    # Incident Reporting UI
│   │   ├── Survey.fxml      # Surveys UI
│   │   ├── Profiling.fxml   # User Profiling UI
│   ├── assets/
│   │   ├── images/          # UI Images and Icons
│   │   ├── styles.css       # Styling for the UI
│── target/ (Generated files after build)
│── README.md (Project Documentation)
│── pom.xml (Maven Dependencies, if applicable)
```

## Troubleshooting
- **Issue:** `javafx.fxml.LoadException`
  - Ensure all `.fxml` files exist and are correctly referenced.
  - Verify the controller class matches the FXML declaration.
- **Buttons not working?**
  - Ensure the correct `@FXML` annotations are used.
  - Check the button ID matches the FXML file.
- **Application not running?**
  - Verify JavaFX libraries are configured in IntelliJ.


## Author
[Eman Khalid](https://www.linkedin.com/in/eman-khalid-b5b3a4216/)

