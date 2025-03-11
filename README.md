# Booktopia
Booktopia is a intelligent book recommendation system inspired by StoryGraph. The system will use multiple data structures and algorithmis to analyze user preferences and reading habits, ultimately providing personalized book suggestions. Users will be able to import book datasets, manipulate data structures, and execute sorting and filtering algorithms through an interactive GUI.

# Background
This project is a NHL Stenden student project for the course 3.3 **Algorithmic Programming** to learn the fundamentals of algorithms and data structures.

## Project Team
- Caterina Aresti
- Joey Krämer
- Nicanor Martinez

# Data Structure and Algorithm applied in the project

## Data Structure
- Binary Search Tree (BST) for storing books by title.
- HashMap for storing user information.
- LinkedList for maintaining reading order and recommendations.
## Algorithm
- Binary Search for searching within the BST.
- Linear Search for searching user information in the HashMap.
- Quick Sort for sorting the list of books.
- Merge Sort for sorting books by rating, number of pages, or publication date.

# Core Features
- Find books by title, author, or genre.
- Sort books by title, author, or publication date.
- Rate books
- Filter books by genre, rating, or number of pages.
- Personalized book suggestions based on user preferences and reading habits.

# Resources

## Data Set
- https://www.kaggle.com/datasets/jealousleopard/goodreadsbooks

**Library Management System**
- **Data Structures**:
- Binary Search Tree (BST) for storing books by title.
- Hash Table for storing user information.
- Linked List for managing book loans.
- **Algorithms**:
- Search algorithms: Binary Search (for BST) and Linear Search (for the hash table).
- Sorting algorithms: Quick Sort (for sorting books by title) and Merge Sort (for sorting users by loan date).
- **Extension**: Implement a visualization of the sorting and searching processes.



### Extended Features and Functionalities

#### 1. **User Roles and Authentication**

- **User Types**: Implement different user roles such as Admin, Librarian, and Member.
- **Authentication**: Allow users to register and log in. Use the Hash Table to store user credentials securely (e.g., hashed passwords).

#### 2. **Book Management**

- **Add Books**: Admins can add new books to the library, which will be stored in the BST by title.
- **Remove Books**: Admins can remove books from the library.
- **Update Book Information**: Admins can update details such as author, genre, and availability status.

#### 3. **User Management**

- **View User Profiles**: Librarians can view user profiles, including loan history and current loans.
- **Update User Information**: Users can update their personal information (e.g., contact details).

#### 4. **Book Loans**

- **Loan Books**: Members can borrow books, which will be managed using a Linked List to track current loans.
- **Return Books**: Members can return books, and the system will update the loan status.
- **Loan History**: Maintain a history of loans for each user, which can be accessed by librarians.

#### 5. **Search Functionality**

- **Search Books**: Implement a search feature that allows users to find books by title, author, or genre using the Binary Search Tree and Hash Table.
- **Search Users**: Allow librarians to search for users by name or ID using the Hash Table.

#### 6. **Sorting Functionality**

- **Sort Books**: Provide options to sort books by title, author, or publication date using Quick Sort.
- **Sort Users**: Allow sorting of users by loan date or name using Merge Sort.

#### 7. **Visualization**

- **Algorithm Visualization**: Create visual representations of the sorting and searching processes. This could include:
    - Animations showing how the BST is traversed during a search.
    - Graphical representation of the sorting process, showing how elements are moved and compared.
- **Statistics Dashboard**: Display statistics such as the total number of books, total loans, and most popular genres.

#### 8. **Additional Features**

- **Notifications**: Implement a notification system to alert users about overdue books or new arrivals.
- **Recommendations**: Use a simple recommendation algorithm to suggest books based on user borrowing history.
- **Reports**: Generate reports for librarians, such as the most borrowed books, user activity, and overdue loans.

### Technical Implementation

#### 1. **Data Structures**

- **Binary Search Tree (BST)**: Implement methods for insertion, deletion, and searching for books.
- **Hash Table**: Use separate chaining or open addressing for collision resolution. Implement methods for adding, removing, and searching for users.
- **Linked List**: Implement methods for adding and removing loans, as well as traversing the list to display loan history.

#### 2. **Algorithms**

- **Search Algorithms**:
    - **Binary Search**: Implement for searching within the BST.
    - **Linear Search**: Implement for searching user information in the Hash Table.
- **Sorting Algorithms**:
    - **Quick Sort**: Implement for sorting the list of books.
    - **Merge Sort**: Implement for sorting user loan history.

#### 3. **Graphical User Interface (GUI)**

- Use a GUI framework (e.g., JavaFX, Tkinter) to create an intuitive interface.
- Design separate views for different user roles (Admin, Librarian, Member).
- Include forms for adding and updating books and users, as well as buttons for searching and sorting.

### Example Workflow

1. **User Registration/Login**: A user registers or logs in to the system.
2. **Book Search**: The user searches for a book by title using the search feature.
3. **Book Loan**: If the book is available, the user can borrow it, which updates the Linked List of loans.
4. **Sorting and Visualization**: The user can choose to sort the list of books or view the visualization of the sorting/searching process.
5. **Return Process**: The user returns the book, and the system updates the loan status.

### Conclusion



This extended Library Management System project not only meets the assignment requirements but also provides a rich set of features that can be further enhanced. The combination of data structures, algorithms, and a user-friendly GUI will create a comprehensive application that demonstrates your understanding of algorithms and data structures while also being practical and useful.




# Booktopia
Booktopia is a library management system that allows users to find books, track pages and rate books.

# Background
This project is a NHL Stenden student project for the course 3.3 **Algorithmic Programming** to learn the fundamentals of algorithms and data structures.

## Project Team
- Caterina Aresti
- Joey Krämer
- Nicanor Martinez

# Data Structure and Algorithm applied in the project

## Data Structure
- Binary Search Tree (BST) for storing books by title.
- Hash Table for storing user information.
-
## Algorithm
- Search algorithms: Binary Search (for BST) and Linear Search (for the hash table).
- Sorting algorithms: Quick Sort (for sorting books by title)

**Library Management System**
- **Data Structures**:
- Binary Search Tree (BST) for storing books by title.
- Hash Table for storing user information.
- Linked List for managing book loans.
- **Algorithms**:
- Search algorithms: Binary Search (for BST) and Linear Search (for the hash table).
- Sorting algorithms: Quick Sort (for sorting books by title) and Merge Sort (for sorting users by loan date).
- **Extension**: Implement a visualization of the sorting and searching processes.



### Extended Features and Functionalities

#### 1. **User Roles and Authentication**

- **User Types**: Implement different user roles such as Admin, Librarian, and Member.
- **Authentication**: Allow users to register and log in. Use the Hash Table to store user credentials securely (e.g., hashed passwords).

#### 2. **Book Management**

- **Add Books**: Admins can add new books to the library, which will be stored in the BST by title.
- **Remove Books**: Admins can remove books from the library.
- **Update Book Information**: Admins can update details such as author, genre, and availability status.

#### 3. **User Management**

- **View User Profiles**: Librarians can view user profiles, including loan history and current loans.
- **Update User Information**: Users can update their personal information (e.g., contact details).

#### 4. **Book Loans**

- **Loan Books**: Members can borrow books, which will be managed using a Linked List to track current loans.
- **Return Books**: Members can return books, and the system will update the loan status.
- **Loan History**: Maintain a history of loans for each user, which can be accessed by librarians.

#### 5. **Search Functionality**

- **Search Books**: Implement a search feature that allows users to find books by title, author, or genre using the Binary Search Tree and Hash Table.
- **Search Users**: Allow librarians to search for users by name or ID using the Hash Table.

#### 6. **Sorting Functionality**

- **Sort Books**: Provide options to sort books by title, author, or publication date using Quick Sort.
- **Sort Users**: Allow sorting of users by loan date or name using Merge Sort.

#### 7. **Visualization**

- **Algorithm Visualization**: Create visual representations of the sorting and searching processes. This could include:
    - Animations showing how the BST is traversed during a search.
    - Graphical representation of the sorting process, showing how elements are moved and compared.
- **Statistics Dashboard**: Display statistics such as the total number of books, total loans, and most popular genres.

#### 8. **Additional Features**

- **Notifications**: Implement a notification system to alert users about overdue books or new arrivals.
- **Recommendations**: Use a simple recommendation algorithm to suggest books based on user borrowing history.
- **Reports**: Generate reports for librarians, such as the most borrowed books, user activity, and overdue loans.

### Technical Implementation

#### 1. **Data Structures**

- **Binary Search Tree (BST)**: Implement methods for insertion, deletion, and searching for books.
- **Hash Table**: Use separate chaining or open addressing for collision resolution. Implement methods for adding, removing, and searching for users.
- **Linked List**: Implement methods for adding and removing loans, as well as traversing the list to display loan history.

#### 2. **Algorithms**

- **Search Algorithms**:
    - **Binary Search**: Implement for searching within the BST.
    - **Linear Search**: Implement for searching user information in the Hash Table.
- **Sorting Algorithms**:
    - **Quick Sort**: Implement for sorting the list of books.
    - **Merge Sort**: Implement for sorting user loan history.

#### 3. **Graphical User Interface (GUI)**

- Use a GUI framework (e.g., JavaFX, Tkinter) to create an intuitive interface.
- Design separate views for different user roles (Admin, Librarian, Member).
- Include forms for adding and updating books and users, as well as buttons for searching and sorting.

### Example Workflow

1. **User Registration/Login**: A user registers or logs in to the system.
2. **Book Search**: The user searches for a book by title using the search feature.
3. **Book Loan**: If the book is available, the user can borrow it, which updates the Linked List of loans.
4. **Sorting and Visualization**: The user can choose to sort the list of books or view the visualization of the sorting/searching process.
5. **Return Process**: The user returns the book, and the system updates the loan status.

### Conclusion

This extended Library Management System project not only meets the assignment requirements but also provides a rich set of features that can be further enhanced. The combination of data structures, algorithms, and a user-friendly GUI will create a comprehensive application that demonstrates your understanding of algorithms and data structures while also being practical and useful.

Project Title: Intelligent Book Recommendation System

Project Overview:
This project aims to develop an intelligent book recommendation system inspired by StoryGraph. The system will use multiple data structures and algorithms to analyze user preferences and reading habits, ultimately providing personalized book suggestions. Users will be able to import book datasets, manipulate data structures, and execute sorting and filtering algorithms through an interactive GUI.

Key Features:

Data Structures:

Implement three distinct data structures to store and manage book data (e.g., Linked List, Binary Search Tree, HashMap).
Include essential operations such as adding, retrieving, and deleting books.
Use generic programming for flexibility.
Enable object comparison based on different attributes (e.g., title, author, rating).
Algorithms:

Implement at least four functional algorithms.
Sorting algorithms will work with custom comparators and object-based comparisons (e.g., sorting by rating, number of pages, or popularity).
Algorithms will support generic types for broader applicability.
Graphical User Interface (GUI):

Provide an intuitive interface for importing datasets and converting them into different data structures.
Enable users to select and execute various algorithms via the GUI.
Display real-time data about sorting speed, dataset size, chosen data structure, and algorithm performance.
Dataset Usage:

Convert provided book datasets into objects of a predefined class.
Ensure these objects are usable across all implemented data structures.
Apply various sorting and filtering techniques to refine recommendations.
Expected Outcome:
A fully functional book recommendation system where users can explore books, analyze reading trends, and receive personalized suggestions based on structured data and efficient algorithms. The system will meet all project requirements while delivering an interactive and insightful user experience.