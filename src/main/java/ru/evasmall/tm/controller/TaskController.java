package ru.evasmall.tm.controller;

import ru.evasmall.tm.Application;
import ru.evasmall.tm.entity.Task;
import ru.evasmall.tm.entity.User;
import ru.evasmall.tm.exeption.IncorrectFormatException;
import ru.evasmall.tm.exeption.ProjectNotFoundException;
import ru.evasmall.tm.exeption.TaskNotFoundException;
import ru.evasmall.tm.service.ProjectService;
import ru.evasmall.tm.service.ProjectTaskService;
import ru.evasmall.tm.service.TaskService;
import ru.evasmall.tm.service.UserService;
import ru.evasmall.tm.util.Control;

import java.util.List;

import static ru.evasmall.tm.constant.TerminalMassage.*;

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
    public int createTask() throws IncorrectFormatException {
        System.out.println("CREATE TASK");
        System.out.println(TASK_NAME_ENTER);
        final String name = scanner.nextLine();
        System.out.println(TASK_DESCRIPTION_ENTER);
        final String description = scanner.nextLine();
        taskService.create(name, description, Application.userIdCurrent);
        System.out.println("OK");
        return 0;
    }

    //Изменение задачи по индексу с учетом принадлежности задачи пользователю.
    public int updateTaskByIndex() throws TaskNotFoundException, IncorrectFormatException {
        System.out.println("UPDATE TASK");
        System.out.println(TASK_INDEX_ENTER);
        final Integer index = control.scannerIndexIsInteger();
        if (index != null) {
            final Task task = taskService.findByIndexUserId(index);
            System.out.println(TASK_NAME_ENTER);
            final String name = scanner.nextLine();
            System.out.println(TASK_DESCRIPTION_ENTER);
            final String description = scanner.nextLine();
            taskService.update(task.getId(), name, description);
            System.out.println("OK");
            return 0;
        }
        else return -1;
    }

    //Изменение задачи по идентификатору с учетом принадлежности задачи пользователю.
    public int updateTaskById() throws TaskNotFoundException, IncorrectFormatException {
        System.out.println("UPDATE TASK");
        System.out.println(TASK_ID_ENTER);
        final Long id = control.scannerIdIsLong();
        if (id != null) {
            final Task task = taskService.findByIdUserId(id);
            System.out.println(TASK_NAME_ENTER);
            final String name = scanner.nextLine();
            System.out.println(TASK_DESCRIPTION_ENTER);
            final String description = scanner.nextLine();
            taskService.update(task.getId(), name, description);
            System.out.println("OK");
            return 0;
        }
        else return -1;
    }

    //Удаление задачи по индексу с учетом принадлежности задачи пользователю.
    public int removeTaskByIndex() throws TaskNotFoundException, IncorrectFormatException {
        System.out.println("REMOVE TASK BY INDEX");
        System.out.println(TASK_INDEX_ENTER);
        final Integer index = control.scannerIndexIsInteger();
        if (index != null) {
            taskService.removeByIndexUserId(index);
            System.out.println("OK");
            return 0;
        }
        else return -1;
    }

    //Удаление задачи по идентификатору с учетом принадлежности задачи пользователю.
    public int removeTaskById() throws TaskNotFoundException, IncorrectFormatException {
        System.out.println("REMOVE TASK BY ID");
        System.out.println(TASK_ID_ENTER);
        final Long id = control.scannerIdIsLong();
        if (id != null) {
            taskService.removeByIdUserId(id);
            System.out.println("OK");
            return 0;
        }
        else return -1;
     }

    //Удаление всех задач (функционал доступен только администраторам).
    public int clearTask() {
        if (userService.findByUserId(Application.userIdCurrent).isAdminTrue()) {
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
    public int viewTaskByIndex() throws TaskNotFoundException, IncorrectFormatException {
        System.out.println(TASK_INDEX_ENTER);
        final Integer index = control.scannerIndexIsInteger();
        if (index != null) {
            final Task task = taskService.findByIndex(index);
            viewTask(task);
            return 0;
        }
        return -1;
    }

    //Просмотр списка задач по наименованию
    public int viewTaskByName() throws TaskNotFoundException, IncorrectFormatException {
        System.out.println(TASK_NAME_ENTER);
        String name = scanner.nextLine();
        final List <Task> tasks = taskService.findByName(name);
        for (Task task: tasks) {
            viewTask(task);
        }
        return 0;
    }

    //Просмотр задачи по идентификатору.
    public int viewTaskById() throws TaskNotFoundException, IncorrectFormatException {
        System.out.println(TASK_ID_ENTER);
        final Long id = control.scannerIdIsLong();
        if (id != null) {
            final Task task = taskService.findById(id);
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
    public int listTaskByProjectId() throws ProjectNotFoundException, IncorrectFormatException {
        if (Application.userIdCurrent == null) {
            System.out.println("LIST TASKS NOT ACCESS FOR UNAUTHORIZED USER!");
            return -1;
        }
        else {
            System.out.println("LIST TASK BY PROJECT");
            System.out.println(PROJECT_ID_ENTER);
            final Long projectId = control.scannerIdIsLong();
            if (projectId != null) {
                projectService.findById(projectId);
                final List<Task> tasks = taskService.findAllByProjectId(projectId);
                viewTasks(tasks);
                System.out.println("OK");
                return 0;
            }
            return -1;
        }
    }

    //Добавление задачи к проекту по идентификаторам с учетом принадлежности проекта пользователю.
    public int addTaskToProjectByIds() throws ProjectNotFoundException, TaskNotFoundException, IncorrectFormatException {
        System.out.println("ADD TASK TO PROJECT BY IDS");
        System.out.println(PROJECT_ID_ENTER);
        final Long projectId = control.scannerIdIsLong();
        if (projectId != null) {
            projectService.findByIdUserId(projectId);
            System.out.println(TASK_ID_ENTER);
            final Long taskId = control.scannerIdIsLong();
            if (taskId != null) {
                taskService.findByIdUserId(taskId);
                projectTaskService.addTaskToProject(projectId, taskId, Application.userIdCurrent);
                System.out.println("OK");
                return 0;
            }
            return -1;
        }
        return -1;
    }

    //Удаление задачи из проекта по идентификаторам.
    public int removeTaskFromProjectByIds() throws ProjectNotFoundException, TaskNotFoundException, IncorrectFormatException {
        System.out.println("REMOVE TASK FROM PROJECT BY IDS");
        System.out.println(PROJECT_ID_ENTER);
        final Long projectId = control.scannerIdIsLong();
        if (projectId != null) {
            projectService.findByIdUserId(projectId);
            System.out.println(TASK_ID_ENTER);
            final Long taskId = control.scannerIdIsLong();
            if (taskId != null) {
                taskService.findById(taskId);
                projectTaskService.removeTaskFromProject(projectId, taskId);
                System.out.println("OK");
                return 0;
            }
            return -1;
        }
        return -1;
    }

    //Удаление проекта со принадлежащими ему задачами по идентификатору.
    public int removeProjectByIdWithTasks() throws ProjectNotFoundException, TaskNotFoundException, IncorrectFormatException {
        System.out.println("REMOVE PROJECT WITH BY ID");
        System.out.println(PROJECT_ID_ENTER);
        final Long projectId = control.scannerIdIsLong();
        if (projectId != null) {
            projectTaskService.removeProjectByIdWithTask(projectId);
            System.out.println("OK");
            return 0;
        }
        return -1;
    }

    //Удаление проекта со принадлежащими ему задачами по индексу.
    public int removeProjectByIndexWithTasks() throws ProjectNotFoundException, TaskNotFoundException, IncorrectFormatException {
        System.out.println("REMOVE PROJECT WITH BY INDEX");
        System.out.println(PROJECT_INDEX_ENTER);
        final Integer index = control.scannerIndexIsInteger();
        if (index != null) {
            projectTaskService.removeProjectByIndexWithTask(index);
            System.out.println("OK");
            return 0;
        }
        else return  -1;
    }

    //Добавление принадлежности задачи пользователю по идентификатору задачи и логину пользователя.
    public int addTaskToUser() throws TaskNotFoundException, IncorrectFormatException {
        System.out.println("ADD TASK TO USER");
        System.out.println("PLEASE ENTER LOGIN:");
        final User user1 = userService.findByLogin(scanner.nextLine());
        if (user1 == null) {
            System.out.println("LOGIN NOT EXIST!");
        }
        else {
            final Long userId = user1.getUserid();
            if (userId != null) {
                System.out.println(TASK_ID_ENTER);
                final Long taskId = control.scannerIdIsLong();
                if (taskId != null) {
                    taskService.findByIdUserId(taskId);
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
    public int removeTaskFromUser() throws TaskNotFoundException, IncorrectFormatException {
        System.out.println("REMOVE TASK FROM USER");
        System.out.println(TASK_ID_ENTER);
        final Long taskId = control.scannerIdIsLong();
        if (taskId != null) {
            final Task task = taskService.findByIdUserId(taskId);
            if (task.getUserid() == null) {
                System.out.println("TASK NOT HAVE USER.");
                return -1;
            }
            if (task.getUserid().equals(Application.userIdCurrent) || userService.findByUserId(Application.userIdCurrent).isAdminTrue()) {
                task.setUserid(null);
                System.out.println("ОК");
                return 0;
            }
        }
        return -1;
    }

}