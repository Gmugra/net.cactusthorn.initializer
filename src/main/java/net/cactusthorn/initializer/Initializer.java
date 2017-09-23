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
	
	private Set<String> additionalDateTimeFormatPatterns = new HashSet<>();
	
	private Map<String, ITypes> types = new HashMap<>();
	
	public Initializer() {
		types.put(PrimitiveTypes.class.getName(), new PrimitiveTypes());
		types.put(SimpleTypes.class.getName(), new SimpleTypes());
		types.put(DateTimeTypes.class.getName(), new DateTimeTypes());
		types.put(ListSetTypes.class.getName(), new ListSetTypes());
		types.put(ArrayTypes.class.getName(), new ArrayTypes());
		types.put(MapTypes.class.getName(), new MapTypes());
	}
	
	public Initializer addInitializer(ITypes initializer) {
		
		String name = initializer.getClass().getName();
		if (!types.containsKey(name) ) {
			types.put(name, initializer);
		}
		return this;
	}
	
	public Initializer addDateTimeFormatPattern(String formatPattern) {
		
		additionalDateTimeFormatPatterns.add(formatPattern);
		return this;
	}
	
	public void initialize(Map<String,String> configMap, Collection<?> collection) throws InitializerException {
		
		ConfigPropertiesBundle configBundle = new ConfigPropertiesBundle("default");
		if (configMap != null) {
			configBundle.putAll(configMap);
		}
		initialize(configBundle, collection);
	}
	
	public void initialize(Map<String,String> configMap, Object... objects) throws InitializerException {
		
		ConfigPropertiesBundle configBundle = new ConfigPropertiesBundle("default");
		if (configMap != null) {
			configBundle.putAll(configMap);
		}
		initialize(configBundle, objects);
	}

	public void initialize(ConfigPropertiesBundle configBundle, Collection<?> collection) throws InitializerException {
		
		if (collection == null || collection.isEmpty() ) {
			return;
		}
		
		//to be thread safe
		List<ITypes> availableTypes = cloneTypes();
		
		collection.forEach(object -> initialize(availableTypes, configBundle, object ) );
	}
	
	public void initialize(ConfigPropertiesBundle configBundle, Object... objects) throws InitializerException {
		
		if (objects == null || objects.length == 0 ) {
			return;
		}
		
		//to be thread safe
		List<ITypes> availableInitializers = cloneTypes();
		
		for (Object object : objects) {
			initialize(availableInitializers, configBundle, object);
		}
	}
	
	private List<ITypes> cloneTypes() {
		
		List<ITypes> clonedTypes = new ArrayList<>();
		for (ITypes type : types.values() ) {
			try {
				ITypes cloned = type.clone();
				additionalDateTimeFormatPatterns.forEach(p -> cloned.addDateTimeFormatPattern(p));
				clonedTypes.add(cloned);
			} catch (CloneNotSupportedException e) {
				// ???
			}
		}
		return clonedTypes;
	}
	
	private void initialize(List<ITypes> availableTypes, ConfigPropertiesBundle configBundle, Object object) throws InitializerException {
		
		Class<?> clazz = object.getClass();
		
		for (Field field : clazz.getDeclaredFields() ) {
			
			Info info = Info.build(configBundle.getName(), clazz, field);
				
			if (info == null) {
				
				//not annotated field
				
				continue;
			}
			
			String propertyValue = getPropertyValue(info, configBundle);
			
			if(propertyValue == null ) {
				
				//Property for the field is not present and it is permissible by policy, so -> do nothing
				
				continue;
			}
			
			if (!availableTypes.stream().anyMatch(t -> t.setObject(object, field, info, propertyValue, availableTypes) ) ) {
				
				throw new InitializerException(info, StandardError.UNSUPPORTED_TYPE);
			}
		}
		
	}
	
	private String getPropertyValue(Info info, ConfigPropertiesBundle configBundle) throws InitializerException {
		
		InitPropertyPolicy policy = info.getPolicy();
		
		if (!configBundle.contains(info.getName()) ) {
			
			if (policy == REQUIRED || policy == REQUIRED_NOT_EMPTY ) {
				throw new InitializerException(info, StandardError.REQUIRED_PROPERTY);
			}
			
			return null;
		}
		
		//Note: Normally, configuration data loaded from file(s), so "null" value is not usual case.
		String value = configBundle.get(info.getName());
		if (value == null) {value = "";}
		
		if (value.isEmpty() && (policy == NOT_EMPTY || policy == REQUIRED_NOT_EMPTY ) ) {
			
			throw new InitializerException(info, StandardError.NOT_EMPTY_PROPERTY);
		}
		
		return value;
	}

}
