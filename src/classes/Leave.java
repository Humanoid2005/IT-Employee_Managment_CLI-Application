package classes;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Leave {
    public int leave_id;
    public Employee employee;
    public Date start_date;
    public Date end_date;
    public String request_to;
    public boolean approved;

    public Leave() {
    }

    public Leave(int leave_id,Employee employee, Date start_date, Date end_date, String request_to, boolean approved) {
        this.leave_id = leave_id;
        this.employee = employee;
        this.start_date = start_date;
        this.end_date = end_date;
        this.request_to = request_to;
        this.approved = approved;
    }

    public Leave(ResultSet rs) throws SQLException {
        this.leave_id = rs.getInt("leave_id");
        this.start_date = rs.getDate("start_date");
        this.end_date = rs.getDate("end_date");
        this.request_to = rs.getString("request_to");
        this.approved = rs.getBoolean("approved");
    }

    public void setEmployee(Employee e){
        this.employee = e;
    }
}
