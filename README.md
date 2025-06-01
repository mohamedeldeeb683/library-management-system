# Library Management System - Spring Boot REST API

## Description
This project is a **Library Management System** developed using **Spring Boot**, offering a comprehensive set of **RESTful APIs** to manage core library operations. The system aims to streamline the tracking of books, members, authors, and borrowing transactions, all while providing a robust role-based security layer.

## Features
* **Comprehensive Book Management:** Add, view, update, and delete books with extensive metadata (authors, publishers, categories, ISBN, publication year, etc.).
* **Member Management:** Register and update borrower details.
* **User & System Management:** Control system users (administrators, librarians, staff) and define their roles and access permissions.
* **Borrowing & Return Functionality:** Record book borrowing transactions and track their status, including returns.
* **Robust Security:** Implements **Basic Authentication** and **Role-Based Access Control** using Spring Security, ensuring secure password storage.
* **Input Validation:** Guarantees the integrity of incoming API data, preventing errors and enhancing security.
* **Activity Logging:** Logs important user activities within the system (e.g., CRUD operations, borrowing, returning).
* **Interactive API Documentation:** Utilizes **Swagger UI** to provide comprehensive and interactive API documentation.

## Technologies Used
* **Backend Framework:** Java 17 / Spring Boot 3.5.0
* **Database:** H2 Database (In-Memory for development)
* **ORM:** Spring Data JPA & Hibernate
* **Security:** Spring Security
* **Developer Tools:** Project Lombok, Spring Boot DevTools
* **Build Tool:** Maven
* **API Documentation:** Springdoc OpenAPI (Swagger UI)

## Database Schema (ERD)
    https://github.com/mohamedeldeeb683
![](C:\Users\M.ELDEEB\Downloads\ERD.jpeg)
This diagram illustrates the core entities within the database and their relationships:


## How to Run the Application

To get this application up and running on your local machine, follow these steps:

### Prerequisites
Ensure you have the following installed:
* **Java Development Kit (JDK) 17 or newer**
* **Apache Maven 3.6+**
* **An Integrated Development Environment (IDE) like IntelliJ IDEA**

### Steps
1.  **Clone the Repository:**
    ```bash
    git clone <Your GitHub Repository Link Here>
    cd library-management-system
    ```
2.  **Open in IntelliJ IDEA:**
    * Launch IntelliJ IDEA.
    * Go to `File` -> `Open` and select the cloned project directory.
    * Upon opening, IntelliJ should automatically import Maven dependencies. If not, right-click on `pom.xml` and select `Maven` -> `Reload Project`.
3.  **Confirm Lombok Setup:**
    * In IntelliJ IDEA, navigate to `File` -> `Settings` (or `IntelliJ IDEA` -> `Settings` on macOS).
    * Go to `Build, Execution, Deployment` -> `Compiler` -> `Annotation Processors`.
    * Ensure that `Enable annotation processing` is checked.
4.  **Run the Application:**
    * Locate the `LibraryManagementSystemApplication.java` file in `src/main/java/com/code81/library/lms`.
    * Right-click on this file and select `Run 'LibraryManagementSystemApplication.main()'`.
    * The application will start on port `8081` by default.

## API Endpoints & Usage

You can interact with the APIs using tools like **Postman** or through the **Swagger UI**.

### Base API URL
`http://localhost:8081/api`

### H2 Console (Database Access)
You can access the H2 database console to browse the data:
* **URL:** `http://localhost:8081/h2-console`
* **JDBC URL:** `jdbc:h2:mem:librarydb`
* **User Name:** `sa`
* **Password:** (Leave blank)

### Authentication
Most APIs require authentication using **Basic Authentication**. You can use the following default user credentials (loaded from `src/main/resources/schema.sql`):

| Username     | Password   | Role          |
| :----------- | :--------- | :------------ |
| `admin`      | `adminpass`| ADMINISTRATOR |
| `librarian1` | `libpass`  | LIBRARIAN     |
| `staff1`     | `staffpass`| STAFF         |

### Interactive API Documentation (Swagger UI)

To access the interactive API documentation and test endpoints directly:
* **URL:** `http://localhost:8081/swagger-ui.html`
* Click the **"Authorize"** button and enter the credentials (Username/Password) for the users listed above.

### Example Endpoints

**Authors**
* `GET /api/authors` - Retrieve all authors (Requires Authentication)
* `GET /api/authors/{id}` - Retrieve an author by ID (Requires Authentication)
* `POST /api/authors` - Add a new author (Requires ADMINISTRATOR role)
* `PUT /api/authors/{id}` - Update an author (Requires ADMINISTRATOR role)
* `DELETE /api/authors/{id}` - Delete an author (Requires ADMINISTRATOR role)

**Books**
* `GET /api/books` - Retrieve all books (Requires Authentication)
* `POST /api/books` - Add a new book (Requires ADMINISTRATOR, LIBRARIAN, or STAFF role)
* `PUT /api/books/{id}` - Update a book (Requires ADMINISTRATOR, LIBRARIAN, or STAFF role)
* `DELETE /api/books/{id}` - Delete a book (Requires ADMINISTRATOR role)

**Members**
* `GET /api/members` - Retrieve all members (Requires ADMINISTRATOR, LIBRARIAN, or STAFF role)
* `POST /api/members` - Add a new member (Requires LIBRARIAN role)

*(**Note:** Other endpoints for Publishers, Categories, Borrowing Transactions follow a similar pattern with appropriate role-based access control.)*

## Design Choices

* **Spring Boot:** Chosen for its rapid application development capabilities and simplicity in setting up RESTful services.
* **H2 Database:** Utilized as an in-memory database for ease of setup and fast development cycles, eliminating the need for an external database installation.
* **Spring Security:** Provides a robust and comprehensive security solution, enabling efficient authentication and role-based access control.
* **Lombok:** Used to reduce boilerplate code (e.g., Getters, Setters, Constructors), making the codebase cleaner and more readable.
* **JPA & Hibernate:** Offer a powerful abstraction layer for database interaction, simplifying CRUD operations and entity management.
* **Complex Relationships (Many-to-Many):** Handled effectively using junction tables (e.g., `book_author`, `book_category`) to ensure data integrity and design flexibility.

## Submission Notes

This project has been developed for code 81 .