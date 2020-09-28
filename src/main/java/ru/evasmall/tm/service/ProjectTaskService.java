package ru.evasmall.tm.service;

import ru.evasmall.tm.Application;
import ru.evasmall.tm.entity.Project;
import ru.evasmall.tm.entity.Task;
import ru.evasmall.tm.exeption.ProjectNotFoundException;
import ru.evasmall.tm.exeption.TaskNotFoundException;
import ru.evasmall.tm.repository.ProjectRepository;
import ru.evasmall.tm.repository.TaskRepository;
import ru.evasmall.tm.util.Control;

import java.util.Collections;
import java.util.List;

import static ru.evasmall.tm.constant.TerminalMassage.*;

public class ProjectTaskService extends AbstractService {

    private final ProjectRepository projectRepository;

    private final TaskRepository taskRepository;

    private final TaskService taskService;

    private final ProjectService projectService;

    private final Control control = new Control();

    private static ProjectTaskService instance = null;

    public ProjectTaskService() {
        this.projectRepository = ProjectRepository.getInstance();
        this.taskRepository = TaskRepository.getInstance();
        this.taskService = TaskService.getInstance();
        this.projectService = ProjectService.getInstance();
    }

    public static ProjectTaskService getInstance() {
        if (instance == null) {
            instance = new ProjectTaskService();
        }
        return instance;
    }

    //Вывод всех задач проекта.
    public List<Task> findAllByProjectId(final Long projectId) {
        if (projectId == null) return Collections.emptyList();
        return taskRepository.findAddByProjectId(projectId);
    }

    //Просмотр списка задач, принадлежащих проекту по идентификатору.
    public int listTaskByProjectId() throws ProjectNotFoundException {
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
                taskService.viewTasks(tasks);
                System.out.println("OK");
                return 0;
            }
            return -1;
        }
    }

    //Удаление задачи из проекта.
    public Task removeTaskFromProject(final Long projectId, final Long taskId) {
        final Task task = taskRepository.findByProjectIdAndId(projectId, taskId);
        task.setProjectId(null);
        return task;
    }

    //Удаление задачи из проекта по идентификаторам.
    public int removeTaskFromProjectByIds() throws ProjectNotFoundException, TaskNotFoundException {
        System.out.println("REMOVE TASK FROM PROJECT BY IDS");
        System.out.println(PROJECT_ID_ENTER);
        final Long projectId = control.scannerIdIsLong();
        if (projectId != null) {
            projectService.findByIdUserId(projectId);
            System.out.println(TASK_ID_ENTER);
            final Long taskId = control.scannerIdIsLong();
            if (taskId != null) {
                taskService.findById(taskId);
                removeTaskFromProject(projectId, taskId);
                System.out.println("OK");
                return 0;
            }
            return -1;
        }
        return -1;
    }

    //Добавление задачи в проект.
    public Task addTaskToProject(final Long projectId, final Long taskId, final Long userId) throws ProjectNotFoundException, TaskNotFoundException  {
        projectRepository.findById(projectId);
        final Task task = taskRepository.findById(taskId);
        task.setProjectId(projectId);
        task.setUserid(userId);
        return task;
    }

    //Добавление задачи к проекту по идентификаторам с учетом принадлежности проекта пользователю.
    public int addTaskToProjectByIds() throws ProjectNotFoundException, TaskNotFoundException {
        System.out.println("ADD TASK TO PROJECT BY IDS");
        System.out.println(PROJECT_ID_ENTER);
        final Long projectId = control.scannerIdIsLong();
        if (projectId != null) {
            projectService.findByIdUserId(projectId);
            System.out.println(TASK_ID_ENTER);
            final Long taskId = control.scannerIdIsLong();
            if (taskId != null) {
                taskService.findByIdUserId(taskId);
                addTaskToProject(projectId, taskId, Application.userIdCurrent);
                System.out.println("OK");
                return 0;
            }
            return -1;
        }
        return -1;
    }

    //Удаление проекта со связанными задачами по идентификатору.
    public Project removeProjectByIdWithTask(final Long projectId) throws ProjectNotFoundException, TaskNotFoundException  {
        final Project project = projectRepository.findById(projectId);
        final List<Task> tasks = findAllByProjectId(projectId);
        if (tasks == null) return project;
        for (Task task: tasks) {
            taskRepository.removeById(task.getId());
        }
        projectRepository.removeById(projectId);
        return project;
    }

    //Удаление проекта со принадлежащими ему задачами по идентификатору.
    public int removeProjectByIdWithTasks() throws ProjectNotFoundException, TaskNotFoundException {
        System.out.println("REMOVE PROJECT WITH BY ID");
        System.out.println(PROJECT_ID_ENTER);
        final Long projectId = control.scannerIdIsLong();
        if (projectId != null) {
            removeProjectByIdWithTask(projectId);
            System.out.println("OK");
            return 0;
        }
        return -1;
    }

    //Удаление проекта со связанными задачами по индексу.
    public Project removeProjectByIndexWithTask(final int index) throws ProjectNotFoundException, TaskNotFoundException {
        final Project project = projectRepository.findByIndex(index);
        final List<Task> tasks = findAllByProjectId(project.getId());
        if (tasks == null) return project;
        for (Task task: tasks) {
            taskRepository.removeById(task.getId());
        }
        projectRepository.removeById(project.getId());
        return project;
    }

    //Удаление проекта со принадлежащими ему задачами по индексу.
    public int removeProjectByIndexWithTasks() throws ProjectNotFoundException, TaskNotFoundException {
        System.out.println("REMOVE PROJECT WITH BY INDEX");
        System.out.println(PROJECT_INDEX_ENTER);
        final Integer index = control.scannerIndexIsInteger();
        if (index != null) {
            removeProjectByIndexWithTask(index);
            System.out.println("OK");
            return 0;
        }
        else return  -1;
    }

}
