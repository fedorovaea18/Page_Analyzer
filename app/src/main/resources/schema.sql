DROP TABLE IF EXISTS urls;
/*DROP TABLE IF EXISTS url_checks;*/

CREATE TABLE urls (
                      id BIGINT GENERATED ALWAYS AS IDENTITY,
                      name VARCHAR(255),
                      created_at TIMESTAMP
);
/*CREATE TABLE url_checks (
                            id BIGINT GENERATED ALWAYS AS IDENTITY,
                            url_id BIGINT REFERENCES urls (id) NOT NULL,
                            status_code INTEGER,
                            h1 VARCHAR(255),
                            title VARCHAR(255),
                            description TEXT,
                            created_at TIMESTAMP,
                            FOREIGN KEY (url_id) REFERENCES urls (id),
);*/