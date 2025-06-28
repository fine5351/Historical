-- H2 compatible schema for MyBatis Flex tests

CREATE TABLE IF NOT EXISTS skill
(
  id         BIGINT PRIMARY KEY AUTO_INCREMENT,
  name       VARCHAR(255) NOT NULL,
  created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  created_by VARCHAR(50)  NOT NULL,
  updated_by VARCHAR(50)  NOT NULL
);
