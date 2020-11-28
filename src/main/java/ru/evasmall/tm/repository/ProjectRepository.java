package ru.evasmall.tm.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.evasmall.tm.entity.Project;
import ru.evasmall.tm.exeption.ProjectNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class ProjectRepository extends AbstractRepository<Project> {

    private static final Logger logger = LogManager.getLogger(ProjectRepository.class);

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

    public String getObjectName(final Project project) {
        if (project == null) return null;
        return project.getName();
    }

    public Project create(final String name, String description, Long userid) {
        final Project project = new Project(name);
        project.setName(name);
        project.setDescription(description);
        project.setUserid(userid);
        objects.add(project);
        addObjectToMap(project);
        logger.trace("PROJECT CREATED: NEW NAME: {} NEW DESCRIPTION: {}", name, description);
        return project;
    }

    public Project update(final Long id, final String name, String description) throws ProjectNotFoundException {
        final Project project = findById(id);
        removeObjectFromMap(project);
        project.setId(id);
        project.setName(name);
        project.setDescription(description);
        addObjectToMap(project);
        logger.trace("PROJECT UPDATE. ID: {} NEW NAME: {} NEW DESCRIPTION: {}", id, name, description);
        return project;
    }

    public Project findByIndex(int index) throws ProjectNotFoundException {
        if (index < 0 || index > objects.size() - 1)
            throw new ProjectNotFoundException("PROJECT NOT FOUND BY INDEX: " + (index + 1) +". FAIL.");
        return objects.get(index);
    }

    public List<Project> findByName(final String name) throws ProjectNotFoundException {
        final List<Project> projectsNew = new ArrayList<>();
        if (name.isEmpty())
            throw new IllegalArgumentException("PROJECT NAME IS EMPTY. FAIL.");
        if (objectsName.get(name) == null || objectsName.get(name).isEmpty())
            throw new ProjectNotFoundException("PROJECT NOT FOUND BY NAME: " + name + ". FAIL.");
        for (final Project project: objectsName.get(name)) {
            projectsNew.add(project);
        }
        return projectsNew;
    }

    public Project findById(final Long id) throws ProjectNotFoundException {
        if (id == null)
            throw new ProjectNotFoundException("PROJECT ID IS EMPTY. FAIL.");
        for (final Project project: objects) {
            if (project.getId().equals(id))
                return project;
        }
        throw new ProjectNotFoundException("PROJECT NOT FOUND BY ID: " + id + ". FAIL.");
    }

    public Project removeById (final Long id) throws ProjectNotFoundException {
        final Project project = findById(id);
        removeObjectFromMap(project);
        objects.remove(project);
        System.out.println(objectsName);
        logger.info("PROJECT ID: {} DELETE", id);
        return project;
    }

    public Project removeByIndex (final int index) throws ProjectNotFoundException {
        final Project project = findByIndex(index);
        removeObjectFromMap(project);
        objects.remove(project);
        logger.info("PROJECT INDEX: {} DELETE", index + 1);
        return project;
    }

}