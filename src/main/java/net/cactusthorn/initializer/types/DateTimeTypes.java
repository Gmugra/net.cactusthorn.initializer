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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.TimeZone;
import java.util.Date;
import java.util.Calendar;

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
		DateTimeFormatter formatter = initProperties.getDateTimeFormatter();
		
		if (Date.class.equals(fieldType) ) {

			return Value.of(empty ? null : getJavaUtilDate(info, propertyValue, formatter) );
		}
		if (LocalDate.class.equals(fieldType) ) {
			
			return Value.of(empty ? null : getLocalDate(info, propertyValue, formatter) );
		}
		if (LocalDateTime.class.equals(fieldType) ) {
			
			return Value.of(empty ? null : getLocalDateTime(info, propertyValue, formatter) );
		}
		if (ZonedDateTime.class.equals(fieldType) ) {
			
			return Value.of(empty ? null : getZonedDateTime(info, propertyValue, formatter) );
		}
		if (Calendar.class.equals(fieldType) ) {
			
			if (empty) { 
				return Value._null();
			}
	
			Calendar calendar = null;
			try {
				ZonedDateTime zdt = getZonedDateTime(info, propertyValue, formatter);
				calendar = Calendar.getInstance(TimeZone.getTimeZone(zdt.getZone()));
				
				Date date = Date.from(zdt.toInstant()); 
				calendar.setTime(date);
			} catch (InitializerException ze ) {
				LocalDateTime ldt = getLocalDateTime(info, propertyValue, formatter);
				Date date = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant()); 
				calendar = Calendar.getInstance();
				calendar.setTime(date);
			}
			
			return Value.of(calendar);
		}
		
		return Value.empty();
	}
	
	private Date getJavaUtilDate(Info info, String propertyValue, DateTimeFormatter formatter ) throws InitializerException {
		return Date.from(getInstant(info, propertyValue, formatter ) );
	}
	
	private ZonedDateTime getZonedDateTime(Info info, String propertyValue, DateTimeFormatter formatter ) throws InitializerException {
		try {
			return ZonedDateTime.parse(propertyValue, formatter);
		} catch (DateTimeParseException e ) {
			throw new InitializerException(info, UNPARSEABLE_DATETIME, e);
		}
	}
	
	private LocalDate getLocalDate(Info info, String propertyValue, DateTimeFormatter formatter ) throws InitializerException {
		try {
			ZonedDateTime zdt = ZonedDateTime.parse(propertyValue, formatter);
			return zdt.withZoneSameInstant(TimeZone.getDefault().toZoneId() ).toLocalDate();
		} catch (DateTimeParseException e ) {
			try {
				return LocalDate.parse(propertyValue, formatter);
			} catch (DateTimeParseException ee ) {
				throw new InitializerException(info, UNPARSEABLE_DATETIME, ee);
			}
		}
	}
	
	private LocalDateTime getLocalDateTime(Info info, String propertyValue, DateTimeFormatter formatter ) throws InitializerException {
		try {
			ZonedDateTime zdt = ZonedDateTime.parse(propertyValue, formatter);
			return zdt.withZoneSameInstant(TimeZone.getDefault().toZoneId() ).toLocalDateTime();
		} catch (DateTimeParseException e ) {
			try {
				return LocalDateTime.parse(propertyValue, formatter);
			} catch (DateTimeParseException ee ) {
				throw new InitializerException(info, UNPARSEABLE_DATETIME, ee);
			}
		}
	}
		
	private Instant getInstant(Info info, String propertyValue, DateTimeFormatter formatter ) throws InitializerException {
		try {			
			return getZonedDateTime(info, propertyValue, formatter).toInstant();
		} catch (InitializerException ze ) {
			return getLocalDateTime(info, propertyValue, formatter).atZone(ZoneId.systemDefault()).toInstant();
		}
	}
}
