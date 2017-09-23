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

import java.util.Map;
import java.util.HashMap;

public class ConfigPropertiesBundle {

	private String name = "";
	private Map<String,String> properties = new HashMap<>();
	
	public ConfigPropertiesBundle(String name) {
		this.name = name;
	}
	
	public ConfigPropertiesBundle(String name, Map<String,String> properties ) {
		
		this.name = name;
		putAll(properties);
	}
	
	public String getName() {
		return name;
	}
	
	public ConfigPropertiesBundle put(String propertyName, Object propertyValue) {
		
		properties.put(propertyName, propertyValue == null ? null : propertyValue.toString());
		
		return this;
	}
	
	public ConfigPropertiesBundle putAll(Map<String,String> propertie) {
		this.properties.putAll(properties);
		return this;
	}
	
	public ConfigPropertiesBundle clearProperties() {
		properties.clear();
		return this;
	}
	
	public boolean contains(String name ) {
		return properties.containsKey(name );
	}

	public String get(String name) {
		return properties.get(name);
	}
}
