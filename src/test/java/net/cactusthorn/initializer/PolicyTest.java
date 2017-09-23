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
import net.cactusthorn.initializer.Initializer;
import net.cactusthorn.initializer.InitializerException;
import net.cactusthorn.initializer.ConfigPropertiesBundle;
import net.cactusthorn.initializer.annotations.*;
import static net.cactusthorn.initializer.annotations.InitPropertyPolicy.*;

public class PolicyTest {

	Initializer initializer = new Initializer();
	ConfigPropertiesBundle bundle = new ConfigPropertiesBundle("test");
	
	@InitProperty(OPTIONAL)
	boolean optional = true;
	
	@InitProperty(REQUIRED)
	boolean required;
	
	@InitProperty
	boolean notEmpty;
	
	@InitProperty(REQUIRED_NOT_EMPTY)
	boolean requiredNotEmpty;
	
	@Test
	public void testOptional() throws InitializerException {
		
		bundle
			.clearProperties()
			.put("required", true)
			.put("requiredNotEmpty", true);
		
		initializer.initialize(bundle, this);
		
		assertEquals(optional, true);
		assertEquals(notEmpty, false);
	}
	
	@Test(expected = net.cactusthorn.initializer.InitializerException.class)
	public void testRequired() throws InitializerException {
		
		bundle
			.clearProperties()
			.put("requiredNotEmpty", true);
	
		initializer.initialize(bundle, this);
	}
	
	@Test(expected = net.cactusthorn.initializer.InitializerException.class)
	public void testNotEmpty() throws InitializerException {
		
		bundle
			.clearProperties()
			.put("required", true)
			.put("notEmpty", null)
			.put("requiredNotEmpty", true);
		
		initializer.initialize(bundle, this);
	}
	
	@Test(expected = net.cactusthorn.initializer.InitializerException.class)
	public void testRequiredNotEmpty() throws InitializerException {
		
		bundle
			.clearProperties()
			.put("required", true)
			.put("requiredNotEmpty", "");
		
		initializer.initialize(bundle, this);
	}
	
}
