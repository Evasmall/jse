package ru.evasmall.tm.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.evasmall.tm.entity.Project;
import ru.evasmall.tm.exeption.ProjectNotFoundException;

import java.util.*;

public class ProjectRepository {

    private static final Logger logger = LogManager.getLogger(ProjectRepository.class);

    private final List<Project> projects = new ArrayList<>();

    private static ProjectRepository instance = null;

    public static ProjectRepository getInstance() {
        if (instance == null) {
            synchronized(ProjectRepository.class) {
                if (instance == null)
                    instance = new ProjectRepository();
            }
        }
        return instance;
    }

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
        logger.trace("PROJECT CREATED: NEW NAME: {} NEW DESCRIPTION: {}", name, description);
        return project;
    }

    public Project update(final Long id, final String name, String description) throws ProjectNotFoundException {
        final Project project = findById(id);
        removeProjectFromMap(project);
        project.setId(id);
        project.setName(name);
        project.setDescription(description);
        addProjectToMap(project);
        logger.trace("PROJECT UPDATE. ID: {} NEW NAME: {} NEW DESCRIPTION: {}", id, name, description);
        return project;
    }

    public void clear() {
        projects.clear();
        projectsName.clear();
        logger.info("CLEAR ALL PROJECTS.");
    }

    public Project findByIndex(int index) throws ProjectNotFoundException {
        if (index < 0 || index > projects.size() - 1)
            throw new ProjectNotFoundException("PROJECT NOT FOUND BY INDEX: " + (index + 1) +". FAIL.");
        return projects.get(index);
    }

    public List<Project> findByName(final String name) throws ProjectNotFoundException {
        final List<Project> projectsNew = new ArrayList<>();
        if (name.isEmpty())
            throw new IllegalArgumentException("PROJECT NAME IS EMPTY. FAIL.");
        if (projectsName.get(name) == null || projectsName.get(name).isEmpty())
            throw new ProjectNotFoundException("PROJECT NOT FOUND BY NAME: " + name + ". FAIL.");
        for (final Project project: projectsName.get(name)) {
            projectsNew.add(project);
        }
        return projects;
    }

    public Project findById(final Long id) throws ProjectNotFoundException {
        if (id == null)
            throw new ProjectNotFoundException("PROJECT ID IS EMPTY. FAIL.");
        for (final Project project: projects) {
            if (project.getId().equals(id))
                return project;
        }
        throw new ProjectNotFoundException("PROJECT NOT FOUND BY ID: " + id + ". FAIL.");
    }

    public Project removeById (final Long id) throws ProjectNotFoundException {
        final Project project = findById(id);
        removeProjectFromMap(project);
        projects.remove(project);
        System.out.println(projectsName);
        logger.info("PROJECT ID: {} DELETE", id);
        return project;
    }

    public Project removeByIndex (final int index) throws ProjectNotFoundException {
        final Project project = findByIndex(index);
        removeProjectFromMap(project);
        projects.remove(project);
        logger.info("PROJECT INDEX: {} DELETE", index);
        return project;
    }

    public int size() {
        return projects.size();
    }

    public void removeProjectFromMap(final Project project) {
        final String name = project.getName();
        HashSet<Project> projectsHashMap = projectsName.get(name);
        if (projectsHashMap != null)
            projectsHashMap.remove(project);
        if (projectsHashMap.isEmpty())
            projectsName.remove(name);
    }

    private void addProjectToMap(final Project project) {
        final String name = project.getName();
        HashSet<Project> projectsHashMap = projectsName.get(name);
        if (projectsHashMap != null)
            projectsHashMap.add(project);
        else {
            projectsHashMap = new HashSet<>();
            projectsHashMap.add(project);
            projectsName.put(name, projectsHashMap);
        }
    }

}