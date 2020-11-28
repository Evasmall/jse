package ru.evasmall.tm.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.evasmall.tm.Application;
import ru.evasmall.tm.entity.Project;
import ru.evasmall.tm.exeption.ProjectNotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.evasmall.tm.constant.TerminalConst.RETURN_ERROR;
import static ru.evasmall.tm.constant.TerminalConst.RETURN_OK;

class ProjectServiceTest {

    private ProjectService projectService;

    private Long user_id = 10000000L;

    private Long id = 2L;

    private String name = "PROJECT_NAME";

    private String description = "PROJECT_DESCRIPTION";

    private Long userIdAdmin = 1L;

    private final Application application = new Application();

    @BeforeEach
    void setUp() {
        projectService = ProjectService.getInstance();
    }

    @Test
    void writeProjectJsonCorrect() {
        application.userIdCurrent = 2L;
        assertEquals(RETURN_OK, projectService.writeProjectJson());
    }

    @Test
    void writeProjectJsonException() {
        assertEquals(RETURN_ERROR, projectService.writeProjectJson());
    }

    @Test
    void writeProjectXMLCorrect() {
        application.userIdCurrent = id;
        assertEquals(RETURN_OK, projectService.writeProjectXML());
    }

    @Test
    void writeProjectXMLException() {
        assertEquals(RETURN_ERROR, projectService.writeProjectXML());
    }

    @Test
    void readProjectJsonCorrect() {
        application.userIdCurrent = userIdAdmin;
        assertEquals(RETURN_OK, projectService.readProjectJson());
    }

    @Test
    void readProjectJsonException() {
        assertEquals(RETURN_ERROR, projectService.readProjectJson());
        application.userIdCurrent = id;
        assertEquals(RETURN_ERROR, projectService.readProjectJson());
    }

    @Test
    void readProjectXMLCorrect() {
        application.userIdCurrent = userIdAdmin;
        assertEquals(RETURN_OK, projectService.readProjectXML());
    }

    @Test
    void readProjectXMLException() {
        assertEquals(RETURN_ERROR, projectService.readProjectXML());
        application.userIdCurrent = id;
        assertEquals(RETURN_ERROR, projectService.readProjectXML());
    }

    @Test
    void findAllCorrect() {
        application.userIdCurrent = userIdAdmin;
        projectService.clearProject();
        List<Project> projects = new ArrayList<>();
        projects.add(projectService.create(name, description, user_id));
        projects.add(projectService.create("name2", "description2", user_id));
        assertEquals(projects, projectService.findAll());
    }

    @Test
    void createCorrect() {
        Project project = projectService.create(name, description, user_id);
        assertEquals(name, project.getName());
        assertEquals(description, project.getDescription());
    }

    @Test
    void createException() {
        assertThrows(IllegalArgumentException.class, () -> projectService.create(null, description, user_id));
        assertThrows(IllegalArgumentException.class, () -> projectService.create("", description, user_id));
        assertThrows(IllegalArgumentException.class, () -> projectService.create(name, null, user_id));
        assertThrows(IllegalArgumentException.class, () -> projectService.create(name, "", user_id));
    }

    @Test
    void createProjectException() {
        assertEquals(RETURN_ERROR, projectService.createProject());
    }

    @Test
    void updateCorrect() throws ProjectNotFoundException {
        application.userIdCurrent = user_id;
        Project project = projectService.create(name, description, user_id);
        projectService.update(project.getId(), "new_name", "new_description");
        assertEquals("new_name", project.getName());
        assertEquals("new_description", project.getDescription());
    }

    @Test
    void updateException() throws ProjectNotFoundException {
        Project project = projectService.create(name, description, user_id);
        projectService.update(project.getId(), "new_name", "new_description");
        assertEquals(name, project.getName());
        assertEquals(description, project.getDescription());

        application.userIdCurrent = user_id;
        assertThrows(IllegalArgumentException.class, () -> projectService.update(project.getId(), null, "new_description"));
        assertThrows(IllegalArgumentException.class, () -> projectService.update(project.getId(), "new_name", null));
        assertThrows(ProjectNotFoundException.class, () -> projectService.update(3L, "new_name", "new_description"));
    }

    @Test
    void updateProjectByIndexException() throws ProjectNotFoundException {
        assertEquals(RETURN_ERROR, projectService.updateProjectByIndex());
    }

    @Test
    void updateProjectByIdException() throws ProjectNotFoundException {
        assertEquals(RETURN_ERROR, projectService.updateProjectById());
    }

    @Test
    void findByIndexCorrect() throws ProjectNotFoundException {
        application.userIdCurrent = userIdAdmin;
        projectService.clearProject();
        Project project = projectService.create(name, description, user_id);
        assertEquals(project, projectService.findByIndex(0));
    }

    @Test
    void findByIndexException() {
        application.userIdCurrent = userIdAdmin;
        ProjectService.getInstance().clearProject();
        assertThrows(ProjectNotFoundException.class, () -> projectService.findByIndex(5));
    }

    @Test
    void findByIndexUserIdCorrect() throws ProjectNotFoundException {
        application.userIdCurrent = user_id;
        Project project = projectService.create(name, description, user_id);
        assertEquals(project, projectService.findById(project.getId()));
    }

    @Test
    void findByIndexUserIdException() {
        assertThrows(ProjectNotFoundException.class, () -> projectService.findById(5L));
        assertThrows(ProjectNotFoundException.class, () -> projectService.findById(null));
    }

    @Test
    void findByNameCorrect() throws ProjectNotFoundException {
        application.userIdCurrent = user_id;
        Project project = projectService.create(name, description, user_id);
        List<Project> projectsNew = new ArrayList<>();
        projectsNew.add(project);
        assertEquals(projectsNew, projectService.findByName(name));
    }

    @Test
    void findByNameException() {
        assertThrows(NullPointerException.class,() -> projectService.findByName(null));
        assertThrows(IllegalArgumentException.class,() -> projectService.findByName(""));
        assertThrows(ProjectNotFoundException.class,() -> projectService.findByName("Неизвестный проект"));
    }

    @Test
    void viewProjectByNameException() throws ProjectNotFoundException {
        assertEquals(RETURN_ERROR, projectService.viewProjectByName());
    }

    @Test
    void findByNameUserIdException() throws ProjectNotFoundException {
        assertThrows(NullPointerException.class,() -> projectService.findByNameUserId(null));
        assertThrows(IllegalArgumentException.class,() -> projectService.findByNameUserId(""));
        assertThrows(ProjectNotFoundException.class,() -> projectService.findByNameUserId("Неизвестный проект"));
    }

    @Test
    void findByIdException() {
        assertThrows(ProjectNotFoundException.class,() -> projectService.findById(null));
        assertThrows(ProjectNotFoundException.class,() -> projectService.findById(5L));
    }

    @Test
    void viewProjectByIdException() throws ProjectNotFoundException {
        assertEquals(RETURN_ERROR, projectService.viewProjectById());
    }

    @Test
    void clearProjectCorrect() {
        projectService.createBeginProjects();
        assertFalse(projectService.findAll().isEmpty());
        application.userIdCurrent = userIdAdmin;
        assertEquals(RETURN_OK, projectService.clearProject());
    }

    @Test
    void clearProjectException() {
        projectService.createBeginProjects();
        assertFalse(projectService.findAll().isEmpty());
        assertEquals(RETURN_ERROR, projectService.clearProject());
        application.userIdCurrent = id;
        assertEquals(RETURN_ERROR, projectService.clearProject());
    }

    @Test
    void listProjectCorrect() {
        Application.userIdCurrent = userIdAdmin;
        assertEquals(RETURN_OK, projectService.listProject());
    }

    @Test
    void listProjectException() {
        assertEquals(RETURN_ERROR, projectService.listProject());
    }

    @Test
    void findByIdUserIdCorrect() throws ProjectNotFoundException {
        application.userIdCurrent = user_id;
        Project project = projectService.create(name, description, user_id);
        assertEquals(project, projectService.findByIdUserId(project.getId()));
    }
    @Test
    void findByIdUserIdException() {
        assertThrows(ProjectNotFoundException.class,() -> projectService.findByIdUserId(null));
        assertThrows(ProjectNotFoundException.class,() -> projectService.findByIdUserId(5L));
    }

    @Test
    void removeByIdException() {
        assertThrows(ProjectNotFoundException.class,() -> projectService.removeById(null));
        assertThrows(ProjectNotFoundException.class,() -> projectService.removeById(5L));
    }

    @Test
    void removeByIdUserIdException() {
        assertThrows(ProjectNotFoundException.class,() -> projectService.removeByIdUserId(null));
        assertThrows(ProjectNotFoundException.class,() -> projectService.removeByIdUserId(5L));
    }

    @Test
    void removeProjectByIdException() throws ProjectNotFoundException {
        assertEquals(RETURN_ERROR, projectService.removeProjectById());
    }

    @Test
    void removeByIndexException() {
        assertThrows(ProjectNotFoundException.class,() -> projectService.removeByIndex(100));
    }

    @Test
    void removeByIndexUserIdException() {
        assertThrows(ProjectNotFoundException.class,() -> projectService.removeByIndexUserId(100));
    }

    @Test
    void removeProjectByIndexException() throws ProjectNotFoundException {
        assertEquals(RETURN_ERROR, projectService.removeProjectById());
    }

    @Test
    void addProjectToUserException() throws ProjectNotFoundException {
        assertEquals(RETURN_ERROR, projectService.addProjectToUser());
        application.userIdCurrent = user_id;
        Project project = projectService.create(name, description, user_id);
        projectService.addProjectToUser(3L, project.getId());
        assertEquals(3L, project.getUserid());
    }

    @Test
    void removeProjectFromUser() throws ProjectNotFoundException {
        assertEquals(RETURN_ERROR, projectService.removeProjectFromUser());
    }

    @Test
    void createBeginProjectsCorrect() {
        projectService.createBeginProjects();
        assertTrue(!projectService.findAll().isEmpty());
    }

}