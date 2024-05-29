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
    created_at TIMESTAMP,
    last_updated TIMESTAMP,
    priority ENUM('REAL','CRITICAL','IMPORTANT','ROUTINE') NOT NULL,
    status ENUM('FINISHED','UNFINISHED','IN_PROGRESS') NOT NULL
);
use user_db ;
CREATE TABLE user (
    id INT auto_increment PRIMARY KEY,
    first_name VARCHAR(50)NOT NULL,
    last_name VARCHAR(50)NOT NULL,
    email VARCHAR(100)  NOT NULL UNIQUE,
    password VARCHAR(200)NOT NULL,
    otp INT NOT NULL ,
    enabled BOOLEAN
);
CREATE TABLE  JWT (
  id INT auto_increment  PRIMARY KEY ,
  user_id INT  NOT NULL,
  expiration_date datetime NOT NULL,
  token_type VARCHAR(50) NOT null ,
  FOREIGN KEY (user_id) REFERENCES user(id)
);