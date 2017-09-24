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
import java.util.Properties;

public class InitProperties {

	private String name = "default";
	private Map<String,String> properties = new HashMap<>();
	
	public InitProperties() {
	}
	
	public static InitProperties from(Map<String,String> properties) {
		
		return new InitProperties().putAll(properties);
	}
	
	public static InitProperties from(Properties properties) {
		
		InitProperties $new = new InitProperties();
		if (properties == null) {
			return $new;
		}
		
		properties.stringPropertyNames().forEach(n -> $new.put(n, properties.getProperty(n)));
		return $new;
	}
	
	public InitProperties setName(String name) {
		this.name = name;
		return this;
	}
	
	public String getName() {
		return name;
	}
	
	public <T> InitProperties put(String propertyName, T propertyValue) {
		
		properties.put(propertyName, propertyValue == null ? null : propertyValue.toString());
		return this;
	}
	
	public InitProperties putAll(Map<String,String> properties) {
		if (properties != null) {
			this.properties.putAll(properties);
		}
		return this;
	}
	
	public InitProperties clear() {
		properties.clear();
		return this;
	}
	
	public boolean contains(String name ) {
		return properties.containsKey(name );
	}

	public String get(String propertyName) {
		return properties.get(propertyName);
	}
}
