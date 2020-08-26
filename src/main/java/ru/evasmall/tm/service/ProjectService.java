package ru.evasmall.tm.service;

import ru.evasmall.tm.Application;
import ru.evasmall.tm.entity.Project;
import ru.evasmall.tm.repository.ProjectRepository;

import java.util.Collections;
import java.util.List;

public class ProjectService {

    private final ProjectRepository projectRepository;



    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public Project create(String name, String description, Long userid) {
        if (name == null || name.isEmpty()) return null;
        if (description == null || description.isEmpty()) return null;
        return projectRepository.create(name, description, userid);
    }

    public Project update(Long id, String name, String description) {
        if (id == null) return null;
        if (name == null || name.isEmpty()) return null;
        if (description == null || description.isEmpty()) return null;
        return projectRepository.update(id, name, description);
    }

    public void clear() {
        projectRepository.clear();
    }

    //Поиск проекта по индексу
    public Project findByIndex(int index) {
        if (index < 0 || index > projectRepository.size() - 1) return null;
        return projectRepository.findByIndex(index);
    }

    //Поиск проекта по индексу с учетом принадлежности пользователю текущей сессии
    public Project findByIndexUserId(int index) {
        if (index < 0 || index > projectRepository.size() - 1) return null;
        if (projectRepository.findByIndex(index) == null) return null;
        if (projectRepository.findByIndex(index).getUserid().equals(Application.userIdCurrent)) return projectRepository.findByIndex(index);
        else return null;
    }

    //Поиск проекта по наименованию
    public List<Project> findByName(String name) {
        if (name == null || name.isEmpty()) return null;
        return projectRepository.findByName(name);
    }

    //Поиск проекта по наименованию с учетом принадлежности пользователю текущей сессии
    public List<Project> findByNameUserId(String name) {
        if (name == null || name.isEmpty()) return null;
        if (projectRepository.findByName(name) == null) return null;
        for (Project project: projectRepository.findByName(name)) {
            if (project.getUserid().equals(Application.userIdCurrent)) return projectRepository.findByName(name);
        }
        return null;
    }

    //Поиск проекта по идентификатору
    public Project findById(Long id) {
        if (id == 0) return null;
        if (projectRepository.findById(id) == null) return  null;
        return projectRepository.findById(id);
    }

    //Поиск проекта по идентификатору с учетом принадлежности пользователю текущей сессии
    public Project findByIdUserId(Long id) {
        if (id == 0) return null;
        if (projectRepository.findById(id) == null) return  null;
        if (projectRepository.findById(id).getUserid().equals(Application.userIdCurrent)) return projectRepository.findById(id);
        return null;
    }

    //Удаление проекта по идентификатору
    public Project removeById(Long id) {
        if (id == null ) return null;
        if (projectRepository.findById(id) == null) return  null;
        return projectRepository.removeById(id);
    }

    //Удаление проекта по идентификатору с учетом принадлежности пользователю текущей сессии
    public Project removeByIdUserId(Long id) {
        if (id == null ) return null;
        if (projectRepository.findById(id) == null) return  null;
        if (projectRepository.findById(id).getUserid().equals(Application.userIdCurrent)) return projectRepository.removeById(id);
        return null;
    }

    //Удаление проекта по индексу
    public Project removeByIndex(int index) {
        if (index < 0 || index > projectRepository.size() -1) return null;
        return projectRepository.removeByIndex(index);
    }

    //Удаление проекта по индексу с учетом принадлежности пользователю текущей сессии
    public Project removeByIndexUserId(int index) {
        if (index < 0 || index > projectRepository.size() -1) return null;
        if (projectRepository.findByIndex(index) == null) return null;
        if (projectRepository.findByIndex(index).getUserid().equals(Application.userIdCurrent)) return projectRepository.removeByIndex(index);
        else return null;
    }

    //Сортировка задач по наименованию
    public List<Project> ProjectSortByName(List<Project> projects) {
        Collections.sort(projects, Project.ProjectSortByName);
        return projects;
    }

}