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
import net.cactusthorn.initializer.InitPropertiesBuilder;
import net.cactusthorn.initializer.annotations.InitProperty;

public class PrimitivesTest {

	Initializer initializer = new Initializer();
	InitPropertiesBuilder builder = new InitPropertiesBuilder();
	
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
		
		initializer.initialize(builder.put("_float", "xx").build(), this);
	}
	
	@Test
	public void testDouble() {
		
		initializer.initialize(builder.build(), this);
		assertEquals(_double, -200.43d, 0.0001);
		
		initializer.initialize(builder.put("_double", 66.66d).build(), this);
		assertEquals(_double, 66.66d, 0.0001);

		initializer.initialize(builder.put("_double", "").build(), this);
		assertEquals(_double, 0.0d, 0.0001);
	}
	
	@Test(expected = InitializerException.class)
	public void testFloatException() {
		
		initializer.initialize(builder.put("_float", "qbc").build(), this);
	}
	
	@Test
	public void testFloat() {
		
		initializer.initialize(builder.build(), this);
		assertEquals(_float, -100.99f, 0.0001);

		initializer.initialize(builder.put("_float", 55.55f).build(), this);
		assertEquals(_float, 55.55f, 0.0001);
		
		initializer.initialize(builder.put("_float", "").build(), this);
		assertEquals(_float, 0.0f, 0.0001);
	}
	
	@Test(expected = InitializerException.class)
	public void testShortException() {
		
		initializer.initialize(builder.put("_short", -35000).build(), this);
	}
	
	@Test
	public void testShort() {
		
		initializer.initialize(builder.build(), this);
		assertEquals(_short, (short)50);
		
		initializer.initialize(builder.put("_short", (short)55).build(), this);
		assertEquals(_short, (short)55);
		
		builder.put("_short", "");
		initializer.initialize(builder.put("_short", "").build(), this);
		assertEquals(_short, (short)0);
	}
	
	@Test(expected = InitializerException.class)
	public void testLongException() {
		
		initializer.initialize(builder.put("_long", "true").build(), this);
	}
	
	@Test
	public void testLong() {
		
		initializer.initialize(builder.build(), this);
		assertEquals(_long, 40L);
		
		initializer.initialize(builder.put("_long", 44L).build(), this);
		assertEquals(_long, 44L);

		initializer.initialize(builder.put("_long", "").build(), this);
		assertEquals(_long, 0L);
	}
	
	@Test(expected = InitializerException.class)
	public void testByteException() {
		
		initializer.initialize(builder.put("_byte", 200).build(), this);
	}
	
	@Test
	public void testByte() {
		
		initializer.initialize(builder.build(), this);
		assertEquals(_byte, (byte)30);
		
		initializer.initialize(builder.put("_byte", (byte)33).build(), this);
		assertEquals(_byte, (byte)33);

		initializer.initialize(builder.put("_byte", "").build(), this);
		assertEquals(_byte, (byte)0);
	}
	
	@Test(expected = InitializerException.class)
	public void testIntException() {
		
		initializer.initialize(builder.put("integer", "abc").build(), this);
	}
	
	@Test
	public void testInt() {
		
		initializer.initialize(builder.build(), this);
		assertEquals(integer, 10);
		
		initializer.initialize(builder.put("integer", 20).build(), this);
		assertEquals(integer, 20);
		
		initializer.initialize(builder.put("integer", "").build(), this);
		assertEquals(integer, 0);
	}
	
	@Test
	public void testBoolean() {
		
		initializer.initialize(builder.build(), this);
		assertEquals(bool, false);
		
		initializer.initialize(builder.put("bool", "true").build(), this);
		assertEquals(bool, true);
		
		initializer.initialize(builder.put("bool", "").build(), this);
		assertEquals(bool, false);
		
		bool = true;
		
		initializer.initialize(builder.put("bool", "dcdcdcdc").build(), this);
		assertEquals(bool, false);
	}
	
	@Test
	public void testChar() {
		
		initializer.initialize(builder.build(), this);
		assertEquals(_char, 'A');
		
		initializer.initialize(builder.put("_char", "").build(), this);
		assertEquals(_char, Character.MIN_VALUE);
		
		_char = 'B';
		
		initializer.initialize(builder.put("_char", null).build(), this);
		assertEquals(_char, Character.MIN_VALUE);

		initializer.initialize(builder.put("_char", "Boom").build(), this);
		assertEquals(_char, 'B');
		
		initializer.initialize(builder.put("_char", "C").build(), this);
		assertEquals(_char, 'C');
	}
	
}
