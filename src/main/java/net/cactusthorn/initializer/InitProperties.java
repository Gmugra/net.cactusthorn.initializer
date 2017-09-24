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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
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
	
	public static InitProperties load(Path path) throws IOException {
		Properties utilProperties = new Properties();
		try (BufferedReader buf = Files.newBufferedReader(path ) ) {
			utilProperties.load(buf);
		}
		return from(utilProperties);
	}
	
	public static InitProperties loadFromXML(Path path) throws IOException {
		Properties utilProperties = new Properties();
		try (InputStream buf = Files.newInputStream(path, StandardOpenOption.READ) ) {
			utilProperties.loadFromXML(buf);
		}
		return from(utilProperties);
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
