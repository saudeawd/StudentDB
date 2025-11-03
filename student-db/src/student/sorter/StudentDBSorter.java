package student.sorter;

import student.Student;
import java.util.List;
import java.util.ArrayList;

public class StudentDBSorter {
    
    public static void sortStandard(List<Student> students) {
        students.sort((a, b) -> {
            int monthComp = Integer.compare(a.m_birth_month, b.m_birth_month);
            if (monthComp != 0) return monthComp;
            return Integer.compare(a.m_birth_day, b.m_birth_day);
        });
    }
    
    public static void sortCounting(List<Student> students) {
        int maxMonth = 12;
        int maxDay = 31;
        int totalBuckets = maxMonth * maxDay;
        
        List<List<Student>> buckets = new ArrayList<>(totalBuckets);
        for (int i = 0; i < totalBuckets; i++) {
            buckets.add(new ArrayList<>());
        }
        
        for (Student s : students) {
            int index = (s.m_birth_month - 1) * maxDay + (s.m_birth_day - 1);
            if (index >= 0 && index < totalBuckets) {
                buckets.get(index).add(s);
            }
        }
        
        students.clear();
        for (List<Student> bucket : buckets) {
            students.addAll(bucket);
        }
    }
}
