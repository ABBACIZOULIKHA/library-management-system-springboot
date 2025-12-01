-- ========================
-- USERS TABLE
-- ========================
CREATE TABLE users
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    full_name  VARCHAR(100) NOT NULL,
    email      VARCHAR(120) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    role       ENUM ('ADMIN', 'LIBRARIAN', 'MEMBER') NOT NULL,
    status     ENUM ('ACTIVE', 'SUSPENDED') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id),
    CONSTRAINT unique_email UNIQUE (email)
);

-- ========================
-- AUTHORS TABLE
-- ========================
CREATE TABLE authors
(
    id   BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(100) NOT NULL,
    bio  TEXT,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

-- ========================
-- CATEGORIES TABLE
-- ========================
CREATE TABLE categories
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    name        VARCHAR(80) NOT NULL,
    description TEXT,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id),
    CONSTRAINT unique_category_name UNIQUE (name)
);

-- ========================
-- BOOKS TABLE
-- ========================
CREATE TABLE books
(
    id               BIGINT AUTO_INCREMENT NOT NULL,
    title            VARCHAR(200) NOT NULL,
    isbn             VARCHAR(20),
    publisher        VARCHAR(100),
    publish_year     INT,
    category_id      BIGINT,
    total_copies     INT DEFAULT 1,
    available_copies INT DEFAULT 1,
    shelf_location   VARCHAR(50),
    CONSTRAINT `PRIMARY` PRIMARY KEY (id),
    CONSTRAINT unique_isbn UNIQUE (isbn)
);

-- ========================
-- BOOK AUTHORS (M:N)
-- ========================
CREATE TABLE book_authors
(
    book_id   BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (book_id, author_id)
);

-- ========================
-- LOANS TABLE
-- ========================
CREATE TABLE loans
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    user_id     BIGINT NOT NULL,
    book_id     BIGINT NOT NULL,
    borrowed_at DATE NOT NULL,
    due_date    DATE NOT NULL,
    returned_at DATE,
    status      ENUM ('BORROWED', 'RETURNED', 'OVERDUE') DEFAULT 'BORROWED',
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

-- ========================
-- FINES TABLE
-- ========================
CREATE TABLE fines
(
    id      BIGINT AUTO_INCREMENT NOT NULL,
    loan_id BIGINT NOT NULL,
    amount  DECIMAL(10,2),
    paid    BOOLEAN DEFAULT FALSE,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id),
    CONSTRAINT unique_loan UNIQUE (loan_id)
);

-- ========================
-- RESERVATIONS TABLE
-- ========================
CREATE TABLE reservations
(
    id               BIGINT AUTO_INCREMENT NOT NULL,
    user_id          BIGINT NOT NULL,
    book_id          BIGINT NOT NULL,
    reservation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status           ENUM ('PENDING', 'COMPLETED', 'CANCELLED') DEFAULT 'PENDING',
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

-- ========================
-- AUDIT LOGS TABLE
-- ========================
CREATE TABLE audit_logs
(
    id        BIGINT AUTO_INCREMENT NOT NULL,
    user_id   BIGINT NOT NULL,
    action    VARCHAR(255),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

-- ========================
-- OPTIONAL: PUBLISHERS
-- ========================
CREATE TABLE publishers
(
    id       BIGINT AUTO_INCREMENT NOT NULL,
    name     VARCHAR(100),
    location VARCHAR(100),
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

-- ========================
-- OPTIONAL: REVIEWS
-- ========================
CREATE TABLE reviews
(
    id      BIGINT AUTO_INCREMENT NOT NULL,
    user_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    rating  INT CHECK (rating BETWEEN 1 AND 5),
    comment TEXT,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

-- ========================
-- FOREIGN KEYS
-- ========================

ALTER TABLE books
    ADD CONSTRAINT fk_book_category
        FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE SET NULL;

CREATE INDEX fk_book_category ON books (category_id);

ALTER TABLE book_authors
    ADD CONSTRAINT fk_book_author_book
        FOREIGN KEY (book_id) REFERENCES books (id) ON DELETE CASCADE;

ALTER TABLE book_authors
    ADD CONSTRAINT fk_book_author_author
        FOREIGN KEY (author_id) REFERENCES authors (id) ON DELETE CASCADE;

CREATE INDEX fk_book_author_author ON book_authors (author_id);

ALTER TABLE loans
    ADD CONSTRAINT fk_loan_user
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE NO ACTION;

ALTER TABLE loans
    ADD CONSTRAINT fk_loan_book
        FOREIGN KEY (book_id) REFERENCES books (id) ON DELETE NO ACTION;

CREATE INDEX fk_loan_user ON loans (user_id);
CREATE INDEX fk_loan_book ON loans (book_id);

ALTER TABLE fines
    ADD CONSTRAINT fk_fine_loan
        FOREIGN KEY (loan_id) REFERENCES loans (id) ON DELETE CASCADE;

ALTER TABLE reservations
    ADD CONSTRAINT fk_reservation_user
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE NO ACTION;

ALTER TABLE reservations
    ADD CONSTRAINT fk_reservation_book
        FOREIGN KEY (book_id) REFERENCES books (id) ON DELETE NO ACTION;

CREATE INDEX fk_reservation_user ON reservations (user_id);
CREATE INDEX fk_reservation_book ON reservations (book_id);

ALTER TABLE audit_logs
    ADD CONSTRAINT fk_audit_user
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE NO ACTION;

CREATE INDEX fk_audit_user ON audit_logs (user_id);

ALTER TABLE reviews
    ADD CONSTRAINT fk_review_user
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE reviews
    ADD CONSTRAINT fk_review_book
        FOREIGN KEY (book_id) REFERENCES books (id) ON DELETE CASCADE;

CREATE INDEX fk_review_user ON reviews (user_id);
CREATE INDEX fk_review_book ON reviews (book_id);
