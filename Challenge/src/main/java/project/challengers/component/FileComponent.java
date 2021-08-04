package project.challengers.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileComponent {
    Logger logger = LoggerFactory.getLogger(FileComponent.class);

    @Value("${challengers.path.prefix}${challengers.path.files}")
    private String UPLOAD_FILE_PATH;

    // TODO 파일저장 로직 필요
}
