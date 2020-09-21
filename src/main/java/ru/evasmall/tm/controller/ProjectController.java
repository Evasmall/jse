package ru.evasmall.tm.controller;

import ru.evasmall.tm.entity.Project;
import ru.evasmall.tm.entity.User;
import ru.evasmall.tm.exeption.ObjectNotFoundException;
import ru.evasmall.tm.service.ProjectService;
import ru.evasmall.tm.Application;
import ru.evasmall.tm.service.UserService;
import ru.evasmall.tm.util.Control;

import java.util.List;

public class ProjectController extends AbstractController{

    private final ProjectService projectService;

    private final UserService userService;

    private final SystemController systemController = new SystemController();

    private final Control control = new Control();

    public ProjectController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }

    //Создание проекта
    public int createProject() throws ObjectNotFoundException {
        System.out.println("CREATE PROJECT");
        System.out.println("PLEASE ENTER PROJECT NAME:");
        final String name = scanner.nextLine();
        if (name.isEmpty())
        {
            System.out.println("PROJECT NAME EMPTY. FAIL.");
            return -1;
        }
        System.out.println("PLEASE ENTER PROJECT DESCRIPTION:");
        final String description = scanner.nextLine();
        if (description.isEmpty())
        {
            System.out.println("TASK DESCRIPTION EMPTY. FAIL.");
            return -1;
        }
        Project projectNew = projectService.create(name, description, Application.userIdCurrent);
        if (projectNew == null)
        {
            throw new ObjectNotFoundException("PROJECT NOT CREATE. FAIL.");
        }
        System.out.println("OK");
        return 0;
    }

    //Изменение проекта по индексу с учетом принадлежности проекта
    public int updateProjectByIndex() throws ObjectNotFoundException {
        System.out.println("UPDATE PROJECT");
        System.out.println("ENTER PROJECT INDEX:");
        final Integer index = control.scannerIndexIsInteger();
        if (index != null) {
            final Project project = projectService.findByIndexUserId(index);
            if (project == null) {
                systemController.displayForeign("PROJECT");
                return -1;
            }
            System.out.println("PLEASE ENTER PROJECT NAME:");
            final String name = scanner.nextLine();
            if (name.isEmpty())
            {
                System.out.println("PROJECT NAME EMPTY. FAIL.");
                return -1;
            }
            System.out.println("PLEASE ENTER PROJECT DESCRIPTION:");
            final String description = scanner.nextLine();
            if (description.isEmpty())
            {
                System.out.println("TASK DESCRIPTION EMPTY. FAIL.");
                return -1;
            }
            Project projectNew = projectService.update(project.getId(), name, description);
            if (projectNew == null)
            {
                throw new ObjectNotFoundException("PROJECT NOT UPDATE. FAIL.");
            }
            System.out.println("OK");
            return 0;
        }
        else return -1;
    }

    //Изменение проекта по идентификатору с учетом принадлежности проекта
    public int updateProjectById() throws ObjectNotFoundException {
        System.out.println("UPDATE PROJECT");
        System.out.println("ENTER PROJECT ID:");
        final Long id = control.scannerIdIsLong();
        if (id != null) {
            final Project project = projectService.findByIdUserId(id);
            if (project == null) {
                systemController.displayForeign("PROJECT");
                return -1;
            }
            System.out.println("PLEASE ENTER PROJECT NAME:");
            final String name = scanner.nextLine();
            if (name.isEmpty())
            {
                throw new ObjectNotFoundException("PROJECT NAME EMPTY. FAIL.");
            }
            System.out.println("PLEASE ENTER PROJECT DESCRIPTION:");
            final String description = scanner.nextLine();
            if (description.isEmpty())
            {
                throw new ObjectNotFoundException("PROJECT DESCRIPTION EMPTY. FAIL.");
            }
            Project projectNew = projectService.update(project.getId(), name, description);
            if (projectNew == null)
            {
                throw new ObjectNotFoundException("PROJECT NOT UPDATE. FAIL.");
            }
            System.out.println("OK");
            return 0;
        }
        else return -1;
    }

    //Удаление проекта по индексу с учетом принадлежности проекта
    public int removeProjectByIndex() throws ObjectNotFoundException {
        System.out.println("REMOVE PROJECT BY INDEX");
        System.out.println("PLEASE ENTER PROJECT INDEX:");
        final Integer index = control.scannerIndexIsInteger();
        if (index != null) {
            final Project project = projectService.removeByIndexUserId(index);
            if (project == null) {
                systemController.displayForeign("PROJECT");
                throw new ObjectNotFoundException("PROJECT NOT FOUND BY INDEX.");
            }
            else System.out.println("OK");
            return 0;
        }
        else return -1;
    }

    //Удаление проекта по идентификатору с учетом принадлежности проекта
    public int removeProjectById() throws ObjectNotFoundException {
        System.out.println("REMOVE PROJECT BY ID");
        System.out.println("PLEASE ENTER PROJECT ID:");
        final Long id = control.scannerIdIsLong();
        if (id != null) {
            final Project project = projectService.removeByIdUserId(id);
            if (project == null) {
                systemController.displayForeign("PROJECT");
                throw new ObjectNotFoundException("PROJECT NOT FOUND BY ID.");
            }
            else System.out.println("OK");
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
        if (userService.findByUserId(Application.userIdCurrent).isAdmin_true() == true) {
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
        if (project == null) return;
        //Проверка на авторизацию польователя
        if (Application.userIdCurrent == null) {
            System.out.println("PROJECTS NOT ACCESS FOR UNAUTHORIZED USER!");
            return;
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
    public int viewProjectByName() throws ObjectNotFoundException {
        System.out.print("ENTER PROJECT NAME:");
        String name = scanner.nextLine();
        final List <Project> projects = projectService.findByName(name);
        if (projects == null) {
            systemController.displayForeign("PROJECT");
            throw new ObjectNotFoundException("PROJECT NOT FOUND BY NAME.");
        }
        for (Project project: projects) {
            viewProject(project);
        }
        return 0;
    }

    //Просмотр списка проектов по индексу
    public int viewProjectByIndex() throws ObjectNotFoundException {
        System.out.println("ENTER PROJECT INDEX:");
        final Integer index = control.scannerIndexIsInteger();
        if (index != null) {
            final Project project = projectService.findByIndex(index);
            if (project == null) {
                systemController.displayForeign("PROJECT");
                throw new ObjectNotFoundException("PROJECT NOT FOUND BY INDEX.");
            }
            viewProject(project);
            return 0;
        }
        return -1;
    }

    //Просмотр проекта по идентификатору
    public int viewProjectById() throws ObjectNotFoundException {
        System.out.println("ENTER PROJECT ID:");
        final Long id = control.scannerIdIsLong();
        if (id != null) {
            final Project project = projectService.findById(id);
            if (project == null) {
                System.out.println("PROJECT NOT FOUND.");
                throw new ObjectNotFoundException("PROJECT NOT FOUND BY ID.");
            }
            viewProject(project);
            return 0;
        }
        return -1;
    }

    //Cписок проектов.
    public int listProject() {
        //Проверка на авторизацию польователя
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
    public void viewProjects (final List<Project> projects) {
        if (projects == null || projects.isEmpty()) return;
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
    public int addProjectToUser() throws ObjectNotFoundException {
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
                System.out.println("PLEASE ENTER PROJECT ID:");
                final Long projectId = control.scannerIdIsLong();
                if (projectId != null) {
                    final Project project = projectService.findByIdUserId(projectId);
                    if (project == null) {
                        systemController.displayForeign("PROJECT");
                        throw new ObjectNotFoundException("PROJECT NOT FOUND BY ID.");
                    }
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
    public int removeProjectFromUser() throws ObjectNotFoundException {
        System.out.println("REMOVE PROJECT FROM USER");
        System.out.println("PLEASE ENTER PROJECT ID:");
        final Long projectId = control.scannerIdIsLong();
        if (projectId != null) {
            final Project project = projectService.findByIdUserId(projectId);
            if (project == null) {
                systemController.displayForeign("PROJECT");
                throw new ObjectNotFoundException("PROJECT NOT FOUND BY ID.");
            }
            if (project.getUserid() == null) {
                System.out.println("PROJECT NOT HAVE USER.");
                return -1;
            }
            if (project.getUserid().equals(Application.userIdCurrent) || userService.findByUserId(Application.userIdCurrent).isAdmin_true()) {
                project.setUserid(null);
                System.out.println("ОК");
                return 0;
            }
        }
        return -1;
    }

}