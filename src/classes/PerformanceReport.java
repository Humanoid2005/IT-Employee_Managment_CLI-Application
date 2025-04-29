package classes;
import java.util.ArrayList;
import java.util.Map;

public class PerformanceReport {
    public Employee employee;
    public ArrayList<String>skills;
    public WorkReport project_reports;

    public PerformanceReport(Employee employee, ArrayList<String> skills, WorkReport project_reports) {
        this.employee = employee;
        this.skills = skills;
        this.project_reports = project_reports;
    }

    public void printPerformanceReport() {
        System.out.println("=================================================================================");
        System.out.println("                           EMPLOYEE PERFORMANCE REPORT                           ");
        System.out.println("=================================================================================");
        
        System.out.println("EMPLOYEE DETAILS:");
        System.out.println("-----------------");
        System.out.printf("ID: %-20s | Name: %-30s\n", employee.employee_id, employee.employee_name);
        System.out.printf("Department: %-12d | Manager: %-20s\n", employee.department_id, 
                        (employee.manager_id != 0 ? employee.manager_id : "N/A"));
        System.out.printf("Joined on: %-12s | Salary: ₹%-10d\n", employee.doj, employee.salary);
        System.out.println();
        
        System.out.println("SKILLS:");
        System.out.println("-------");
        if (skills.isEmpty()) {
            System.out.println("No skills recorded for this employee.");
        } else {
            System.out.print("• ");
            System.out.println(String.join("\n• ", skills));
        }
        System.out.println();
        
        System.out.println("PROJECT CONTRIBUTIONS:");
        System.out.println("---------------------");
        
        if (project_reports.project_report.isEmpty()) {
            System.out.println("No project contributions recorded for this employee.");
        } else {
            for (Map.Entry<Project, ArrayList<WorkEntry>> entry : project_reports.project_report.entrySet()) {
                Project project = entry.getKey();
                ArrayList<WorkEntry> workEntries = entry.getValue();
                
                System.out.println("\nProject: " + project.project_name + " (ID: " + project.project_id + ")");
                System.out.println("Department: " + project.department_id);
                System.out.println("Duration: " + project.project_start_date + 
                                  (project.tentative_completion_date != null ? 
                                  " to " + project.tentative_completion_date : 
                                  " (ongoing)"));
                
                System.out.println("\nWork Entries:");
                System.out.println("+-----------------+-----------------+------------------------------------------------+");
                System.out.printf("| %-15s | %-15s | %-46s |\n", "Date", "Hours Worked", "Contribution");
                System.out.println("+-----------------+-----------------+------------------------------------------------+");
                
                int totalHours = 0;
                
                for (WorkEntry we : workEntries) {
                    System.out.printf("| %-15s | %-15d | %-46s |\n", 
                        we.date_of_work, 
                        we.hours,
                        we.contribution, 46);
                    
                    totalHours += we.hours;
                }
                
                System.out.println("+-----------------+-----------------+------------------------------------------------+");
                System.out.printf("| %-15s | %-15d | %-46s |\n", 
                    "TOTAL", 
                    totalHours,
                    "");
                System.out.println("+-----------------+-----------------+------------------------------------------------+");
            }
        }
        
        System.out.println("\n=================================================================================");
    }
}
