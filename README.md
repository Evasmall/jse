**EV-TASK-MANAGER**

- **Application:** JAVA lesson application. Output terminal parameters.

- **OS:** Windows, Linux, Mac 
- **Tech Stack:** Java CE, Apache Maven, IntelliJ IDEA CE, Git 
- **Software:** Java OpenJDK 11.0.7, Apache Maven 3.6.1
- **Hardware:** CPU 64-bit, RAM 8Gb, HDD 80Gb

- **Developer:** Smolkina Evgeniya
- **Email:** smolkina_ev@nlmk.com
- **Backup:** https://github.com/Evasmall/jse

| Build |
| ------ |
| mvn clean install |

| Start |
| ------ |
| java -jar target/ev-task-manager-1.0.34.jar |

| Terminal commands | 
| ------ | 
| version - Display program version. | 
| about - Display developer info. | 
| help - Display list of terminal commands. | 
| history - Display history of terminal commands. | 
| exit - Terminate console application. |
| registration - User registration |
| sign - User sign. |
| user-list - Display list of users sorted by login. |
| user-list-by-fio - Display list of users sorted by lastname, firstname, middlname. |
| user-remove-by-login - Remove user by login (only for ADMIN!). |
| user-update-role - Update user role (only for ADMIN!) |
| user-profile - Display current user session. |
| user-profile-update - Update current user profile. |
| password-change - Change user password. |
| user-exit - Terminate current session. |

| Terminal commands for projects | Terminal commands for tasks | 
| ------ | ------ | 
| project-list - Display list of projects. | task-list - Display list of tasks. | 
| project-create - Create new project by name. | task-create - Create new task by name. | 
| project-clear - Remove all projects (only for ADMIN!). | task-clear - Remove all tasks (only for ADMIN!). |
| project-view-by-name - View project by name. | task-view-by-name - View task by name. |
| project-view-by-index - View project by index. | task-view-by-index - View task by index. |
| project-view-by-id - View project by id. | task-view-by-id - View task by id. |
| project-remove-by-id - Remove project by id (for current user). | task-remove-by-id - Remove task by id (for current user). |
| project-remove-by-index - Remove project by index (for current user). | task-remove-by-index - Remove task by index (for current user). |
| project-update-by-id - Update project by id (for current user). | task-update-by-id - Update task by id (for current user). |
| project-update-by-index - Update project by index (for current user). | task-update-by-index - Update task by index (for current user). |
| project-add-user - Add project for user by login. | task-add-user - Add task for user by login. |
| project-remove-user - Remove project from user. | task-remove-user - Remove task from user. | 
| | task-list-by-project-id - Display task list by project id. | 
| | task-add-to_project-by-ids - Add task to project by ids (for current user). | task-remove-from-project-by-ids - Remove task from project by ids (for current user). | 
| project-remove-by-id-with-tasks - Remove project by id with tasks (for project of current user). |
| project-remove-by-index-with-tasks - Remove project by index with tasks (for project of current user). | |
| object-json - Write projects, tasks, users in json file. | |
| object-xml - Write projects, tasks, users in xml file. | |
| read-json - Read projects, tasks, users from json file. | |
| read-xml- Read projects, tasks, users from xml file. | |