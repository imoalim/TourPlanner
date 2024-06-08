# TourPlanner

# 🎋 Intro:
This project is a TourPlanner application that helps users manage and organize tours. It provides functionalities to add, edit, delete, and view tours, along with the ability to add tour logs. The application also includes a search feature to filter tours by name. The backend is powered by Spring Boot to handle the server-side operations.

# 📦 Technologies:
- **Frontend**:
  - **Angular**: Frontend framework for building dynamic web applications.
  - **Bootstrap**: CSS framework for responsive design.
  - **NgBootstrap**: Angular implementation of Bootstrap components.
  - **ngx-toastr**: Library for displaying toast notifications.
  - **TypeScript**: Language for building robust and scalable applications.
  - **HTML/CSS**: Standard web technologies for structuring and styling the application.
- **Backend**:
  - **Spring Boot**: Framework for building production-ready applications.
  - **Java**: Programming language used for the backend logic.
  - **Spring Data JPA**: For database interactions.
  - **H2 Database**: In-memory database for development and testing.
  - **Spring Security**: For securing the application.

# 👩🏽‍🍳 Features:
- **Add/Edit/Delete Tours**: Manage your tours efficiently with options to add, edit, and delete tours.
- **View Tour Details**: View detailed information about each tour.
- **Add Tour Logs**: Keep track of tour activities by adding logs.
- **Search Functionality**: Easily search for tours by name.
- **Tooltips**: Enhanced user experience with tooltips for better navigation.
- **User Authentication**: Secure the application with user authentication (upcoming feature).

# 💭 Process:
1. **Project Setup**: Initialized Angular and Spring Boot projects and installed necessary dependencies.
2. **Component Development**: Created and structured components for managing tours and logs.
3. **API Development**: Developed RESTful APIs with Spring Boot for managing tour data.
4. **Styling**: Applied responsive design using Bootstrap and custom CSS.
5. **Feature Integration**: Integrated CRUD functionalities, search feature, and backend APIs.
6. **Testing**: Tested the application to ensure all features work as expected.
7. **Refinement**: Refined the UI and improved user experience with tooltips and notifications.

# 📚 Learnings:
- **Angular**: Deepened understanding of Angular framework and its powerful features.
- **Spring Boot**: Gained experience in building and deploying Spring Boot applications.
- **Bootstrap**: Enhanced skills in creating responsive designs.
- **Component-Based Architecture**: Learned to design and develop a modular and maintainable codebase.
- **Async Operations**: Gained experience in handling asynchronous operations and API integration.
- **User Experience**: Improved skills in enhancing user experience with tooltips and notifications.

# ✨ Improvements:
- **Authentication**: Add user authentication to secure the application.
- **Pagination**: Implement pagination for better management of large tour lists.
- **Advanced Search**: Enhance the search functionality to include more filters.
- **Performance Optimization**: Optimize the application for better performance and faster load times.
- **UI Enhancements**: Further refine the UI for a more modern and intuitive design.

# 🚦 Running the Project: 

## Frontend (Angular):
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-username/tourplanner.git
2. Navigate to Project Directory:
   cd tourplanner/frontend

3. Install Dependencies:
   npm install
4. Run the Application:
   ng serve

Open your browser and navigate to http://localhost:4200

## Backend (Spring Boot):
1. Navigate to Backend Directory:
   cd tourplanner/backend
2. Build the Project:
   ./mvnw clean install
3. Run the Application:
   ./mvnw spring-boot:run
   
The backend server will start on http://localhost:8080
