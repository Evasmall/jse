package ru.evasmall.tm.repository;

import ru.evasmall.tm.entity.Task;

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

    public Task update(final Long id, final String name, String description) {
        final Task task = findById(id);
        if (task == null) return null;
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

    public List<Task> findByName(final String name) {
        final List<Task> tasks = new ArrayList<>();
        if (tasksName.get(name) == null) return null;
        for (final Task task: tasksName.get(name)) {
            tasks.add(task);
        }
        return tasks;
    }

    public Task findById(final Long id) {
        for (final Task task: tasks) {
            if(task.getId().equals(id)) return task;
        }
        return null;
    }

    public Task findByProjectIdAndId(final Long projectId, final Long id) {
        for (final Task task: tasks) {
            final Long idProject = task.getProjectId();
            if (idProject == null) continue;
            if (!idProject.equals(projectId)) continue;
            if(task.getId().equals(id)) return task;
        }
        return null;
    }

    public Task findByIndex(final int index) {
        return tasks.get(index);
    }

    public Task removeById (final Long id) {
        final Task task = findById(id);
        if (task == null) return null;
        removeTaskFromMap(task);
        tasks.remove(task);
        return task;
    }

    public Task removeByIndex (final int index) {
        final Task task = findByIndex(index);
        if (task == null) return null;
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
        if (tasksHashMap != null) {
            tasksHashMap.remove(task);
        }
        final List <Task> tasksHashMap1 = findByName(name);
        if (tasksHashMap1.isEmpty()) {
            {
                tasksName.remove(name);
            }
        }
    }

    private void addTaskToMap(final Task task) {
        final String name = task.getName();
        HashSet<Task> tasksHashMap = tasksName.get(name);
        if (tasksHashMap != null) {
            tasksHashMap.add(task);
        }
        else {
            tasksHashMap = new HashSet<>();
            tasksHashMap.add(task);
            tasksName.put(name, tasksHashMap);
        }
    }

}