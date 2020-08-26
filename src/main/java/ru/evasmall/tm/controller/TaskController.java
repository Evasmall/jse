package ru.evasmall.tm.controller;

import ru.evasmall.tm.Application;
import ru.evasmall.tm.entity.Project;
import ru.evasmall.tm.entity.Task;
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
    public int createTask() {
        System.out.println("CREATE TASK");
        System.out.println("PLEASE ENTER TASK NAME:");
        final String name = scanner.nextLine();
        System.out.println("PLEASE ENTER TASK DESCRIPTION:");
        final String description = scanner.nextLine();
        final Long userId = Application.userIdCurrent;
        taskService.create(name, description, userId);
        System.out.println("OK");
        return 0;
    }

    //Изменение задачи по индексу с учетом принадлежности задачи пользователю.
    public int updateTaskByIndex() {
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
            System.out.println("PLEASE ENTER TASK DESCRIPTION:");
            final String description = scanner.nextLine();
            taskService.update(task.getId(), name, description);
            System.out.println("OK");
            return 0;
        }
        else return -1;
    }

    //Изменение задачи по идентификатору с учетом принадлежности задачи пользователю.
    public int updateTaskById() {
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
            System.out.println("PLEASE ENTER TASK DESCRIPTION:");
            final String description = scanner.nextLine();
            taskService.update(task.getId(), name, description);
            System.out.println("OK");
            return 0;
        }
        else return -1;
    }

    //Удаление задачи по индексу с учетом принадлежности задачи пользователю.
    public int removeTaskByIndex() {
        System.out.println("REMOVE TASK BY INDEX");
        System.out.println("PLEASE ENTER TASK INDEX:");
        final Integer index = control.scannerIndexIsInteger();
        if (index != null) {
            final Task task = taskService.findByIndexUserId(index);
            if (task == null) {
                systemController.displayForeign("TASK");
                return 0;
            }
            final Task taskRemove = taskService.removeByIndexUserId(index);
            if (task == null) System.out.println("FAIL");
            else System.out.println("OK");
            return 0;
        }
        else return -1;
    }

    //Удаление задачи по идентификатору с учетом принадлежности задачи пользователю.
    public int removeTaskById() {
        System.out.println("REMOVE TASK BY ID");
        System.out.println("PLEASE ENTER TASK ID:");
        final Long id = control.scannerIdIsLong();
        if (id != null) {
            final Task task = taskService.removeByIdUserId(id);
            if (task == null) systemController.displayForeign("TASK");
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
        System.out.println("VIEW TASK");
        System.out.println("ID: " + task.getId());
        System.out.println("NAME: " + task.getName());
        System.out.println("DESCRIPTION: " + task.getDescription());
        System.out.println("PROJECT ID: " + task.getProjectId());
        System.out.println("USER LOGIN: " + userService.findByUserId(task.getUserid()).getLogin());
        System.out.println("_____");
    }

    //Просмотр задачи по индексу.
    public int viewTaskByIndex() {
        System.out.println("ENTER TASK INDEX:");
        final Integer index = control.scannerIndexIsInteger();
        if (index != null) {
            final Task task = taskService.findByIndex(index);
            if (task == null) {
                System.out.println("TASK NOT FOUND");
                return 0;
            }
            viewTask(task);
            return 0;
        }
        else return -1;
    }

    //Просмотр списка задач по наименованию
    public int viewTaskByName() {
        System.out.print("ENTER TASK NAME:");
        String name = scanner.nextLine();
        final List <Task> tasks = taskService.findByName(name);
        if (tasks == null) {
            systemController.displayForeign("TASK");
            return -1;
        }
        for (Task task: tasks) {
            viewTask(task);
        }
        return 0;
    }

    //Просмотр задачи по идентификатору.
    public int viewTaskById() {
        System.out.println("ENTER TASK ID:");
        final Long id = control.scannerIdIsLong();
        if (id != null) {
            final Task task = taskService.findById(id);
            if (task == null) {
                System.out.println("TASK NOT FOUND.");
                return -1;
            }
            viewTask(task);
            return 0;
        }
        return -1;
    }

    //Cписок задач.
    public int listTask() {
        System.out.println("LIST TASK");
        int index = 1;
        viewTasks(taskService.findAll());
        System.out.println("OK");
        return 0;
    }

    //Просмотр списка задач.
    public void viewTasks (final List<Task> tasks) {
        if (tasks == null || tasks.isEmpty()) return;
        int index = 1;
        taskService.TaskSortByName(tasks);
        for (final Task task: tasks) {
            System.out.println(index + ". TASKID: " + task.getId() + "; NAME: " + task.getName() + "; DESCRIPTION: "
                    + task.getDescription() + "; PROJECTID: " + task.getProjectId()
                    + "; USER LOGIN: " + userService.findByUserId(task.getUserid()).getLogin());
            index++;
        }
    }

    //Просмотр списка задач, принадлежащих проекту по идентификатору.
    public int listTaskByProjectId() {
        System.out.println("LIST TASK BY PROJECT");
        System.out.println("PLEASE ENTER PROJECT ID:");
        final Long projectId = control.scannerIdIsLong();
        if (projectId != null) {
            final Project project = projectService.findById(projectId);
            if (project == null) {
                System.out.println("PROJECT NOT FOUND.");
                return -1;
            }
            final List<Task> tasks = taskService.findAllByProjectId(projectId);
            viewTasks(tasks);
            System.out.println("OK");
            return 0;
        }
        return -1;
    }

    //Добавление задачи к проекту по идентификаторам с учетом принадлежности проекта пользователю.
    public int addTaskToProjectByIds() {
        System.out.println("ADD TASK TO PROJECT BY IDS");
        System.out.println("PLEASE ENTER PROJECT ID:");
        final Long projectId = control.scannerIdIsLong();
        if (projectId != null) {
            final Project project = projectService.findByIdUserId(projectId);
            if (project == null) {
                systemController.displayForeign("PROJECT");
                //System.out.println("PROJECT NOT FOUND.");
                return -1;
            }
            System.out.println("PLEASE ENTER TASK ID:");
            final Long taskId = control.scannerIdIsLong();
            if (taskId != null) {
                final Task task = taskService.findByIdUserId(taskId);
                if (task == null) {
                    System.out.println("TASK NOT FOUND.");
                    return -1;
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
    public int removeTaskFromProjectByIds() {
        System.out.println("REMOVE TASK FROM PROJECT BY IDS");
        System.out.println("PLEASE ENTER PROJECT ID:");
        final Long projectId = control.scannerIdIsLong();
        if (projectId != null) {
            final Project project = projectService.findByIdUserId(projectId);
            if (project == null) {
                System.out.println("PROJECT NOT FOUND.");
                return -1;
            }
            System.out.println("PLEASE ENTER TASK ID:");
            final Long taskId = control.scannerIdIsLong();
            if (taskId != null) {
                final Task task = taskService.findById(taskId);
                if (task == null) {
                    System.out.println("TASK NOT FOUND.");
                    return -1;
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
    public int removeProjectByIdWithTasks() {
        System.out.println("REMOVE PROJECT WITH BY ID");
        System.out.println("PLEASE ENTER PROJECT ID:");
        final Long projectId = control.scannerIdIsLong();
        if (projectId != null) {
            final Project project = projectService.findByIdUserId(projectId);
            if (project == null) {
                System.out.println("PROJECT NOT FOUND.");
                return -1;
            }
            Project projectTask = projectTaskService.removeProjectByIdWithTask(projectId);
            if (projectTask == null) System.out.println("FAIL");
            else System.out.println("OK");
            return 0;
        }
        return -1;
    }

    //Удаление проекта со принадлежащими ему задачами по индексу.
    public int removeProjectByIndexWithTasks() {
        System.out.println("REMOVE PROJECT WITH BY INDEX");
        System.out.println("PLEASE ENTER PROJECT INDEX:");
        final Integer index = control.scannerIndexIsInteger();
        if (index != null) {
            final Project project = projectService.findByIndexUserId(index);
            if (project == null) {
                systemController.displayForeign("PROJECT");
                return -1;
            }
            Project projectRemove = projectTaskService.removeProjectByIndexWithTask(index);
            if (projectRemove == null) System.out.println("FAIL.");
            else System.out.println("OK");
            return 0;
        }
        else return  -1;
    }

}