package project.challengers.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity(name = "NOTICE")
@Builder
@ApiModel("챌린저스 모집 게시글")
public class Notice {

    @Id
    @Column(name = "notice_seq")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(value="챌린저 게시글 순번", example="1")
    private long noticeSeq;

    @Column(nullable = false)
    @ApiModelProperty(value="제목", example="스터디 모집")
    private String title;

    @Column(nullable = false)
    @ApiModelProperty(value="아이디", example="user001")
    private String id;

    @Column(nullable = false)
    @ApiModelProperty(value="유형", example="0")
    private String type;

    @Column(nullable = false, name = "max_people")
    @ApiModelProperty(value="최대인원", example="10")
    private int maxPeople;

    @Column(nullable = false)
    @ApiModelProperty(value="보증", example="1000")
    private int price;

    @Column(nullable = false)
    @ApiModelProperty(value="내용", example="영어 스터디 할 사람 신청해주세요")
    private String content;

    @Column(nullable = false, name = "start_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value="시작시간", example="2021-01-01 00:00:00")
    private LocalDateTime startTime;

    @Column(nullable = false, name = "end_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value="종료시간", example="2021-01-02 00:00:00")
    private LocalDateTime endTime;
}
