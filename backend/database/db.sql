-- Database creation script for book.csv
use booktopia_db;

CREATE TABLE books (
   book_id INTEGER PRIMARY KEY,
   title VARCHAR(255) NOT NULL,
   authors VARCHAR(255) NOT NULL,
   average_rating DECIMAL(3,2) NOT NULL,
   isbn VARCHAR(20),
   isbn13 VARCHAR(20),
   language_code VARCHAR(10),
   num_pages INTEGER,
   ratings_count INTEGER,
   text_reviews_count INTEGER,
   publication_date DATE,
   publisher VARCHAR(255)
);

-- Create indexes for common search fields
CREATE INDEX idx_books_title ON books(title);
CREATE INDEX idx_books_authors ON books(authors);
CREATE INDEX idx_books_isbn ON books(isbn);
CREATE INDEX idx_books_isbn13 ON books(isbn13);