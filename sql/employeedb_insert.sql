use employeedb;

insert into office_branches (city, state, country, address, size_of_office) values
('Bangalore', 'Karnataka', 'India', 'MG Road', 100),
('Hyderabad', 'Telangana', 'India', 'HiTech City', 80),
('Mumbai', 'Maharashtra', 'India', 'Bandra', 120),
('Chennai', 'Tamil Nadu', 'India', 'T. Nagar', 90),
('Delhi', 'Delhi', 'India', 'Connaught Place', 110);

insert into departments (department_name, manager_id) values
('HR', NULL),
('Engineering', NULL),
('Sales', NULL),
('Marketing', NULL),
('Finance', NULL);

insert into employees (employee_password, employee_name, salary, department_id, manager_id, office_location_id, dob, doj, house_address, sex) values
('fkdqjhph', 'Alice Johnson', 60000, 1, NULL, 1, '1990-05-14', '2015-06-20', '12, MG Road', 'F'),
('fkdqjhph', 'Bob Smith', 80000, 2, NULL, 2, '1988-08-22', '2013-09-18', '34, HiTech City', 'M'),
('fkdqjhph', 'Charlie Lee', 75000, 2, 2, 2, '1992-11-05', '2017-04-10', '56, HiTech City', 'M'),
('fkdqjhph', 'Dana White', 70000, 3, NULL, 3, '1991-03-30', '2016-12-01', '78, Bandra', 'F'),
('fkdqjhph', 'Eva Green', 65000, 4, NULL, 4, '1993-02-18', '2018-01-15', '90, T Nagar', 'F'),
('fkdqjhph','Mr. Boss',100000,1,NULL,1,'1982-04-11','2013-09-18','Spacemon apts, Bangalore-560020','M');

update departments set manager_id = 1 where department_id = 1;
update departments set manager_id = 2 where department_id = 2;
update departments set manager_id = 3 where department_id = 3;
update departments set manager_id = 4 where department_id = 4;
update departments set manager_id = 5 where department_id = 5;

update employees set manager_id = 6 where employee_id = 1;
update employees set manager_id = 6 where employee_id = 2;
update employees set manager_id = 6 where employee_id = 3;
update employees set manager_id = 6 where employee_id = 4;
update employees set manager_id = 6 where employee_id = 5;

insert into projects (project_name, project_description, department_id, project_start_date, tentative_completion_date) values
('Project Alpha', 'Build CRM system', 2, '2023-01-01', '2023-12-31'),
('Project Beta', 'Market research', 4, '2023-03-01', '2023-11-30'),
('Project Gamma', 'New Hiring Platform', 1, '2023-02-15', '2023-10-15'),
('Project Delta', 'Sales Optimization', 3, '2023-04-01', '2023-12-01'),
('Project Epsilon', 'Financial Audit System', 5, '2023-05-01', '2023-11-01');

insert into work_log (employee_id, project_id, hours_worked, date_of_work, contribution) values
(1, 1, 8, '2024-04-01', 'Developed modules'),
(2, 1, 6, '2024-04-01', 'Backend services'),
(3, 2, 7, '2024-04-01', 'Marketing research'),
(4, 3, 5, '2024-04-01', 'UI Design'),
(5, 4, 8, '2024-04-01', 'Sales analysis');

insert into dependents (employee_id, dependent_name, sex, dob, relation) values
(1, 'John Johnson', 'M', '2015-06-01', 'Son'),
(2, 'Lily Smith', 'F', '2016-08-15', 'Daughter'),
(3, 'David Lee', 'M', '2017-09-25', 'Son'),
(4, 'Sophia White', 'F', '2018-04-12', 'Daughter'),
(5, 'Grace Green', 'F', '2019-11-23', 'Daughter');

insert into employee_leaves (employee_id, request_to, start_date, end_date,approved) values
(1, 2, '2024-05-10', '2024-05-12', true),
(2, 3, '2024-06-05', '2024-06-07', false),
(3, 2, '2024-07-01', '2024-07-03', true),
(4, 1, '2024-08-15', '2024-08-17', true),
(5, 2, '2024-09-10', '2024-09-12', false);

insert into meetings (meeting_mode, meeting_date, meeting_start_time, meeting_end_time, meeting_location, meeting_organiser_id, department_id, purpose_of_meeting, minutes_of_meeting) values
('Offline', '2024-04-20', '10:00:00', '11:00:00', 'MG Road Conf Room', 1, 2, 'Project kickoff', 'Discussed modules and tasks'),
('Online', '2024-04-21', '14:00:00', '15:00:00', 'Zoom', 2, 4, 'Marketing Plan', 'Discussed ad strategy'),
('Offline', '2024-04-22', '09:30:00', '10:30:00', 'HiTech City', 3, 1, 'Hiring Strategy', 'Recruitment needs discussion'),
('Online', '2024-04-23', '16:00:00', '17:00:00', 'Teams', 4, 3, 'Sales Forecast', 'Predicted sales growth'),
('Offline', '2024-04-24', '11:00:00', '12:00:00', 'T Nagar Boardroom', 5, 5, 'Audit Plan', 'Review financial audits');

insert into attends_meeting (meeting_id, employee_id) values
(1, 1),
(1, 2),
(2, 3),
(3, 4),
(4, 5);

insert into skills (employee_id, skill_name) values
(1, 'Java'),
(2, 'Python'),
(3, 'Marketing'),
(4, 'UI/UX'),
(5, 'Sales');

insert into requests (request_from, request_to, request_description, approved) values
(1, 2, 'Need system access', false),
(2, 3, 'Need marketing report', true),
(3, 2, 'Require recruitment data', true),
(4, 1, 'Need sales statistics', false),
(5, 2, 'Request audit documents', true);
