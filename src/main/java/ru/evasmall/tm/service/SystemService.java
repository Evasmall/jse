package ru.evasmall.tm.service;

import ru.evasmall.tm.Application;

import static ru.evasmall.tm.constant.TerminalConst.RETURN_ERROR;
import static ru.evasmall.tm.constant.TerminalConst.RETURN_OK;

public class SystemService {

    public static void displayWelcome() {
        System.out.println("*** WELCOME TO TASK MANAGER! ***");
        System.out.println("PLEASE ENTER [sign] OR [registration] or [help]:");
    }

    public static int displayHistory() {
        for (String str: Application.history) {
            System.out.println(str);
        }
        return RETURN_OK;
    }

    public static int displayExit() {
        System.out.println("Terminate program. Goodbye!");
        return RETURN_OK;
    }

    public static int displayForAdminOnly() {
        System.out.println("THIS FUNCTIONALITY IS FOR ADMINS ONLY! FAIL.");
        return RETURN_ERROR;
    }

    public static int displayHelp() {
        System.out.println("version - Display program version.");
        System.out.println("about - Display developer info.");
        System.out.println("help - Display list of terminal commands.");
        System.out.println("history - Display history of terminal commands.");
        System.out.println("exit - Terminate console application.");
        System.out.println();
        System.out.println("registration - User registration");
        System.out.println("sign - User sign.");
        System.out.println("user-list - Display list of users sorted by login.");
        System.out.println("user-list-by-fio - Display list of users sorted by lastname, firstname, middlname.");
        System.out.println("user-remove-by-login - Remove user by login (only for ADMIN!).");
        System.out.println("user-update-role - Update user role (only for ADMIN!)");
        System.out.println("user-profile - Display current user session.");
        System.out.println("user-profile-update - Update current user profile.");
        System.out.println("password-change - Change user password.");
        System.out.println("user-exit - Terminate current session.");
        System.out.println();
        System.out.println("project-list - Display list of projects.");
        System.out.println("project-create - Create new project by name.");
        System.out.println("project-clear - Remove all projects.");
        System.out.println("project-view-by-name - View project by name");
        System.out.println("project-view-by-index - View project by index.");
        System.out.println("project-view-by-id - View project by id.");
        System.out.println();
        System.out.println("project-remove-by-id - Remove project by id.");
        System.out.println("project-remove-by-id-with-tasks - Remove project by id with tasks.");
        System.out.println("project-remove-by-index - Remove project by index.");
        System.out.println("project-remove-by-index-with-tasks - Remove project by index with tasks.");
        System.out.println("project-update-by-id - Update project by id.");
        System.out.println("project-update-by-index - Update project by index.");
        System.out.println("project-add-user - Add project for user by login.");
        System.out.println("project-remove-user - Remove project from user.");
        System.out.println();
        System.out.println("task-list - Display list of tasks.");
        System.out.println("task-create - Create new task by name.");
        System.out.println("task-clear - Remove all tasks.");
        System.out.println("task-view-by-name - View task by name");
        System.out.println("task-view-by-index - View task by index.");
        System.out.println("task-view-by-id - View task by id.");
        System.out.println();
        System.out.println("task-remove-by-id - Remove task by id.");
        System.out.println("task-remove-by-index - Remove task by index.");
        System.out.println("task-update-by-id - Update task by id.");
        System.out.println("task-update-by-index - Update task by index.");
        System.out.println("task-add-user - Add task for user by login.");
        System.out.println("task-remove-user - Remove task from user.");
        System.out.println();
        System.out.println("task-list-by-project-id - Display task list by project id.");
        System.out.println("task-add-to_project-by-ids - Add task to project by ids.");
        System.out.println("task-remove-from-project-by-ids - Remove task from project by ids.");
        System.out.println();
        System.out.println("object-json - Write projects, tasks, users in json file.");
        System.out.println("object-xml- Write projects, tasks, users in xml file.");
        System.out.println("read-json - Read projects, tasks, users from json file.");
        System.out.println("read-xml - Read projects, tasks, users from xml file.");

        return RETURN_OK;
    }

    public static int displayVersion() {
        System.out.println("1.0.22");
        return RETURN_OK;
    }

    public static int displayAbout() {
        System.out.println("Evgeniya Smolkina");
        System.out.println("smolkina_ev@nlmk.com");
        return RETURN_OK;
    }

}
