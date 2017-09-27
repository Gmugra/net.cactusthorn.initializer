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

import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class InitPropertiesBuilder {
	
	private static final String DEFAULT_NAME = "default";
	
	private static final Set<String> DEFAULT_PATTERS = 
		new HashSet<>(Arrays.asList("yyyy-MM-dd'T'HH:mm:ssXXX","yyyy-MM-dd'T'HH:mm:ss","yyyy-MM-dd"));
	
	private Set<String> dateTimePatterns = new HashSet<>(DEFAULT_PATTERS );
	
	private char valuesSeparator = ',';
	private char pairSeparator = '=';
	private boolean trimMultiValues;
	
	private String name = DEFAULT_NAME;
	private Map<String,String> properties = new HashMap<>();
	
	public InitPropertiesBuilder setName(String name) {
		this.name = name;
		return this;
	}
	
	public InitPropertiesBuilder clear() {
		
		name = DEFAULT_NAME;
		valuesSeparator = ',';
		pairSeparator = '=';
		trimMultiValues = false;
		
		properties.clear();
		
		for (Iterator<String> it = dateTimePatterns.iterator(); it.hasNext(); ) {
			String pattern = it.next();
			if (DEFAULT_PATTERS.contains(pattern) ) {
				it.remove();
			}
		}
		
		return this;
	}
	
	public InitPropertiesBuilder setValuesSeparator(char valuesSeparator) {
		this.valuesSeparator = valuesSeparator;
		return this;
	}
	
	public InitPropertiesBuilder setPairSeparator(char pairSeparator) {
		this.pairSeparator = pairSeparator;
		return this;
	}
	
	public InitPropertiesBuilder trimMultiValues(boolean trimMultiValues) {
		this.trimMultiValues = trimMultiValues;
		return this;
	}
	
	public InitPropertiesBuilder addDateTimeFormatPattern(String pattern) {
		if (!dateTimePatterns.contains(pattern ) ) {
			dateTimePatterns.add(pattern);
		}
		return this;
	}
	
	public InitPropertiesBuilder putAll(Map<String,String> properties) {
		if (properties != null) {
			properties.entrySet().forEach(e -> this.properties.put(e.getKey(), e.getValue() ) );
		}
		return this;
	}
	
	public InitPropertiesBuilder from(Map<String,String> properties) {
		
		return putAll(properties);
	}
	
	public InitPropertiesBuilder from(Properties properties) {
		
		if (properties == null) {
			return this;
		}
		
		properties.stringPropertyNames().forEach(n -> this.properties.put(n, properties.getProperty(n)));
		return this;
	}
	
	public InitPropertiesBuilder load(Path path) throws IOException {
		Properties utilProperties = new Properties();
		try (BufferedReader buf = Files.newBufferedReader(path ) ) {
			utilProperties.load(buf);
		}
		return from(utilProperties);
	}
	
	public InitPropertiesBuilder loadFromXML(Path path) throws IOException {
		Properties utilProperties = new Properties();
		try (InputStream buf = Files.newInputStream(path, StandardOpenOption.READ) ) {
			utilProperties.loadFromXML(buf);
		}
		return from(utilProperties);
	}
	
	public <T> InitPropertiesBuilder put(String propertyName, T propertyValue) {
		
		if (propertyName == null || propertyValue == null) {
			return this;
		}
		
		properties.put(propertyName, propertyValue.toString());
		return this;
	}
	
	public InitProperties build() {
		
		Splitter splitter = new Splitter(valuesSeparator, pairSeparator, trimMultiValues);
		
		DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
		dateTimePatterns.forEach(p -> builder.appendPattern("[" + p + "]" ) );
		DateTimeFormatter
			dateTimeFormatter = 
				builder
					.parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
					.parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
					.parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
					.parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
					.parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
					.parseDefaulting(ChronoField.MICRO_OF_SECOND, 0)
					.parseDefaulting(ChronoField.MILLI_OF_SECOND, 0)
					.toFormatter();
		
		return 
			new InitProperties(name, properties, splitter, dateTimeFormatter);
	}
}
