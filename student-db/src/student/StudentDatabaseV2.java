package student;

import java.util.List;

public interface StudentDatabaseV2 {
    void init(List<Student> students);
    
    List<Student> findStudentsByBirthday(int month, int day);
    
    void updateGroupByEmail(String email, String newGroup);
    
    String findGroupWithMostSameBirthdays();
}
