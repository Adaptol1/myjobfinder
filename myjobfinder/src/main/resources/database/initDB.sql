CREATE TABLE IF NOT EXISTS vacancies
(
    id                INTEGER   PRIMARY KEY,
    employer          VARCHAR(200) NOT NULL,
    name              VARCHAR(200) NOT NULL,
    hasTest           BOOLEAN      NOT NULL,
    salaryFrom        INTEGER,
    salaryTo          INTEGER,
    salaryCurrency    VARCHAR(8),
    alternateUrl      VARCHAR(100) NOT NULL,
    requirement       VARCHAR(5000),
    responsibility    VARCHAR(5000),
    schedule          VARCHAR(50),
    experience        VARCHAR(20),
    employment        VARCHAR(20),
    isSent            BOOLEAN      NOT NULL
);
