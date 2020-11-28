package ru.evasmall.tm.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.evasmall.tm.Application;
import ru.evasmall.tm.entity.Project;
import ru.evasmall.tm.entity.Task;
import ru.evasmall.tm.exeption.ProjectNotFoundException;
import ru.evasmall.tm.exeption.TaskNotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.evasmall.tm.constant.TerminalConst.RETURN_ERROR;

class ProjectTaskServiceTest {

    private TaskService taskService;

    private ProjectService projectService;

    private ProjectTaskService projectTaskService;

    private Long user_id = 10000000L;

    private Long id = 2L;

    private String name = "NAME";

    private String description = "DESCRIPTION";

    private Long userIdAdmin = 1L;

    private final Application application = new Application();

    @BeforeEach
    void setUp() {
        taskService = TaskService.getInstance();
        projectService = ProjectService.getInstance();
        projectTaskService = ProjectTaskService.getInstance();
    }

    @Test
    void findAllByProjectIdCorrect() throws ProjectNotFoundException, TaskNotFoundException {
        Project project = projectService.create(name, description, userIdAdmin);
        List<Task> tasks = new ArrayList<>();
        Task task1 = taskService.create("name1", "description1", userIdAdmin);
        Task task2 = taskService.create("name2", "description2", userIdAdmin);
        tasks.add(task1);
        tasks.add(task2);
        projectTaskService.addTaskToProject(project.getId(), task1.getId(), userIdAdmin);
        projectTaskService.addTaskToProject(project.getId(), task2.getId(), userIdAdmin);
        assertEquals(tasks, projectTaskService.findAllByProjectId(project.getId()));
    }

    @Test
    void listTaskByProjectIdException() throws ProjectNotFoundException {
        assertEquals(RETURN_ERROR, projectTaskService.listTaskByProjectId());
    }

    @Test
    void removeTaskFromProjectCorrect() throws ProjectNotFoundException, TaskNotFoundException {
        Project project = projectService.create(name, description, user_id);
        Task task = taskService.create(name, description, user_id);
        projectTaskService.addTaskToProject(project.getId(), task.getId(), user_id);
        assertEquals(project.getId(), task.getProjectId());
        projectTaskService.removeTaskFromProject(project.getId(), task.getId());
        assertEquals(null, task.getProjectId());
    }

    @Test
    void removeTaskFromProjectByIdsException() throws ProjectNotFoundException, TaskNotFoundException {
        assertEquals(RETURN_ERROR, projectTaskService.removeTaskFromProjectByIds());
    }

    @Test
    void addTaskToProjectCorrect() throws ProjectNotFoundException, TaskNotFoundException {
        Project project = projectService.create(name, description, user_id);
        Task task = taskService.create(name, description, user_id);
        projectTaskService.addTaskToProject(project.getId(), task.getId(), user_id);
        assertEquals(project.getId(), task.getProjectId());
    }

    @Test
    void addTaskToProjectByIds()throws ProjectNotFoundException, TaskNotFoundException {
        assertEquals(RETURN_ERROR, projectTaskService.addTaskToProjectByIds());
    }

    @Test
    void removeProjectByIdWithTaskCorrect() throws ProjectNotFoundException, TaskNotFoundException {
        application.userIdCurrent = user_id;
        Project project = projectService.create(name, description, user_id);
        Task task = taskService.create(name, description, user_id);
        long task_id = task.getId();
        projectTaskService.addTaskToProject(project.getId(), task.getId(), user_id);
        projectTaskService.removeProjectByIdWithTask(project.getId());
        assertThrows(TaskNotFoundException.class,() -> taskService.findById(task_id));
    }

    @Test
    void removeProjectByIdWithTasksException() throws ProjectNotFoundException, TaskNotFoundException {
        assertEquals(RETURN_ERROR, projectTaskService.removeProjectByIdWithTasks());
    }

    @Test
    void removeProjectByIndexWithTaskCorrect() throws ProjectNotFoundException, TaskNotFoundException {
        application.userIdCurrent = userIdAdmin;
        projectService.clearProject();
        Project project = projectService.create(name, description, userIdAdmin);
        Task task = taskService.create(name, description, userIdAdmin);
        long task_id = task.getId();
        projectTaskService.addTaskToProject(project.getId(), task.getId(), userIdAdmin);
        projectTaskService.removeProjectByIndexWithTask(0);
        assertThrows(TaskNotFoundException.class,() -> taskService.findById(task_id));
    }

    @Test
    void removeProjectByIndexWithTasksException() throws ProjectNotFoundException, TaskNotFoundException {
        assertEquals(RETURN_ERROR, projectTaskService.removeProjectByIndexWithTasks());
    }

}