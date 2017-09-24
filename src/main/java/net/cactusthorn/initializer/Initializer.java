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
	
	private char valuesSep = ',';
	private char pairSep = '=';
	private boolean trimMultiValues;
	
	private Set<String> additionalDateTimeFormatPatterns = new HashSet<>();
	
	private Map<String, ITypes> types = new HashMap<>();
	
	public Initializer() {
		
		addTypes(new PrimitiveTypes());
		addTypes(new SimpleTypes());
		addTypes(new DateTimeTypes());
		addTypes(new ListSetTypes());
		addTypes(new ArrayTypes());
		addTypes(new MapTypes());
	}
	
	public Initializer addTypes(ITypes types) {
		
		String name = types.getClass().getName();
		if (!this.types.containsKey(name) ) {
			
			additionalDateTimeFormatPatterns.forEach(p -> types.addDateTimeFormatPattern(p));
			types.setValuesSeparator(valuesSep).setPairSeparator(pairSep).trimMultiValues(trimMultiValues);
			this.types.put(name, types);
		}
		return this;
	}
	
	public Initializer addDateTimeFormatPattern(String formatPattern) {
		
		additionalDateTimeFormatPatterns.add(formatPattern);
		return this;
	}
	
	public Initializer setValuesSeparator(char separator) {
		this.valuesSep = separator;
		return this;
	}
	
	public Initializer setPairSeparator(char separator) {
		this.pairSep = separator;
		return this;
	}
	
	public Initializer trimMultiValues(boolean trimMultiValues) {
		this.trimMultiValues = trimMultiValues;
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
				cloned.setValuesSeparator(valuesSep).setPairSeparator(pairSep).trimMultiValues(trimMultiValues);
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
