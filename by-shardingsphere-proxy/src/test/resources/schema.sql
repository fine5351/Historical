-- H2 compatible schema, adapted from infra/mariadb/init/01-schema.sql

CREATE TABLE IF NOT EXISTS skill
(
  id         BIGINT PRIMARY KEY AUTO_INCREMENT,
  name       VARCHAR(255) NOT NULL,
  created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  created_by VARCHAR(50)  NOT NULL,
  updated_by VARCHAR(50)  NOT NULL
);

CREATE TABLE IF NOT EXISTS team
(
  id         BIGINT PRIMARY KEY AUTO_INCREMENT,
  name       VARCHAR(255) NOT NULL,
  created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  created_by VARCHAR(50)  NOT NULL,
  updated_by VARCHAR(50)  NOT NULL
);

CREATE TABLE IF NOT EXISTS vacancy
(
  id         BIGINT PRIMARY KEY AUTO_INCREMENT,
  title      VARCHAR(255) NOT NULL,
  created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  created_by VARCHAR(50)  NOT NULL,
  updated_by VARCHAR(50)  NOT NULL
);

CREATE TABLE IF NOT EXISTS resume
(
  id         BIGINT PRIMARY KEY AUTO_INCREMENT,
  file_name  VARCHAR(255) NOT NULL,
  status     INT          NOT NULL DEFAULT 0,
  created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  created_by VARCHAR(50)  NOT NULL,
  updated_by VARCHAR(50)  NOT NULL
);

CREATE TABLE IF NOT EXISTS allowed_skill_mapping
(
  id          BIGINT PRIMARY KEY AUTO_INCREMENT,
  team_id     BIGINT   NOT NULL,
  vacancy_id  BIGINT   NOT NULL,
  skill_id    BIGINT   NOT NULL,
  skill_level INT      NOT NULL,
  created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uk_team_skill_level (team_id, skill_id, skill_level),
  FOREIGN KEY (team_id) REFERENCES team (id),
  FOREIGN KEY (vacancy_id) REFERENCES vacancy (id),
  FOREIGN KEY (skill_id) REFERENCES skill (id)
);

CREATE TABLE IF NOT EXISTS employee
(
  id         BIGINT PRIMARY KEY AUTO_INCREMENT,
  name       VARCHAR(255) NOT NULL,
  address    VARCHAR(255) NOT NULL,
  gender     VARCHAR(50)  NOT NULL,
  roc_id     VARCHAR(255) NOT NULL,
  created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  created_by VARCHAR(50)  NOT NULL,
  updated_by VARCHAR(50)  NOT NULL
);

CREATE TABLE entity_column_mask_structure
(
  id            BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
  method        VARCHAR(255) NOT NULL,
  account       VARCHAR(1024),
  api_uri       VARCHAR(1024),
  mask_settings JSON,
  created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  created_by    NVARCHAR(50) NOT NULL,
  updated_by    NVARCHAR(50) NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE entity_column_mask_flattened
(
  id            BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
  method        VARCHAR(255) NOT NULL,
  account       VARCHAR(1024),
  api_uri       VARCHAR(1024),
  mask_settings JSON,
  created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  created_by    NVARCHAR(50) NOT NULL,
  updated_by    NVARCHAR(50) NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;