package classes;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

public class WorkReport {
    public HashMap<Project,ArrayList<WorkEntry>>project_report;

    public WorkReport() {
        this.project_report = new HashMap<Project, ArrayList<WorkEntry>>();
    }

    public void addWorkEntry(Project key, int hours,String contribution,Date date_of_work){
        WorkEntry p = new WorkEntry();
        p.contribution = contribution;
        p.hours = hours;
        p.date_of_work = date_of_work;
        ArrayList<WorkEntry>ap = new ArrayList<>();
        ap.add(p);
        this.project_report.putIfAbsent(key,ap);
        this.project_report.get(key).add(p);

    }
}

class WorkEntry{
    int hours;
    String contribution;
    Date date_of_work;
}