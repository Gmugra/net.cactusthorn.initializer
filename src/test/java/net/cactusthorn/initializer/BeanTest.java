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

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import net.cactusthorn.initializer.annotations.*;
import net.cactusthorn.initializer.properties.InitProperties;
import net.cactusthorn.initializer.properties.InitPropertiesBuilder;

import static net.cactusthorn.initializer.annotations.InitPropertyPolicy.*;

@InitProperty(REQUIRED)
public class BeanTest {

	@InitProperty(REQUIRED) 
	static class SubTestBean {
		String name;
		int[] values;
	}

	@InitProperty(REQUIRED) 
	static class TestBean {
		java.util.Date date;
		Map<String, Integer> map;
		@InitBean("sub-test-bean") SubTestBean subTestBean;
	}
	
	@InitBean("test-bean") TestBean testBean;
	
	String simple;
	
	@Test
	public void testBean() throws URISyntaxException, IOException {
		
		Path path = Paths.get(getClass().getClassLoader().getResource("init-bean.properties").toURI());
		
		InitProperties prop = new InitPropertiesBuilder().load(path).trimMultiValues(true).build();
		
		new Initializer().initialize(prop, this);
		
		assertEquals("Super Name", testBean.subTestBean.name);
		assertEquals(9, testBean.subTestBean.values.length);
		assertEquals(3, testBean.map.size());
		assertEquals("SIMPLE", simple);
	}
}
