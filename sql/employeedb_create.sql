create database employeedb;
use employeedb;

create table employees(
    employee_id int auto_increment,
    employee_password varchar(100) NOT NULL,
    employee_name varchar(50) NOT NULL,
    salary int NOT NULL,
    department_id int,
    manager_id int,
    office_location_id int,
    dob date NOT NULL,
    doj date NOT NULL,
    house_address varchar(100) NOT NULL,
    sex char(1),
    constraint pk_employee PRIMARY KEY (employee_id)
);

create table work_log(
    employee_id int,
    project_id int,
    hours_worked int NOT NULL,
    date_of_work date NOT NULL,
    contribution varchar(200) NOT NULL
);

create table dependents(
    employee_id int,
    dependent_name varchar(50) NOT NULL,
    sex char(1),
    dob date,
    relation varchar(50) NOT NULL
);

create table employee_leaves(
    leave_id int auto_increment,
    employee_id int,
    request_to int,
    start_date date NOT NULL,
    end_date date NOT NULL,
    approved boolean DEFAULT false,
    constraint pk_leave PRIMARY KEY (leave_id)
);

create table meetings(
    meeting_id int auto_increment,
    meeting_mode varchar(50),
    meeting_date date NOT NULL,
    meeting_start_time time NOT NULL,
    meeting_end_time time NOT NULL,
    meeting_location varchar(100),
    meeting_organiser_id int,
    department_id int,
    purpose_of_meeting varchar(200) NOT NULL,
    minutes_of_meeting varchar(400),
    constraint pk_meeting PRIMARY KEY (meeting_id)
);

create table attends_meeting(
    meeting_id int,
    employee_id int
);

create table skills(
    employee_id int,
    skill_name varchar(50) NOT NULL
);

create table departments(
    department_id int auto_increment,
    department_name varchar(100) NOT NULL,
    manager_id int,
    constraint pk_department PRIMARY KEY (department_id)
);

create table projects(
    project_id int auto_increment,
    project_name varchar(100) NOT NULL,
    project_description varchar(100),
    department_id int,
    project_start_date date NOT NULL,
    tentative_completion_date date,
    constraint pk_project PRIMARY KEY (project_id)
);

create table office_branches(
    branch_id int auto_increment,
    city varchar(100) NOT NULL,
    state varchar(100) NOT NULL,
    country varchar(100) NOT NULL,
    address varchar(100) NOT NULL,
    size_of_office int,
    constraint pk_branch PRIMARY KEY (branch_id)
);

create table requests(
    request_id int auto_increment,
    request_from int,
    request_to int,
    request_description varchar(50),
    approved boolean DEFAULT false,
    constraint pk_request PRIMARY KEY (request_id)
)