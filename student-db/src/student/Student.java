package student;

public class Student {
    public String m_name;
    public String m_surname;
    public String m_email;
    public int m_birth_year;
    public int m_birth_month;
    public int m_birth_day;
    public String m_group;
    public float m_rating;
    public String m_phone_number;

    public Student(String[] fields) {
        m_name = fields[0];
        m_surname = fields[1];
        m_email = fields[2];
        m_birth_year = Integer.parseInt(fields[3]);
        m_birth_month = Integer.parseInt(fields[4]);
        m_birth_day = Integer.parseInt(fields[5]);
        m_group = fields[6];
        m_rating = Float.parseFloat(fields[7]);
        m_phone_number = fields[8];
    }

    public String toCSV() {
        return String.join(",",
                m_name, m_surname, m_email,
                String.valueOf(m_birth_year),
                String.valueOf(m_birth_month),
                String.valueOf(m_birth_day),
                m_group,
                String.valueOf(m_rating),
                m_phone_number);
    }
}
