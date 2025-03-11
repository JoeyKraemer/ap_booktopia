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

## GUI Library
- https://vuejs.org/

## How to Run
1. Clone the repository
2. configure  `src/main/resources/application.properties` with your database credentials.
3. Run `./gradlew bootRun`
4. Open `http://localhost:8080` in your browser

