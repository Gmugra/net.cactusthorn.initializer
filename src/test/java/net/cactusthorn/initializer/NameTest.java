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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import net.cactusthorn.initializer.Initializer;
import net.cactusthorn.initializer.InitializerException;
import net.cactusthorn.initializer.ConfigPropertiesBundle;
import net.cactusthorn.initializer.annotations.*;

public class NameTest {
	
	Initializer initializer = new Initializer();
	ConfigPropertiesBundle bundle = new ConfigPropertiesBundle("test");
	
	@InitPropertyName("BOOL")
	boolean bool;
	
	@InitPropertyName("BOOL2")
	@InitProperty(InitPropertyPolicy.REQUIRED)
	boolean boolWithPolicy;
	
	@InitProperty
	boolean boolNoName;
	
	@Test(expected = InitializerException.class)
	public void testNameWithPolicy() throws InitializerException {
		
		bundle
			.clearProperties()
			.put("boolWithPolicy", true);
		
		initializer.initialize(bundle, this);
	}
	
	@Test
	public void testName() throws InitializerException {
		
		bundle
			.clearProperties()
			.put("BOOL", true)
			.put("BOOL2", true)
			.put("boolNoName", true);
		
		initializer.initialize(bundle, this);
		
		assertEquals(bool, true);
		assertEquals(boolNoName, true);
	}
	
	@Test
	public void testNoName() throws InitializerException {
		
		bundle
			.clearProperties()
			.put("BOOL2", true);
		
		initializer.initialize(bundle, this);
		
		assertEquals(bool, false);
		assertEquals(boolNoName, false);
	}

}
