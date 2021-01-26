package ru.evasmall.tm.service;

import ru.evasmall.tm.Application;
import ru.evasmall.tm.constant.TerminalMassage;
import ru.evasmall.tm.entity.Task;
import ru.evasmall.tm.entity.User;
import ru.evasmall.tm.exeption.TaskNotFoundException;
import ru.evasmall.tm.repository.TaskRepository;
import ru.evasmall.tm.util.Control;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static ru.evasmall.tm.constant.FileNameConst.TASK_JSON;
import static ru.evasmall.tm.constant.FileNameConst.TASK_XML;
import static ru.evasmall.tm.constant.TerminalConst.*;
import static ru.evasmall.tm.constant.TerminalMassage.*;

public class TaskService extends AbstractService {

    private final TaskRepository taskRepository;

    private final Control control = new Control();

    private final UserService userService;

    private final SystemService systemService = new SystemService();

    private static TaskService instance = null;

    public TaskService() {
        this.taskRepository = TaskRepository.getInstance();
        this.userService = UserService.getInstance();
    }

    public static TaskService getInstance() {
        if (instance == null) {
            instance = new TaskService();
        }
        return instance;
    }

    /**
     * Запись всех задач в файл формата JSON.
     */
    public int writeTaskJson() {
        if (Application.userIdCurrent == null) {
            System.out.println(UNAUTHORIZED_USER);
            return RETURN_ERROR;
        }
        taskRepository.writeJson(TASK_JSON);
        System.out.println("TASKS " + TerminalMassage.DATA_WRITTEN_FILES);
        return RETURN_OK;
    }

    /**
     * Запись всех задач в файл формата XML.
     */
    public int writeTaskXML() {
        if (Application.userIdCurrent == null) {
            System.out.println(UNAUTHORIZED_USER);
            return RETURN_ERROR;
        }
        taskRepository.writeXML(TASK_XML);
        System.out.println("TASK " + TerminalMassage.DATA_WRITTEN_FILES);
        return RETURN_OK;
    }

    /**
     * Чтение и перезапись всех задач из файла формата JSON (только для администраторов).
     */
    public int readTaskJson() {
        if (Application.userIdCurrent == null) {
            System.out.println(UNAUTHORIZED_USER);
            return RETURN_ERROR;
        }
        if (userService.findByUserId(Application.userIdCurrent).get().isAdmin()) {
            taskRepository.readJson(TASK_JSON, Task.class);
            System.out.println("TASKS " + TerminalMassage.DATA_READ_FILES);
            return RETURN_OK;
        }
        systemService.displayForAdminOnly();
        return RETURN_ERROR;
    }

    /**
     * Чтение и перезапись всех задач из файла формата XML (только для администраторов).
     */
    public int readTaskXML() {
        if (Application.userIdCurrent == null) {
            System.out.println(UNAUTHORIZED_USER);
            return RETURN_ERROR;
        }
        if (userService.findByUserId(Application.userIdCurrent).get().isAdmin()) {
            System.out.println("TASKS " + TerminalMassage.DATA_READ_FILES);
            taskRepository.readXML(TASK_XML, Task.class);
            return RETURN_OK;
        }
        systemService.displayForAdminOnly();
        return RETURN_ERROR;
    }

    /**
     * @return Список задач
     */
    public Optional<List<Task>> findAll() {
        return taskRepository.findAll();
    }

    /**
     * Создание задачи по параметрам.
     * @param name Наименование
     * @param description Описание
     * @param userid идентификатор пользователя
     * @return Задача
     */
    public Optional<Task> create(String name, String description, Long userid) {
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("TASK NAME IS EMPTY. TASK NOT CREATED. FAIL.");
        if (description == null || description.isEmpty())
            throw new IllegalArgumentException("TASK DESCRIPTION IS EMPTY. TASK NOT CREATED. FAIL.");
        return Optional.ofNullable(taskRepository.create(name, description, userid).get());
    }

    /**
     * Создание задачи.
     */
    public int createTask() {
        boolean taskFlag;
        if (Application.userIdCurrent == null) {
            System.out.println(UNAUTHORIZED_USER);
            return RETURN_ERROR;
        }
        else {
            if (findAll().isEmpty() || findAll() == null) {
                taskFlag = false;
            } else {
                taskFlag = true;
            }
            System.out.println("CREATE TASK");
            System.out.println(TASK_NAME_ENTER);
            final String name = scanner.nextLine();
            System.out.println(TASK_DESCRIPTION_ENTER);
            final String description = scanner.nextLine();
            create(name, description, Application.userIdCurrent);
            System.out.println("OK");
            if(!taskFlag) {
                notifyTaskDeadline();
            }
            return RETURN_OK;
        }
    }

    /**
     * Изменение задачи.
     * @param id Идентификатор задачи
     * @param name Наименование
     * @param description Описание
     * @return Задача
     * @throws TaskNotFoundException Задача не найдена
     */
    public Optional<Task> update(Long id, String name, String description) throws TaskNotFoundException {
        if (Application.userIdCurrent == null) {
            System.out.println(UNAUTHORIZED_USER);
            return Optional.empty();
        }
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("TASK NAME IS EMPTY. TASK NOT UPDATED. FAIL.");
        if (description == null || description.isEmpty())
            throw new IllegalArgumentException("TASK DESCRIPTION IS EMPTY. TASK NOT UPDATED. FAIL.");
        return Optional.ofNullable(taskRepository.update(id, name, description));
    }

    /**
     * Изменение задачи по индексу с учетом принадлежности задачи пользователю.
     * @throws TaskNotFoundException Задача не найдена
     */
    public int updateTaskByIndex() throws TaskNotFoundException {
        if (Application.userIdCurrent == null) {
            System.out.println(UNAUTHORIZED_USER);
            return RETURN_ERROR;
        }
        else {
            System.out.println("UPDATE TASK");
            System.out.println(TASK_INDEX_ENTER);
            final Integer index = control.scannerIndexIsInteger();
            if (index != null) {
                final Task task = findByIndexUserId(index).get();
                System.out.println(TASK_NAME_ENTER);
                final String name = scanner.nextLine();
                System.out.println(TASK_DESCRIPTION_ENTER);
                final String description = scanner.nextLine();
                update(task.getId(), name, description);
                System.out.println("OK");
                return RETURN_OK;
            }
            else return RETURN_ERROR;
        }
    }

    /**
     * Изменение задачи по идентификатору с учетом принадлежности задачи пользователю.
     * @throws TaskNotFoundException Задача не найдена
     */
    public int updateTaskById() throws TaskNotFoundException {
        if (Application.userIdCurrent == null) {
            System.out.println(UNAUTHORIZED_USER);
            return RETURN_ERROR;
        }
        else {
            System.out.println("UPDATE TASK");
            System.out.println(TASK_ID_ENTER);
            final Long id = control.scannerIdIsLong();
            if (id != null) {
                final Task task = findByIdUserId(id).get();
                System.out.println(TASK_NAME_ENTER);
                final String name = scanner.nextLine();
                System.out.println(TASK_DESCRIPTION_ENTER);
                final String description = scanner.nextLine();
                update(task.getId(), name, description);
                System.out.println("OK");
                return RETURN_OK;
            }
            else return RETURN_ERROR;
        }
    }

    /**
     * Удаление всех задач (функционал доступен только администраторам).
     */
    public int clearTask() {
        if (userService.findByUserId(Application.userIdCurrent) == null) {
            systemService.displayForAdminOnly();
            return RETURN_ERROR;
        }
        if (userService.findByUserId(Application.userIdCurrent).get().isAdmin()) {
            System.out.println("CLEAR TASK");
            taskRepository.clearObject();
            System.out.println("CLEAR ALL TASKS. OK.");
            return RETURN_OK;
        }
        else {
            systemService.displayForAdminOnly();
            return RETURN_ERROR;
        }
    }

    /**
     * Просмотр задачи.
     * @param task Задача
     */
    public void viewTask(final Task task) {
        if (task == null) return;
        if (Application.userIdCurrent == null) {
            System.out.println(UNAUTHORIZED_USER);
        }
        else {
            System.out.println("VIEW TASK");
            System.out.println("ID: " + task.getId());
            System.out.println("NAME: " + task.getName());
            System.out.println("DESCRIPTION: " + task.getDescription());
            System.out.println("PROJECT ID: " + task.getProjectId());
            System.out.println("USER LOGIN: " + userService.findByUserId(task.getUserid()).get().getLogin());
            System.out.println("DEADLINE: " + task.getDeadline());
            System.out.println("_____");
        }
    }

    /**
     * Поиск задачи по наименованию
     * @param name Наименование
     * @return Задача
     * @throws TaskNotFoundException Задача не найдена
     */
    public List<Task> findByName(String name) throws TaskNotFoundException {
        return taskRepository.findByName(name);
    }

    /**
     * Просмотр списка задач по наименованию
     * @throws TaskNotFoundException Задача не найдена
     */
    public int viewTaskByName() throws TaskNotFoundException {
        if (Application.userIdCurrent == null) {
            System.out.println(UNAUTHORIZED_USER);
            return RETURN_ERROR;
        }
        else {
            System.out.println(TASK_NAME_ENTER);
            String name = scanner.nextLine();
            final List <Task> tasks = findByName(name);
            tasks.forEach(this::viewTask);
            return RETURN_OK;
        }
    }

    /**
     * Поиск задачи по идентификатору.
     * @param id
     * @return Задача
     * @throws TaskNotFoundException Задача не найдена
     */
    public Optional<Task> findById(Long id) throws TaskNotFoundException {
        return Optional.ofNullable(taskRepository.findById(id));
    }

    /**
     * Поиск задачи по идентификатору с учетом принадлежности пользователю текущей сессии.
     * @param id Идентификатор
     * @return Задача
     * @throws TaskNotFoundException Задача не найдена
     */
    public Optional<Task> findByIdUserId(Long id) throws TaskNotFoundException {
        if (taskRepository.findById(id).getUserid() == null)
            return Optional.ofNullable(taskRepository.findById(id));
        if (taskRepository.findById(id).getUserid().equals(Application.userIdCurrent))
            return Optional.ofNullable(taskRepository.findById(id));
        throw new TaskNotFoundException(TASK_FOREIGN);
    }

    /**
     * Просмотр задачи по идентификатору.
     * @throws TaskNotFoundException Задача не найдена
     */
    public int viewTaskById() throws TaskNotFoundException {
        if (Application.userIdCurrent == null) {
            System.out.println(UNAUTHORIZED_USER);
            return RETURN_ERROR;
        }
        else {
            System.out.println(TASK_ID_ENTER);
            final Long id = control.scannerIdIsLong();
            if (id != null) {
                final Task task = findById(id).get();
                viewTask(task);
                return RETURN_OK;
            }
            return RETURN_ERROR;
        }
    }

    /**
     * Поиск задачи по индексу.
     * @param index Индекс
     * @return Задача
     * @throws TaskNotFoundException Задача не найдена
     */
    public Optional<Task> findByIndex(int index) throws TaskNotFoundException {
        return Optional.ofNullable(taskRepository.findByIndex(index));
    }

    /**
     * Поиск задачи по индексу с учетом принадлежности пользователю текущей сессии.
     * @param index Индекс
     * @return задача
     * @throws TaskNotFoundException Задача не найдена
     */
    public Optional<Task> findByIndexUserId(int index) throws TaskNotFoundException {
        if (taskRepository.findByIndex(index).getUserid().equals(Application.userIdCurrent))
            return Optional.ofNullable(taskRepository.findByIndex(index));
        throw new TaskNotFoundException(TASK_FOREIGN);
    }

    /**
     * Просмотр задачи по индексу.
     * @throws TaskNotFoundException Задача не найдена
     */
    public int viewTaskByIndex() throws TaskNotFoundException {
        if (Application.userIdCurrent == null) {
            System.out.println(UNAUTHORIZED_USER);
            return RETURN_ERROR;
        }
        else {
            System.out.println(TASK_INDEX_ENTER);
            final Integer index = control.scannerIndexIsInteger();
            if (index != null) {
                final Task task = findByIndex(index).get();
                viewTask(task);
                return RETURN_OK;
            }
            return RETURN_ERROR;
        }
    }

    /**
     * Удаление задачи по идентификатору.
     * @param id Идентификатор
     * @return Задача
     * @throws TaskNotFoundException Задача не найдена
     */
    public Optional<Task> removeById(Long id) throws TaskNotFoundException {
        return Optional.ofNullable(taskRepository.removeById(id));
    }

    /**
     * Удаление задачи по идентификатору с учетом принадлежности пользователю текущей сессии.
     * @param id Идентификатор
     * @return Задача
     * @throws TaskNotFoundException Задача не найдена
     */
    public Optional<Task> removeByIdUserId(Long id) throws TaskNotFoundException {
        if (taskRepository.findById(id).getUserid().equals(Application.userIdCurrent))
            return Optional.ofNullable(taskRepository.removeById(id));
        throw new TaskNotFoundException(TASK_FOREIGN + " TASK NOT REMOVED.");
    }

    /**
     * Удаление задачи по идентификатору с учетом принадлежности задачи пользователю.
     * @throws TaskNotFoundException Задача не найдена
     */
    public int removeTaskById() throws TaskNotFoundException {
        if (Application.userIdCurrent == null) {
            System.out.println(UNAUTHORIZED_USER);
            return RETURN_ERROR;
        }
        else {
            System.out.println("REMOVE TASK BY ID");
            System.out.println(TASK_ID_ENTER);
            final Long id = control.scannerIdIsLong();
            if (id != null) {
                removeByIdUserId(id);
                System.out.println("OK");
                return RETURN_OK;
            }
            else return RETURN_ERROR;
        }
    }

    /**
     * Удаление задачи по индексу.
     * @param index Индекс
     * @return Задача
     * @throws TaskNotFoundException Задача не найдена
     */
    public Optional<Task> removeByIndex(int index) throws TaskNotFoundException {
        return Optional.ofNullable(taskRepository.removeByIndex(index));
    }

    /**
     * Удаление задачи по индексу с учетом принадлежности пользователю текущей сессии.
     * @param index Индекс
     * @return Задача
     * @throws TaskNotFoundException Задача не найдена
     */
    public Optional<Task> removeByIndexUserId(int index) throws TaskNotFoundException{
        if (taskRepository.findByIndex(index).getUserid().equals(Application.userIdCurrent))
            return Optional.ofNullable(taskRepository.removeByIndex(index));
        throw new TaskNotFoundException(TASK_FOREIGN + " TASK NOT REMOVED.");
    }

    /**
     * Удаление задачи по индексу с учетом принадлежности задачи пользователю.
     * @throws TaskNotFoundException Задача не найдена
     */
    public int removeTaskByIndex() throws TaskNotFoundException {
        if (Application.userIdCurrent == null) {
            System.out.println(UNAUTHORIZED_USER);
            return RETURN_ERROR;
        }
        else {
            System.out.println("REMOVE TASK BY INDEX");
            System.out.println(TASK_INDEX_ENTER);
            final Integer index = control.scannerIndexIsInteger();
            if (index != null) {
                removeByIndexUserId(index);
                System.out.println("OK");
                return RETURN_OK;
            }
            else return RETURN_ERROR;
        }
    }

    /**
     * Найти все задачи проекта.
     * @param projectId Идентификатор проекта
     * @return Список задач
     */
    public List<Task> findAllByProjectId(Long projectId) {
        if (projectId == null) return null;
        return taskRepository.findAllByProjectId(projectId);
    }

    /**
     * Найти проект по идентификаторам.
     * @param projectId Идентификатор проекта
     * @param id Идентификатор задачи
     * @return Задача
     */
    public Optional<Task> findByProjectIdAndId(Long projectId, Long id) {
        if (projectId == null || id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(taskRepository.findByProjectIdAndId(projectId, id));
    }

    /**
     * Cписок задач.
     */
    public int listTask() {
        //Проверка на авторизацию польователя
        if (Application.userIdCurrent == null) {
            System.out.println(UNAUTHORIZED_USER);
            return RETURN_ERROR;
        }
        else {
            System.out.println("LIST TASK");
            viewTasks(findAll().get());
            System.out.println("OK");
            return RETURN_OK;
        }
    }

    /**
     * Просмотр списка задач.
     * @param tasks Задачи
     */
    public void viewTasks (final List<Task> tasks) {
        if (tasks == null || tasks.isEmpty()) return;
        int index = 1;
        tasks.sort(Comparator.comparing(Task::getName));
        for (final Task task: tasks) {
            final String login1;
            if (userService.findByUserId(task.getUserid()) == null) {
                login1 = null;
            }
            else {
                login1 = userService.findByUserId(task.getUserid()).get().getLogin();
            }
            System.out.println(index + ". TASKID: " + task.getId() + "; NAME: " + task.getName() + "; DESCRIPTION: "
                    + task.getDescription() + "; PROJECTID: " + task.getProjectId()
                    + "; USER LOGIN: " + login1 + "; DEADLINE: " + task.getDeadline());
            index++;
        }
    }

    /**
     * Добавление задачи пользователю.
     * @param userId Идентификатор пользователя
     * @param taskId Идентификатор задачи
     * @return Задача
     * @throws TaskNotFoundException Задача не найдена
     */
    public Optional<Task> addTaskToUser(final Long userId, final Long taskId) throws TaskNotFoundException {
        final Task task = taskRepository.findById(taskId);
        task.setUserid(userId);
        return Optional.ofNullable(task);
    }

    /**
     * Добавление принадлежности задачи пользователю по идентификатору задачи и логину пользователя.
     * @throws TaskNotFoundException Задача не найдена
     */
    public int addTaskToUser() throws TaskNotFoundException {
        if (Application.userIdCurrent == null) {
            System.out.println(UNAUTHORIZED_USER);
            return RETURN_ERROR;
        }
        else {
            System.out.println("ADD TASK TO USER");
            System.out.println("PLEASE ENTER LOGIN:");
            final User user1 = userService.findByLogin(scanner.nextLine()).get();
            if (user1 == null) {
                System.out.println("LOGIN NOT EXIST!");
            }
            else {
                final Long userId = user1.getUserid();
                if (userId != null) {
                    System.out.println(TASK_ID_ENTER);
                    final Long taskId = control.scannerIdIsLong();
                    if (taskId != null) {
                        findByIdUserId(taskId);
                        addTaskToUser(userId, taskId);
                        System.out.println("OK");
                        return RETURN_OK;
                    }
                    return RETURN_ERROR;
                }
            }
            return RETURN_ERROR;
        }
    }

    /**
     * Удаление принадлежности задачи пользователю по идентификатору задачи.
     * @throws TaskNotFoundException Задача не найдена
     */
    public int removeTaskFromUser() throws TaskNotFoundException {
        if (Application.userIdCurrent == null) {
            System.out.println(UNAUTHORIZED_USER);
            return RETURN_ERROR;
        }
        else {
            System.out.println("REMOVE TASK FROM USER");
            System.out.println(TASK_ID_ENTER);
            final Long taskId = control.scannerIdIsLong();
            if (taskId != null) {
                final Task task = findByIdUserId(taskId).get();
                if (task.getUserid() == null) {
                    System.out.println("TASK NOT HAVE USER.");
                    return RETURN_ERROR;
                }
                if (task.getUserid().equals(Application.userIdCurrent) || userService.findByUserId(Application.userIdCurrent).get().isAdmin()) {
                    task.setUserid(null);
                    System.out.println("ОК");
                    return RETURN_OK;
                }
            }
            return RETURN_ERROR;
        }
    }

    /**
     * Создание начальной базы данных задач.
     */
    public void createBeginTasks() {
        TaskService t = TaskService.getInstance();
        t.create("TEST_TASK_3", "DESC TASK 3", UserService.getInstance().findByLogin("ADMIN").get().getUserid() );
        t.create("TEST_TASK_2", "DESC TASK 2", UserService.getInstance().findByLogin("TEST").get().getUserid());
        t.create("TEST_TASK_1", "DESC TASK 1", UserService.getInstance().findByLogin("TEST").get().getUserid());
    }

    /**
     * Запуск фоновых процессов предупреждений о дедлайнах задач и запуска процедуры удаления просроченных задач.
     */
    public int notifyTaskDeadline() {
        CompletableFuture.runAsync(() -> {
            try {
                List<Task> taskList = findAll().get();
                Thread.sleep(3000);
                while (taskList != null || !taskList.isEmpty()) {
                    if (taskList == null || taskList.isEmpty()) {
                        continue;
                    }
                    for (Task task : taskList) {
                        long interval = ChronoUnit.SECONDS.between(LocalDateTime.now(), task.getDeadline());
                        if (interval < 0) {
                            removeTaskDeadline(task.getId());
                        } else
                        if (interval < 300L) {
                            if (task.getNotifyDeadline() != DEADLINE_5_MINUTES) {
                                System.out.println("DEADLINE TASK " + task.getName() + " ID = " + task.getId() + ": " + DEADLINE_5_MINUTES);
                                task.setNotifyDeadline(DEADLINE_5_MINUTES);
                            }
                        } else if (interval < 900L) {
                            if (task.getNotifyDeadline() != DEADLINE_15_MINUTES) {
                                System.out.println("DEADLINE TASK " + task.getName() + " ID = " + task.getId() + ": " + DEADLINE_15_MINUTES);
                                task.setNotifyDeadline(DEADLINE_15_MINUTES);
                            }

                        } else if (interval < 1800L) {
                            if (task.getNotifyDeadline() != DEADLINE_30_MINUTES) {
                                System.out.println("DEADLINE TASK " + task.getName() + " ID = " + task.getId() + ": " + DEADLINE_30_MINUTES);
                                task.setNotifyDeadline(DEADLINE_30_MINUTES);
                            }

                        } else if (interval < 3600L) {
                            if (task.getNotifyDeadline() != DEADLINE_1_HOURS) {
                                System.out.println("DEADLINE TASK " + task.getName() + " ID = " + task.getId() + ": " + DEADLINE_1_HOURS);
                                task.setNotifyDeadline(DEADLINE_1_HOURS);
                            }

                        } else if (interval < 14400L) {
                            if (task.getNotifyDeadline() != DEADLINE_4_HOURS) {
                                System.out.println("DEADLINE TASK " + task.getName() + " ID = " + task.getId() + ": " + DEADLINE_4_HOURS);
                                task.setNotifyDeadline(DEADLINE_4_HOURS);
                            }
                        }
                    }
                }
            } catch (InterruptedException | TaskNotFoundException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        });
        return RETURN_OK;
    }

    /**
     * Удаление просроченных задач
     * @param id Идентификатор задачи
     * @throws TaskNotFoundException
     */
    public void removeTaskDeadline(Long id) throws TaskNotFoundException {
        removeById(id);
        notifyTaskDeadline();
    }

}
