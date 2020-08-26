package ru.evasmall.tm.controller;

import ru.evasmall.tm.entity.Project;
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
    public int createProject() {
        System.out.println("CREATE PROJECT");
        System.out.println("PLEASE ENTER PROJECT NAME:");
        final String name = scanner.nextLine();
        System.out.println("PLEASE ENTER PROJECT DESCRIPTION:");
        final String description = scanner.nextLine();
        projectService.create(name, description, Application.userIdCurrent);
        System.out.println("OK");
        return 0;
    }

    //Изменение проекта по индексу с учетом принадлежности проекта
    public int updateProjectByIndex() {
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
            System.out.println("PLEASE ENTER PROJECT DESCRIPTION:");
            final String description = scanner.nextLine();
            projectService.update(project.getId(), name, description);
            System.out.println("OK");
            return 0;
        }
        else return -1;
    }

    //Изменение проекта по идентификатору с учетом принадлежности проекта
    public int updateProjectById() {
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
            System.out.println("PLEASE ENTER PROJECT DESCRIPTION:");
            final String description = scanner.nextLine();
            projectService.update(project.getId(), name, description);
            System.out.println("OK");
            return 0;
        }
        else return -1;
    }

    //Удаление проекта по индексу с учетом принадлежности проекта
    public int removeProjectByIndex() {
        System.out.println("REMOVE PROJECT BY INDEX");
        System.out.println("PLEASE ENTER PROJECT INDEX:");
        final Integer index = control.scannerIndexIsInteger();
        if (index != null) {
            final Project project = projectService.removeByIndexUserId(index);
            if (project == null) systemController.displayForeign("PROJECT");
            else System.out.println("OK");
            return 0;
        }
        else return -1;
    }

    //Удаление проекта по идентификатору с учетом принадлежности проекта
    public int removeProjectById() {
        System.out.println("REMOVE PROJECT BY ID");
        System.out.println("PLEASE ENTER PROJECT ID:");
        final Long id = control.scannerIdIsLong();
        if (id != null) {
            final Project project = projectService.removeByIdUserId(id);
            if (project == null) systemController.displayForeign("PROJECT");
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
        System.out.println("VIEW PROJECT");
        System.out.println("ID: " + project.getId());
        System.out.println("NAME: " + project.getName());
        System.out.println("DESCRIPTION: " + project.getDescription());
        System.out.println("USER ID: " + project.getUserid());
        System.out.println("USER LOGIN: " + userService.findByUserId(project.getUserid()).getLogin());
        System.out.println("______");
    }

    //Просмотр списка проектов по наименованию
    public int viewProjectByName() {
        System.out.print("ENTER PROJECT NAME:");
        String name = scanner.nextLine();
        final List <Project> projects = projectService.findByName(name);
        if (projects == null) {
            systemController.displayForeign("PROJECT");
            return -1;
        }
        for (Project project: projects) {
            viewProject(project);
        }
        return 0;
    }

    //Просмотр списка проектов по индексу
    public int viewProjectByIndex() {
        System.out.println("ENTER PROJECT INDEX:");
        final Integer index = control.scannerIndexIsInteger();
        if (index != null) {
            final Project project = projectService.findByIndex(index);
            if (project == null) {
                systemController.displayForeign("PROJECT");
                return -1;
            }
            viewProject(project);
            return 0;
        }
        return -1;
    }

    //Просмотр проекта по идентификатору
    public int viewProjectById() {
        System.out.println("ENTER PROJECT ID:");
        final Long id = control.scannerIdIsLong();
        if (id != null) {
            final Project project = projectService.findById(id);
            if (project == null) {
                System.out.println("PROJECT NOT FOUND.");
                return -1;
            }
            viewProject(project);
            return 0;
        }
        return -1;
    }

    //Cписок проектов.
    public int listProject() {
        System.out.println("LIST PROJECTS");
        int index = 1;
        viewProjects(projectService.findAll());
        System.out.println("OK");
        return 0;
    }

    //Просмотр списка проектов.
    public void viewProjects (final List<Project> projects) {
        if (projects == null || projects.isEmpty()) return;
        int index = 1;
        projectService.ProjectSortByName(projects);
        for (final Project project: projects) {
            System.out.println(index + ". PROJECTID: " + project.getId() + "; NAME: " + project.getName() +
                    "; DESCRIPTION: " + project.getDescription() + "; USER ID: " + project.getUserid() +
                    "; USER LOGIN: " + userService.findByUserId(project.getUserid()).getLogin());
            index++;
        }
    }

}