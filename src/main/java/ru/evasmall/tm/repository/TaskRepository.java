package ru.evasmall.tm.repository;

import ru.evasmall.tm.entity.Task;
import ru.evasmall.tm.exeption.IncorrectFormatException;
import ru.evasmall.tm.exeption.TaskNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class TaskRepository {

    private final List<Task> tasks = new ArrayList<>();

    public List<Task> findAll() {
        System.out.println(tasksName);
        return tasks;
    }

    private final HashMap<String, HashSet<Task>> tasksName = new HashMap<>();

    public List<Task> findAllByProjectId(final Long projectId) {
        final List<Task> result = new ArrayList<>();
        for (final Task task: findAll()) {
            if (task.getProjectId() == null) continue;
            if (task.getProjectId().equals(projectId)) result.add(task);
        }
        return result;
    }

    public Task create(final String name, final String description, final Long userId) {
        final Task task = new Task(name);
        task.setName(name);
        task.setDescription(description);
        task.setUserid(userId);
        tasks.add(task);
        addTaskToMap(task);
        return task;
    }

    public Task update(final Long id, final String name, String description) throws TaskNotFoundException{
        final Task task = findById(id);
        removeTaskFromMap(task);
        task.setId(id);
        task.setName(name);
        task.setDescription(description);
        addTaskToMap(task);
        return task;
    }

    public List<Task> findAddByProjectId(final Long projectId) {
        final List<Task> result = new ArrayList<>();
        for (final Task task: findAll()) {
            final Long idProject = task.getProjectId();
            if (idProject == null) continue;
            if (idProject.equals(projectId)) result.add(task);
        }
        return result;
    }

    public void clear() {
        tasks.clear();
        tasksName.clear();
    }

    public List<Task> findByName(final String name) throws TaskNotFoundException, IncorrectFormatException {
        final List<Task> tasks = new ArrayList<>();
        if (name.isEmpty())
            throw new IncorrectFormatException("TASK NAME IS EMPTY. FAIL.");
        if (tasksName.get(name) == null || tasksName.get(name).isEmpty())
            throw new TaskNotFoundException("TASK NOT FOUND BY NAME: " + name + ". FAIL.");
        if (tasksName.get(name) == null)
            return null;
        for (final Task task: tasksName.get(name)) {
            tasks.add(task);
        }
        return tasks;
    }

    public Task findById(final Long id) throws TaskNotFoundException {
        if (id == null)
            throw new TaskNotFoundException("TASK ID IS EMPTY. FAIL.");
        for (final Task task: tasks) {
            if(task.getId().equals(id))
                return task;
        }
        throw new TaskNotFoundException("TASK NOT FOUND BY ID: " + id + ". FAIL.");
    }

    public Task findByProjectIdAndId(final Long projectId, final Long id) throws TaskNotFoundException {
        for (final Task task: tasks) {
            final Long idProject = task.getProjectId();
            if (idProject == null) continue;
            if (!idProject.equals(projectId)) continue;
            if(task.getId().equals(id)) return task;
        }
        return null;
    }

    public Task findByIndex(final int index) throws TaskNotFoundException {
        if (index < 0 || index > tasks.size() - 1) {
            throw new TaskNotFoundException("TASK NOT FOUND BY INDEX: " + (index + 1) +".");
        }
        return tasks.get(index);
    }

    public Task removeById (final Long id) throws TaskNotFoundException {
        final Task task = findById(id);
        removeTaskFromMap(task);
        tasks.remove(task);
        return task;
    }

    public Task removeByIndex (final int index) throws TaskNotFoundException{
        final Task task = findByIndex(index);
        removeTaskFromMap(task);
        tasks.remove(task);
        return task;
    }

    public int size() {
        return tasks.size();
    }

    public void removeTaskFromMap(final Task task) {
        final String name = task.getName();
        HashSet<Task> tasksHashMap = tasksName.get(name);
        if (tasksHashMap != null)
            tasksHashMap.remove(task);
        if (tasksHashMap.isEmpty())
            tasksName.remove(name);
    }

    private void addTaskToMap(final Task task) {
        final String name = task.getName();
        HashSet<Task> tasksHashMap = tasksName.get(name);
        if (tasksHashMap != null)
            tasksHashMap.add(task);
        else {
            tasksHashMap = new HashSet<>();
            tasksHashMap.add(task);
            tasksName.put(name, tasksHashMap);
        }
    }

}