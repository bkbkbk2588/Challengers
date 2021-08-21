package project.challengers.service;

import project.challengers.DTO.point.MyAllPointDto;
import project.challengers.DTO.point.MyPointDto;
import project.challengers.DTO.point.PointDto;
import project.challengers.DTO.point.PointInfoDto;

public interface PointService {
    int addPoint(PointDto point, String id);
    int withdrawPoint(PointDto point, String id);
    PointInfoDto getPoint(String id);
    MyPointDto getHistory(String id, int status);
    MyAllPointDto getAllHistory(String id);
}
