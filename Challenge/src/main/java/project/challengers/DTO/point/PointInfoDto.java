package project.challengers.DTO.point;

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
@ApiModel("자신의 포인트 정보")
public class PointInfoDto {
    @ApiModelProperty(value = "id", example = "user001")
    String id;

    @ApiModelProperty(value = "포인트", example = "1000")
    Integer point;
}
