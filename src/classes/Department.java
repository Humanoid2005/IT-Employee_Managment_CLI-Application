package classes;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Department {
    public int department_id;
    public String department_name;
    public int manager_id;

    public Department() {
    }

    public Department(int department_id, String department_name, int manager_id) {
        this.department_id = department_id;
        this.department_name = department_name;
        this.manager_id = manager_id;
    }

    public Department(ResultSet rs) throws SQLException {
        this.department_id = rs.getInt("department_id");
        this.department_name = rs.getString("department_name");
        this.manager_id = rs.getInt("manager_id");
    }
}
