package project.challengers.DTO.point;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("포인트 입출금 이력")
public class MyPointDto {
    @ApiModelProperty(value = "id", example = "user001")
    private String id;

    @ApiModelProperty(value = "이력 리스트", example = "")
    private List<PointHistoryDto> pointHistoryList;
}
