package project.challengers.DTO.notice;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("게시글 정보")
public class NoticeDto {

    @ApiModelProperty(value = "게시글 순번", example = "1")
    long noticeSeq;

    @ApiModelProperty(value="제목", example="스터디 모집")
    String title;

    @ApiModelProperty(value="방장 아이디", example="user001")
    String id;

    @ApiModelProperty(value="게시글 유형", example="0")
    String type;

    @ApiModelProperty(value="최대인원", example="10")
    int maxPeople;

    @ApiModelProperty(value="보증금", example="1000")
    int price;

    @ApiModelProperty(value="내용", example="영어 스터디 할 사람 신청해주세요")
    String content;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value="시작시간", example="2021-01-01 00:00:00")
    LocalDateTime startTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value="종료시간", example="2021-01-02 00:00:00")
    LocalDateTime endTime;

    @JsonIgnore
    @ApiModelProperty(value="업데이트 시간", example="2021-01-02 00:00:00")
    LocalDateTime updateTime;
}
