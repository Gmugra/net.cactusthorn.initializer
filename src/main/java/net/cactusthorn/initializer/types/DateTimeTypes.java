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
package net.cactusthorn.initializer.types;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.HashMap;
import java.util.List;


import net.cactusthorn.initializer.InitializerException;
import net.cactusthorn.initializer.annotations.Info;
import static net.cactusthorn.initializer.InitializerException.StandardError.*;

public class DateTimeTypes implements ITypes {
	
	private static final String DATE_TIME_TZ = "yyyy-MM-dd'T'HH:mm:ssXXX";
	private static final String DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss";
	private static final String DATE = "yyyy-MM-dd";
	
	private Map<String, SimpleDateFormat> formats = new HashMap<>();
	
	public DateTimeTypes() {
		formats.put(DATE_TIME_TZ, new SimpleDateFormat(DATE_TIME_TZ));
		formats.put(DATE_TIME, new SimpleDateFormat(DATE_TIME));
		formats.put(DATE, new SimpleDateFormat(DATE));
	}
	
	@Override
	public DateTimeTypes clone() throws CloneNotSupportedException {
		
		DateTimeTypes cloned = (DateTimeTypes)super.clone();
		
		Map<String, SimpleDateFormat> newFormats = new HashMap<>();
		formats.entrySet().forEach(e -> newFormats.put(e.getKey(), (SimpleDateFormat)e.getValue().clone()));
		cloned.formats = newFormats;
		
		return cloned;
	}
	
	@Override
	public void addDateTimeFormatPattern(String formatPattern) {	
		if (!formats.containsKey(formatPattern) ) {
			formats.put(formatPattern, new SimpleDateFormat(formatPattern));
		}
	}
	
	@Override
	public Value<?> createObject(Class<?> fieldType, Type fieldGenericType, Info info, String propertyValue, List<ITypes> availableTypes) throws InitializerException {
		
		boolean empty = propertyValue.isEmpty();
		
		if (java.util.Date.class.equals(fieldType) ) {

			return Value.of(empty ? null : createDate(info, propertyValue));
		}
		if (java.sql.Date.class.equals(fieldType) ) {
			
			return Value.of(empty ? null : new java.sql.Date(createDate(info, propertyValue).getTime() ) );
		}
		if (java.util.Calendar.class.equals(fieldType) ) {
			
			if (empty) { 
				return Value._null();
			}
			
			java.util.Date date = createDate(info, propertyValue);
			java.util.Calendar calendar = java.util.Calendar.getInstance();
			calendar.setTime(date);
			
			return Value.of(calendar);
		}
		
		return Value.empty();
	}
	
	private java.util.Date createDate(Info info, String configurationValue ) throws InitializerException {
		
		java.util.Date date = null;

		for (SimpleDateFormat f : formats.values() ) {
			try {
				date = f.parse(configurationValue);
				return date;	
			} catch (ParseException pe ) {
				continue;
			}
		}
		
		throw new InitializerException(info, UNPARSEABLE_DATETIME); 
	}
}
