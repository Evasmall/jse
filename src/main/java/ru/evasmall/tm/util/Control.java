package ru.evasmall.tm.util;

import ru.evasmall.tm.service.AbstractService;

public class Control extends AbstractService {

    //Функция возврата значения Long
    public Long scannerIdIsLong () {
        final String idStr = scanner.nextLine();
        if (idStr.isEmpty()) {
            throw new IllegalArgumentException ("ID IS EMPTY. FAIL.");
        }
        try {
            Long.parseLong(idStr);
            return Long.parseLong(idStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException ("ID INCORRECT FORMAT. ID MAST BE LONG. FAIL.");
        }
    }

    //Функция возврата значения Integer
    public Integer scannerIndexIsInteger () {
        final String idStr = scanner.nextLine();
        if (idStr.isEmpty()) {
            throw new IllegalArgumentException ("INDEX IS EMPTY. FAIL.");
        }
        try {
            Integer.parseInt(idStr);
            return Integer.parseInt(idStr) - 1;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException ("INDEX INCORRECT FORMAT. INDEX MAST BE INTEGER. FAIL.");
        }
    }

}
