-- Users
INSERT INTO USERS (USER_ID, USER_NAME, USER_PWD)
VALUES
    ('carlos', 'Carlos García', 'carlos123'),
    ('pedro', 'Pedro Fernández', 'pedro123'),
    ('juan', 'Juan Antolín', 'juan123'),
    ('alfonso', 'Alfonoso Gutierrez', 'alfonso123');

-- Groups
INSERT INTO GROUPS (GROUP_ID, GROUP_NAME)
VALUES
    (1, 'Cumpleaños Alfonso'),
    (2, 'Boda Juan');

-- User_groups
INSERT INTO USERS_GROUPS (GROUP_ID, USER_ID)
VALUES
   (1, 'carlos'),
   (1, 'pedro'),
   (1, 'juan'),
   (2, 'carlos');
