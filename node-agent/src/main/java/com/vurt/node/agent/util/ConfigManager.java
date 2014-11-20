package com.vurt.node.agent.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Properties;

/**
 * 平台配置管理器
 * 
 * @author yanyl
 */
public class ConfigManager  {

	private static ConfigManager instance;

	private static Properties properties;
	
	private static boolean initted=false;

	public static ConfigManager getInstance() {
		if (initted&&instance == null) {
			instance = new ConfigManager();
		}
		return instance;
	}
	
	public static void init(File propertiesFile){
		properties=new Properties();
		try {
			properties.load(new FileInputStream(propertiesFile));
			initted=true;
		} catch (FileNotFoundException e) {
			System.out.println("配置文件未找到："+propertiesFile.getAbsolutePath());
		} catch (IOException e) {
			System.out.println("配置文件读取失败："+propertiesFile.getAbsolutePath());
		}
	}
	
	private ConfigManager(){
	}

	/**
	 * 获取配置项值
	 * 
	 * @param name
	 *            配置名称
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
