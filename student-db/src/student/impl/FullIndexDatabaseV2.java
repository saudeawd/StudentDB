package student.impl;

import student.Student;
import student.StudentDatabaseV2;
import java.util.*;

public class FullIndexDatabaseV2 implements StudentDatabaseV2 {
    private Map<String, Student> emailMap;
    private Map<String, List<Student>> birthMap;
    private Map<String, Map<String, Integer>> groupBirthCountMap;

    @Override
    public void init(List<Student> students) {
        emailMap = new HashMap<>();
        birthMap = new HashMap<>();
        groupBirthCountMap = new HashMap<>();

        for (Student s : students) {
            emailMap.put(s.m_email, s);
            
            String birthKey = s.m_birth_month + "-" + s.m_birth_day;
            birthMap.computeIfAbsent(birthKey, k -> new ArrayList<>()).add(s);
            
            String group = s.m_group;
            groupBirthCountMap
                .computeIfAbsent(group, k -> new HashMap<>())
                .merge(birthKey, 1, Integer::sum);
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
            String oldGroup = s.m_group;
            String birthKey = s.m_birth_month + "-" + s.m_birth_day;

            Map<String, Integer> oldCounts = groupBirthCountMap.get(oldGroup);
            if (oldCounts != null) {
                oldCounts.merge(birthKey, -1, Integer::sum);
                if (oldCounts.get(birthKey) == 0) {
                    oldCounts.remove(birthKey);
                }
            }

            groupBirthCountMap
                .computeIfAbsent(newGroup, k -> new HashMap<>())
                .merge(birthKey, 1, Integer::sum);
            
            s.m_group = newGroup;
        }
    }

    @Override
    public String findGroupWithMostSameBirthdays() {
        String maxGroup = null;
        int maxCount = 0;
        
        for (Map.Entry<String, Map<String, Integer>> entry : groupBirthCountMap.entrySet()) {
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
