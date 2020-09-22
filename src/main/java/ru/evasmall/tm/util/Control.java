package ru.evasmall.tm.util;

import ru.evasmall.tm.controller.AbstractController;
import ru.evasmall.tm.controller.SystemController;
import ru.evasmall.tm.exeption.IncorrectFormatException;

public class Control extends AbstractController {

    private final SystemController systemController = new SystemController();

    //Функция возврата значения Long
    public Long scannerIdIsLong () throws IncorrectFormatException {
        final String idStr = scanner.nextLine();
        final Long id;
        if (idStr.isEmpty()) {
            throw new IncorrectFormatException("ID IS EMPTY. FAIL.");
        }
        try {
            Long.parseLong(idStr);
            return id = Long.parseLong(idStr);
        } catch (NumberFormatException e) {
            throw new IncorrectFormatException("ID INCORRECT FORMAT. ID MAST BE LONG. FAIL.");
        }
    }

    //Функция возврата значения Integer
    public Integer scannerIndexIsInteger () throws IncorrectFormatException {
        final String idStr = scanner.nextLine();
        final Integer index;
        if (idStr.isEmpty()) {
            throw new IncorrectFormatException("INDEX IS EMPTY. FAIL.");
        }
        try {
            Integer.parseInt(idStr);
            return index = Integer.parseInt(idStr) - 1;
        } catch (NumberFormatException e) {
            throw new IncorrectFormatException("INDEX INCORRECT FORMAT. INDEX MAST BE INTEGER. FAIL.");
        }
    }

}
