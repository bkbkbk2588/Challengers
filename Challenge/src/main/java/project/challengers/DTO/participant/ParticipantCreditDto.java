package project.challengers.DTO.participant;

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
@ApiModel("벌금 외상 정보")
public class ParticipantCreditDto {
    @ApiModelProperty(value = "참여자 id", example = "user001")
    String id;

    @ApiModelProperty(value = "벌금 외상", example = "1000")
    int credit;
}
