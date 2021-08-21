package project.challengers.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import project.challengers.DTO.point.MyAllPointDto;
import project.challengers.DTO.point.MyPointDto;
import project.challengers.DTO.point.PointDto;
import project.challengers.DTO.point.PointInfoDto;
import project.challengers.service.PointService;

@Api(tags = {"포인트"})
@RestController
@RequestMapping("/point")
public class PointController {

    @Autowired
    PointService pointService;

    @ApiOperation("포인트 충전")
    @PostMapping("/deposit")
    public int addPoint(@RequestBody PointDto point, Authentication authentication) {
        return pointService.addPoint(point, (String) authentication.getPrincipal());
    }

    @ApiOperation("포인트 출금")
    @PostMapping("/withdraw")
    public int withdrawPoint(@RequestBody PointDto point, Authentication authentication) {
        return pointService.withdrawPoint(point, (String) authentication.getPrincipal());
    }

    @ApiOperation("포인트 조회")
    @GetMapping("/my-point")
    public PointInfoDto getPoint(Authentication authentication) {
        return pointService.getPoint((String) authentication.getPrincipal());
    }

    @ApiOperation("포인트 충전 이력 조회")
    @GetMapping("/deposit/history")
    public MyPointDto getAddHistory(Authentication authentication) {
        return pointService.getHistory((String) authentication.getPrincipal(), 0);
    }

    @ApiOperation("포인트 출금 이력 조회")
    @GetMapping("/withdraw/history")
    public MyPointDto getWithdrawHistory(Authentication authentication) {
        return pointService.getHistory((String) authentication.getPrincipal(), 1);
    }

    @ApiOperation("포인트 입출금 이력 조회")
    @GetMapping("/all/history")
    public MyAllPointDto getAllHistory(Authentication authentication) {
        return pointService.getAllHistory((String) authentication.getPrincipal());
    }
}
