-- H2 compatible data, adapted from infra/mariadb/init/02-data.sql

-- 插入初始資料到 skill 表
INSERT INTO skill (name, created_by, updated_by, created_at, updated_at)
SELECT 'C#', 'system', 'system', NOW(), NOW()
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM skill WHERE name = 'C#');
INSERT INTO skill (name, created_by, updated_by, created_at, updated_at)
SELECT 'Java', 'system', 'system', NOW(), NOW()
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM skill WHERE name = 'Java');
INSERT INTO skill (name, created_by, updated_by, created_at, updated_at)
SELECT 'Python', 'system', 'system', NOW(), NOW()
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM skill WHERE name = 'Python');
INSERT INTO skill (name, created_by, updated_by, created_at, updated_at)
SELECT 'Golang', 'system', 'system', NOW(), NOW()
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM skill WHERE name = 'Golang');
INSERT INTO skill (name, created_by, updated_by, created_at, updated_at)
SELECT 'MSSQL', 'system', 'system', NOW(), NOW()
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM skill WHERE name = 'MSSQL');
INSERT INTO skill (name, created_by, updated_by, created_at, updated_at)
SELECT 'MySQL', 'system', 'system', NOW(), NOW()
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM skill WHERE name = 'MySQL');

-- 插入初始資料到 team 表
INSERT INTO team (name, created_by, updated_by, created_at, updated_at)
SELECT 'RD1', 'system', 'system', NOW(), NOW()
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM team WHERE name = 'RD1');
INSERT INTO team (name, created_by, updated_by, created_at, updated_at)
SELECT 'RD2', 'system', 'system', NOW(), NOW()
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM team WHERE name = 'RD2');
INSERT INTO team (name, created_by, updated_by, created_at, updated_at)
SELECT 'RD3', 'system', 'system', NOW(), NOW()
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM team WHERE name = 'RD3');
INSERT INTO team (name, created_by, updated_by, created_at, updated_at)
SELECT 'RD4', 'system', 'system', NOW(), NOW()
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM team WHERE name = 'RD4');
INSERT INTO team (name, created_by, updated_by, created_at, updated_at)
SELECT 'STR', 'system', 'system', NOW(), NOW()
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM team WHERE name = 'STR');
INSERT INTO team (name, created_by, updated_by, created_at, updated_at)
SELECT 'PM', 'system', 'system', NOW(), NOW()
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM team WHERE name = 'PM');
INSERT INTO team (name, created_by, updated_by, created_at, updated_at)
SELECT 'SRE', 'system', 'system', NOW(), NOW()
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM team WHERE name = 'SRE');

-- 插入初始資料到 vacancy 表
INSERT INTO vacancy (title, created_by, updated_by, created_at, updated_at)
SELECT 'C# Junior Developer', 'system', 'system', NOW(), NOW()
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM vacancy WHERE title = 'C# Junior Developer');
INSERT INTO vacancy (title, created_by, updated_by, created_at, updated_at)
SELECT 'C# Senior Developer', 'system', 'system', NOW(), NOW()
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM vacancy WHERE title = 'C# Senior Developer');
INSERT INTO vacancy (title, created_by, updated_by, created_at, updated_at)
SELECT 'C# Team leader', 'system', 'system', NOW(), NOW()
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM vacancy WHERE title = 'C# Team leader');
INSERT INTO vacancy (title, created_by, updated_by, created_at, updated_at)
SELECT 'Java Junior Developer', 'system', 'system', NOW(), NOW()
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM vacancy WHERE title = 'Java Junior Developer');
INSERT INTO vacancy (title, created_by, updated_by, created_at, updated_at)
SELECT 'Java Senior Developer', 'system', 'system', NOW(), NOW()
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM vacancy WHERE title = 'Java Senior Developer');
INSERT INTO vacancy (title, created_by, updated_by, created_at, updated_at)
SELECT 'Java Team leader', 'system', 'system', NOW(), NOW()
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM vacancy WHERE title = 'Java Team leader');

-- 插入初始資料到 employee 表
INSERT INTO employee (id, name, address, gender, roc_id, created_by, updated_by, created_at, updated_at)
SELECT 1,
       'Alice Smith',
       '123 Maple St',
       'Female',
       'A123456789',
       'system',
       'system',
       NOW(),
       NOW()
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM employee WHERE id = 1);
INSERT INTO employee (id, name, address, gender, roc_id, created_by, updated_by, created_at, updated_at)
SELECT 2,
       'Bob Johnson',
       '456 Oak Ave',
       'Male',
       'B987654321',
       'system',
       'system',
       NOW(),
       NOW()
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM employee WHERE id = 2);
INSERT INTO employee (id, name, address, gender, roc_id, created_by, updated_by, created_at, updated_at)
SELECT 3,
       'Charlie Brown',
       '789 Pine Ln',
       'Male',
       'C112233445',
       'system',
       'system',
       NOW(),
       NOW()
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM employee WHERE id = 3);

INSERT INTO entity_column_mask (account, method, api_uri, mask_settings)
VALUES ('employee',
        'get',
        '/employee/{id}',
        '{"Employee":{"name":true}}');