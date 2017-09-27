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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;

import net.cactusthorn.initializer.InitializerException;
import net.cactusthorn.initializer.annotations.Info;
import net.cactusthorn.initializer.properties.InitProperties;

import static net.cactusthorn.initializer.InitializerException.StandardError.*;

public class DateTimeTypes implements ITypes {
	
	@Override
	public DateTimeTypes clone() throws CloneNotSupportedException {
		
		return (DateTimeTypes)super.clone();
	}
	
	@Override
	public Value<?> createObject(
		Class<?> fieldType, 
		Type fieldGenericType,
		Info info, 
		String propertyValue, 
		InitProperties initProperties, 
		Collection<ITypes> availableTypes) throws InitializerException {
		
		boolean empty = propertyValue.isEmpty();
		
		if (java.util.Date.class.equals(fieldType) ) {

			DateTimeFormatter formatter = initProperties.getDateTimeFormatter();
			
			return Value.of(empty ? null : getJavaUtilDate(info, propertyValue, formatter) );
		}
		if (java.sql.Date.class.equals(fieldType) ) {
			
			DateTimeFormatter formatter = initProperties.getDateTimeFormatter();
			
			return Value.of(empty ? null : 
				new java.sql.Date(getJavaUtilDate(info, propertyValue, formatter).getTime() ) );
		}
		if (java.util.Calendar.class.equals(fieldType) ) {
			
			if (empty) { 
				return Value._null();
			}
			
			DateTimeFormatter formatter = initProperties.getDateTimeFormatter();
			
			java.util.Calendar calendar = java.util.Calendar.getInstance();
			calendar.setTime(getJavaUtilDate(info, propertyValue, formatter) );
			
			return Value.of(calendar);
		}
		
		return Value.empty();
	}
	
	private java.util.Date getJavaUtilDate(Info info, String propertyValue, DateTimeFormatter formatter ) throws InitializerException {
		return java.sql.Date.from(getInstant(info, propertyValue, formatter ) );
	}
		
	private Instant getInstant(Info info, String propertyValue, DateTimeFormatter formatter ) throws InitializerException {
		
		try {
			
			return ZonedDateTime.parse(propertyValue, formatter).toInstant();
		} catch (DateTimeParseException ze ) {
			
			try {
				
				return LocalDateTime.parse(propertyValue, formatter).atZone(ZoneId.systemDefault()).toInstant();
			} catch (DateTimeParseException le ) {
				
				throw new InitializerException(info, UNPARSEABLE_DATETIME); 
			}
		}
	}
}
