package ru.evasmall.tm.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.evasmall.tm.entity.Project;
import ru.evasmall.tm.exeption.ProjectNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public Optional<Project> create(final String name, String description, Long userid) {
        final Optional<Project> project = Optional.of(new Project(name));
        project.ifPresent(p -> p.setName(name));
        project.ifPresent(p -> p.setDescription(description));
        project.ifPresent(p -> p.setUserid(userid));
        project.ifPresent(objects::add);
        addObjectToMap(project.get());
        logger.trace("PROJECT CREATED: NEW NAME: {} NEW DESCRIPTION: {}", name, description);
        return project;
    }

    public Optional<Project> update(final Long id, final String name, String description) throws ProjectNotFoundException {
        final Optional<Project> project = findById(id);
        removeObjectFromMap(project.get());
        project.ifPresent(p -> p.setId(id));
        project.ifPresent(p -> p.setName(name));
        project.ifPresent(p -> p.setDescription(description));
        addObjectToMap(project.get());
        logger.trace("PROJECT UPDATE. ID: {} NEW NAME: {} NEW DESCRIPTION: {}", id, name, description);
        return project;
    }

    public Optional<Project> findByIndex(int index) throws ProjectNotFoundException {
        if (index < 0 || index > objects.size() - 1)
            throw new ProjectNotFoundException("PROJECT NOT FOUND BY INDEX: " + (index + 1) +". FAIL.");
        return Optional.ofNullable(objects.get(index));
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

    public Optional<Project> findById(final Long id) throws ProjectNotFoundException {
        if (id == null)
            throw new ProjectNotFoundException("PROJECT ID IS EMPTY. FAIL.");
        for (final Project project: objects) {
            if (project.getId().equals(id))
                return Optional.ofNullable(project);
        }
        throw new ProjectNotFoundException("PROJECT NOT FOUND BY ID: " + id + ". FAIL.");
    }

    public Optional<Project> removeById (final Long id) throws ProjectNotFoundException {
        final Optional<Project> project = findById(id);
        removeObjectFromMap(project.get());
        project.ifPresent(objects::remove);
        System.out.println(objectsName);
        logger.info("PROJECT ID: {} DELETE", id);
        return project;
    }

    public Optional<Project> removeByIndex (final int index) throws ProjectNotFoundException {
        final Optional<Project> project = findByIndex(index);
        removeObjectFromMap(project.get());
        project.ifPresent(objects::remove);
        logger.info("PROJECT INDEX: {} DELETE", index + 1);
        return project;
    }

}