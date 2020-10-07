package ru.evasmall.tm.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public abstract class AbstractRepository<T> {

    public final List<T> objects = new ArrayList<>();

    public final HashMap<String, HashSet<T>> objectsName = new HashMap<>();

    public abstract String getObjectName(final T object);

    public List<T> findAll() {
        return objects;
    }

    public void addObjectToMap(final T object) {
        final String name = getObjectName(object);
        HashSet<T> objectsHashMap = objectsName.get(name);
        if (objectsHashMap != null)
            objectsHashMap.add(object);
        else {
            objectsHashMap = new HashSet<>();
            objectsHashMap.add(object);
            objectsName.put(name, objectsHashMap);
        }
    }

    public void removeObjectFromMap(final T object) {
        final String name = getObjectName(object);
        HashSet<T> objectsHashMap = objectsName.get(name);
        if (objectsHashMap != null)
            objectsHashMap.remove(object);
        if (objectsHashMap.isEmpty())
            objectsName.remove(name);
    }

    public int size() {
        return objects.size();
    }

    public void clear() {
        objects.clear();
        objectsName.clear();
    }

}
