use todo;
CREATE TABLE todo (
                      id INT  auto_increment PRIMARY KEY,
                      title VARCHAR(255),
                      user_id INT,
                      todo_details_id INT,
                      FOREIGN KEY (todo_details_ID) REFERENCES todo_details(id)
);
CREATE TABLE todo_details (
                              id INT auto_increment PRIMARY KEY,
                              description TEXT,
                              priority VARCHAR(50),
                              status VARCHAR(50),
                              created_at TIMESTAMP,
                              last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);