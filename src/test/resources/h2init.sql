DROP TABLE IF EXISTS tourist_attraction_tags;
DROP TABLE IF EXISTS tourist_attraction;
DROP TABLE IF EXISTS tags;
DROP TABLE IF EXISTS cities;

CREATE TABLE cities (
                        city_id INT AUTO_INCREMENT PRIMARY KEY,
                        city_name VARCHAR(255)
);

CREATE TABLE tags (
                      tag_id INT AUTO_INCREMENT PRIMARY KEY,
                      tag_description VARCHAR(255)
);

CREATE TABLE tourist_attraction (
                                    attraction_id INT AUTO_INCREMENT PRIMARY KEY,
                                    name VARCHAR(255) NOT NULL,
                                    description VARCHAR(255) NOT NULL,
                                    city_id INT,
                                    FOREIGN KEY (city_id) REFERENCES cities(city_id) ON DELETE CASCADE
);

CREATE TABLE tourist_attraction_tags (
                                         tag_id INT,
                                         attraction_id INT,
                                         PRIMARY KEY (tag_id, attraction_id),
                                         FOREIGN KEY (tag_id) REFERENCES tags(tag_id) ON DELETE CASCADE,
                                         FOREIGN KEY (attraction_id) REFERENCES tourist_attraction(attraction_id) ON DELETE CASCADE
);

INSERT INTO cities (city_name)
VALUES ('København'),
       ('Helsingør');


INSERT INTO tourist_attraction (name, description, city_id)
VALUES ('Tivoli', 'En park i København', 1),
       ('Kronborg slot', 'Et slot', 2);

INSERT INTO tags (tag_description)
VALUES ('Forlystelsespark'),
       ('Slot');

INSERT INTO tourist_attraction_tags (tag_id, attraction_id)
VALUES (1, 1), (2,2);