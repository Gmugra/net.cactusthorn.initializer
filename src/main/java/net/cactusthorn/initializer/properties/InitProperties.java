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
package net.cactusthorn.initializer.properties;

import java.util.Map;
import java.util.Properties;
import java.util.HashMap;
import java.io.IOException;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;

public class InitProperties {
	
	private String name;
	private Map<String,String> properties = new HashMap<>();
	private Map<String,InitProperties> beanProperties = new HashMap<>();
	private Splitter splitter;
	private DateTimeFormatter dateTimeFormatter;
	
	InitProperties(String name, Map<String,String> properties, Splitter splitter, DateTimeFormatter dateTimeFormatter) {
		this.name = name;
		this.splitter = splitter;
		this.dateTimeFormatter = dateTimeFormatter;
		
		properties.entrySet().forEach(e -> put(e.getKey(),e.getValue() ) );
	}
	
	private InitProperties(String name, Splitter splitter, DateTimeFormatter dateTimeFormatter) {
		this.name = name;
		this.splitter = splitter;
		this.dateTimeFormatter = dateTimeFormatter;
	}
	
	public Splitter getSplitter() {
		return splitter;
	}
	
	public DateTimeFormatter getDateTimeFormatter() {
		return dateTimeFormatter;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean contains(String name ) {
		return properties.containsKey(name );
	}

	public String get(String propertyName) {
		return properties.get(propertyName);
	}
	
	public InitProperties getBeanInitProperties(String beanName ) {
		return beanProperties.get(beanName);
	}
	
	public boolean containsBeanInitProperties(String beanName ) {
		return beanProperties.containsKey(beanName );
	}
	
	private void put(String propertyName, String propertyValue) {
		
		int dotIndex = propertyName.indexOf('.');
		if (dotIndex != -1 ) {
			
			String beanName = propertyName.substring(0,dotIndex);
			if (!beanProperties.containsKey(beanName)) {
				beanProperties.put(beanName, 
					new InitProperties(beanName, splitter, dateTimeFormatter) );
			}
			beanProperties
				.get(beanName)
				.put(propertyName.substring(dotIndex+1), propertyValue );
		}
		
		properties.put(propertyName, propertyValue);
	}
	
	public static InitProperties load(Path path) throws IOException {
		return new InitPropertiesBuilder().load(path).build();
	}
	
	public static InitProperties loadFromXML(Path path) throws IOException {
		return new InitPropertiesBuilder().loadFromXML(path).build();
	}
	
	public static InitProperties from(Properties properties) {
		return new InitPropertiesBuilder().from(properties).build();
	}
	
	public static InitProperties from(Map<String,String> properties) {
		return new InitPropertiesBuilder().from(properties).build();
	}
	
}
