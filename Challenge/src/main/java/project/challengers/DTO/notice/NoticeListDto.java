package project.challengers.DTO.notice;

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
@ApiModel("게시글 목록")
public class NoticeListDto {
    @ApiModelProperty(value = "검색페이징", example = "")
    SearchPagingDto searchPaging;

    @ApiModelProperty(value = "게시글 목록", example = "")
    List<NoticeDto> noticeList;
}
