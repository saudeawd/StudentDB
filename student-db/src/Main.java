import student.Student;
import student.StudentDatabaseV2;
import student.impl.ArrayListDatabaseV2;
import student.impl.HashEmailDatabaseV2;
import student.impl.FullIndexDatabaseV2;
import student.sorter.StudentDBSorter;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.*;

public class Main {
    
    public static List<Student> readCSV(String filename) throws IOException {
        List<Student> students = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        br.readLine();
        while ((line = br.readLine()) != null) {
            String[] fields = line.split(",");
            if (fields.length == 9) {
                students.add(new Student(fields));
            }
        }
        br.close();
        return students;
    }
    
    public static void writeCSV(String filename, List<Student> students) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
        bw.write("m_name,m_surname,m_email,m_birth_year,m_birth_month,m_birth_day,m_group,m_rating,m_phone_number");
        bw.newLine();
        for (Student s : students) {
            bw.write(s.toCSV());
            bw.newLine();
        }
        bw.close();
    }
    
    public static void benchmarkOperations(StudentDatabaseV2 db, List<Student> students, 
                                         int op1Count, int op2Count, int op3Count) {
        Random rand = new Random(42);
        int totalStudents = students.size();
        
        long startTime = System.currentTimeMillis();
        long endTime = startTime + 10000;
        int op1Done = 0, op2Done = 0, op3Done = 0;
        
        while (System.currentTimeMillis() < endTime) {
            int totalOps = op1Count + op2Count + op3Count;
            int opType = rand.nextInt(totalOps);
            
            if (opType < op1Count) {
                int month = rand.nextInt(12) + 1;
                int day = rand.nextInt(28) + 1;
                db.findStudentsByBirthday(month, day);
                op1Done++;
            } else if (opType < op1Count + op2Count) {
                Student randomStudent = students.get(rand.nextInt(totalStudents));
                db.updateGroupByEmail(randomStudent.m_email, "NEW-GROUP");
                op2Done++;
            } else {
                db.findGroupWithMostSameBirthdays();
                op3Done++;
            }
        }
        
        System.out.printf("Operations completed - Op1: %d, Op2: %d, Op3: %d, Total: %d\n", 
                         op1Done, op2Done, op3Done, op1Done + op2Done + op3Done);
    }
    
    public static long getAccurateMemoryUsage() {
        try {
            System.gc();
            Thread.sleep(100);
            System.gc();
            Thread.sleep(100);
            
            MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
            MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
            MemoryUsage nonHeapUsage = memoryBean.getNonHeapMemoryUsage();
            
            return heapUsage.getUsed() + nonHeapUsage.getUsed();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        }
    }
    
    public static long measureMemoryUsage(Runnable initializationTask) {
        long before = getAccurateMemoryUsage();
        initializationTask.run();
        long after = getAccurateMemoryUsage();
        return after - before;
    }
    
    public static void main(String[] args) throws IOException {
        List<Student> students = readCSV("data/students.csv");
        System.out.println("Loaded " + students.size() + " students");

        List<List<Student>> subsets = Arrays.asList(
            students.subList(0, Math.min(100, students.size())),
            students.subList(0, Math.min(1000, students.size())),
            students.subList(0, Math.min(10000, students.size())),
            students.size() >= 100000 ? students.subList(0, 100000) : students
        );
        
        int[] sizes = {100, 1000, 10000, 100000};
        int op1Count = 2, op2Count = 100, op3Count = 30;
        
        String[] dbNames = {"ArrayList", "HashEmail", "FullIndex"};
        
        for (int i = 0; i < dbNames.length; i++) {
            System.out.println("\n=== " + dbNames[i] + " Database ===");
            for (int j = 0; j < subsets.size(); j++) {
                if (sizes[j] > subsets.get(j).size()) continue;
                
                System.out.println("Testing with " + sizes[j] + " students:");
                
                final int dbIndex = i;
                final int subsetIndex = j;

                long memoryUsed = measureMemoryUsage(() -> {
                    StudentDatabaseV2 db = createDatabase(dbIndex);
                    db.init(new ArrayList<>(subsets.get(subsetIndex)));
                });
                
                System.out.printf("Memory usage: %.2f MB\n", memoryUsed / (1024.0 * 1024.0));

                StudentDatabaseV2 db = createDatabase(i);
                db.init(new ArrayList<>(subsets.get(j)));
                benchmarkOperations(db, subsets.get(j), op1Count, op2Count, op3Count);
            }
        }
        
        System.out.println("\n=== Sorting Performance (S6) ===");
        List<Student> copy1 = new ArrayList<>(students);
        List<Student> copy2 = new ArrayList<>(students);
        
        long startTime = System.currentTimeMillis();
        StudentDBSorter.sortStandard(copy1);
        long standardTime = System.currentTimeMillis() - startTime;
        System.out.println("Standard sort time: " + standardTime + " ms");
        
        startTime = System.currentTimeMillis();
        StudentDBSorter.sortCounting(copy2);
        long countingTime = System.currentTimeMillis() - startTime;
        System.out.println("Counting sort time: " + countingTime + " ms");

        boolean correct = true;
        for (int i = 0; i < copy1.size(); i++) {
            Student a = copy1.get(i);
            Student b = copy2.get(i);
            if (a.m_birth_month != b.m_birth_month || a.m_birth_day != b.m_birth_day) {
                correct = false;
                break;
            }
        }
        System.out.println("Sorting correctness: " + correct);
        
        writeCSV("data/students_sorted.csv", copy1);
        System.out.println("Sorted data saved to data/students_sorted.csv");
    }
    
    private static StudentDatabaseV2 createDatabase(int index) {
        switch (index) {
            case 0: return new ArrayListDatabaseV2();
            case 1: return new HashEmailDatabaseV2();
            case 2: return new FullIndexDatabaseV2();
            default: throw new IllegalArgumentException("Unknown database index: " + index);
        }
    }
}