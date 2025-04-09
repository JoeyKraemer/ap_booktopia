# Booktopia

Booktopia is an intelligent book management system that demonstrates the practical application of various data structures and algorithms. The system allows users to import, manipulate, visualize, and search book data using different tree-based data structures, showcasing real-world applications of algorithmic efficiency.

## Project Overview

# Background
This project is a NHL Stenden student project for the course 3.3 **Algorithmic Programming** to learn the fundamentals of algorithms and data structures.

### Project Team
- Caterina Aresti
- Joey Krämer
- Nicanor Martinez

## Data Structures Implemented

The project allows users to dynamically switch between three tree-based data structures for storing and manipulating book data:

1. **Binary Search Tree (BST)** - A basic binary tree where each node has at most two children, with all nodes in the left subtree having values less than the node's key, and all nodes in the right subtree having values greater than the node's key.

2. **AVL Tree** - A self-balancing binary search tree where the heights of the two child subtrees of any node differ by at most one, ensuring O(log n) operations.

3. **B-Tree** - A self-balancing tree data structure that maintains sorted data and allows searches, sequential access, insertions, and deletions in logarithmic time.

Each tree implementation handles operations such as insertion, deletion, and searching with different efficiency characteristics, which are displayed in the UI for comparison.

## Algorithms Implemented

- **Tree-Specific Search Algorithms** - Each tree type implements its own optimized search algorithm
- **Tree Conversion Algorithms** - Algorithms for converting between different tree types while preserving data
- **Sorting Algorithms** - Various sorting implementations for displaying and organizing book data
- **CSV Parsing and Data Import** - Algorithms for efficiently parsing and importing book data

## Core Features

- **Multiple Tree Data Structures** - Switch between BST, AVL, and B-Tree implementations to compare performance
- **Performance Metrics** - View processing time (in milliseconds) for various operations
- **Data Import** - Import book data from CSV files
- **Sorting Options** - Sort books by various properties (title, author, rating, etc.)
- **REST API** - Access all functionality through a RESTful API

## Technical Stack

- **Backend**: Java Spring Boot
- **Frontend**: React
- **Build Tool**: Gradle
- **Data Format**: JSON for data exchange, CSV for data import

## Setup and Installation

### Prerequisites
- Java 11 or higher
- Node.js 16.x or higher (required for frontend)
  - On macOS: Install via Homebrew (`brew install node`) or download from [nodejs.org](https://nodejs.org/)
  - Verify installation with `node -v` and `npm -v`
- Gradle

### Backend Setup
1. Clone the repository
```
git clone https://github.com/yourusername/booktopia.git
cd booktopia
```

2. Build and run the backend
```
cd backend
./gradlew bootRun
// or run it in your IDE
```
The server will start on http://localhost:8080

### Frontend Setup
```
cd frontend
npm install
npm run dev
```
The frontend development server will start on http://localhost:3000

## Using the Application

### Importing Data

1. **CSV Format**: Prepare your CSV file with a header row. The first column will be used as the key.
   Example format:
   ```
   title,author,rating,isbn,pages,year
   "To Kill a Mockingbird","Harper Lee",4.3,9780061120084,324,1960
   "1984","George Orwell",4.2,9780451524935,328,1949
   ```

2. **Import Methods**:
   - **Through the UI**: Use the "Import Data" button in the UI and select your CSV file
   - **Through the API**: Send a POST request to `/api/data/import` with the CSV file as a multipart form data
   - **Using Postman for CSV Import**:
    1. Open Postman and create a new POST request to `{{baseUrl}}/api/data/import-csv`
    2. Select the "Body" tab and choose "form-data"
    3. Add a key named "file" and set the type to "File"
    4. Click "Select Files" and choose your CSV file
    5. Send the request
    6. The response will include:
    - Processing time in milliseconds
    - Success status
    - Count of imported records
    - A message confirming successful import

3. **Import Process**:
   - The system reads the CSV header to determine field names
   - Each row is converted to a JSON object with properties matching the header names
   - Data is inserted into the current active tree structure (BST, AVL, or B-Tree)
   - The UI will display a success message with the number of records imported and processing time

4. **Performance Note**:
   - **File Size Limitation**: 
     - CSV files cannot exceed 50MB in size
     - For optimal frontend performance, limit CSV files to 500 lines or fewer
   - While the backend tree structures can handle larger datasets efficiently, the frontend is not optimized for very large datasets
   - You can test larger datasets using Postman and the backend API directly
   - With large datasets, frontend operations like initial loading and sorting will be noticeably slower, but the application will still function correctly

5. **Sample Data**:
   - Sample CSV files are available in the `backend/src/main/resources` folder
   - Files prefixed with `short_` (e.g., `short_books.csv`, `short_movies.csv`) are optimized for testing with the frontend
   - These sample files can be used to quickly test the import functionality and tree operations

### Tree Conversions

1. Use the tree type selector in the UI to convert between different tree implementations
2. The system will:
   - Extract all key-value pairs from the current tree
   - Create a new tree of the selected type
   - Insert all data into the new tree
   - Display performance metrics for the conversion

### Searching

1. **Exact Key Search**: Enter a book title (or other key) to find an exact match
2. **Linear Search**: Search across all fields for partial matches
3. The system uses:
   - Tree-specific search algorithms for exact key matches
   - Comprehensive search for partial matches across all fields

### Viewing and Sorting Data

1. Sort data by any available field
2. Performance metrics are displayed for all operations

## API Documentation

The API endpoints can be easily explored and tested using the included Postman collection:

- Import the `postman.json` file into Postman to access all API endpoints with examples
- The collection includes pre-configured requests for all operations

## API Endpoints

### Data Operations
- `GET /api/data/tree-type` - Get current tree type
- `POST /api/data/tree/avl` - Convert to AVL Tree
- `POST /api/data/tree/bst` - Convert to BST
- `POST /api/data/tree/btree` - Convert to B-Tree
- `GET /api/data/keys` - Get all keys
- `GET /api/data/values` - Get all values
- `POST /api/data` - Add new data
- `DELETE /api/data/{key}` - Remove data by key
- `GET /api/data/{key}` - Search for specific key

### Display Operations
- `GET /api/display/table` - Get data formatted for table display
- `GET /api/display/cards` - Get data formatted for card display
- `GET /api/display/sort/{field}` - Sort data by field
- `GET /api/display/properties` - Get sortable properties

### Search Operations
- `GET /api/search` - Search across all data

### Import Operations
- `POST /api/import/csv` - Import data from CSV

## Performance Considerations

- All operations track and display processing time in milliseconds
- The different tree implementations offer different performance characteristics:
  - **BST**: Simple but can become unbalanced with certain insertion patterns
  - **AVL**: Maintains balance but has higher overhead for insertions and deletions
  - **B-Tree**: Excellent for large datasets and disk-based storage

## Project Structure

```
booktopia/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/nhlstenden/booktopia/
│   │   │   │       ├── AVL/           # AVL Tree implementation
│   │   │   │       ├── BST/           # Binary Search Tree implementation
│   │   │   │       ├── btree/         # B-Tree implementation
│   │   │   │       ├── controller/    # REST controllers
│   │   │   │       └── services/      # Business logic services
│   │   │   └── resources/
│   │   └── test/
│   └── build.gradle
├── frontend/         # React frontend
└── README.md
```

## Credits

- Book data sample from [Kaggle/GoodReads Books](https://www.kaggle.com/datasets/jealousleopard/goodreadsbooks)
