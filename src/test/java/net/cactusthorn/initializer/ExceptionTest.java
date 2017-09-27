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

import static org.junit.Assert.*;

import java.util.StringTokenizer;

import net.cactusthorn.initializer.Initializer;
import net.cactusthorn.initializer.InitializerException;
import net.cactusthorn.initializer.annotations.InitProperty;
import net.cactusthorn.initializer.properties.InitPropertiesBuilder;

import static net.cactusthorn.initializer.InitializerException.StandardError.*;
import static net.cactusthorn.initializer.annotations.InitPropertyPolicy.*;

import org.junit.Test;

public class ExceptionTest {

	Initializer initializer = new Initializer();
	InitPropertiesBuilder builder = new InitPropertiesBuilder();
	
	@InitProperty
	int _int;
	
	@InitProperty(REQUIRED)
	boolean bool;
	
	@InitProperty(OPTIONAL)
	StringTokenizer st;

	@Test
	public void testUNSUPPORTED_TYPE() {
		
		try {
			initializer.initialize(builder.put("bool", true).put("st", "do it").build(), this);
			fail();
		} catch (InitializerException e ) {
			assertEquals(UNSUPPORTED_TYPE,e.getStandardError());
		}
	}
	
	@Test
	public void testWRONG_VALUE() {
		
		try {
			initializer.initialize(builder.put("_int", 66.66d).put("bool", true).build(), this);
			fail();
		} catch (InitializerException e ) {
			assertEquals(WRONG_VALUE,e.getStandardError());
		}
	}
	
	@Test
	public void testNOT_EMPTY_PROPERTY() {
		
		try {
			initializer.initialize(builder.put("bool", true).put("_int", "").build(), this);
			fail();
		} catch (InitializerException e ) {
			assertEquals(NOT_EMPTY_PROPERTY,e.getStandardError());
		}
	}
	
	@Test
	public void testREQUIRED_PROPERTY() {
		
		try {
			initializer.initialize(builder.clear().put("_int", 1).build(), this);
			fail();
		} catch (InitializerException e ) {
			assertEquals(REQUIRED_PROPERTY,e.getStandardError());
		}
	}

}
