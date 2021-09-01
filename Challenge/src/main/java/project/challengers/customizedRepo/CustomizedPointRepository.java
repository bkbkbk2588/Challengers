package project.challengers.customizedRepo;

import java.util.List;

public interface CustomizedPointRepository {
    int updatePoint(int deposit, String id);
    int updateUserPoint(int deposit, List<String> id);
    int updateMasterPoint(int deposit, String id);
}
