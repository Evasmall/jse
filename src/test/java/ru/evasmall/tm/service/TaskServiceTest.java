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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static ru.evasmall.tm.constant.TerminalConst.RETURN_ERROR;
import static ru.evasmall.tm.constant.TerminalConst.RETURN_OK;

class TaskServiceTest {

    private TaskService taskService;

    private ProjectService projectService;

    private ProjectTaskService projectTaskService;

    private Long user_id = 10000000L;

    private Long id = 2L;

    private String name = "TASK_NAME";

    private String description = "TASK_DESCRIPTION";

    private Long userIdAdmin = 1L;

    private final Application application = new Application();

    @BeforeEach
    void setUp() {
        taskService = TaskService.getInstance();
        projectService = ProjectService.getInstance();
        projectTaskService = ProjectTaskService.getInstance();
    }

    @Test
    void writeTaskJsonCorrect() {
        application.userIdCurrent = id;
        assertEquals(RETURN_OK,taskService.writeTaskJson());
    }

    @Test
    void writeTaskJsonException() {
        assertEquals(RETURN_ERROR, taskService.writeTaskJson());
    }

    @Test
    void writeTaskXMLCorrect() {
        application.userIdCurrent = id;
        assertEquals(RETURN_OK, taskService.writeTaskXML());
    }

    @Test
    void writeTaskXMLException() {
        assertEquals(RETURN_ERROR,taskService.writeTaskXML());
    }

    @Test
    void readTaskJsonCorrect() {
        application.userIdCurrent = userIdAdmin;
        assertEquals(RETURN_OK, taskService.readTaskJson());
    }

    @Test
    void readTaskJsonException() {
        assertEquals(RETURN_ERROR, taskService.readTaskJson());
        application.userIdCurrent = id;
        assertEquals(RETURN_ERROR, taskService.readTaskJson());
    }

    @Test
    void readTaskXMLCorrect() {
        application.userIdCurrent = userIdAdmin;
        assertEquals(RETURN_OK, taskService.readTaskXML());
    }

    @Test
    void readTaskXMLException() {
        assertEquals(RETURN_ERROR, taskService.readTaskXML());
        application.userIdCurrent = id;
        assertEquals(RETURN_ERROR, taskService.readTaskXML());
    }

    @Test
    void findAllCorrect() {
        application.userIdCurrent = userIdAdmin;
        taskService.clearTask();
        List<Task> tasks = new ArrayList<>();
        tasks.add(taskService.create(name, description, user_id).get());
        tasks.add(taskService.create("name2", "description2", user_id).get());
        assertEquals(tasks, taskService.findAll());
    }

    @Test
    void createCorrect() {
        Task task = taskService.create(name, description, user_id).get();
        assertEquals(name, task.getName());
        assertEquals(description, task.getDescription());
    }

    @Test
    void createException() {
        assertThrows(IllegalArgumentException.class, () -> taskService.create(null, description, user_id));
        assertThrows(IllegalArgumentException.class, () -> taskService.create("", description, user_id));
        assertThrows(IllegalArgumentException.class, () -> taskService.create(name, null, user_id));
        assertThrows(IllegalArgumentException.class, () -> taskService.create(name, "", user_id));
    }

    @Test
    void createTaskException() {
        assertEquals(RETURN_ERROR, taskService.createTask());
    }

    @Test
    void updateCorrect() throws TaskNotFoundException {
        application.userIdCurrent = user_id;
        Task task = taskService.create(name, description, user_id).get();
        taskService.update(task.getId(), "new_name", "new_description");
        assertEquals("new_name", task.getName());
        assertEquals("new_description", task.getDescription());
    }

    @Test
    void updateException() throws TaskNotFoundException {
        Task task = taskService.create(name, description, user_id).get();
        taskService.update(task.getId(), "new_name", "new_description");
        assertEquals(name, task.getName());
        assertEquals(description, task.getDescription());

        application.userIdCurrent = user_id;
        assertThrows(IllegalArgumentException.class, () -> taskService.update(task.getId(), null, "new_description"));
        assertThrows(IllegalArgumentException.class, () -> taskService.update(task.getId(), "new_name", null));
        assertThrows(TaskNotFoundException.class, () -> taskService.update(3L, "new_name", "new_description"));
    }

    @Test
    void updateTaskByIndexException() throws TaskNotFoundException {
        assertEquals(RETURN_ERROR, taskService.updateTaskByIndex());
    }

    @Test
    void updateTaskByIdException() throws TaskNotFoundException {
        assertEquals(RETURN_ERROR, taskService.updateTaskById());
    }

    @Test
    void clearTaskCorrect() {
        taskService.createBeginTasks();
        assertFalse(taskService.findAll().isEmpty());
        application.userIdCurrent = userIdAdmin;
        assertEquals(RETURN_OK, taskService.clearTask());
    }

    @Test
    void clearTaskException() {
        taskService.createBeginTasks();
        assertFalse(taskService.findAll().isEmpty());
        assertEquals(RETURN_ERROR, taskService.clearTask());
        application.userIdCurrent = id;
        assertEquals(RETURN_ERROR, taskService.clearTask());
    }

    @Test
    void findByNameCorrect() throws TaskNotFoundException {
        application.userIdCurrent = user_id;
        Task task = taskService.create(name, description, user_id).get();
        List<Task> tasksNew = new ArrayList<>();
        tasksNew.add(task);
        assertEquals(tasksNew, taskService.findByName(name));
    }

    @Test
    void findByNameException() {
        assertThrows(NullPointerException.class,() -> taskService.findByName(null));
        assertThrows(IllegalArgumentException.class,() -> taskService.findByName(""));
        assertThrows(TaskNotFoundException.class,() -> taskService.findByName("Неизвестная задача"));
    }

    @Test
    void viewTaskByNameException() throws TaskNotFoundException {
        assertEquals(RETURN_ERROR, taskService.viewTaskByName());
    }

    @Test
    void findByIdException() {
        assertThrows(TaskNotFoundException.class,() -> taskService.findById(null));
        assertThrows(TaskNotFoundException.class,() -> taskService.findById(5L));
    }

    @Test
    void findByIdUserIdCorrect() throws TaskNotFoundException {
        application.userIdCurrent = user_id;
        Task task = taskService.create(name, description, user_id).get();
        assertEquals(task, taskService.findByIdUserId(task.getId()));
    }
    @Test
    void findByIdUserIdException() {
        assertThrows(TaskNotFoundException.class,() -> taskService.findByIdUserId(null));
        assertThrows(TaskNotFoundException.class,() -> taskService.findByIdUserId(5L));
    }

    @Test
    void viewTaskByIdException() throws TaskNotFoundException {
        assertEquals(RETURN_ERROR, taskService.viewTaskById());
    }

    @Test
    void findByIndexCorrect() throws TaskNotFoundException {
        application.userIdCurrent = userIdAdmin;
        taskService.clearTask();
        Task task = taskService.create(name, description, user_id).get();
        assertEquals(task, taskService.findByIndex(0));
    }

    @Test
    void findByIndexException() {
        application.userIdCurrent = userIdAdmin;
        projectService.clearProject();
        assertThrows(TaskNotFoundException.class, () -> taskService.findByIndex(5));
    }

    @Test
    void findByIndexUserIdCorrect() throws TaskNotFoundException {
        application.userIdCurrent = user_id;
        Task task = taskService.create(name, description, user_id).get();
        assertEquals(task, taskService.findById(task.getId()));
    }

    @Test
    void findByIndexUserIdException() {
        assertThrows(TaskNotFoundException.class, () -> taskService.findById(5L));
        assertThrows(TaskNotFoundException.class, () -> taskService.findById(null));
    }

    @Test
    void viewTaskByIndexException() throws TaskNotFoundException {
        assertEquals(RETURN_ERROR, taskService.viewTaskByIndex());
    }

    @Test
    void removeByIdException() {
        assertThrows(TaskNotFoundException.class,() -> taskService.removeById(null));
        assertThrows(TaskNotFoundException.class,() -> taskService.removeById(5L));
    }

    @Test
    void removeByIdUserIdException() {
        assertThrows(TaskNotFoundException.class,() -> taskService.removeByIdUserId(null));
        assertThrows(TaskNotFoundException.class,() -> taskService.removeByIdUserId(5L));
    }

    @Test
    void removeTaskByIdException() throws TaskNotFoundException {
        assertEquals(RETURN_ERROR, taskService.removeTaskById());
    }

    @Test
    void removeByIndexException() {
        assertThrows(TaskNotFoundException.class,() -> taskService.removeByIndex(100));
    }

    @Test
    void removeByIndexUserIdException() {
        assertThrows(TaskNotFoundException.class,() -> taskService.removeByIndexUserId(100));
    }

    @Test
    void removeTaskByIndexException() throws TaskNotFoundException {
        assertEquals(RETURN_ERROR, taskService.removeTaskById());
    }

    @Test
    void findAllByProjectIdCorrect() throws ProjectNotFoundException, TaskNotFoundException, NullPointerException {
        Optional<Project> project = projectService.create(name, description, userIdAdmin);
        List<Task> tasks = new ArrayList<>();
        Task task1 = taskService.create("name1", "description1", userIdAdmin).get();
        Task task2 = taskService.create("name2", "description2", userIdAdmin).get();
        tasks.add(task1);
        tasks.add(task2);
        projectTaskService.addTaskToProject(project.get().getId(), task1.getId(), userIdAdmin);
        projectTaskService.addTaskToProject(project.get().getId(), task2.getId(), userIdAdmin);
        assertEquals(tasks, taskService.findAllByProjectId(project.get().getId()));
    }

    @Test
    void listTaskCorrect() {
        Application.userIdCurrent = userIdAdmin;
        assertEquals(RETURN_OK, taskService.listTask());
    }

    @Test
    void listTaskException() {
        assertEquals(RETURN_ERROR, taskService.listTask());
    }

    @Test
    void addTaskToUserCorrect() throws TaskNotFoundException {
        Task task = taskService.create(name, description, null).get();
        taskService.addTaskToUser(user_id, task.getId());
        assertEquals(user_id, task.getUserid());
    }

    @Test
    void AddTaskToUserException() throws TaskNotFoundException {
        assertEquals(RETURN_ERROR, taskService.addTaskToUser());
    }

    @Test
    void removeTaskFromUser() throws TaskNotFoundException {
        assertEquals(RETURN_ERROR, taskService.removeTaskFromUser());
    }

    @Test
    void createBeginTasksCorrect() {
        taskService.createBeginTasks();
        assertTrue(!taskService.findAll().isEmpty());
    }

    @Test
    void removeTaskDeadlineException() {
        assertThrows(TaskNotFoundException.class,() -> taskService.removeTaskDeadline(id));
    }

}