package ru.evasmall.tm.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.evasmall.tm.entity.Task;
import ru.evasmall.tm.exeption.TaskNotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.evasmall.tm.constant.TerminalConst.DEADLINE_8_HOURS;

public class TaskRepository extends AbstractRepository<Task> {

    private static final Logger logger = LogManager.getLogger(TaskRepository.class);

    private static TaskRepository instance = null;

    public static TaskRepository getInstance() {
        if (instance == null) {
            synchronized(TaskRepository.class) {
                if (instance == null)
                    instance = new TaskRepository();
            }
        }
        return instance;
    }

    public String getObjectName (Task task) {
        if (task == null) return null;
        return task.getName();
    }

    public List<Task> findAllByProjectId(final Long projectId) {
        final List<Task> result = new ArrayList<>();
        for (final Task task: findAll().get()) {
            if (task.getProjectId() == null) continue;
            if (task.getProjectId().equals(projectId)) result.add(task);
        }
        return result;
    }

    public Optional<Task> create(final String name, final String description, final Long userId) {
        final Optional<Task> task = Optional.of(new Task(name));
        task.ifPresent(t -> t.setName(name));
        task.ifPresent(t -> t.setDescription(description));
        task.ifPresent(t -> t.setUserid(userId));
        task.ifPresent(t -> t.setDeadline(LocalDateTime.now().plusHours(8)));
        task.ifPresent(t -> t.setNotifyDeadline(DEADLINE_8_HOURS));
        objects.add(task.get());
        addObjectToMap(task.get());
        logger.trace("TASK CREATED: NEW NAME: {} NEW DESCRIPTION: {}", name, description);
        return task;
    }

    public synchronized Task update(final Long id, final String name, String description) throws TaskNotFoundException{
        final Task task = findById(id);
        removeObjectFromMap(task);
        task.setId(id);
        task.setName(name);
        task.setDescription(description);
        addObjectToMap(task);
        logger.trace("TASK UPDATE. ID: {} NEW NAME: {} NEW DESCRIPTION: {}", id, name, description);
        return task;
    }

    public List<Task> findAddByProjectId(final Long projectId) {
        final List<Task> result = new ArrayList<>();
        for (final Task task: findAll().get()) {
            final Long idProject = task.getProjectId();
            if (idProject == null) continue;
            if (idProject.equals(projectId)) result.add(task);
        }
        return result;
    }

    public List<Task> findByName(final String name) throws TaskNotFoundException {
        final List<Task> tasksNew = new ArrayList<>();
        if (name.isEmpty())
            throw new IllegalArgumentException("TASK NAME IS EMPTY. FAIL.");
        if (objectsName.get(name) == null || objectsName.get(name).isEmpty())
            throw new TaskNotFoundException("TASK NOT FOUND BY NAME: " + name + ". FAIL.");
        for (final Task task: objectsName.get(name)) {
            tasksNew.add(task);
        }
        return tasksNew;
    }

    public Task findById(final Long id) throws TaskNotFoundException {
        if (id == null)
            throw new TaskNotFoundException("TASK ID IS EMPTY. FAIL.");
        for (final Task task: objects) {
            if(task.getId().equals(id))
                return task;
        }
        throw new TaskNotFoundException("TASK NOT FOUND BY ID: " + id + ". FAIL.");
    }

    public Task findByProjectIdAndId(final Long projectId, final Long id) {
        for (final Task task: objects) {
            final Long idProject = task.getProjectId();
            if (idProject == null || !idProject.equals(projectId)) continue;
            if(task.getId().equals(id)) return task;
        }
        return null;
    }

    public Task findByIndex(final int index) throws TaskNotFoundException {
        if (index < 0 || index > objects.size() - 1) {
            throw new TaskNotFoundException("TASK NOT FOUND BY INDEX: " + (index + 1) +".");
        }
        return objects.get(index);
    }

    public synchronized Task removeById (final Long id) throws TaskNotFoundException {
        final Task task = findById(id);
        removeObjectFromMap(task);
        objects.remove(task);
        logger.info("TASK ID: {} DELETE", id);
        return task;
    }

    public synchronized Task removeByIndex (final int index) throws TaskNotFoundException{
        final Task task = findByIndex(index);
        removeObjectFromMap(task);
        objects.remove(task);
        logger.info("TASK INDEX: {} DELETE", index + 1);
        return task;
    }

}