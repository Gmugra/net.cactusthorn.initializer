/*******************************************************************************
 * Copyright (C) 2017, Alexei Khatskevich
 * All rights reserved.
 * 
 * Licensed under the BSD 2-clause (Simplified) License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://opensource.org/licenses/BSD-2-Clause
 ******************************************************************************/
package net.cactusthorn.initializer.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.cactusthorn.initializer.InitializerException;

public final class Info {

	private String configBundleName;
	private Class<?> clazz;
	private Field field;
	private InitPropertyPolicy policy = InitPropertyPolicy.NOT_EMPTY;
	private String name;
	private boolean isEnvVariable;
	private boolean isBean;
	
	private Info(String configBundleName, Class<?> clazz) {
		this.configBundleName = configBundleName;
		this.clazz = clazz;
	}
	
	private Info(String configBundleName, Class<?> clazz, Field field) {
		this(configBundleName, clazz);
		this.field = field;
		name = field.getName();
	}
	
	public static Info build(String configBundleName, Class<?> clazz) throws InitializerException {
		
		Info info = null;
		
		Annotation annotation = clazz.getAnnotation(InitProperty.class);
		if (annotation != null ) {
			info = new Info(configBundleName, clazz);
			try {
				Method method = annotation.annotationType().getDeclaredMethod("value");
				info.policy = (InitPropertyPolicy)method.invoke(annotation);
			} catch (NoSuchMethodException|IllegalAccessException|IllegalArgumentException|InvocationTargetException e) {
				throw new InitializerException(info, e);
			}
		}
		
		return info;
	}
	
	public static Info build(String configBundleName, Class<?> clazz, Info classInfo, Field field) throws InitializerException {
		
		Info info = null;
		
		Annotation nameAnnotation = field.getAnnotation(InitPropertyName.class);
		Annotation beanAnnotation = field.getAnnotation(InitBean.class);
		Annotation envAnnotation = field.getAnnotation(InitEnvVariable.class);
		Annotation initAnnotation = field.getAnnotation(InitProperty.class);
		
		try {
			
			if (nameAnnotation != null ) {
				
				info = new Info(configBundleName, clazz, field);
				Method method = nameAnnotation.annotationType().getDeclaredMethod("value");
				info.name = (String)method.invoke(nameAnnotation);
			} else if (beanAnnotation != null) {
				
				info = new Info(configBundleName, clazz, field);
				Method method = beanAnnotation.annotationType().getDeclaredMethod("value");
				info.name = (String)method.invoke(beanAnnotation);
				info.isBean = true;
			} else if (envAnnotation != null) {
				
				info = new Info(configBundleName, clazz, field);
				Method method = envAnnotation.annotationType().getDeclaredMethod("value");
				info.name = (String)method.invoke(envAnnotation);
				info.isEnvVariable = true;
			}
		
			if (initAnnotation != null ) {
				
				if (info == null ) {info = new Info(configBundleName, clazz, field);}
				Method method = initAnnotation.annotationType().getDeclaredMethod("value");
				info.policy = (InitPropertyPolicy)method.invoke(initAnnotation);
			} else if (classInfo != null ) {
				
				if (info == null ) {info = new Info(configBundleName, clazz, field);}
				info.policy = classInfo.getPolicy();
			}
			
		} catch (NoSuchMethodException|IllegalAccessException|IllegalArgumentException|InvocationTargetException e) {
			throw new InitializerException(info, e);
		}
		
		return info;
	}
	
	public Class<?> getFieldClass() {
		return clazz;
	}

	public Field getField() {
		return field;
	}

	public InitPropertyPolicy getPolicy() {
		return policy;
	}

	public String getName() {
		return name;
	}
	
	public String getConfigBundleName() {
		return configBundleName;
	}
	
	public boolean isEnvVariable() {
		return isEnvVariable;
	}
	
	public boolean isBean() {
		return isBean;
	}

	@Override
	public String toString() {
		return "name=" + name
				+ ", policy=" + policy
				+ ", field=" + field.getName()
				+ ", class=" + clazz.getName()
				+ ", isEnvVariable=" + isEnvVariable
				+ ", isBean=" + isBean;
	}
}
