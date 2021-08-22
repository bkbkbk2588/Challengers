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
@ApiModel("도전 신청자 목록")
public class ApplyListDto {
    @ApiModelProperty(value = "id", example = "user001")
    String id;

    @ApiModelProperty(value = "보증금", example = "1000")
    int deposit;

    @ApiModelProperty(value = "도전방 번호", example = "1")
    long noticeSeq;
}
