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

import java.util.*;

import org.junit.Test;

import static net.cactusthorn.initializer.annotations.InitPropertyPolicy.OPTIONAL;
import net.cactusthorn.initializer.Initializer;
import net.cactusthorn.initializer.InitializerException;
import net.cactusthorn.initializer.InitPropertiesBuilder;
import net.cactusthorn.initializer.annotations.*;

public class DateTimeTest {
	
	Initializer initializer = new Initializer();
	InitPropertiesBuilder pb = new InitPropertiesBuilder();
	
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
		
		new Initializer().initialize(pb.addDateTimeFormatPattern("dd.MM.yyyy").put("date", "21.12.1976").build(), this);
		assertNotNull(date);
	}
	
	@Test(expected = InitializerException.class)
	public void testUtilDateException() {
		
		initializer.initialize(pb.put("date", "21.12.1976").build(), this);
	}
	
	@Test
	public void testCalendar() {
		
		initializer.initialize(pb.put("calendar", "2017-09-17T11:16:50+01:00").build(), this);
		assertNotNull(calendar);
		
		calendar = null;
		initializer.initialize(pb.put("calendar", "2017-09-17T11:16:50").build(), this);
		assertNotNull(calendar);
		
		calendar = null;
		initializer.initialize(pb.put("calendar", "2017-10-05").build(), this);
		assertNotNull(calendar);
		
		calendar = currentCalendar;
		
		initializer.initialize(pb.put("calendar", "").build(), this);
		assertNull(calendar);
	}
	
	
	@Test
	public void testSqlDate() {
		
		initializer.initialize(pb.put("sqldate", "2017-09-17T11:16:50+01:00").build(), this);
		assertNotNull(sqldate);
		
		sqldate = null;
		
		initializer.initialize(pb.put("sqldate", "2017-09-17T11:16:50").build(), this);
		assertNotNull(sqldate);
		
		sqldate = null;
		
		initializer.initialize(pb.put("sqldate", "2017-09-17T11:16:50").build(), this);
		assertNotNull(sqldate);
		
		sqldate = currentSqlDate;
		
		initializer.initialize(pb.put("sqldate", "").build(), this);
		assertNull(sqldate);
	}

	@Test
	public void testUtilDate() {
		
		initializer.initialize(pb.put("date", "2017-09-17T11:16:50+01:00").build(), this);
		assertNotNull(date);
		
		date = null;
		
		initializer.initialize(pb.put("date", "2017-09-17T11:16:50").build(), this);
		assertNotNull(date);
		
		date = null;
		
		initializer.initialize(pb.put("date", "2017-10-05").build(), this);
		assertNotNull(date);
		
		date = currentUtilDate;
		
		initializer.initialize(pb.put("date", "").build(), this);
		assertNull(date);
	}

}
