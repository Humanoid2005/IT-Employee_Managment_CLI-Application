package classes;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Employee {
    public int employee_id;
    public String employee_password;
    public String employee_name;
    public int salary;
    public int department_id;
    public int manager_id;
    public int office_location_id;
    public Date dob;
    public Date doj;
    public String house_address;
    public String sex;

    
    public Employee() {
    }

    
    public Employee(int employee_id, String employee_password, String employee_name, int salary, int department_id,
                    int manager_id, int office_location_id, Date dob, Date doj, String house_address, String sex) {
        this.employee_id = employee_id;
        this.employee_password = employee_password;
        this.employee_name = employee_name;
        this.salary = salary;
        this.department_id = department_id;
        this.manager_id = manager_id;
        this.office_location_id = office_location_id;
        this.dob = dob;
        this.doj = doj;
        this.house_address = house_address;
        this.sex = sex;
    }

    public Employee(ResultSet rs) throws SQLException {
        this.employee_id = rs.getInt("employee_id");
        this.employee_password = rs.getString("employee_password");
        this.employee_name = rs.getString("employee_name");
        this.salary = rs.getInt("salary");
        this.department_id = rs.getInt("department_id");
        this.manager_id = rs.getInt("manager_id");
        this.office_location_id = rs.getInt("office_location_id");
        this.dob = rs.getDate("dob");
        this.doj = rs.getDate("doj");
        this.house_address = rs.getString("house_address");
        this.sex = rs.getString("sex");
    }
}
