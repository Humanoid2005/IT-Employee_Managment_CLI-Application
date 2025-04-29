package classes;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class Project{
    public int project_id;
    public String project_name;
    public int department_id;
    public Date project_start_date;
    public Date tentative_completion_date;
    public String project_description;

    public Project() {
    }

    public Project(int project_id, String project_name,int department_id, Date project_start_date, Date tentative_completion_date,String project_descption) {
        this.project_id = project_id;
        this.project_name = project_name;
        this.department_id = department_id;
        this.project_start_date = project_start_date;
        this.tentative_completion_date = tentative_completion_date;
        this.project_description = project_descption;
    }

    public Project(ResultSet rs) throws SQLException {
        this.project_id = rs.getInt("project_id");
        this.project_name = rs.getString("project_name");
        this.department_id = rs.getInt("department_id");
        this.project_start_date = rs.getDate("project_start_date");
        this.tentative_completion_date = rs.getDate("tentative_completion_date");
        this.project_description = rs.getString("project_description");
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project projects = (Project) o;
        return Objects.equals(project_id, projects.project_id);
    }

    public int hashCode() {
        return Objects.hash(project_id);
    }

}