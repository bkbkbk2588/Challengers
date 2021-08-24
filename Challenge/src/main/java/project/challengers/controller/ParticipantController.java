package project.challengers.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import project.challengers.DTO.participant.ParticipantDto;
import project.challengers.DTO.participant.ParticipantCreditDto;
import project.challengers.service.ParticipantService;

import java.util.List;

@Api(tags = {"도전 참가자"})
@RestController
@RequestMapping("/participant")
public class ParticipantController {
    Logger logger = LoggerFactory.getLogger(ParticipantController.class);

    @Autowired
    ParticipantService participantService;

    @ApiOperation(value = "참가자 상태 변경")
    @PutMapping(value = "/out/{noticeSeq}/{id}/{type}")
    public int setParticipantType(@ApiParam(name = "도전방 번호") @PathVariable("noticeSeq") long noticeSeq,
                                  @ApiParam(name = "id") @PathVariable("id") String id,
                                  @ApiParam(name = "참가자 상태") @PathVariable("type") int type, Authentication authentication) {
        return participantService.setParticipantType(noticeSeq, id, (String) authentication.getPrincipal(), type);
    }

    @ApiOperation(value = "참가자 전체 조회")
    @GetMapping(value = "/{noticeSeq}")
    public List<ParticipantDto> getAllParticipant(@ApiParam(name = "도전방 번호")@PathVariable long noticeSeq, Authentication authentication) {
        return participantService.getAllParticipant(noticeSeq, (String) authentication.getPrincipal());
    }

    @ApiOperation(value = "참가자 상태 별 조회")
    @GetMapping(value = "/{noticeSeq}/{type}")
    public List<ParticipantDto> getParticipantStatus(@ApiParam(name = "도전방 번호") @PathVariable("noticeSeq") long noticeSeq,
                                                     @ApiParam(name = "참가자 상태") @PathVariable("type") int type, Authentication authentication) {
        return participantService.getParticipantStatus(noticeSeq, type, (String) authentication.getPrincipal());
    }

    @ApiOperation(value = "참가자 블라인드 처리")
    @PutMapping(value = "/blind/{noticeSeq}/{id}")
    public int setBlind(@ApiParam("도전방 번호") @PathVariable("noticeSeq") long noticeSeq,
                        @ApiParam("블라인드 시킬 id") @PathVariable("id") String id, Authentication authentication) {
        return 0;
    }

    @ApiOperation(value = "참가자 강퇴 처리")
    @DeleteMapping(value = "/delete/{noticeSeq}/{id}")
    public int setDelete(@ApiParam("도전방 번호") @PathVariable("noticeSeq") long noticeSeq,
                        @ApiParam("블라인드 시킬 id") @PathVariable("id") String id, Authentication authentication) {
        return 0;
    }

    @ApiOperation(value = "참가자 벌금 외상 조회")
    @GetMapping(value = "/fine/{noticeSeq}")
    public List<ParticipantCreditDto> getUserFine(@ApiParam("도전방 번호") @PathVariable("noticeSeq") long noticeSeq,
                                                  Authentication authentication) {
        return participantService.getUserFine(noticeSeq,(String) authentication.getPrincipal());
    }
}
