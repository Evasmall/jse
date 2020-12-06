package ru.evasmall.tm.repository;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static ru.evasmall.tm.constant.TerminalConst.RETURN_ERROR;
import static ru.evasmall.tm.constant.TerminalConst.RETURN_OK;

public abstract class AbstractRepository<T> {

    private static final Logger logger = LogManager.getLogger(AbstractRepository.class);

    public List<T> objects = new ArrayList<>();

    public ConcurrentMap<String, HashSet<T>> objectsName = new ConcurrentHashMap<>();

    public abstract String getObjectName(final T object);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final XmlMapper xmlMapper = new XmlMapper();

    public List<T> findAll() {
        return objects;
    }

    public synchronized void addObjectToMap(final T object) {
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

    public synchronized void removeObjectFromMap(final T object) {
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

    public T createObject(final T object) {
        objects.add(object);
        addObjectToMap(object);
        return object;
    }

    public synchronized void clearObject() {
        objects.clear();
        objectsName.clear();
    }

    /**
     * Запись всех объектов в файл формата JSON.
     */
    public int writeJson(String fileName) {
        final List<T> objectsJson = findAll();
        if (objectsJson == null || objectsJson.isEmpty()) return RETURN_ERROR;
        try {
            File file = new File(fileName);
            objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            objectMapper.writeValue(file, objectsJson);
        } catch (IOException e) {
            logger.error(e.getMessage() + "ERROR. DATA NOT WRITTEN.");
        }
        return RETURN_OK;
    }

    /**
     * Чтение и перезапись всех объектов из файла формата JSON.
     */
    public int readJson(String fileName, Class clazz) {
        try {
            objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            List<T> objectFromJson = objectMapper.readValue(new File(fileName), TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, clazz));
            clearObject();
            for (T object : objectFromJson) {
                createObject(object);
            }
        } catch (IOException e) {
            logger.error(e.getMessage() + "ERROR. DATA NOT READ.");
            return RETURN_ERROR;
        }
        return RETURN_OK;
    }

    /**
     * Запись всех объектов в файл формата XML.
     */
    public int writeXML(String fileName) {
        final List<T> objectsXML = findAll();
        if (objectsXML == null || objectsXML.isEmpty()) return RETURN_ERROR;
        try {
            File file = new File(fileName);
            xmlMapper.writeValue(file, objectsXML);
        } catch (IOException e) {
            logger.error(e.getMessage() + "ERROR. DATA NOT WRITTEN.");
        }
        return RETURN_OK;
    }

    /**
     * Чтение и перезапись всех объектов из файла формата XML.
     */
    public int readXML(String fileName, Class clazz) {
        try {
            List<T> objectFromXML = xmlMapper.readValue(new File(fileName), TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, clazz));
            clearObject();
            for (T object : objectFromXML) {
                createObject(object);
            }
        } catch (IOException e) {
            logger.error(e.getMessage() + "ERROR. DATA NOT READ.");
            return RETURN_ERROR;
        }
        return RETURN_OK;
    }

}
