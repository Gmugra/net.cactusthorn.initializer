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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.*;

import java.util.StringTokenizer;

import net.cactusthorn.initializer.Initializer;
import net.cactusthorn.initializer.InitializerException;
import net.cactusthorn.initializer.annotations.InitProperty;
import net.cactusthorn.initializer.properties.InitPropertiesBuilder;

import static net.cactusthorn.initializer.annotations.InitPropertyPolicy.*;

public class ExceptionTest {

	Initializer initializer = new Initializer();
	InitPropertiesBuilder builder = new InitPropertiesBuilder();
	
	@InitProperty
	int _int;
	
	@InitProperty(REQUIRED)
	boolean bool;
	
	@InitProperty(OPTIONAL)
	StringTokenizer st;
	
	@InitProperty(OPTIONAL)
	int[] array;
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	public void testUNSUPPORTED_TYPE() {
		
		expectedException.expect(InitializerException.class);
		expectedException.expectMessage(containsString("has unsupported type"));
		
		initializer.initialize(builder.put("bool", true).put("st", "do it").build(), this);
	}
	
	@Test
	public void testWRONG_MULTI_VALUE() {
		
		expectedException.expect(InitializerException.class);
		expectedException.expectMessage(containsString("contain wrong value at position"));
		
		initializer.initialize(builder.put("array", "1,a,3").put("bool", true).build(), this);
	}
	
	@Test
	public void testWRONG_VALUE() {
		
		expectedException.expect(InitializerException.class);
		expectedException.expectMessage(containsString("contain wrong value for the field"));
		
		initializer.initialize(builder.put("_int", 66.66d).put("bool", true).build(), this);
	}
	
	@Test
	public void testNOT_EMPTY_PROPERTY() {
		
		expectedException.expect(InitializerException.class);
		expectedException.expectMessage(containsString("with not empty value"));
		
		initializer.initialize(builder.put("bool", true).put("_int", "").build(), this);
	}
	
	@Test
	public void testREQUIRED_PROPERTY() {
		
		expectedException.expect(InitializerException.class);
		expectedException.expectMessage(containsString("require property"));
		
		initializer.initialize(builder.clear().put("_int", 1).build(), this);
	}

}
