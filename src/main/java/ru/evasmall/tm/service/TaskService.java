package ru.evasmall.tm.service;

import ru.evasmall.tm.Application;
import ru.evasmall.tm.entity.Task;
import ru.evasmall.tm.repository.TaskRepository;

import java.util.Collections;
import java.util.List;

public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    //Создание задачи по параметрам.
    public Task create(String name, String description, Long userid) {
        if (name == null || name.isEmpty()) return null;
        if (description == null || description.isEmpty()) return null;
        return taskRepository.create(name, description, userid);
    }

    //Изменение задачи.
    public Task update(Long id, String name, String description) {
        if (id == null) return null;
        if (name == null || name.isEmpty()) return null;
        if (description == null || description.isEmpty()) return null;
        return taskRepository.update(id, name, description);
    }

    //Удаление всех задач.
    public void clear() {
        taskRepository.clear();
    }

    //Поиск задачи по наименованию
    public List<Task> findByName(String name) {
        if (name == null || name.isEmpty()) return null;
        return taskRepository.findByName(name);
    }

    //Поиск задачи по идентификатору.
    public Task findById(Long id) {
        if (id == null) return null;
        if (taskRepository.findById(id) == null) return  null;
        return taskRepository.findById(id);
    }

    //Поиск задачи по идентификатору с учетом принадлежности пользователю текущей сессии.
    public Task findByIdUserId(Long id) {
        if (id == null) return null;
        if (taskRepository.findById(id) == null) return  null;
        if (taskRepository.findById(id).getUserid().equals(Application.userIdCurrent))
            return taskRepository.findById(id);
        return null;
    }

    //Поиск задачи по индексу.
    public Task findByIndex(int index) {
        if (index < 0 || index > taskRepository.size() - 1) return null;
        return taskRepository.findByIndex(index);
    }

    //Поиск задачи по индексу с учетом принадлежности пользователю текущей сессии.
    public Task findByIndexUserId(int index) {
        if (index < 0 || index > taskRepository.size() - 1) return null;
        if (taskRepository.findByIndex(index).getUserid().equals(Application.userIdCurrent))
            return taskRepository.findByIndex(index);
        else return null;
    }

    //Удаление задачи по идентификатору.
    public Task removeById(Long id) {
        if (id == null ) return null;
        return taskRepository.removeById(id);
    }

    //Удаление задачи по идентификатору с учетом принадлежности пользователю текущей сессии.
    public Task removeByIdUserId(Long id) {
        if (id == null ) return null;
        if (taskRepository.findById(id) == null) return  null;
        if (taskRepository.findById(id).getUserid().equals(Application.userIdCurrent))
            return taskRepository.removeById(id);
        else return null;
    }

    //Удаление задачи по индексу.
    public Task removeByIndex(int index) {
        if (index < 0 || index > taskRepository.size() -1) return null;
        return taskRepository.removeByIndex(index);
    }

    //Удаление задачи по индексу с учетом принадлежности пользователю текущей сессии.
    public Task removeByIndexUserId(int index) {
        if (index < 0 || index > taskRepository.size() -1) return null;
        if (taskRepository.findByIndex(index).getUserid().equals(Application.userIdCurrent))
            return taskRepository.removeByIndex(index);
        else return null;
    }

    //Найти все проекты по идентификатору.
    public List<Task> findAllByProjectId(Long projectId) {
        if (projectId == null) return null;
        return taskRepository.findAllByProjectId(projectId);
    }

    //Найти проект по идентификаторам.
    public Task findByProjectIdAndId(Long projectId, Long id) {
        if (projectId == null || id == null) return null;
        return taskRepository.findByProjectIdAndId(projectId, id);
    }

    //Сортировка задач по наименованию
    public List<Task> TaskSortByName(List<Task> tasks) {
        Collections.sort(tasks, Task.TaskSortByName);
        return tasks;
    }

}
