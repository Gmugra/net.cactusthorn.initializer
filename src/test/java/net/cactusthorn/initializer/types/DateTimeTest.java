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

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Test;

import static net.cactusthorn.initializer.annotations.InitPropertyPolicy.OPTIONAL;
import net.cactusthorn.initializer.Initializer;
import net.cactusthorn.initializer.InitializerException;
import net.cactusthorn.initializer.ConfigPropertiesBundle;
import net.cactusthorn.initializer.annotations.*;

public class DateTimeTest {
	
	Initializer initializer = new Initializer();
	ConfigPropertiesBundle bundle = new ConfigPropertiesBundle("test");
	
	java.util.Calendar currentCalendar = Calendar.getInstance(); 
	java.util.Date currentUtilDate = currentCalendar.getTime();
	java.sql.Date currentSqlDate = new java.sql.Date(currentUtilDate.getTime());
	
	@InitProperty(OPTIONAL)
	java.util.Date date;

	@InitProperty(OPTIONAL)
	java.sql.Date sqldate;
	
	@InitProperty(OPTIONAL)
	java.util.Calendar calendar;
	
	@Test
	public void testCustomFormat() {
		
		Initializer local = 
			new Initializer().addDateTimeFormatPattern("dd.MM.yyyy");
		
		date = null;
		
		bundle.clearProperties().put("date", "21.12.1976");
		local.initialize(bundle, this);
		assertNotNull(date);
	}
	
	@Test(expected = InitializerException.class)
	public void testUtilDateException() {
		
		date = null;
		
		bundle.clearProperties().put("date", "21.12.1976");
		initializer.initialize(bundle, this);
	}
	
	@Test
	public void testCalendar() {
		
		bundle.clearProperties().put("calendar", "2017-09-17T11:16:50+01:00");
		initializer.initialize(bundle, this);
		assertNotNull(calendar);
		
		calendar = null;
		bundle.put("calendar", "2017-09-17T11:16:50");
		initializer.initialize(bundle, this);
		assertNotNull(calendar);
		
		calendar = null;
		bundle.put("calendar", "2017-10-05");
		initializer.initialize(bundle, this);
		assertNotNull(calendar);
		
		calendar = currentCalendar;
		
		bundle.put("calendar", "");
		initializer.initialize(bundle, this);
		assertNull(calendar);
	}
	
	
	@Test
	public void testSqlDate() {
		
		bundle.clearProperties().put("sqldate", "2017-09-17T11:16:50+01:00");
		initializer.initialize(bundle, this);
		assertNotNull(sqldate);
		
		sqldate = null;
		
		bundle.put("sqldate", "2017-09-17T11:16:50");
		initializer.initialize(bundle, this);
		assertNotNull(sqldate);
		
		sqldate = null;
		
		bundle.put("sqldate", "2017-10-05");
		initializer.initialize(bundle, this);
		assertNotNull(sqldate);
		
		sqldate = currentSqlDate;
		
		bundle.put("sqldate", "");
		initializer.initialize(bundle, this);
		assertNull(sqldate);
	}

	@Test
	public void testUtilDate() {
		
		date = null;
		
		bundle.clearProperties().put("date", "2017-09-17T11:16:50+01:00");
		initializer.initialize(bundle, this);
		assertNotNull(date);
		
		date = null;
		
		bundle.put("date", "2017-09-17T11:16:50");
		initializer.initialize(bundle, this);
		assertNotNull(date);
		
		date = null;
		
		bundle.put("date", "2017-10-05");
		initializer.initialize(bundle, this);
		assertNotNull(date);
		
		date = currentUtilDate;
		
		bundle.put("date", "");
		initializer.initialize(bundle, this);
		assertNull(date);
	}

}
