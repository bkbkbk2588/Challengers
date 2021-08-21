package project.challengers.controller;

import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"도전 신청"})
@RestController
@RequestMapping("/apply")
public class ApplyController {
    Logger logger = LoggerFactory.getLogger(ApplyController.class);

    /*
        TODO

     */
}
