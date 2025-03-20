use booktopia_db;

COPY books(book_id, title, authors, average_rating, isbn, isbn13, language_code, num_pages,
    ratings_count, text_reviews_count, publication_date, publisher)
FROM 'database/books.csv' DELIMITER ',' CSV HEADER;