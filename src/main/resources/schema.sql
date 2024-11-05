CREATE TABLE IF NOT EXISTS dena (
        userId BIGINT NOT NULL,
        amount INT DEFAULT 0,
        PRIMARY KEY (userId)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS paona (
        userId BIGINT NOT NULL,
        amount INT DEFAULT 0,
        PRIMARY KEY (userId)
) ENGINE=InnoDB;
