package com.vurt.node.agent.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Properties;

/**
 * 平台配置管理器
 *
 * @author yanyl
 */
public class ConfigManager {

    private static ConfigManager instance;

    private static Properties properties;

    private static File propertiesFile;

    private static boolean initted = false;

    public static ConfigManager getInstance() {
        if (initted && instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    public static void init(File propertiesFile) {
        ConfigManager.propertiesFile = propertiesFile;
        properties = new Properties();
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(propertiesFile);
            properties.load(inputStream);
            initted = true;
        } catch (FileNotFoundException e) {
            System.out.println("配置文件未找到：" + propertiesFile.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("配置文件读取失败：" + propertiesFile.getAbsolutePath());
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    private ConfigManager() {
    }

    /**
     * 获取配置项值
     *
     * @param name 配置名称
     * @return 如果知道名称的配置不存在则返回null
     */
    public String getConfig(String name) {
        if (properties != null) {
            String value = properties.getProperty(name);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    /**
     * 修改配置文件的值
     *
     * @param name  属性名
     * @param value 新值
     * @throws IOException 写入属性文件时可能发生的IO异常
     */
    public void setConfig(String name, String value) throws IOException {
        if (properties != null) {
            String oldValue = properties.getProperty(name);
            if (!StringUtils.equals(value, oldValue)) {
                properties.put(name, value);
                FileOutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(propertiesFile);
                    properties.store(outputStream, "");
                    outputStream.flush();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    IOUtils.closeQuietly(outputStream);
                }

            }
        }
    }

    /**
     * 读取所有的配置项的Key
     */
    @SuppressWarnings("unchecked")
    public Enumeration<String> getConfigNames() {
        if (properties != null) {
            return (Enumeration<String>) properties.propertyNames();
        }
        return emptyEnumeration();
    }

    @SuppressWarnings("unchecked")
    private static <T> Enumeration<T> emptyEnumeration() {
        return (Enumeration<T>) EmptyEnumeration.EMPTY_ENUMERATION;
    }

    private static class EmptyEnumeration<E> implements Enumeration<E> {
        static final EmptyEnumeration<Object> EMPTY_ENUMERATION = new EmptyEnumeration<Object>();

        public boolean hasMoreElements() {
            return false;
        }

        public E nextElement() {
            throw new NoSuchElementException();
        }
    }
}
