-- H2 compatible data for MyBatis Flex tests

-- 插入初始資料到 skill 表
INSERT INTO skill (name, created_by, updated_by, created_at, updated_at)
SELECT 'Java', 'system', 'system', NOW(), NOW()
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM skill WHERE name = 'Java');
INSERT INTO skill (name, created_by, updated_by, created_at, updated_at)
SELECT 'Python', 'system', 'system', NOW(), NOW()
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM skill WHERE name = 'Python');
INSERT INTO skill (name, created_by, updated_by, created_at, updated_at)
SELECT 'C#', 'system', 'system', NOW(), NOW()
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM skill WHERE name = 'C#');
