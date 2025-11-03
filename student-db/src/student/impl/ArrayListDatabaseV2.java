package student.impl;

import student.Student;
import student.StudentDatabaseV2;
import java.util.*;

public class ArrayListDatabaseV2 implements StudentDatabaseV2 {
    private List<Student> students;

    @Override
    public void init(List<Student> students) {
        this.students = new ArrayList<>(students);
    }

    @Override
    public List<Student> findStudentsByBirthday(int month, int day) {
        List<Student> result = new ArrayList<>();
        for (Student s : students) {
            if (s.m_birth_month == month && s.m_birth_day == day) {
                result.add(s);
            }
        }
        return result;
    }

    @Override
    public void updateGroupByEmail(String email, String newGroup) {
        for (Student s : students) {
            if (s.m_email.equals(email)) {
                s.m_group = newGroup;
                return;
            }
        }
    }

    @Override
    public String findGroupWithMostSameBirthdays() {
        Map<String, Map<String, Integer>> groupBirthCount = new HashMap<>();
        
        for (Student s : students) {
            String birthKey = s.m_birth_month + "-" + s.m_birth_day;
            groupBirthCount
                .computeIfAbsent(s.m_group, k -> new HashMap<>())
                .merge(birthKey, 1, Integer::sum);
        }

        String maxGroup = null;
        int maxCount = 0;
        
        for (Map.Entry<String, Map<String, Integer>> entry : groupBirthCount.entrySet()) {
            int groupMax = entry.getValue().values().stream()
                .max(Integer::compare).orElse(0);
            if (groupMax > maxCount) {
                maxCount = groupMax;
                maxGroup = entry.getKey();
            }
        }
        return maxGroup;
    }
}
