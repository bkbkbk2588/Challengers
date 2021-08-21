package project.challengers.DTO.point;

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
@ApiModel("포인트 입출금 이력정보")
public class PointAllHistoryDto {
    @ApiModelProperty(value = "포인트", example = "1000")
    private int point;

    @ApiModelProperty(value = "입출금 시간", example = "2021-08-21 13:09:40.237318")
    private LocalDateTime insertTime;

    @ApiModelProperty(value = "입출금 구분", example = "입금")
    private String type;
}
