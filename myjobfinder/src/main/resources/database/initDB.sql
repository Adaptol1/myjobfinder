CREATE TABLE IF NOT EXISTS vacancies
(
    id                BIGSERIAL PRIMARY KEY,
    name              VARCHAR(200) NOT NULL,
    hasTest           BOOLEAN      NOT NULL,
    salaryFrom        INTEGER,
    salaryTo          INTEGER,
    salaryCurrency    VARCHAR(8),
    applyAlternateUrl VARCHAR(100) NOT NULL,
    url               VARCHAR(100) NOT NULL,
    alternateUrl      VARCHAR(100) NOT NULL,
    requirement       VARCHAR(5000),
    responsibility    VARCHAR(5000),
    schedule          VARCHAR(50),
    experience        VARCHAR(20),
    employment        VARCHAR(20),
    isSent           BOOLEAN      NOT NULL
);
