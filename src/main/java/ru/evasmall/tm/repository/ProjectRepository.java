package ru.evasmall.tm.repository;

import ru.evasmall.tm.entity.Project;

import java.util.*;

public class ProjectRepository {

    private final List<Project> projects = new ArrayList<>();

    public List<Project> findAll() {
        System.out.println(projectsName);
        return projects;
    }

    private final HashMap<String, HashSet<Project>> projectsName = new HashMap<>();

    public Project create(final String name, String description, Long userid) {
        final Project project = new Project(name);
        project.setName(name);
        project.setDescription(description);
        project.setUserid(userid);
        projects.add(project);
        addProjectToMap(project);
        return project;
    }

    public Project update(final Long id, final String name, String description) {
        final Project project = findById(id);
        if (project == null) return null;
        removeProjectFromMap(project);
        project.setId(id);
        project.setName(name);
        project.setDescription(description);
        addProjectToMap(project);
        return project;
    }

    public void clear() {
        projects.clear();
        projectsName.clear();
    }

    public Project findByIndex(int index) {
        return projects.get(index);
    }

    public List<Project> findByName(final String name) {
        final List<Project> projects = new ArrayList<>();
        if (projectsName.get(name) == null) return null;
        for (final Project project: projectsName.get(name)) {
            projects.add(project);
        }
        return projects;
    }

    public Project findById(final Long id) {
        for (final Project project: projects) {
            if(project.getId().equals(id)) return project;
        }
        return null;
    }

    public Project removeById (final Long id) {
        final Project project = findById(id);
        if (project == null) return null;
        removeProjectFromMap(project);
        projects.remove(project);
        System.out.println(projectsName);
        return project;
    }

    public Project removeByIndex (final int index) {
        final Project project = findByIndex(index);
        if (project == null) return null;
        removeProjectFromMap(project);
        projects.remove(project);
        System.out.println(projectsName);
        return project;
    }

    public int size() {
        return projects.size();
    }

    public void removeProjectFromMap(final Project project) {
        final String name = project.getName();
        HashSet<Project> projects = projectsName.get(name);
        if (projects != null) {
            projects.remove(project);
        }
        final List <Project> projects1 = findByName(name);
        if (projects1.isEmpty()) {
            {
                projectsName.remove(name);
            }
        }
    }

    private void addProjectToMap(final Project project) {
        HashSet<Project> projectsHashMap = projectsName.get(project.getName());
        if (projectsHashMap == null) {
            projectsHashMap = new HashSet<>();
            projectsHashMap.add(project);
            projectsName.put(project.getName(), projectsHashMap);
        }
        else {
            projectsHashMap.add(project);
        }
    }

}