package project.challengers.service;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpRequest;
import project.challengers.DTO.auth.AuthDto;
import reactor.core.publisher.Flux;

import java.io.IOException;

public interface ChallengeService {
    int attendanceAuth(Flux<FilePart> filePartFlux, long noticeSeq, String id);
    AuthDto getAuthFile(long noticeSeq, String masterId, ServerHttpRequest req);
    byte[] downloadFile(String fileName) throws IOException;
}
