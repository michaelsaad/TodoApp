use todo;
CREATE TABLE todo_details (
                              id INT auto_increment PRIMARY KEY,
                              description TEXT,
                              created_at TIMESTAMP,
                              last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                              priority ENUM('REAL','CRITICAL','IMPORTANT','ROUTINE') NOT NULL,
                              status ENUM('FINISHED','UNFINISHED','IN_PROGRESS') NOT NULL
);
CREATE TABLE todo (
                      id INT  auto_increment PRIMARY KEY,
                      title VARCHAR(255),
                      user_id INT,
                      todo_details_id INT,
                      FOREIGN KEY (todo_details_ID) REFERENCES todo_details(id)
);

use user_db ;
CREATE TABLE user (
                      id INT auto_increment PRIMARY KEY,
                      first_name VARCHAR(50)NOT NULL,
                      last_name VARCHAR(50)NOT NULL,
                      email VARCHAR(100)  NOT NULL UNIQUE,
                      password VARCHAR(200)NOT NULL,
                      role ENUM('USER','ADMIN','MANGER') NOT NULL,
                      verified BOOLEAN DEFAULT false,
                      otp INT NOT NULL ,
                      otpGeneratedTime TIMESTAMP NOT NULL

);
CREATE TABLE  JWT (
                      id INT auto_increment  PRIMARY KEY ,
                      token VARCHAR(50)NOT NULL,
                      revoked BOOLEAN DEFAULT false,
                      expired BOOLEAN DEFAULT false,
                      token_type ENUM('BEARER') NOT NULL ,
                      user_id INT  NOT NULL,
                      FOREIGN KEY (user_id) REFERENCES user(id)
);