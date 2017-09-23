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

import org.junit.Test;

import static org.junit.Assert.*;
import static net.cactusthorn.initializer.annotations.InitPropertyPolicy.OPTIONAL;
import net.cactusthorn.initializer.Initializer;
import net.cactusthorn.initializer.InitializerException;
import net.cactusthorn.initializer.ConfigPropertiesBundle;
import net.cactusthorn.initializer.annotations.InitProperty;

public class PrimitivesTest {

	Initializer initializer = new Initializer();
	ConfigPropertiesBundle bundle = new ConfigPropertiesBundle("test");
	
	@InitProperty(OPTIONAL)
	boolean bool;
	
	@InitProperty(OPTIONAL)
	char _char = 'A';
	
	@InitProperty(OPTIONAL)
	int integer = 10;
	
	@InitProperty(OPTIONAL)
	byte _byte = (byte)30;
	
	@InitProperty(OPTIONAL)
	long _long = 40L;
	
	@InitProperty(OPTIONAL)
	short _short = (short)50;
	
	@InitProperty(OPTIONAL)
	float _float = -100.99f;
	
	@InitProperty(OPTIONAL)
	double _double = -200.43d;
	
	@Test(expected = InitializerException.class)
	public void testDoubleException() {
		
		bundle
			.clearProperties()
			.put("_float", "xx");
		
		initializer.initialize(bundle, this);
	}
	
	@Test
	public void testDouble() {
		
		bundle.clearProperties();
		initializer.initialize(bundle, this);
		assertEquals(_double, -200.43d, 0.0001);
		
		bundle.put("_double", 66.66d);
		initializer.initialize(bundle, this);
		assertEquals(_double, 66.66d, 0.0001);
		
		bundle.put("_double", null);
		initializer.initialize(bundle, this);
		assertEquals(_double, 0.0d, 0.0001);
		
		_double = 44.99d;
		
		bundle.put("_double", ""); 
		initializer.initialize(bundle, this);
		assertEquals(_double, 0.0d, 0.0001);
	}
	
	@Test(expected = InitializerException.class)
	public void testFloatException() {
		
		bundle
			.clearProperties()
			.put("_float", "qbc");
		
		initializer.initialize(bundle, this);
	}
	
	@Test
	public void testFloat() {
		
		bundle.clearProperties();
		initializer.initialize(bundle, this);
		assertEquals(_float, -100.99f, 0.0001);
		
		bundle.put("_float", 55.55f);
		initializer.initialize(bundle, this);
		assertEquals(_float, 55.55f, 0.0001);
		
		bundle.put("_float", null);
		initializer.initialize(bundle, this);
		assertEquals(_float, 0.0f, 0.0001);
		
		_float = 10.10f;
		
		bundle.put("_float", "");
		initializer.initialize(bundle, this);
		assertEquals(_float, 0.0f, 0.0001);
	}
	
	@Test(expected = InitializerException.class)
	public void testShortException() {
		
		bundle
			.clearProperties()
			.put("_short", -35000);
		
		initializer.initialize(bundle, this);
	}
	
	@Test
	public void testShort() {
		
		bundle.clearProperties();
		initializer.initialize(bundle, this);
		assertEquals(_short, (short)50);
		
		bundle.put("_short", (short)55);
		initializer.initialize(bundle, this);
		assertEquals(_short, (short)55);
		
		bundle.put("_short", null);
		initializer.initialize(bundle, this);
		assertEquals(_short, (short)0);
		
		_short = 23;
		
		bundle.put("_short", "");
		initializer.initialize(bundle, this);
		assertEquals(_short, (short)0);
	}
	
	@Test(expected = InitializerException.class)
	public void testLongException() {
		
		bundle
			.clearProperties()
			.put("_long", "true");
		
		initializer.initialize(bundle, this);
	}
	
	@Test
	public void testLong() {
		
		bundle.clearProperties();
		initializer.initialize(bundle, this);
		assertEquals(_long, 40L);
		
		bundle.put("_long", 44L);
		initializer.initialize(bundle, this);
		assertEquals(_long, 44L);
		
		bundle.put("_long", null);
		initializer.initialize(bundle, this);
		assertEquals(_long, 0L);
		
		_long = 10L;
		
		bundle.put("_long", "");
		initializer.initialize(bundle, this);
		assertEquals(_long, 0L);
	}
	
	@Test(expected = InitializerException.class)
	public void testByteException() {
		
		bundle
			.clearProperties()
			.put("_byte", 200);
		
		initializer.initialize(bundle, this);
	}
	
	@Test
	public void testByte() {
		
		bundle.clearProperties();
		initializer.initialize(bundle, this);
		assertEquals(_byte, (byte)30);
		
		bundle.put("_byte", (byte)33);
		initializer.initialize(bundle, this);
		assertEquals(_byte, (byte)33);
		
		bundle.put("_byte", null);
		initializer.initialize(bundle, this);
		assertEquals(_byte, (byte)0);
		
		_byte = 5;
		
		bundle.put("_byte", "");
		initializer.initialize(bundle, this);
		assertEquals(_byte, (byte)0);
	}
	
	@Test(expected = InitializerException.class)
	public void testIntException() {
		
		bundle
			.clearProperties()
			.put("integer", "abc");
		
		initializer.initialize(bundle, this);
	}
	
	@Test
	public void testInt() {
		
		bundle.clearProperties();
		initializer.initialize(bundle, this);
		assertEquals(integer, 10);
		
		bundle.put("integer", 20);
		initializer.initialize(bundle, this);
		assertEquals(integer, 20);
		
		bundle.put("integer", null);
		initializer.initialize(bundle, this);
		assertEquals(integer, 0);
		
		integer = 11;
		
		bundle.put("integer", "");
		initializer.initialize(bundle, this);
		assertEquals(integer, 0);
	}
	
	@Test
	public void testBoolean() {
		
		bundle.clearProperties();
		initializer.initialize(bundle, this);
		assertEquals(bool, false);
		
		bundle.put("bool", "true");
		initializer.initialize(bundle, this);
		assertEquals(bool, true);
		
		bundle.put("bool", null);
		initializer.initialize(bundle, this);
		assertEquals(bool, false);
		
		bool = true;
		
		bundle.put("bool", "");
		initializer.initialize(bundle, this);
		assertEquals(bool, false);
		
		bool = true;
		
		bundle.put("bool", "dcdcdcdc");
		initializer.initialize(bundle, this);
		assertEquals(bool, false);
	}
	
	@Test
	public void testChar() {
		
		bundle.clearProperties();
		initializer.initialize(bundle, this);
		assertEquals(_char, 'A');
		
		bundle.put("_char", "");
		initializer.initialize(bundle, this);
		assertEquals(_char, Character.MIN_VALUE);
		
		_char = 'B';
		
		bundle.put("_char", null);
		initializer.initialize(bundle, this);
		assertEquals(_char, Character.MIN_VALUE);
		
		bundle.put("_char", "Boom");
		initializer.initialize(bundle, this);
		assertEquals(_char, 'B');
		
		bundle.put("_char", "C");
		initializer.initialize(bundle, this);
		assertEquals(_char, 'C');
	}
	
}
