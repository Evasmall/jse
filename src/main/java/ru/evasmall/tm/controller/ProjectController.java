package ru.evasmall.tm.controller;

import ru.evasmall.tm.entity.Project;
import ru.evasmall.tm.entity.User;
import ru.evasmall.tm.exeption.IncorrectFormatException;
import ru.evasmall.tm.exeption.ProjectNotFoundException;
import ru.evasmall.tm.service.ProjectService;
import ru.evasmall.tm.Application;
import ru.evasmall.tm.service.UserService;
import ru.evasmall.tm.util.Control;

import java.util.List;

import static ru.evasmall.tm.constant.TerminalMassage.*;

public class ProjectController extends AbstractController{

    private final ProjectService projectService;

    private final UserService userService;

    private final SystemController systemController = new SystemController();

    private final Control control = new Control();

 //   private static final Logger logger = LogManager.getLogger(ProjectController.class);

    public ProjectController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }

    //Создание проекта
    public int createProject() throws IncorrectFormatException{
        System.out.println("CREATE PROJECT");
        System.out.println(PROJECT_NAME_ENTER);
        final String name = scanner.nextLine();
        System.out.println(PROJECT_DESCRIPTION_ENTER);
        final String description = scanner.nextLine();
        projectService.create(name, description, Application.userIdCurrent);
        System.out.println("OK");
        return 0;
    }

    //Изменение проекта по индексу с учетом принадлежности проекта
    public int updateProjectByIndex() throws ProjectNotFoundException, IncorrectFormatException {
        System.out.println("UPDATE PROJECT");
        System.out.println(PROJECT_INDEX_ENTER);
        final Integer index = control.scannerIndexIsInteger();
        if (index != null) {
            final Project project = projectService.findByIndexUserId(index);
            System.out.println(PROJECT_NAME_ENTER);
            final String name = scanner.nextLine();
            System.out.println(PROJECT_DESCRIPTION_ENTER);
            final String description = scanner.nextLine();
            projectService.update(project.getId(), name, description);
            System.out.println("OK");
            return 0;
        }
        else return -1;
    }

    //Изменение проекта по идентификатору с учетом принадлежности проекта
    public int updateProjectById() throws ProjectNotFoundException, IncorrectFormatException {
        System.out.println("UPDATE PROJECT");
        System.out.println(PROJECT_ID_ENTER);
        final Long id = control.scannerIdIsLong();
        if (id != null) {
            final Project project = projectService.findByIdUserId(id);
            System.out.println(PROJECT_NAME_ENTER);
            final String name = scanner.nextLine();
            System.out.println(PROJECT_DESCRIPTION_ENTER);
            final String description = scanner.nextLine();
            projectService.update(project.getId(), name, description);
            System.out.println("OK");
            return 0;
        }
        else return -1;
    }

    //Удаление проекта по индексу с учетом принадлежности проекта
    public int removeProjectByIndex() throws ProjectNotFoundException, IncorrectFormatException {
        System.out.println("REMOVE PROJECT BY INDEX");
        System.out.println(PROJECT_INDEX_ENTER);
        final Integer index = control.scannerIndexIsInteger();
        if (index != null) {
            projectService.removeByIndexUserId(index);
            System.out.println("OK");
            return 0;
        }
        else return -1;
    }

    //Удаление проекта по идентификатору с учетом принадлежности проекта
    public int removeProjectById() throws ProjectNotFoundException, IncorrectFormatException {
        System.out.println("REMOVE PROJECT BY ID");
        System.out.println(PROJECT_ID_ENTER);
        final Long id = control.scannerIdIsLong();
        if (id != null) {
            projectService.removeByIdUserId(id);
            System.out.println("OK");
            return 0;
        }
        else return -1;
    }

    //Удаление всех проектов (доступно только администраторам).
    public int clearProject() {
        if (userService.findByUserId(Application.userIdCurrent) == null) {
            systemController.displayForAdminOnly();
            return -1;
        }
        if (userService.findByUserId(Application.userIdCurrent).isAdminTrue()) {
            System.out.println("CLEAR PROJECT");
            projectService.clear();
            System.out.println("OK");
            return 0;
        }
        else {
            systemController.displayForAdminOnly();
            return -1;
        }
    }

    //Просмотр проекта
    public void viewProject(final Project project) {
        //Проверка на авторизацию польователя
        if (Application.userIdCurrent == null) {
            System.out.println("PROJECTS NOT ACCESS FOR UNAUTHORIZED USER!");
        }
        else {
            System.out.println("VIEW PROJECT");
            System.out.println("ID: " + project.getId());
            System.out.println("NAME: " + project.getName());
            System.out.println("DESCRIPTION: " + project.getDescription());
            System.out.println("USER ID: " + project.getUserid());
            System.out.println("USER LOGIN: " + userService.findByUserId(project.getUserid()).getLogin());
            System.out.println("______");
        }
    }

    //Просмотр списка проектов по наименованию
    public int viewProjectByName() throws ProjectNotFoundException, IncorrectFormatException {
        System.out.println(PROJECT_NAME_ENTER);
        String name = scanner.nextLine();
        final List <Project> projects = projectService.findByName(name);
        for (Project project: projects) {
            viewProject(project);
        }
        return 0;
    }

    //Просмотр списка проектов по индексу
    public int viewProjectByIndex() throws ProjectNotFoundException, IncorrectFormatException {
        System.out.println(PROJECT_INDEX_ENTER);
        final Integer index = control.scannerIndexIsInteger();
        if (index != null) {
            final Project project = projectService.findByIndex(index);
            viewProject(project);
            return 0;
        }
        return -1;
    }

    //Просмотр проекта по идентификатору
    public int viewProjectById() throws ProjectNotFoundException, IncorrectFormatException {
        System.out.println(PROJECT_ID_ENTER);
        final Long id = control.scannerIdIsLong();
        if (id != null) {
            final Project project = projectService.findById(id);
            viewProject(project);
            return 0;
        }
        return -1;
    }

    //Cписок проектов.
    public int listProject() throws ProjectNotFoundException {
        //Проверка на авторизацию пользователя
        if (Application.userIdCurrent == null) {
            System.out.println("LIST PROJECTS NOT ACCESS FOR UNAUTHORIZED USER!");
            return -1;
        }
        else {
            System.out.println("LIST PROJECTS");
            viewProjects(projectService.findAll());
            System.out.println("OK");
            return 0;
        }
    }

    //Просмотр списка проектов.
    public void viewProjects (final List<Project> projects) throws ProjectNotFoundException {
        int index = 1;
        projectService.ProjectSortByName(projects);
        for (final Project project: projects) {
            final String login1;
            if (userService.findByUserId(project.getUserid()) == null) {
                login1 = null;
            }
            else {
                login1 = userService.findByUserId(project.getUserid()).getLogin();
            }
            System.out.println(index + ". PROJECTID: " + project.getId() + "; NAME: " + project.getName() +
                    "; DESCRIPTION: " + project.getDescription() + "; USER ID: " + project.getUserid() +
                    "; USER LOGIN: " + login1);
            index++;
        }
    }

    //Добавление принадлежности проекта пользователю по идентификатору проекта и логину пользователя.
    public int addProjectToUser() throws ProjectNotFoundException, IncorrectFormatException {
        System.out.println("ADD PROJECT TO USER");
        System.out.println("PLEASE ENTER LOGIN:");
        final User user1 = userService.findByLogin(scanner.nextLine());
        if (user1 == null) {
            System.out.println("LOGIN NOT EXIST!");
            return -1;
        }
        else {
            final Long userId = user1.getUserid();
            if (userId != null) {
                System.out.println(PROJECT_ID_ENTER);
                final Long projectId = control.scannerIdIsLong();
                if (projectId != null) {
                    projectService.findByIdUserId(projectId);
                    projectService.addProjectToUser(userId, projectId);
                    System.out.println("OK");
                    return 0;
                }
                return -1;
            }
        }
        return -1;
    }

    //Удаление принадлежности проекта пользователю по идентификатору задачи.
    public int removeProjectFromUser() throws ProjectNotFoundException, IncorrectFormatException {
        System.out.println("REMOVE PROJECT FROM USER");
        System.out.println(PROJECT_ID_ENTER);
        final Long projectId = control.scannerIdIsLong();
        if (projectId != null) {
            final Project project = projectService.findByIdUserId(projectId);
            if (project.getUserid() == null) {
                System.out.println("PROJECT NOT HAVE USER.");
                return -1;
            }
            if (project.getUserid().equals(Application.userIdCurrent) || userService.findByUserId(Application.userIdCurrent).isAdminTrue()) {
                project.setUserid(null);
                System.out.println("ОК");
                return 0;
            }
        }
        return -1;
    }

}