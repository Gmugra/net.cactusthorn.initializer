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

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class InitPropertiesTest {
	
	Map<String,String> prop = new HashMap<>();
	
	@Before
	public void init() {
		prop.put("check", "true");
		prop.put("values", "1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 7406529596973765");
		prop.put("dates", "first=2017-09-17T11:16:50+01:00 , secod=2017-10-05 , third=2017-09-17T11:16:50");
	}

	@Test
	public void testUtilProperties() throws URISyntaxException, IOException {
		
		Path path = Paths.get(getClass().getClassLoader().getResource("init.properties").toURI());
		
		Properties utilProp = new Properties();
		try (BufferedReader buf = Files.newBufferedReader(path ) ) {
			utilProp.load(buf);
		}
		
		InitProperties ip = InitProperties.from(utilProp);	
		assertTrue(ip.contains("dates"));
	}
	
	@Test
	public void testMap() {
		InitProperties ip = InitProperties.from(prop);		
		assertTrue(ip.contains("dates"));
	}
	
	@Test
	public void testClear() {
		InitProperties ip = InitProperties.from(prop).clear();
		assertFalse(ip.contains("dates"));
	}
	
	@Test
	public void testPut() {
		InitProperties ip = InitProperties.from(prop).clear().put("name", false);
		assertFalse(ip.contains("dates"));
		assertTrue(ip.contains("name"));
	}
}
