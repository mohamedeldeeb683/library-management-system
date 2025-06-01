-- DDL (Data Definition Language) Script for H2 Database

-- Drop tables if they exist to allow for clean re-creation
-- Order matters due to foreign key constraints
DROP TABLE IF EXISTS borrowing_transaction;
DROP TABLE IF EXISTS book_author;
DROP TABLE IF EXISTS book_category;
DROP TABLE IF EXISTS book;
DROP TABLE IF EXISTS author;
DROP TABLE IF EXISTS publisher;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS member;
DROP TABLE IF EXISTS app_user;
DROP TABLE IF EXISTS role;

-- 1. Role Table
CREATE TABLE role (
                      id BIGINT PRIMARY KEY,
                      name VARCHAR(50) NOT NULL UNIQUE
);
-- تعريف SEQUENCE لجدول role
CREATE SEQUENCE role_seq START WITH 100 INCREMENT BY 1;

-- 2. AppUser Table
CREATE TABLE app_user (
                          id BIGINT PRIMARY KEY,
                          username VARCHAR(100) NOT NULL UNIQUE,
                          password_hash VARCHAR(255) NOT NULL,
                          email VARCHAR(100) UNIQUE,
                          role_id BIGINT NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          last_login_at TIMESTAMP,
                          FOREIGN KEY (role_id) REFERENCES role(id)
);
-- تعريف SEQUENCE لجدول app_user
CREATE SEQUENCE app_user_seq START WITH 100 INCREMENT BY 1;

-- 3. Publisher Table
CREATE TABLE publisher (
                           id BIGINT PRIMARY KEY,
                           name VARCHAR(255) NOT NULL UNIQUE,
                           address VARCHAR(255),
                           contact_info VARCHAR(255)
);
-- تعريف SEQUENCE لجدول publisher
CREATE SEQUENCE publisher_seq START WITH 100 INCREMENT BY 1;

-- 4. Author Table
CREATE TABLE author (
                        id BIGINT PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        biography CLOB
);
-- تعريف SEQUENCE لجدول author
CREATE SEQUENCE author_seq START WITH 100 INCREMENT BY 1;

-- 5. Category Table
CREATE TABLE category (
                          id BIGINT PRIMARY KEY,
                          name VARCHAR(255) NOT NULL UNIQUE,
                          parent_category_id BIGINT,
                          FOREIGN KEY (parent_category_id) REFERENCES category(id)
);
-- تعريف SEQUENCE لجدول category
CREATE SEQUENCE category_seq START WITH 100 INCREMENT BY 1;

-- 6. Book Table
CREATE TABLE book (
                      id BIGINT PRIMARY KEY,
                      title VARCHAR(255) NOT NULL,
                      isbn VARCHAR(20) UNIQUE,
                      publication_year INT,
                      edition VARCHAR(50),
                      summary CLOB,
                      cover_image_url VARCHAR(255),
                      language VARCHAR(50),
                      publisher_id BIGINT,
                      available BOOLEAN NOT NULL DEFAULT TRUE,
                      FOREIGN KEY (publisher_id) REFERENCES publisher(id)
);
-- تعريف SEQUENCE لجدول book
CREATE SEQUENCE book_seq START WITH 100 INCREMENT BY 1;

-- 7. Book_Author Junction Table
CREATE TABLE book_author (
                             book_id BIGINT NOT NULL,
                             author_id BIGINT NOT NULL,
                             PRIMARY KEY (book_id, author_id),
                             FOREIGN KEY (book_id) REFERENCES book(id) ON DELETE CASCADE,
                             FOREIGN KEY (author_id) REFERENCES author(id) ON DELETE CASCADE
);

-- 8. Book_Category Junction Table
CREATE TABLE book_category (
                               book_id BIGINT NOT NULL,
                               category_id BIGINT NOT NULL,
                               PRIMARY KEY (book_id, category_id),
                               FOREIGN KEY (book_id) REFERENCES book(id) ON DELETE CASCADE,
                               FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE
);

-- 9. Member Table
CREATE TABLE member (
                        id BIGINT PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        address VARCHAR(255),
                        phone VARCHAR(20) UNIQUE,
                        email VARCHAR(100) UNIQUE,
                        registration_date DATE DEFAULT CURRENT_DATE
);
-- تعريف SEQUENCE لجدول member
CREATE SEQUENCE member_seq START WITH 100 INCREMENT BY 1;

-- 10. Borrowing_Transaction Table
CREATE TABLE borrowing_transaction (
                                       id BIGINT PRIMARY KEY,
                                       book_id BIGINT NOT NULL,
                                       member_id BIGINT NOT NULL,
                                       app_user_id BIGINT,
                                       borrow_date DATE DEFAULT CURRENT_DATE,
                                       due_date DATE NOT NULL,
                                       return_date DATE,
                                       returned BOOLEAN NOT NULL DEFAULT FALSE,
                                       FOREIGN KEY (book_id) REFERENCES book(id),
                                       FOREIGN KEY (member_id) REFERENCES member(id),
                                       FOREIGN KEY (app_user_id) REFERENCES app_user(id)
);
-- تعريف SEQUENCE لجدول borrowing_transaction
CREATE SEQUENCE borrowing_transaction_seq START WITH 100 INCREMENT BY 1;


-- DML (Data Manipulation Language) Script for H2 Database
-- Insert sample data into tables
-
-- Insert Roles
INSERT INTO role (id, name) VALUES (1, 'ADMINISTRATOR');
INSERT INTO role (id, name) VALUES (2, 'LIBRARIAN');
INSERT INTO role (id, name) VALUES (3, 'STAFF');

-- Insert App Users
INSERT INTO app_user (id, username, password_hash, email, role_id, created_at) VALUES (1, 'admin', '$2a$10$6z7qZxY8YVn.IdwOOpoc3.rYfIhp.LuedziI6rL/VtYJd1Ld2rTFa', 'admin@example.com', 1, CURRENT_TIMESTAMP());
INSERT INTO app_user (id, username, password_hash, email, role_id, created_at) VALUES (2, 'librarian1', '$2a$10$3diHDKp1khvPaZw1JlIF5uEQvDNF9AtA1wh7tyK7t2/lULzKssQ1C', 'librarian1@example.com', 2, CURRENT_TIMESTAMP());
INSERT INTO app_user (id, username, password_hash, email, role_id, created_at) VALUES (3, 'staff1', '$2a$10$vJ8/icEY.K1NQMintJBMsOsnlJ7Rc7HKGDIqytgQ6lcVp1Ogln4xW', 'staff1@example.com', 3, CURRENT_TIMESTAMP());

-- Insert Publishers
INSERT INTO publisher (id, name, address, contact_info) VALUES (1, 'Penguin Random House', 'New York, USA', 'info@penguinrandomhouse.com');
INSERT INTO publisher (id, name, address, contact_info) VALUES (2, 'HarperCollins', 'London, UK', 'contact@harpercollins.com');
INSERT INTO publisher (id, name, address, contact_info) VALUES (3, 'Elsevier', 'Amsterdam, Netherlands', 'info@elsevier.com');

-- Insert Authors
INSERT INTO author (id, name, biography) VALUES (1, 'J.K. Rowling', 'British author, creator of Harry Potter.');
INSERT INTO author (id, name, biography) VALUES (2, 'Stephen King', 'American author of horror, suspense, science fiction, and fantasy novels.');
INSERT INTO author (id, name, biography) VALUES (3, 'Isaac Asimov', 'American writer and professor of biochemistry, known for his science fiction.');
INSERT INTO author (id, name, biography) VALUES (4, 'Carl Sagan', 'American astronomer, planetary scientist, cosmologist, astrophysicist, astrobiologist, author, and science communicator.');

-- Insert Categories (Hierarchical example)
INSERT INTO category (id, name, parent_category_id) VALUES (1, 'Fiction', NULL);
INSERT INTO category (id, name, parent_category_id) VALUES (2, 'Science', NULL);
INSERT INTO category (id, name, parent_category_id) VALUES (3, 'History', NULL);
INSERT INTO category (id, name, parent_category_id) VALUES (4, 'Fantasy', 1);
INSERT INTO category (id, name, parent_category_id) VALUES (5, 'Horror', 1);
INSERT INTO category (id, name, parent_category_id) VALUES (6, 'Science Fiction', 1);
INSERT INTO category (id, name, parent_category_id) VALUES (7, 'Physics', 2);
INSERT INTO category (id, name, parent_category_id) VALUES (8, 'Astronomy', 2);

-- Insert Books
INSERT INTO book (id, title, isbn, publication_year, edition, summary, cover_image_url, language, publisher_id, available) VALUES
    (1, 'Harry Potter and the Sorcerer''s Stone', '978-0-7475-3269-9', 1997, '1st', 'The first novel in the Harry Potter series.', 'http://example.com/hp1.jpg', 'English', 1, TRUE);
INSERT INTO book (id, title, isbn, publication_year, edition, summary, cover_image_url, language, publisher_id, available) VALUES
    (2, 'The Lord of the Rings', '978-0-618-26027-4', 1954, 'Special', 'An epic high-fantasy novel.', 'http://example.com/lotr.jpg', 'English', 1, TRUE);
INSERT INTO book (id, title, isbn, publication_year, edition, summary, cover_image_url, language, publisher_id, available) VALUES
    (3, 'Cosmos', '978-0-345-53943-4', 1980, 'Paperback', 'A journey through space and time.', 'http://example.com/cosmos.jpg', 'English', 2, TRUE);
INSERT INTO book (id, title, isbn, publication_year, edition, summary, cover_image_url, language, publisher_id, available) VALUES
    (4, 'Dune', '978-0-441-17271-9', 1965, 'Paperback', 'A science fiction masterpiece.', 'http://example.com/dune.jpg', 'English', 2, TRUE);

-- Insert Book_Author relationships
INSERT INTO book_author (book_id, author_id) VALUES (1, 1);
INSERT INTO book_author (book_id, author_id) VALUES (3, 4);
INSERT INTO book_author (book_id, author_id) VALUES (4, 3);

-- Insert Book_Category relationships
INSERT INTO book_category (book_id, category_id) VALUES (1, 1);
INSERT INTO book_category (book_id, category_id) VALUES (1, 4);
INSERT INTO book_category (book_id, category_id) VALUES (2, 1);
INSERT INTO book_category (book_id, category_id) VALUES (2, 4);
INSERT INTO book_category (book_id, category_id) VALUES (3, 2);
INSERT INTO book_category (book_id, category_id) VALUES (3, 8);
INSERT INTO book_category (book_id, category_id) VALUES (4, 1);
INSERT INTO book_category (book_id, category_id) VALUES (4, 6);

-- Insert Members
INSERT INTO member (id, name, address, phone, email, registration_date) VALUES (1, 'Ahmed Hassan', '123 Nasr St, Cairo', '01012345678', 'ahmed@example.com', CURRENT_DATE());
INSERT INTO member (id, name, address, phone, email, registration_date) VALUES (2, 'Fatma Ali', '45 Mohandessin Sq, Giza', '01123456789', 'fatma@example.com', CURRENT_DATE());

-- Insert Borrowing Transactions (example)
INSERT INTO borrowing_transaction (id, book_id, member_id, app_user_id, borrow_date, due_date, return_date, returned) VALUES
    (1, 1, 1, 2, CURRENT_DATE(), DATEADD('DAY', 14, CURRENT_DATE()), NULL, FALSE);
INSERT INTO borrowing_transaction (id, book_id, member_id, app_user_id, borrow_date, due_date, return_date, returned) VALUES
    (2, 3, 2, 3, DATEADD('DAY', -30, CURRENT_DATE()), DATEADD('DAY', -16, CURRENT_DATE()), DATEADD('DAY', -10, CURRENT_DATE()), TRUE);
INSERT INTO borrowing_transaction (id, book_id, member_id, app_user_id, borrow_date, due_date, return_date, returned) VALUES
    (3, 4, 1, 2, DATEADD('DAY', -30, CURRENT_DATE()), DATEADD('DAY', -16, CURRENT_DATE()), NULL, FALSE);