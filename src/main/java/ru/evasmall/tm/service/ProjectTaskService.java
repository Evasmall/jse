package ru.evasmall.tm.service;

import ru.evasmall.tm.entity.Project;
import ru.evasmall.tm.entity.Task;
import ru.evasmall.tm.exeption.ProjectNotFoundException;
import ru.evasmall.tm.exeption.TaskNotFoundException;
import ru.evasmall.tm.repository.ProjectRepository;
import ru.evasmall.tm.repository.TaskRepository;

import java.util.Collections;
import java.util.List;

public class ProjectTaskService {

    private final ProjectRepository projectRepository;

    private final TaskRepository taskRepository;

    public ProjectTaskService(ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    //Вывод всех задач проекта.
    public List<Task> findAllByProjectId(final Long projectId) {
        if (projectId == null) return Collections.emptyList();
        return taskRepository.findAddByProjectId(projectId);
    }

    //Удаление задачи из проекта.
    public Task removeTaskFromProject(final Long projectId, final Long taskId) throws TaskNotFoundException {
        final Task task = taskRepository.findByProjectIdAndId(projectId, taskId);
        task.setProjectId(null);
        return task;
    }

    //Добавление задачи в проект.
    public Task addTaskToProject(final Long projectId, final Long taskId, final Long userId) throws ProjectNotFoundException, TaskNotFoundException  {
        final Project project = projectRepository.findById(projectId);
        final Task task = taskRepository.findById(taskId);
        task.setProjectId(projectId);
        task.setUserid(userId);
        return task;
    }

    //Удаление проекта со связанными задачами по идентификатору.
    public Project removeProjectByIdWithTask(final Long projectId) throws ProjectNotFoundException, TaskNotFoundException  {
        final Project project = projectRepository.findById(projectId);
        final List<Task> tasks = findAllByProjectId(projectId);
        if (tasks == null) return project;
        for (Task task: tasks) {
            taskRepository.removeById(task.getId());
        }
        projectRepository.removeById(projectId);
        return project;
    }

    //Удаление проекта со связанными задачами по индексу.
    public Project removeProjectByIndexWithTask(final int index) throws ProjectNotFoundException, TaskNotFoundException {
        final Project project = projectRepository.findByIndex(index);
        final List<Task> tasks = findAllByProjectId(project.getId());
        if (tasks == null) return project;
        for (Task task: tasks) {
            taskRepository.removeById(task.getId());
        }
        projectRepository.removeById(project.getId());
        return project;
    }

}
