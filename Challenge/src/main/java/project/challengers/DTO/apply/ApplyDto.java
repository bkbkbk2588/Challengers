package project.challengers.DTO.apply;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("신청 정보")
public class ApplyDto {
    @ApiModelProperty(value = "챌린저 공지 번호", example = "1")
    long noticeSeq;

    @ApiModelProperty(value = "제출한 보증금", example = "1000")
    int deposit;
}
