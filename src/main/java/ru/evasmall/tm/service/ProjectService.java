package ru.evasmall.tm.service;

import ru.evasmall.tm.Application;
import ru.evasmall.tm.entity.Project;
import ru.evasmall.tm.entity.User;
import ru.evasmall.tm.exeption.ProjectNotFoundException;
import ru.evasmall.tm.repository.ProjectRepository;
import ru.evasmall.tm.util.Control;

import java.util.Collections;
import java.util.List;

import static ru.evasmall.tm.constant.TerminalMassage.*;

public class ProjectService extends AbstractService {

    private final ProjectRepository projectRepository;

    private final UserService userService;

    private final Control control = new Control();

    private final SystemService systemService = new SystemService();

    private static ProjectService instance = null;

    public ProjectService() {
        this.projectRepository = ProjectRepository.getInstance();
        this.userService = UserService.getInstance();
    }

    public static ProjectService getInstance() {
        if (instance == null) {
            instance = new ProjectService();
        }
        return instance;
    }

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public Project create(String name, String description, Long userid) {
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("PROJECT NAME IS EMPTY. PROJECT NOT CREATED. FAIL.");
        if (description == null || description.isEmpty())
            throw new IllegalArgumentException("PROJECT DESCRIPTION IS EMPTY. PROJECT NOT CREATED. FAIL.");
        return projectRepository.create(name, description, userid);
    }

    //Создание проекта
    public int createProject() {
        System.out.println("CREATE PROJECT");
        System.out.println(PROJECT_NAME_ENTER);
        final String name = scanner.nextLine();
        System.out.println(PROJECT_DESCRIPTION_ENTER);
        final String description = scanner.nextLine();
        create(name, description, Application.userIdCurrent);
        System.out.println("OK");
        return 0;
    }

    public Project update(Long id, String name, String description) throws ProjectNotFoundException {
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("PROJECT NAME IS EMPTY. PROJECT NOT UPDATED. FAIL.");
        if (description == null || description.isEmpty())
            throw new IllegalArgumentException("PROJECT DESCRIPTION IS EMPTY. PROJECT NOT UPDATED. FAIL.");
        return projectRepository.update(id, name, description);
    }

    //Изменение проекта по индексу с учетом принадлежности проекта
    public int updateProjectByIndex() throws ProjectNotFoundException {
        System.out.println("UPDATE PROJECT");
        System.out.println(PROJECT_INDEX_ENTER);
        final Integer index = control.scannerIndexIsInteger();
        if (index != null) {
            final Project project = findByIndexUserId(index);
            System.out.println(PROJECT_NAME_ENTER);
            final String name = scanner.nextLine();
            System.out.println(PROJECT_DESCRIPTION_ENTER);
            final String description = scanner.nextLine();
            update(project.getId(), name, description);
            System.out.println("OK");
            return 0;
        }
        else return -1;
    }

    //Изменение проекта по идентификатору с учетом принадлежности проекта
    public int updateProjectById() throws ProjectNotFoundException {
        System.out.println("UPDATE PROJECT");
        System.out.println(PROJECT_ID_ENTER);
        final Long id = control.scannerIdIsLong();
        if (id != null) {
            final Project project = findByIdUserId(id);
            System.out.println(PROJECT_NAME_ENTER);
            final String name = scanner.nextLine();
            System.out.println(PROJECT_DESCRIPTION_ENTER);
            final String description = scanner.nextLine();
            update(project.getId(), name, description);
            System.out.println("OK");
            return 0;
        }
        else return -1;
    }

    //Поиск проекта по индексу
    public Project findByIndex(int index) throws ProjectNotFoundException {
        return projectRepository.findByIndex(index);
    }

    //Просмотр списка проектов по индексу
    public int viewProjectByIndex() throws ProjectNotFoundException {
        System.out.println(PROJECT_INDEX_ENTER);
        final Integer index = control.scannerIndexIsInteger();
        if (index != null) {
            final Project project = findByIndex(index);
            viewProject(project);
            return 0;
        }
        return -1;
    }

    //Поиск проекта по индексу с учетом принадлежности пользователю текущей сессии
    public Project findByIndexUserId(int index) throws ProjectNotFoundException {
        if (projectRepository.findByIndex(index).getUserid().equals(Application.userIdCurrent))
            return projectRepository.findByIndex(index);
        throw new ProjectNotFoundException(PROJECT_FOREIGN);
    }

    //Поиск проекта по наименованию
    public List<Project> findByName(String name) throws ProjectNotFoundException {
        return projectRepository.findByName(name);
    }

    //Просмотр списка проектов по наименованию
    public int viewProjectByName() throws ProjectNotFoundException {
        System.out.println(PROJECT_NAME_ENTER);
        String name = scanner.nextLine();
        final List <Project> projects = findByName(name);
        for (Project project: projects) {
            viewProject(project);
        }
        return 0;
    }

    //Поиск проекта по наименованию с учетом принадлежности пользователю текущей сессии
    public List<Project> findByNameUserId(String name) throws ProjectNotFoundException {
        for (Project project: projectRepository.findByName(name)) {
            return projectRepository.findByName(name);
        }
        throw new ProjectNotFoundException(PROJECT_FOREIGN);
    }

    //Поиск проекта по идентификатору
    public Project findById(Long id) throws ProjectNotFoundException {
        return projectRepository.findById(id);
    }

    //Просмотр проекта по идентификатору
    public int viewProjectById() throws ProjectNotFoundException {
        System.out.println(PROJECT_ID_ENTER);
        final Long id = control.scannerIdIsLong();
        if (id != null) {
            final Project project = findById(id);
            viewProject(project);
            return 0;
        }
        return -1;
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

    public void clear() {
        projectRepository.clear();
    }

    //Удаление всех проектов (доступно только администраторам).
    public int clearProject() {
        if (userService.findByUserId(Application.userIdCurrent) == null) {
            systemService.displayForAdminOnly();
            return -1;
        }
        if (userService.findByUserId(Application.userIdCurrent).isAdminTrue()) {
            System.out.println("CLEAR PROJECT");
            clear();
            System.out.println("OK");
            return 0;
        }
        else {
            systemService.displayForAdminOnly();
            return -1;
        }
    }

    //Cписок проектов.
    public int listProject() {
        //Проверка на авторизацию пользователя
        if (Application.userIdCurrent == null) {
            System.out.println("LIST PROJECTS NOT ACCESS FOR UNAUTHORIZED USER!");
            return -1;
        }
        else {
            System.out.println("LIST PROJECTS");
            viewProjects(findAll());
            System.out.println("OK");
            return 0;
        }
    }

    //Просмотр списка проектов.
    public void viewProjects (final List<Project> projects) {
        int index = 1;
        projectSortByName(projects);
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

    //Поиск проекта по идентификатору с учетом принадлежности пользователю текущей сессии
    public Project findByIdUserId(Long id) throws ProjectNotFoundException {
        if (projectRepository.findById(id).getUserid() == null)
            return projectRepository.findById(id);
        if (projectRepository.findById(id).getUserid().equals(Application.userIdCurrent))
            return projectRepository.findById(id);
        throw new ProjectNotFoundException(PROJECT_FOREIGN);
    }

    //Удаление проекта по идентификатору
    public Project removeById(Long id) throws ProjectNotFoundException {
        return projectRepository.removeById(id);
    }

    //Удаление проекта по идентификатору с учетом принадлежности пользователю текущей сессии
    public Project removeByIdUserId(Long id) throws ProjectNotFoundException {
        if (projectRepository.findById(id).getUserid().equals(Application.userIdCurrent))
            return projectRepository.removeById(id);
        throw new ProjectNotFoundException(PROJECT_FOREIGN +"PROJECT NOT REMOVED.");
    }

    //Удаление проекта по идентификатору с учетом принадлежности проекта
    public int removeProjectById() throws ProjectNotFoundException {
        System.out.println("REMOVE PROJECT BY ID");
        System.out.println(PROJECT_ID_ENTER);
        final Long id = control.scannerIdIsLong();
        if (id != null) {
            removeByIdUserId(id);
            System.out.println("OK");
            return 0;
        }
        else return -1;
    }

    //Удаление проекта по индексу
    public Project removeByIndex(int index) throws ProjectNotFoundException {
        return projectRepository.removeByIndex(index);
    }

    //Удаление проекта по индексу с учетом принадлежности пользователю текущей сессии
    public Project removeByIndexUserId(int index) throws ProjectNotFoundException {
        if (projectRepository.findByIndex(index).getUserid().equals(Application.userIdCurrent))
            return projectRepository.removeByIndex(index);
        throw new ProjectNotFoundException(PROJECT_FOREIGN +"PROJECT NOT REMOVED.");
    }

    //Удаление проекта по индексу с учетом принадлежности проекта
    public int removeProjectByIndex() throws ProjectNotFoundException {
        System.out.println("REMOVE PROJECT BY INDEX");
        System.out.println(PROJECT_INDEX_ENTER);
        final Integer index = control.scannerIndexIsInteger();
        if (index != null) {
            removeByIndexUserId(index);
            System.out.println("OK");
            return 0;
        }
        else return -1;
    }

    //Сортировка проектов по наименованию
    public List<Project> projectSortByName(List<Project> projects) {
        Collections.sort(projects, Project.ProjectSortByName);
        return projects;
    }

    //Добавление проекта пользователю.
    public Project addProjectToUser(final Long userId, final Long projectId) throws ProjectNotFoundException {
        final Project project = projectRepository.findById(projectId);
        project.setUserid(userId);
        return project;
    }

    //Добавление принадлежности проекта пользователю по идентификатору проекта и логину пользователя.
    public int addProjectToUser() throws ProjectNotFoundException {
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
                    findByIdUserId(projectId);
                    addProjectToUser(userId, projectId);
                    System.out.println("OK");
                    return 0;
                }
                return -1;
            }
        }
        return -1;
    }

    //Удаление принадлежности проекта пользователю по идентификатору задачи.
    public int removeProjectFromUser() throws ProjectNotFoundException {
        System.out.println("REMOVE PROJECT FROM USER");
        System.out.println(PROJECT_ID_ENTER);
        final Long projectId = control.scannerIdIsLong();
        if (projectId != null) {
            final Project project = findByIdUserId(projectId);
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