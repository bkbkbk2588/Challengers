

CREATE TABLE member(          
id VARCHAR(20) NOT NULL COMMENT "아이디" ,
pw VARCHAR(255) NOT NULL COMMENT "비밀번호",
name VARCHAR(20) NOT NULL COMMENT "이름",
nickname VARCHAR(20)NOT NULL COMMENT "닉네임",
email VARCHAR(25) NOT NULL COMMENT "이메일",
phone VARCHAR(20) NOT NULL COMMENT "휴대전화",
PRIMARY KEY (id),
UNIQUE KEY(phone)
);

CREATE TABLE auth (
auth_seq INT NOT NULL AUTO_INCREMENT COMMENT "인증 순번" ,
id VARCHAR(20) NOT NULL COMMENT "아이디" ,
photo VARCHAR(255) COMMENT "사진경로" ,
video VARCHAR(255) COMMENT "동영상경로" ,
PRIMARY KEY (auth_seq),
FOREIGN KEY (id) REFERENCES member (id)
);


CREATE TABLE point (
point_seq INT NOT NULL AUTO_INCREMENT COMMENT "포인트 번호" ,
id VARCHAR(20) NOT NULL COMMENT "아이디" ,
point INT default 0 COMMENT "포인트(없을시 0)" ,
PRIMARY KEY (point_seq)
);

CREATE TABLE notice (
notice_seq INT NOT NULL AUTO_INCREMENT COMMENT "챌린저 공지 번호",
title VARCHAR(50) NOT NULL COMMENT "공지 제목",
id VARCHAR(20) NOT NULL COMMENT "아이디",
type INT NOT NULL COMMENT "유형 (0: 벌금, 1:몰아주기)",
max_people INT not null COMMENT "최대인원수",
price INT not null  COMMENT "보증금",
content VARCHAR(255) NOT NULL  COMMENT "내용",
image_path VARCHAR(255)  COMMENT "사진경로",
start_time datetime NOT NULL  COMMENT "시작시간",
end_time datetime NOT NULL COMMENT "종료시간",
PRIMARY KEY (notice_seq),
FOREIGN KEY (id) REFERENCES member (id)
);

CREATE TABLE participant (
patricipant_seq  INT NOT NULL AUTO_INCREMENT COMMENT "참여자 순번",
notice_seq INT not null COMMENT "공지번호",
master_id VARCHAR(20) NOT NULL COMMENT "방장아이디",
participant_id VARCHAR(20) NOT NULL COMMENT "참가자_아이디",
participant_type INT COMMENT "유형(0 : 정상참가자, 1 : 블라인드 처리, 2 : 강퇴당한사람, 3: 중간 퇴장한 사람, 정상 퇴장)",
primary key (patricipant_seq),
FOREIGN KEY (notice_seq) REFERENCES notice (notice_seq)
);



CREATE TABLE apply (
apply_seq INT NOT NULL AUTO_INCREMENT COMMENT "번호",
id VARCHAR(20) NOT NULL COMMENT "아이디",
notice_seq INT NOT NULL COMMENT "챌린저 공지 번호",
deposit INT NOT NULL COMMENT "유형(0:보증금 냄, 1:보증금 안냄)",
primary key (apply_seq),
FOREIGN KEY (id) REFERENCES participant (participant_id),
FOREIGN KEY (notice_seq) REFERENCES notice (notice_seq)
);

