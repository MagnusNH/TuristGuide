CREATE TABLE IF NOT EXISTS cities (
                                      city_id INT AUTO_INCREMENT PRIMARY KEY,
                                      city_name VARCHAR(255)
    );

CREATE TABLE IF NOT EXISTS tags (
                                    tag_id INT AUTO_INCREMENT PRIMARY KEY,
                                    tag_description VARCHAR(255)
    );

CREATE TABLE IF NOT EXISTS tourist_attraction (
                                                  attraction_id INT AUTO_INCREMENT PRIMARY KEY,
                                                  name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    city_id INT,
    FOREIGN KEY (city_id) REFERENCES cities(city_id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS tourist_attraction_tags (
                                                       tag_id INT,
                                                       attraction_id INT,
                                                       PRIMARY KEY (tag_id, attraction_id),
    FOREIGN KEY (tag_id) REFERENCES tags(tag_id) ON DELETE CASCADE,
    FOREIGN KEY (attraction_id) REFERENCES tourist_attraction(attraction_id) ON DELETE CASCADE
    );