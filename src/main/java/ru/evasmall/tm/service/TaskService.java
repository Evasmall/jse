package ru.evasmall.tm.service;

import ru.evasmall.tm.Application;
import ru.evasmall.tm.entity.Task;
import ru.evasmall.tm.exeption.IncorrectFormatException;
import ru.evasmall.tm.exeption.TaskNotFoundException;
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
    public Task create(String name, String description, Long userid) throws IncorrectFormatException {
        if (name == null || name.isEmpty())
            throw new IncorrectFormatException("TASK NAME IS EMPTY. TASK NOT CREATED. FAIL.");
        if (description == null || description.isEmpty())
            throw new IncorrectFormatException("TASK DESCRIPTION IS EMPTY. TASK NOT CREATED. FAIL.");
        return taskRepository.create(name, description, userid);
    }

    //Изменение задачи.
    public Task update(Long id, String name, String description) throws TaskNotFoundException, IncorrectFormatException {
        if (name == null || name.isEmpty())
            throw new IncorrectFormatException("TASK NAME IS EMPTY. TASK NOT UPDATED. FAIL.");
        if (description == null || description.isEmpty())
            throw new IncorrectFormatException("TASK DESCRIPTION IS EMPTY. TASK NOT UPDATED. FAIL.");
        return taskRepository.update(id, name, description);
    }

    //Удаление всех задач.
    public void clear() {
        taskRepository.clear();
    }

    //Поиск задачи по наименованию
    public List<Task> findByName(String name) throws TaskNotFoundException, IncorrectFormatException {
        return taskRepository.findByName(name);
    }

    //Поиск задачи по идентификатору.
    public Task findById(Long id) throws TaskNotFoundException {
        return taskRepository.findById(id);
    }

    //Поиск задачи по идентификатору с учетом принадлежности пользователю текущей сессии.
    public Task findByIdUserId(Long id) throws TaskNotFoundException {
        if (taskRepository.findById(id).getUserid() == null)
            return taskRepository.findById(id);
        if (taskRepository.findById(id).getUserid().equals(Application.userIdCurrent))
            return taskRepository.findById(id);
        throw new TaskNotFoundException("TASK IS FOREIGN. FAIL.");
    }

    //Поиск задачи по индексу.
    public Task findByIndex(int index) throws TaskNotFoundException {
        return taskRepository.findByIndex(index);
    }

    //Поиск задачи по индексу с учетом принадлежности пользователю текущей сессии.
    public Task findByIndexUserId(int index) throws TaskNotFoundException {
        if (taskRepository.findByIndex(index).getUserid().equals(Application.userIdCurrent))
            return taskRepository.findByIndex(index);
        throw new TaskNotFoundException("TASK IS FOREIGN. FAIL.");
    }

    //Удаление задачи по идентификатору.
    public Task removeById(Long id) throws TaskNotFoundException {
        return taskRepository.removeById(id);
    }

    //Удаление задачи по идентификатору с учетом принадлежности пользователю текущей сессии.
    public Task removeByIdUserId(Long id) throws TaskNotFoundException {
        if (taskRepository.findById(id).getUserid().equals(Application.userIdCurrent))
            return taskRepository.removeById(id);
        throw new TaskNotFoundException("TASK IS FOREIGN. TASK NOT REMOVED. FAIL.");
    }

    //Удаление задачи по индексу.
    public Task removeByIndex(int index) throws TaskNotFoundException {
        return taskRepository.removeByIndex(index);
    }

    //Удаление задачи по индексу с учетом принадлежности пользователю текущей сессии.
    public Task removeByIndexUserId(int index) throws TaskNotFoundException{
        if (taskRepository.findByIndex(index).getUserid().equals(Application.userIdCurrent))
            return taskRepository.removeByIndex(index);
        throw new TaskNotFoundException("TASK IS FOREIGN. TASK NOT REMOVED. FAIL.");
    }

    //Найти все проекты по идентификатору.
    public List<Task> findAllByProjectId(Long projectId) {
        if (projectId == null) return null;
        return taskRepository.findAllByProjectId(projectId);
    }

    //Найти проект по идентификаторам.
    public Task findByProjectIdAndId(Long projectId, Long id) throws TaskNotFoundException {
        if (projectId == null || id == null) return null;
        return taskRepository.findByProjectIdAndId(projectId, id);
    }

    //Сортировка задач по наименованию
    public List<Task> TaskSortByName(List<Task> tasks) {
        Collections.sort(tasks, Task.TaskSortByName);
        return tasks;
    }

    //Добавление задачи пользователю.
    public Task addTaskToUser(final Long userId, final Long taskId) throws TaskNotFoundException {
        final Task task = taskRepository.findById(taskId);
        task.setUserid(userId);
        return task;
    }

}
