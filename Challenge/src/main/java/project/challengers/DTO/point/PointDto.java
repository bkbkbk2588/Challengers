package project.challengers.DTO.point;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("포인트 정보")
public class PointDto {
    @ApiModelProperty(value="포인트", example="1000")
    Integer point;
}
