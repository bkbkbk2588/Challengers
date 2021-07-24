package project.challengers.DTO.notice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@ApiModel(value="검색페이징")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchPagingDTO{
    @ApiModelProperty(value="offset 값: 0부터시작", example = "0")
    long offset;

    @ApiModelProperty(value="조회목록 크기", example = "10")
    long size;

    @ApiModelProperty(value="전체 수 ", example = "4")
    long totalCount;


}
