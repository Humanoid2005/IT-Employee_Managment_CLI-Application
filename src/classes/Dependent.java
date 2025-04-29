package classes;

import java.sql.Date;

public class Dependent {
    public int employee_id;
    public String dependent_name;
    public String sex;
    public Date dob;
    public String relation;

    public Dependent() {
    }

    public Dependent(int employee_id, String dependent_name, String sex, Date dob, String relation) {
        this.employee_id = employee_id;
        this.dependent_name = dependent_name;
        this.sex = sex;
        this.dob = dob;
        this.relation = relation;
    }
}
