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

import net.cactusthorn.initializer.annotations.InitEnvVariable;
import net.cactusthorn.initializer.properties.InitPropertiesBuilder;

public class EnvVariableTest {

	@InitEnvVariable("JAVA_HOME")
	String javaHome;
	
	@Test
	public void testJavaHome() {
		
		new Initializer().initialize(new InitPropertiesBuilder().build(), this);
		assertNotNull(javaHome);
	}
}
