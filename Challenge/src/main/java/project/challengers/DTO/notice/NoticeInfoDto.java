package project.challengers.DTO.notice;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("게시글 정보")
public class NoticeInfoDto {
    @ApiModelProperty(value="제목", example="스터디 모집")
    String title;

    @ApiModelProperty(value="게시글 유형", example="0")
    String type;

    @ApiModelProperty(value="최대인원", example="10")
    Integer maxPeople;

    @ApiModelProperty(value="보증금", example="1000")
    Integer price;

    @ApiModelProperty(value="내용", example="영어 스터디 할 사람 신청해주세요")
    String content;

    @ApiModelProperty(value="방장 id", example="user001")
    String id;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value="시작시간", example="2021-01-01 00:00:00")
    LocalDateTime startTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value="종료시간", example="2021-01-02 00:00:00")
    LocalDateTime endTime;

    @ApiModelProperty(value = "[localhost:8080/downloadFile/1.jpg]")
    List<String> fileUrl;
}
