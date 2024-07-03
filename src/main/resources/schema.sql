CREATE TABLE schedule
(
    id   BIGINT       NOT NULL AUTO_INCREMENT,
    time VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE reservation
(
    id      BIGINT       NOT NULL AUTO_INCREMENT,
    name    VARCHAR(255) NOT NULL,
    date    VARCHAR(255) NOT NULL,
    schedule_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (schedule_id) REFERENCES schedule(id)
);
