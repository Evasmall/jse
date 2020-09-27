package ru.evasmall.tm.service;

import ru.evasmall.tm.Application;
import ru.evasmall.tm.entity.Project;
import ru.evasmall.tm.exeption.ProjectNotFoundException;
import ru.evasmall.tm.repository.ProjectRepository;

import java.util.Collections;
import java.util.List;

import static ru.evasmall.tm.constant.TerminalMassage.PROJECT_FOREIGN;

public class ProjectService {

    private final ProjectRepository projectRepository;

    private static ProjectService instance = null;

    public ProjectService() {
        this.projectRepository = ProjectRepository.getInstance();
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

    public Project update(Long id, String name, String description) throws ProjectNotFoundException {
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("PROJECT NAME IS EMPTY. PROJECT NOT UPDATED. FAIL.");
        if (description == null || description.isEmpty())
            throw new IllegalArgumentException("PROJECT DESCRIPTION IS EMPTY. PROJECT NOT UPDATED. FAIL.");
        return projectRepository.update(id, name, description);
    }

    public void clear() {
        projectRepository.clear();
    }

    //Поиск проекта по индексу
    public Project findByIndex(int index) throws ProjectNotFoundException {
        return projectRepository.findByIndex(index);
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

}