use employeedb;

alter table employees
add constraint employee_fk_department FOREIGN KEY(department_id) REFERENCES departments(department_id) ON DELETE SET NULL;
alter table employees
add constraint employee_fk_employee FOREIGN KEY(manager_id) REFERENCES employees(employee_id) ON DELETE SET NULL;
alter table employees
add constraint employee_fk_location FOREIGN KEY(office_location_id) REFERENCES office_branches(branch_id) ON DELETE SET NULL;


alter table work_log
add constraint worklog_fk_project FOREIGN KEY(project_id) REFERENCES projects(project_id) ON DELETE CASCADE;
alter table work_log
add constraint worklog_fk_employee FOREIGN KEY(employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE;

alter table dependents
add constraint dependents_fk_employee FOREIGN KEY(employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE;

alter table employee_leaves
add constraint employee_leaves_fk_employee FOREIGN KEY(employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE;
alter table employee_leaves
add constraint employee_leaves_fk_requestto_employee FOREIGN KEY(request_to) REFERENCES employees(employee_id);

alter table skills
add constraint skills_fk_employee FOREIGN KEY(employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE;

alter table meetings
add constraint meeting_organiser_fk_employee FOREIGN KEY(meeting_organiser_id) REFERENCES employees(employee_id) ON DELETE SET NULL;
alter table meetings
add constraint meeting_organiser_fk_department FOREIGN KEY(department_id) REFERENCES departments(department_id) ON DELETE CASCADE;

alter table attends_meeting
add constraint meeting_id_fk_meeting FOREIGN KEY(meeting_id) REFERENCES meetings(meeting_id) ON DELETE CASCADE;
alter table attends_meeting
add constraint employee_id_fk_meeting FOREIGN KEY(employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE;

alter table departments
add constraint manager_fk_employee FOREIGN KEY(manager_id) REFERENCES employees(employee_id) ON DELETE SET NULL;

alter table projects
add constraint projects_fk_department FOREIGN KEY(department_id) REFERENCES departments(department_id) ON DELETE CASCADE;

alter table requests
add constraint employee_fk_request_from FOREIGN KEY(request_from) REFERENCES employees(employee_id) ON DELETE CASCADE;
alter table requests
add constraint employee_fk_request_to FOREIGN KEY(request_to) REFERENCES employees(employee_id) ON DELETE CASCADE;