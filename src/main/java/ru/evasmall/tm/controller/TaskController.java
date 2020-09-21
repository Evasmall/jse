package ru.evasmall.tm.controller;

import ru.evasmall.tm.Application;
import ru.evasmall.tm.entity.Project;
import ru.evasmall.tm.entity.Task;
import ru.evasmall.tm.entity.User;
import ru.evasmall.tm.exeption.ObjectNotFound;
import ru.evasmall.tm.service.ProjectService;
import ru.evasmall.tm.service.ProjectTaskService;
import ru.evasmall.tm.service.TaskService;
import ru.evasmall.tm.service.UserService;
import ru.evasmall.tm.util.Control;

import java.util.List;

public class TaskController extends AbstractController{

    private final TaskService taskService;

    private final ProjectService projectService;

    private final ProjectTaskService projectTaskService;

    private final UserService userService;

    private final SystemController systemController = new SystemController();

    private final Control control = new Control();

    public TaskController(TaskService taskService, ProjectTaskService projectTaskService, UserService userService, ProjectService projectService) {
        this.taskService = taskService;
        this.projectTaskService = projectTaskService;
        this.userService = userService;
        this.projectService = projectService;
    }

    //Создание задачи.
    public int createTask() throws ObjectNotFound {
        System.out.println("CREATE TASK");
        System.out.println("PLEASE ENTER TASK NAME:");
        final String name = scanner.nextLine();
        if (name.isEmpty()) {
            System.out.println("TASK NAME EMPTY. FAIL.");
            return -1;
        }
        System.out.println("PLEASE ENTER TASK DESCRIPTION:");
        final String description = scanner.nextLine();
        if (description.isEmpty())
        {
            System.out.println("TASK DESCRIPTION EMPTY. FAIL.");
            return -1;
        }
        final Long userId = Application.userIdCurrent;
        Task taskNew = taskService.create(name, description, userId);
        if (taskNew == null) {
            throw new ObjectNotFound("TASK NOT CREATE. FAIL.");
        }
        System.out.println("OK");
        return 0;
    }

    //Изменение задачи по индексу с учетом принадлежности задачи пользователю.
    public int updateTaskByIndex() throws ObjectNotFound {
        System.out.println("UPDATE TASK");
        System.out.println("ENTER TASK INDEX:");
        final Integer index = control.scannerIndexIsInteger();
        if (index != null) {
            final Task task = taskService.findByIndexUserId(index);
            if (task == null) {
                systemController.displayForeign("TASK");
                return 0;
            }
            System.out.println("PLEASE ENTER TASK NAME:");
            final String name = scanner.nextLine();
            if (name.isEmpty()) {
                System.out.println("TASK NAME EMPTY. FAIL.");
                return -1;
            }
            System.out.println("PLEASE ENTER TASK DESCRIPTION:");
            final String description = scanner.nextLine();
            if (description.isEmpty())
            {
                System.out.println("TASK DESCRIPTION EMPTY. FAIL.");
                return -1;
            }
            Task taskNew = taskService.update(task.getId(), name, description);
            if (taskNew == null) {
                throw new ObjectNotFound("TASK NOT UPDATE. FAIL.");
            }
            System.out.println("OK");
            return 0;
        }
        else return -1;
    }

    //Изменение задачи по идентификатору с учетом принадлежности задачи пользователю.
    public int updateTaskById() throws ObjectNotFound {
        System.out.println("UPDATE TASK");
        System.out.println("ENTER TASK ID:");
        final Long id = control.scannerIdIsLong();
        if (id != null) {
            final Task task = taskService.findByIdUserId(id);
            if (task == null) {
                systemController.displayForeign("TASK");
                return -1;
            }
            System.out.println("PLEASE ENTER TASK NAME:");
            final String name = scanner.nextLine();
            if (name.isEmpty()) {
                System.out.println("TASK NAME EMPTY. FAIL.");
                return -1;
            }
            System.out.println("PLEASE ENTER TASK DESCRIPTION:");
            final String description = scanner.nextLine();
            if (description.isEmpty())
            {
                System.out.println("TASK DESCRIPTION EMPTY. FAIL.");
                return -1;
            }
            Task taskNew = taskService.update(task.getId(), name, description);
            if (taskNew == null) {
                throw new ObjectNotFound("TASK NOT UPDATE. FAIL.");
            }
            System.out.println("OK");
            return 0;
        }
        else return -1;
    }

    //Удаление задачи по индексу с учетом принадлежности задачи пользователю.
    public int removeTaskByIndex() throws ObjectNotFound {
        System.out.println("REMOVE TASK BY INDEX");
        System.out.println("PLEASE ENTER TASK INDEX:");
        final Integer index = control.scannerIndexIsInteger();
        if (index != null) {
            final Task task = taskService.removeByIndexUserId(index);
            if (task == null) {
                systemController.displayForeign("TASK");
                throw new ObjectNotFound("TASK NOT FOUND BY INDEX.");
            }
            else System.out.println("OK");
            return 0;
        }
        else return -1;
    }

    //Удаление задачи по идентификатору с учетом принадлежности задачи пользователю.
    public int removeTaskById() throws ObjectNotFound {
        System.out.println("REMOVE TASK BY ID");
        System.out.println("PLEASE ENTER TASK ID:");
        final Long id = control.scannerIdIsLong();
        if (id != null) {
            final Task task = taskService.removeByIdUserId(id);
            if (task == null) {
                systemController.displayForeign("TASK");
                throw new ObjectNotFound("TASK NOT FOUND BY ID.");
            }
            else System.out.println("OK");
            return 0;
        }
        else return -1;
     }

    //Удаление всех задач (функционал доступен только администраторам).
    public int clearTask() {
        if (userService.findByUserId(Application.userIdCurrent).isAdmin_true() == true) {
            System.out.println("CLEAR TASK");
            taskService.clear();
            System.out.println("OK");
            return 0;
        }
        else {
            systemController.displayForAdminOnly();
            return -1;
        }
    }

    //Просмотр задачи.
    public void viewTask(final Task task) {
        if (task == null) return;
        if (Application.userIdCurrent == null) {
            System.out.println("TASKS NOT ACCESS FOR UNAUTHORIZED USER!");
            return;
        }
        else {
            System.out.println("VIEW TASK");
            System.out.println("ID: " + task.getId());
            System.out.println("NAME: " + task.getName());
            System.out.println("DESCRIPTION: " + task.getDescription());
            System.out.println("PROJECT ID: " + task.getProjectId());
            System.out.println("USER LOGIN: " + userService.findByUserId(task.getUserid()).getLogin());
            System.out.println("_____");
        }
    }

    //Просмотр задачи по индексу.
    public int viewTaskByIndex() throws ObjectNotFound {
        System.out.println("ENTER TASK INDEX:");
        final Integer index = control.scannerIndexIsInteger();
        if (index != null) {
            final Task task = taskService.findByIndex(index);
            if (task == null) {
                throw new ObjectNotFound("TASK NOT FOUND BY INDEX.");
            }
            viewTask(task);
            return 0;
        }
        else return -1;
    }

    //Просмотр списка задач по наименованию
    public int viewTaskByName() throws ObjectNotFound {
        System.out.print("ENTER TASK NAME:");
        String name = scanner.nextLine();
        final List <Task> tasks = taskService.findByName(name);
        if (tasks == null) {
            throw new ObjectNotFound("TASK NOT FOUND BY NAME.");
        }
        for (Task task: tasks) {
            viewTask(task);
        }
        return 0;
    }

    //Просмотр задачи по идентификатору.
    public int viewTaskById() throws ObjectNotFound {
        System.out.println("ENTER TASK ID:");
        final Long id = control.scannerIdIsLong();
        if (id != null) {
            final Task task = taskService.findById(id);
            if (task == null) {
                throw new ObjectNotFound("TASK NOT FOUND BY ID.");
            }
            viewTask(task);
            return 0;
        }
        return -1;
    }

    //Cписок задач.
    public int listTask() {
        //Проверка на авторизацию польователя
        if (Application.userIdCurrent == null) {
            System.out.println("LIST TASKS NOT ACCESS FOR UNAUTHORIZED USER!");
            return -1;
        }
        else {
            System.out.println("LIST TASK");
            viewTasks(taskService.findAll());
            System.out.println("OK");
            return 0;
        }
    }

    //Просмотр списка задач.
    public void viewTasks (final List<Task> tasks) {
        if (tasks == null || tasks.isEmpty()) return;
        int index = 1;
        taskService.TaskSortByName(tasks);
        for (final Task task: tasks) {
            final String login1;
            if (userService.findByUserId(task.getUserid()) == null) {
                login1 = null;
            }
            else {
                login1 = userService.findByUserId(task.getUserid()).getLogin();
            }
            System.out.println(index + ". TASKID: " + task.getId() + "; NAME: " + task.getName() + "; DESCRIPTION: "
                    + task.getDescription() + "; PROJECTID: " + task.getProjectId()
                    + "; USER LOGIN: " + login1);
            index++;
        }
    }

    //Просмотр списка задач, принадлежащих проекту по идентификатору.
    public int listTaskByProjectId() throws ObjectNotFound {
        if (Application.userIdCurrent == null) {
            System.out.println("LIST TASKS NOT ACCESS FOR UNAUTHORIZED USER!");
            return -1;
        }
        else {
            System.out.println("LIST TASK BY PROJECT");
            System.out.println("PLEASE ENTER PROJECT ID:");
            final Long projectId = control.scannerIdIsLong();
            if (projectId != null) {
                final Project project = projectService.findById(projectId);
                if (project == null) {
                    systemController.displayForeign("PROJECT");
                    throw new ObjectNotFound("PROJECT NOT FOUND BY ID.");
                }
                final List<Task> tasks = taskService.findAllByProjectId(projectId);
                viewTasks(tasks);
                System.out.println("OK");
                return 0;
            }
            return -1;
        }
    }

    //Добавление задачи к проекту по идентификаторам с учетом принадлежности проекта пользователю.
    public int addTaskToProjectByIds() throws ObjectNotFound {
        System.out.println("ADD TASK TO PROJECT BY IDS");
        System.out.println("PLEASE ENTER PROJECT ID:");
        final Long projectId = control.scannerIdIsLong();
        if (projectId != null) {
            final Project project = projectService.findByIdUserId(projectId);
            if (project == null) {
                systemController.displayForeign("PROJECT");
                throw new ObjectNotFound("PROJECT NOT FOUND BY ID.");
            }
            System.out.println("PLEASE ENTER TASK ID:");
            final Long taskId = control.scannerIdIsLong();
            if (taskId != null) {
                final Task task = taskService.findByIdUserId(taskId);
                if (task == null) {
                    systemController.displayForeign("TASK");
                    throw new ObjectNotFound("TASK NOT FOUND BY ID.");
                }
                projectTaskService.addTaskToProject(projectId, taskId, Application.userIdCurrent);
                System.out.println("OK");
                return 0;
            }
            return -1;
        }
        return -1;
    }

    //Удаление задачи из проекта по идентификаторам.
    public int removeTaskFromProjectByIds() throws ObjectNotFound {
        System.out.println("REMOVE TASK FROM PROJECT BY IDS");
        System.out.println("PLEASE ENTER PROJECT ID:");
        final Long projectId = control.scannerIdIsLong();
        if (projectId != null) {
            final Project project = projectService.findByIdUserId(projectId);
            if (project == null) {
                systemController.displayForeign("PROJECT");
                throw new ObjectNotFound("PROJECT NOT FOUND BY ID.");
            }
            System.out.println("PLEASE ENTER TASK ID:");
            final Long taskId = control.scannerIdIsLong();
            if (taskId != null) {
                final Task task = taskService.findById(taskId);
                if (task == null) {
                    systemController.displayForeign("TASK");
                    throw new ObjectNotFound("TASK NOT FOUND BY ID.");
                }
                projectTaskService.removeTaskFromProject(projectId, taskId);
                System.out.println("OK");
                return 0;
            }
            return -1;
        }
        return -1;
    }

    //Удаление проекта со принадлежащими ему задачами по идентификатору.
    public int removeProjectByIdWithTasks() throws ObjectNotFound {
        System.out.println("REMOVE PROJECT WITH BY ID");
        System.out.println("PLEASE ENTER PROJECT ID:");
        final Long projectId = control.scannerIdIsLong();
        if (projectId != null) {
            final Project project = projectService.findByIdUserId(projectId);
            if (project == null) {
                systemController.displayForeign("PROJECT");
                throw new ObjectNotFound("PROJECT NOT FOUND BY ID.");
            }
            Project projectTask = projectTaskService.removeProjectByIdWithTask(projectId);
            if (projectTask == null) System.out.println("FAIL");
            else System.out.println("OK");
            return 0;
        }
        return -1;
    }

    //Удаление проекта со принадлежащими ему задачами по индексу.
    public int removeProjectByIndexWithTasks() throws ObjectNotFound {
        System.out.println("REMOVE PROJECT WITH BY INDEX");
        System.out.println("PLEASE ENTER PROJECT INDEX:");
        final Integer index = control.scannerIndexIsInteger();
        if (index != null) {
            final Project project = projectService.findByIndexUserId(index);
            if (project == null) {
                systemController.displayForeign("PROJECT");
                throw new ObjectNotFound("PROJECT NOT FOUND BY ID.");
            }
            Project projectRemove = projectTaskService.removeProjectByIndexWithTask(index);
            if (projectRemove == null) System.out.println("FAIL.");
            else System.out.println("OK");
            return 0;
        }
        else return  -1;
    }

    //Добавление принадлежности задачи пользователю по идентификатору задачи и логину пользователя.
    public int addTaskToUser() throws ObjectNotFound {
        System.out.println("ADD TASK TO USER");
        System.out.println("PLEASE ENTER LOGIN:");
        final User user1 = userService.findByLogin(scanner.nextLine());
        if (user1 == null) {
            System.out.println("LOGIN NOT EXIST!");
        }
        else {
            final Long userId = user1.getUserid();
            if (userId != null) {
                System.out.println("PLEASE ENTER TASK ID:");
                final Long taskId = control.scannerIdIsLong();
                if (taskId != null) {
                    final Task task = taskService.findByIdUserId(taskId);
                    if (task == null) {
                        systemController.displayForeign("TASK");
                        throw new ObjectNotFound("TASK NOT FOUND BY ID.");
                    }
                    taskService.addTaskToUser(userId, taskId);
                    System.out.println("OK");
                    return 0;
                }
                return -1;
            }
        }
        return -1;
    }

    //Удаление принадлежности задачи пользователю по идентификатору задачи.
    public int removeTaskFromUser() throws ObjectNotFound {
        System.out.println("REMOVE TASK FROM USER");
        System.out.println("PLEASE ENTER TASK ID:");
        final Long taskId = control.scannerIdIsLong();
        if (taskId != null) {
            final Task task = taskService.findByIdUserId(taskId);
            if (task == null) {
                systemController.displayForeign("TASK");
                throw new ObjectNotFound("TASK NOT FOUND BY ID.");
            }
            if (task.getUserid() == null) {
                System.out.println("TASK NOT HAVE USER.");
                return -1;
            }
            if (task.getUserid().equals(Application.userIdCurrent) || userService.findByUserId(Application.userIdCurrent).isAdmin_true()) {
                task.setUserid(null);
                System.out.println("ОК");
                return 0;
            }
        }
        return -1;
    }

}