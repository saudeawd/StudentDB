package student.impl;

import student.Student;
import student.StudentDatabaseV2;
import java.util.*;

public class HashEmailDatabaseV2 implements StudentDatabaseV2 {
    private Map<String, Student> emailMap;
    private Map<String, List<Student>> birthMap;
    private List<Student> students;

    @Override
    public void init(List<Student> students) {
        this.students = new ArrayList<>(students);
        emailMap = new HashMap<>();
        birthMap = new HashMap<>();
        
        for (Student s : students) {
            emailMap.put(s.m_email, s);
            String key = s.m_birth_month + "-" + s.m_birth_day;
            birthMap.computeIfAbsent(key, k -> new ArrayList<>()).add(s);
        }
    }

    @Override
    public List<Student> findStudentsByBirthday(int month, int day) {
        String key = month + "-" + day;
        return birthMap.getOrDefault(key, new ArrayList<>());
    }

    @Override
    public void updateGroupByEmail(String email, String newGroup) {
        Student s = emailMap.get(email);
        if (s != null) {
            s.m_group = newGroup;
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
