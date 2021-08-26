
CREATE TABLE MEMBER(
    id VARCHAR(20) NOT NULL COMMENT "아이디" ,
    pw VARCHAR(255) NOT NULL COMMENT "비밀번호",
    name VARCHAR(20) NOT NULL COMMENT "이름",
    nickname VARCHAR(20)NOT NULL COMMENT "닉네임",
    email VARCHAR(25) NOT NULL COMMENT "이메일",
    phone VARCHAR(20) NOT NULL COMMENT "휴대전화",
    PRIMARY KEY (id),
    UNIQUE KEY(phone)
);

CREATE TABLE POINT (
    point_seq BIGINT NOT NULL AUTO_INCREMENT COMMENT "포인트 번호" ,
    id VARCHAR(20) NOT NULL COMMENT "아이디" ,
    point INT DEFAULT 0 COMMENT "포인트(없을시 0)" ,
    PRIMARY KEY (point_seq),
    UNIQUE KEY(id),
    FOREIGN KEY (id) REFERENCES MEMBER (id) ON DELETE CASCADE
);

CREATE TABLE POINT_HISTORY (
    point_history_seq BIGINT NOT NULL AUTO_INCREMENT COMMENT "포인트 이력 번호" ,
    id VARCHAR(20) NOT NULL COMMENT "아이디" ,
    status INT NOT NULL COMMENT "0:입급, 1:출금",
    point INT NOT NULL COMMENT "포인트" ,
    insert_time DATETIME NOT NULL  COMMENT "입출금 시간",
    PRIMARY KEY (point_history_seq),
    FOREIGN KEY (id) REFERENCES MEMBER (id) ON DELETE CASCADE
);

CREATE TABLE NOTICE (
    notice_seq BIGINT NOT NULL AUTO_INCREMENT COMMENT "챌린저 공지 번호",
    title VARCHAR(50) NOT NULL COMMENT "공지 제목",
    id VARCHAR(20) NOT NULL COMMENT "아이디",
    type INT NOT NULL COMMENT "유형 (0: 벌금, 1:몰아주기)",
    max_people INT NOT NULL COMMENT "최대인원수",
    price INT NOT NULL  COMMENT "보증금",
    content VARCHAR(255) NOT NULL  COMMENT "내용",
    start_time DATETIME NOT NULL  COMMENT "시작시간",
    end_time DATETIME NOT NULL COMMENT "종료시간",
    update_time DATETIME COMMENT "게시글 업데이트 시간",
    PRIMARY KEY (notice_seq),
    FOREIGN KEY (id) REFERENCES MEMBER (id) ON DELETE CASCADE
);

CREATE TABLE NOTICE_FILE (
    file_seq BIGINT NOT NULL AUTO_INCREMENT COMMENT "파일 순번",
    notice_seq BIGINT NOT NULL COMMENT "공지 번호",
    file_name VARCHAR(255) COMMENT "파일 이름",
    file_path VARCHAR(255)  COMMENT "사진 경로",
    PRIMARY KEY (file_seq),
    FOREIGN KEY (notice_seq) REFERENCES NOTICE (notice_seq) ON DELETE CASCADE
);

CREATE TABLE AUTH (
    auth_seq BIGINT NOT NULL AUTO_INCREMENT COMMENT "인증 순번" ,
    notice_seq BIGINT NOT NULL COMMENT "공지번호",
    id VARCHAR(20) NOT NULL COMMENT "아이디" ,
    file_name VARCHAR(255) COMMENT "파일이름" ,
    file_path VARCHAR(255) COMMENT "파일경로" ,
    auth_date DATE COMMENT "인증 시간",
    PRIMARY KEY (auth_seq),
    FOREIGN KEY (id) REFERENCES MEMBER (id) ON DELETE CASCADE,
    FOREIGN KEY (notice_seq) REFERENCES NOTICE (notice_seq) ON DELETE CASCADE
);

CREATE TABLE PARTICIPANT (
    patricipant_seq  BIGINT NOT NULL AUTO_INCREMENT COMMENT "참여자 순번",
    notice_seq BIGINT NOT NULL COMMENT "공지번호",
    master_id VARCHAR(20) NOT NULL COMMENT "방장아이디",
    participant_id VARCHAR(20) NOT NULL COMMENT "참가자_아이디",
    warning INT NOT NULL DEFAULT 0 COMMENT "경고 횟수 3회면 블라인드 처리, 7회 이상이면 강퇴 처리",
    participant_type INT COMMENT "유형(0 : 정상참가자, 1 : 블라인드 처리, 2 : 강퇴당한사람, 3: 중간 퇴장한 사람, 정상 퇴장, 4: 방 종료)",
    credit INT DEFAULT 0 COMMENT "벌금 외상"
    PRIMARY KEY (patricipant_seq),
    FOREIGN KEY (notice_seq) REFERENCES NOTICE (notice_seq) ON DELETE CASCADE
);

CREATE TABLE APPLY (
    apply_seq BIGINT NOT NULL AUTO_INCREMENT COMMENT "번호",
    id VARCHAR(20) NOT NULL COMMENT "신청 아이디",
    notice_seq BIGINT NOT NULL COMMENT "챌린저 공지 번호",
    deposit INT NOT NULL COMMENT "제출한 보증금",
    PRIMARY KEY (apply_seq),
    FOREIGN KEY (id) REFERENCES MEMBER (id) ON DELETE CASCADE,
    FOREIGN KEY (notice_seq) REFERENCES NOTICE (notice_seq) ON DELETE CASCADE
);

CREATE TABLE CHALLENGE (
    challenge_seq BIGINT NOT NULL COMMENT "도전 번호",
    money INT COMMENT "보증금 포함 모은 벌금",
    status INT COMMENT "상태값 (0:시작 전, 1:진행중, 2:종료)",
    PRIMARY KEY (challenge_seq),
    FOREIGN KEY (challenge_seq) REFERENCES NOTICE (notice_seq) ON DELETE CASCADE
);

