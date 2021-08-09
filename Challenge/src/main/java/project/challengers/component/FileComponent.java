package project.challengers.component;

import com.google.common.io.Files;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebInputException;
import project.challengers.exception.ChallengersException;
import reactor.core.publisher.Flux;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class FileComponent {
    Logger logger = LoggerFactory.getLogger(FileComponent.class);

    @Value("${challengers.path.prefix}${challengers.path.files}")
    private String UPLOAD_FILE_PATH;

    public Flux<Pair<String, String>> save(Flux<FilePart> filePartFlux) {
        return filePartFlux.map(filePart -> {
            File newFile = null;
            try {
                newFile = new File(UPLOAD_FILE_PATH + File.separator
                        + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"))
                        + File.separator + System.currentTimeMillis()
                        + new String(filePart.filename().getBytes("ISO-8859-1"), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                Files.createParentDirs(newFile);
                Files.touch(newFile);
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("file save fail");
                throw new ChallengersException("file save fail");

            }

//            final BufferedReader[] reader = new BufferedReader[1];
//
            filePart.content().subscribe(s -> {
                BufferedReader reader = new BufferedReader(new InputStreamReader(s.asInputStream()));

                while (true) {
                    try {
                        if (reader.readLine() == null)
                            break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
//
//            String str = "";
//            while (true) {
//                try {
//                    if (!((str = reader[0].readLine()) != null)) break;
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                System.out.println(str);
//            }

            // ??????
            filePart.transferTo(newFile);

            System.out.println("size : " + newFile.length());

//            try {
//                FileReader a = new FileReader(newFile);
//                int ch;
//                while ((ch = a.read()) != -1) {
//                    System.out.println("!!!!!");
//                    System.out.print((char) ch);
//                }
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            String fileName = "";
            try {
                fileName = new String(filePart.filename().getBytes("ISO-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
            return Pair.of(fileName, newFile.getAbsolutePath());
        }).onErrorReturn(ServerWebInputException.class, Pair.of(null, null))
                .filter(p -> p.getLeft() != null && p.getRight() != null);
    }
}
