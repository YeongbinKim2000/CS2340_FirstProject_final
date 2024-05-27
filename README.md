# CS2340_FirstProject_final

### Programming Languages
* Java (Android Studio)

### Description
The College Scheduler App is a mobile application designed to assist college students in managing their academic schedules efficiently. The app provides a straightforward 
and user-friendly interface that helps students keep track of their classes, assignments, and exams. By offering a variety of core functionalities, the app aims to enhance 
students' organization and focus on their academic commitments.

### Features
* **Class Schedule Management**: Easily add, edit, and delete calsses. View your weekly schedule at a glance.
* **Assignment Tracking**: Keep track of assignments with due dates, descriptions, and priority levels.
* **Exam Notifications**: Set reminders for upcoming exams.
* **User-Friendly Interface**: Intuitive design that makes it easy for students to navigate and use the app effectively.
* **Fragment-Based Architecture**: Modular design using fragments for better organization and maintenance.

### Project Structure
The project is structured with several key components to ensure modularity and ease of development. Below is a brief description of the main files:
#### Fragments
* **HomeFragment.java**: The main fragment displaying an overview of the student's schedule and upcoming tasks.
* **DashboardFragment.java**: Contains the dashboard interface where students can get a quick summary of their schedule and academic progress.
* **TodoFragment.java**: Manages the to-do list, allowing students to add and track their assignments and tasks.
#### ViewModels
* **DashboardViewModel.java**: Handles the logic and data for the DashboardFragment, ensuring the UI remains updated with the latest information.
* **ClassListViewModel.java**: Manages the data for the class list, handling the retrieval and organization of class information.
* **TodoViewModel.java**: Manages the to-do list data, keeping track of assignments and tasks.
#### Models
* **ClassItem.java**: The model class representing a single class item, including properties like class name, time, and location.
