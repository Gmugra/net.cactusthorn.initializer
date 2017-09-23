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
	
	private Info(String configBundleName, Class<?> clazz, Field field) {
		
		this.configBundleName = configBundleName;
		this.clazz = clazz;
		this.field = field;
		name = field.getName();
	}
	
	public static Info build(String configBundleName, Class<?> clazz, Field field) throws InitializerException {
		
		Info info = null;
		
		for (Annotation annotation : field.getAnnotations() ) {
			
			Class<? extends Annotation> annotatedType = annotation.annotationType();
			
			try {
				if (InitPropertyName.class.equals(annotatedType ) ) {
					
					if (info == null ) {info = new Info(configBundleName, clazz, field);}
					
					Method method = annotatedType.getDeclaredMethod("value");
					info.name = (String)method.invoke(annotation);
				}
				
				if (InitProperty.class.equals(annotatedType ) ) {
					
					if (info == null ) {info = new Info(configBundleName, clazz, field);}
					
					Method method = annotatedType.getDeclaredMethod("value");
					info.policy = (InitPropertyPolicy)method.invoke(annotation);
				}
			} catch (NoSuchMethodException|SecurityException|IllegalAccessException|IllegalArgumentException|InvocationTargetException e) {
				throw new InitializerException(info, e);
			}
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

	@Override
	public String toString() {
		return "name="+name+", policy="+policy + ", field=" + field.getName() + ", class=" + clazz.getName();
	}
}
