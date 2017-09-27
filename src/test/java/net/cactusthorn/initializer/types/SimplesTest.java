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

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import static org.junit.Assert.*;
import static net.cactusthorn.initializer.annotations.InitPropertyPolicy.OPTIONAL;
import net.cactusthorn.initializer.Initializer;
import net.cactusthorn.initializer.InitializerException;
import net.cactusthorn.initializer.annotations.InitProperty;
import net.cactusthorn.initializer.properties.InitPropertiesBuilder;

public class SimplesTest {

	Initializer initializer = new Initializer();
	InitPropertiesBuilder builder = new InitPropertiesBuilder();
	
	@InitProperty(OPTIONAL)
	Boolean bool = Boolean.FALSE;
	
	@InitProperty(OPTIONAL)
	Character _char = 'A';
	
	@InitProperty(OPTIONAL)
	Integer integer = 10;
	
	@InitProperty(OPTIONAL)
	Byte _byte = (byte)30;
	
	@InitProperty(OPTIONAL)
	Long _long = 40L;
	
	@InitProperty(OPTIONAL)
	Short _short = (short)50;
	
	@InitProperty(OPTIONAL)
	Float _float = -100.99f;
	
	@InitProperty(OPTIONAL)
	Double _double = -200.43d;
	
	@InitProperty(OPTIONAL)
	String string = "test";
	
	@InitProperty(OPTIONAL)
	BigDecimal bigd = new BigDecimal(-100.8574774d);
	
	@InitProperty(OPTIONAL)
	BigInteger bigi = new BigInteger("2095725747485959595054403");
	
	@Test(expected = InitializerException.class)
	public void testBigIntegerException() {
		
		initializer.initialize(builder.put("bigi", "xx").build(), this);
	}
	
	@Test
	public void testBigInteger() {
		
		initializer.initialize(builder.build(), this);
		assertEquals(bigi, new BigInteger("2095725747485959595054403"));
		
		initializer.initialize(builder.put("bigi", "209572").build(), this);
		assertEquals(bigi, new BigInteger("209572"));
	
		initializer.initialize(builder.put("bigi", "").build(), this);
		assertNull(bigi);
	}
	
	@Test(expected = InitializerException.class)
	public void testBigDecimalException() {
		
		initializer.initialize(builder.put("bigd", "xx").build(), this);
	}
	
	@Test
	public void testBigDecimal() {
		
		initializer.initialize(builder.build(), this);
		assertEquals(bigd, new BigDecimal(-100.8574774d));
		
		initializer.initialize(builder.put("bigd", new BigDecimal(66.66d)).build(), this);
		assertEquals(bigd, new BigDecimal(66.66d));

		initializer.initialize(builder.put("bigd", "").build(), this);
		assertNull(bigd);
	}
	
	@Test
	public void testString() {
		
		initializer.initialize(builder.build(), this);
		assertEquals(string, "test");
		
		initializer.initialize(builder.put("string", "one").build(), this);
		assertEquals(string, "one");
		
		initializer.initialize(builder.put("string", "").build(), this);
		assertNull(string);
	}
	
	@Test(expected = InitializerException.class)
	public void testDoubleException() {
		
		initializer.initialize(builder.put("_double", "xx").build(), this);
	}
	
	@Test
	public void testDouble() {
		
		initializer.initialize(builder.build(), this);
		assertEquals(_double, -200.43d, 0.0001);
		
		initializer.initialize(builder.put("_double", 66.66d).build(), this);
		assertEquals(_double, 66.66d, 0.0001);
		
		initializer.initialize(builder.put("_double", "").build(), this);
		assertNull(_double);
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
		assertNull(_float);
	}
	
	@Test(expected = InitializerException.class)
	public void testShortException() {
		
		initializer.initialize(builder.put("_short", -35000).build(), this);
	}
	
	@Test
	public void testShort() {
		
		initializer.initialize(builder.build(), this);
		assertEquals(_short, Short.valueOf((short)50));
		
		initializer.initialize(builder.put("_short", Short.valueOf((short)55)).build(), this);
		assertEquals(_short, Short.valueOf((short)55));
		
		initializer.initialize(builder.put("_short", "").build(), this);
		assertNull(_short);
	}
	
	@Test(expected = InitializerException.class)
	public void testLongException() {
		
		initializer.initialize(builder.put("_long", "true").build(), this);
	}
	
	@Test
	public void testLong() {
		
		initializer.initialize(builder.build(), this);
		assertEquals(_long, Long.valueOf(40L));
		
		initializer.initialize(builder.put("_long", Long.valueOf(44L)).build(), this);
		assertEquals(_long, Long.valueOf(44L));
		
		initializer.initialize(builder.put("_long", "").build(), this);
		assertNull(_long);
	}
	
	@Test(expected = InitializerException.class)
	public void testByteException() {
		
		initializer.initialize(builder.put("_byte", 200).build(), this);
	}
	
	@Test
	public void testByte() {
		
		initializer.initialize(builder.build(), this);
		assertEquals(_byte, Byte.valueOf((byte)30));
	
		initializer.initialize(builder.put("_byte", Byte.valueOf((byte)33)).build(), this);
		assertEquals(_byte, Byte.valueOf((byte)33));
		
		initializer.initialize(builder.put("_byte", "").build(), this);
		assertNull(_byte);
	}
	
	@Test(expected = InitializerException.class)
	public void testIntException() {
		
		initializer.initialize(builder.put("integer", "abc").build(), this);
	}
	
	@Test
	public void testInt() {
		
		initializer.initialize(builder.build(), this);
		assertEquals(integer, Integer.valueOf(10));
		
		initializer.initialize(builder.put("integer", Integer.valueOf(20)).build(), this);
		assertEquals(integer, Integer.valueOf(20));
		
		initializer.initialize(builder.put("integer", "").build(), this);
		assertNull(integer);
	}
	
	@Test
	public void testBoolean() {
		
		initializer.initialize(builder.build(), this);
		assertEquals(bool, Boolean.FALSE);
		
		initializer.initialize(builder.put("bool", "true").build(), this);
		assertEquals(bool, Boolean.TRUE);
		initializer.initialize(builder.put("bool", "").build(), this);
		assertNull(bool);
		
		initializer.initialize(builder.put("bool", "dcdcdcdc").build(), this);
		assertEquals(bool, Boolean.FALSE);
	}
	
	@Test
	public void testChar() {
		
		initializer.initialize(builder.build(), this);
		assertEquals(_char, Character.valueOf('A'));
		
		initializer.initialize(builder.put("_char", "").build(), this);
		assertNull(_char);

		initializer.initialize(builder.put("_char", "Boom").build(), this);
		assertEquals(_char, Character.valueOf('B'));
		
		initializer.initialize(builder.put("_char", "C").build(), this);
		assertEquals(_char, Character.valueOf('C'));
	}
	
}
