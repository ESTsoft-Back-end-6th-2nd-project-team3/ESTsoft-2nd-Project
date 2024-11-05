DROP TABLE IF EXISTS user;
CREATE TABLE user
(
    user_id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    email             VARCHAR(255),
    nickname          VARCHAR(255),
    pii               VARCHAR(255),
    created_at        TIMESTAMP,
    updated_at        TIMESTAMP,
    is_active         TINYINT,
    level             VARCHAR(255),
    last_login        TIMESTAMP,
    login_count       INT,
    user_agent        TEXT,
    profile_image_url TEXT,
    activity_score    INT,
    badge_image_data  TEXT,
    awarded_title     VARCHAR(255),
    self_intro        TEXT,
    sns_link          TEXT
);

DROP TABLE IF EXISTS activity_score;
CREATE TABLE activity_score
(
    score_id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id            BIGINT,
    score_fluctuations INT,
    fluctuation_at     TIMESTAMP,
    fluctuation_reason VARCHAR(255)
);

DROP TABLE IF EXISTS post;
CREATE TABLE post
(
    post_id    BIGINT PRIMARY KEY AUTO_INCREMENT,
    title      VARCHAR(255),
    content    TEXT,
    user_id    BIGINT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    post_type  VARCHAR(255),
    target_id  BIGINT,
    is_active  TINYINT,
    view_count INT,
    like_count INT
);

DROP TABLE IF EXISTS comment;
CREATE TABLE comment
(
    comment_id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    content           TEXT,
    post_id           BIGINT,
    user_id           BIGINT,
    parent_comment_id BIGINT,
    created_at        TIMESTAMP,
    updated_at        TIMESTAMP,
    is_active         TINYINT,
    like_count        INT
);

DROP TABLE IF EXISTS category;
CREATE TABLE category
(
    category_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(255)
);

DROP TABLE IF EXISTS region;
CREATE TABLE region
(
    region_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name      VARCHAR(255)
);

DROP TABLE IF EXISTS objective;
CREATE TABLE objective
(
    objective_id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    content              TEXT,
    user_id              BIGINT,
    created_at           TIMESTAMP,
    updated_at           TIMESTAMP,
    objective_year_month DATE,
    is_completed         TINYINT
);

DROP TABLE IF EXISTS likes;
CREATE TABLE likes
(
    like_id    BIGINT PRIMARY KEY AUTO_INCREMENT,
    like_type  VARCHAR(255),
    target_id  BIGINT,
    user_id    BIGINT,
    created_at TIMESTAMP
);

/*ALTER TABLE ACTIVITY_SCORE ADD FOREIGN KEY (user_id) REFERENCES USER (user_id);
ALTER TABLE POST ADD FOREIGN KEY (user_id) REFERENCES USER (user_id);
ALTER TABLE COMMENT ADD FOREIGN KEY (post_id) REFERENCES POST (post_id);
ALTER TABLE COMMENT ADD FOREIGN KEY (user_id) REFERENCES USER (user_id);
ALTER TABLE COMMENT ADD FOREIGN KEY (parent_comment_id) REFERENCES COMMENT (comment_id);
ALTER TABLE OBJECTIVE ADD FOREIGN KEY (user_id) REFERENCES USER (user_id);
ALTER TABLE LIKE ADD FOREIGN KEY (user_id) REFERENCES USER (user_id);*/

INSERT INTO user(email, nickname, pii, created_at, updated_at, is_active, level, last_login, login_count, user_agent, activity_score)
VALUES ('test@test.com', 'testnickname', '1234561234567', NOW(), NOW(), 1, '관리자', NOW(), 0, 'user agent test', 0);