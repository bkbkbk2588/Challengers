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
@ApiModel("참가자 정보")
public class ParticipantDto {
    @ApiModelProperty(value = "챌린저 공지 번호", example = "1")
    long challengeSeq;

    @ApiModelProperty(value = "참여자 id", example = "user001")
    String id;

    @ApiModelProperty(value = "참여자 상태", example = "normal")
    String status;


}
