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
package net.cactusthorn.initializer;

import java.util.*;
import java.lang.reflect.*;

import net.cactusthorn.initializer.annotations.*;
import net.cactusthorn.initializer.types.*;
import static net.cactusthorn.initializer.InitializerException.StandardError;
import static net.cactusthorn.initializer.annotations.InitPropertyPolicy.*;

public final class Initializer {
	
	private Map<String, ITypes> types = new HashMap<>();
	
	public Initializer() {
		
		addTypes(PrimitiveTypes.class);
		addTypes(SimpleTypes.class);
		addTypes(DateTimeTypes.class);
		addTypes(ListSetTypes.class);
		addTypes(ArrayTypes.class);
		addTypes(MapTypes.class);
	}
	
	/*
	 * Note: do not support not static inner classes
	 */
	public Initializer addTypes(Class<? extends ITypes> types) {
		
		String name = types.getName();
		if (!this.types.containsKey(name) ) {
		
			Constructor<? extends ITypes> constructor = null;
			try {
				constructor = types.getDeclaredConstructor();
			} catch (NoSuchMethodException|SecurityException e) {
				throw new InitializerException(null, e);
			}
			
			ITypes $types = null;
			boolean isAccessible = constructor.isAccessible();
			constructor.setAccessible(true);
			try {
				$types = constructor.newInstance();
				constructor.setAccessible(isAccessible);
			} catch (InvocationTargetException|IllegalAccessException|InstantiationException e) {
				constructor.setAccessible(isAccessible);
				throw new InitializerException(null, e);
			}
			
			this.types.put(name, $types);
		}
		return this;
	}
	
	public Initializer addTypes(ITypes types) throws CloneNotSupportedException {
		
		String name = types.getClass().getName();
		if (!this.types.containsKey(name) ) {
			
			ITypes cloned = types.clone();	
			this.types.put(name, cloned);
		}
		return this;
	}

	public void initialize(InitProperties configBundle, Collection<?> collection) {
		
		if (collection == null || collection.isEmpty() ) {
			return;
		}
		
		initialize(configBundle, collection.toArray() );
	}
	
	public void initialize(InitProperties configBundle, Object... objects) {
		
		if (objects == null || objects.length == 0 ) {
			return;
		}
		
		for (Object object : objects) {
			initialize(types.values(), configBundle, object);
		}
	}
	
	private void initialize(Collection<ITypes> availableTypes, InitProperties initProperties, Object object) {
		
		Class<?> clazz = object.getClass();
		
		for (Field field : clazz.getDeclaredFields() ) {
			
			Info info = Info.build(initProperties.getName(), clazz, field);
				
			if (info == null) {
				
				//not annotated field
				continue;
			}
	
			if (info.isBean() ) {
				
				InitPropertyPolicy policy = info.getPolicy();
				boolean contains = initProperties.containsBeanInitProperties(info.getName() );
				if (!contains && (policy == REQUIRED || policy == REQUIRED_NOT_EMPTY ) ){
					
					throw new InitializerException(info, StandardError.REQUIRED_PROPERTY);
				} else if (!contains ) {
					
					setBean(info, field, object, null);					
					continue;
				}
				
				Object bean = createBean(info,field);
				initialize(availableTypes, initProperties.getBeanInitProperties(info.getName() ), bean); 
				setBean(info, field, object, bean);		
				continue;
			}
			
			String propertyValue = getPropertyValue(info, initProperties);
			
			if(propertyValue == null ) {
				
				//Property for the field is not present and it is permissible by policy, so -> do nothing
				continue;
			}
			
			if (!availableTypes.stream()
					.anyMatch(t -> t.setObject(object, field, info, propertyValue, initProperties, availableTypes) ) ) {
				
				throw new InitializerException(info, StandardError.UNSUPPORTED_TYPE);
			}
		}
		
	}
	
	private Object createBean(Info info, Field field) {
		
		Constructor<?> beanConstructor = null;
		try {
			beanConstructor = (Constructor<?>)field.getType().getDeclaredConstructor();
		} catch (NoSuchMethodException|SecurityException e) {
			throw new InitializerException(info, e);
		}
		
		Object bean = null;
		boolean isAccessible = beanConstructor.isAccessible();
		beanConstructor.setAccessible(true);
		try {
			bean = beanConstructor.newInstance();
			beanConstructor.setAccessible(isAccessible);
		} catch (InvocationTargetException|IllegalAccessException|InstantiationException e) {
			beanConstructor.setAccessible(isAccessible);
			throw new InitializerException(info, e);
		}
		
		return bean;
	}
	
	private void setBean(Info info, Field field, Object mainObject, Object bean ) {
		
		boolean accessible = field.isAccessible();
		field.setAccessible(true);
		try {
			field.set(mainObject, bean);
			field.setAccessible(accessible);
		} catch (IllegalArgumentException|IllegalAccessException e) {
			field.setAccessible(accessible);
			throw new InitializerException(info, e);
		}
	}
	
	private String checkValue(Info info, boolean exists, String value) {
		
		InitPropertyPolicy policy = info.getPolicy();
		
		String resultValue = value == null ? "" : value;
		
		if (!exists && (policy == REQUIRED || policy == REQUIRED_NOT_EMPTY ) ){
			throw new InitializerException(info, StandardError.REQUIRED_PROPERTY);
		} else if (!exists ) {
			return null;
		} else if (resultValue.isEmpty() && (policy == NOT_EMPTY || policy == REQUIRED_NOT_EMPTY ) ) {
			throw new InitializerException(info, StandardError.NOT_EMPTY_PROPERTY);
		} 
		return resultValue;
	}
	
	private String getPropertyValue(Info info, InitProperties initProperties) {
		
		if (info.isEnvVariable() ) {
			
			String env = System.getenv(info.getName() );
			return checkValue(info, env != null, env);
		}
		
		return checkValue(info, initProperties.contains(info.getName()), initProperties.get(info.getName()) );
	}

}
