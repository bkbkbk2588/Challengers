create database challenge;
use challenge;

CREATE TABLE member(          
ID varchar(20) NOT NULL COMMENT "아이디" ,
PW varchar(255) NOT NULL COMMENT "비밀번호",
NAME varchar(20) NOT NULL COMMENT "이름",
NICKNAME varchar(20)NOT NULL COMMENT "닉네임",
EMAIL varchar(25) NOT NULL COMMENT "이메일", 
PHONE varchar(20) NOT NULL COMMENT "휴대전화",
PRIMARY KEY (ID) 
);

CREATE TABLE auth (
auth_seq int NOT NULL AUTO_INCREMENT COMMENT "인증 순번" ,
ID varchar(20) NOT NULL COMMENT "아이디" ,
photo varchar(255) COMMENT "사진경로" ,
video varchar(255) COMMENT "동영상경로" ,
primary key (auth_seq),
FOREIGN KEY (id) REFERENCES member (id)
);


CREATE TABLE point (
point_seq int NOT NULL AUTO_INCREMENT COMMENT "포인트 번호" ,
ID varchar(20) NOT NULL COMMENT "아이디" ,
POINT INT default 0 COMMENT "포인트(없을시 0)" ,
primary key (point_seq)
);

Create table notice (
notice_seq int NOT NULL AUTO_INCREMENT COMMENT "공지 번호",
title varchar(50) NOT NULL COMMENT "공지 제목",
ID varchar(20) NOT NULL COMMENT "아이디",
type enum('0','1') COMMENT "유형 (0: 벌금, 1:몰아주기)",
max_people int not null COMMENT "최대인원수",
price int not null  COMMENT "금액",
content varchar(255) NOT NULL  COMMENT "내용",
image_path varchar(255) not null  COMMENT "사진경로",
start_time date NOT NULL  COMMENT "시작시간",
end_time date NOT NULL  COMMENT "종료시간",
primary key (notice_seq),
FOREIGN KEY (id) REFERENCES member (id)
);

Create table participant (
patricipant_seq  int NOT NULL AUTO_INCREMENT COMMENT "참여자 순번",
notice_seq int not null COMMENT "공지번호",
master_id varchar(20) NOT NULL COMMENT "방장아이디",
participant_id varchar(20) NOT NULL COMMENT "참가자_아이디",
participant_type enum('0','1','2','3') COMMENT "유형(0 : 정상참가자, 1 : 블라인드 처리, 2 : 강퇴당한사람, 3: 중간 퇴장한 사람, 정상 퇴장)",
primary key (patricipant_seq),
FOREIGN KEY (notice_seq) REFERENCES notice (notice_seq)
);



create table apply (
apply_seq int NOT NULL AUTO_INCREMENT COMMENT "번호",
id varchar(20) COMMENT "아이디",
deposit enum('0','1') COMMENT "유형(0:보증금 냄, 1:보증금 안냄)",
primary key (apply_seq),
FOREIGN KEY (id) REFERENCES participant (participant_id)
);

Create table auth_clip (
auth_clip_seq int NOT NULL AUTO_INCREMENT,
photo varchar(255),
video varchar(255),
primary key (auth_clip_seq),
FOREIGN KEY (auth_clip_seq) REFERENCES auth (auth_seq)
);
