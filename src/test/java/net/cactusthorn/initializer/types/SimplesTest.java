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
import net.cactusthorn.initializer.ConfigPropertiesBundle;
import net.cactusthorn.initializer.annotations.InitProperty;

public class SimplesTest {

	Initializer initializer = new Initializer();
	ConfigPropertiesBundle bundle = new ConfigPropertiesBundle("test");
	
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
	public void testBigIntegerException() throws InitializerException {
		
		bundle
			.clearProperties()
			.put("bigi", "xx");
		
		initializer.initialize(bundle, this);
	}
	
	@Test
	public void testBigInteger() throws InitializerException {
		
		bundle.clearProperties();
		initializer.initialize(bundle, this);
		assertEquals(bigi, new BigInteger("2095725747485959595054403"));
		
		bundle.put("bigi", "209572");
		initializer.initialize(bundle, this);
		assertEquals(bigi, new BigInteger("209572"));
		
		bundle.put("bigi", null);
		initializer.initialize(bundle, this);
		assertNull(bigi);
		
		bigi = new BigInteger("209572");
		
		bundle.put("bigi", "");
		initializer.initialize(bundle, this);
		assertNull(bigi);
	}
	
	@Test(expected = InitializerException.class)
	public void testBigDecimalException() throws InitializerException {
		
		bundle
			.clearProperties()
			.put("bigd", "xx");
		
		initializer.initialize(bundle, this);
	}
	
	@Test
	public void testBigDecimal() throws InitializerException {
		
		bundle.clearProperties();
		initializer.initialize(bundle, this);
		assertEquals(bigd, new BigDecimal(-100.8574774d));
		
		bundle.put("bigd", new BigDecimal(66.66d));
		initializer.initialize(bundle, this);
		assertEquals(bigd, new BigDecimal(66.66d));
		
		bundle.put("bigd", null);
		initializer.initialize(bundle, this);
		assertNull(bigd);
		
		bigd = new BigDecimal(66.66d);
		
		bundle.put("bigd", "");
		initializer.initialize(bundle, this);
		assertNull(bigd);
	}
	
	@Test
	public void testString() throws InitializerException {
		
		bundle.clearProperties();
		initializer.initialize(bundle, this);
		assertEquals(string, "test");
		
		bundle.put("string", "one");
		initializer.initialize(bundle, this);
		assertEquals(string, "one");
		
		bundle.put("string", null);
		initializer.initialize(bundle, this);
		assertNull(string);
		
		string = "AAA";
		
		bundle.put("string", "");
		initializer.initialize(bundle, this);
		assertNull(string);
	}
	
	@Test(expected = InitializerException.class)
	public void testDoubleException() throws InitializerException {
		
		bundle
			.clearProperties()
			.put("_double", "xx");
		
		initializer.initialize(bundle, this);
	}
	
	@Test
	public void testDouble() throws InitializerException {
		
		bundle.clearProperties();
		initializer.initialize(bundle, this);
		assertEquals(_double, -200.43d, 0.0001);
		
		bundle.put("_double", 66.66d);
		initializer.initialize(bundle, this);
		assertEquals(_double, 66.66d, 0.0001);
		
		bundle.put("_double", null);
		initializer.initialize(bundle, this);
		assertNull(_double);
		
		_double = 20.20d;
		
		bundle.put("_double", "");
		initializer.initialize(bundle, this);
		assertNull(_double);
	}
	
	@Test(expected = InitializerException.class)
	public void testFloatException() throws InitializerException {
		
		bundle
			.clearProperties()
			.put("_float", "qbc");
		
		initializer.initialize(bundle, this);
	}
	
	@Test
	public void testFloat() throws InitializerException {
		
		bundle.clearProperties();
		initializer.initialize(bundle, this);
		assertEquals(_float, -100.99f, 0.0001);
		
		bundle.put("_float", 55.55f);
		initializer.initialize(bundle, this);
		assertEquals(_float, 55.55f, 0.0001);
		
		bundle.put("_float", null);
		initializer.initialize(bundle, this);
		assertNull(_float);
		
		_float = 10.10f;
		
		bundle.put("_float", "");
		initializer.initialize(bundle, this);
		assertNull(_float);
	}
	
	@Test(expected = InitializerException.class)
	public void testShortException() throws InitializerException {
		
		bundle
			.clearProperties()
			.put("_short", -35000);
		
		initializer.initialize(bundle, this);
	}
	
	@Test
	public void testShort() throws InitializerException {
		
		bundle.clearProperties();
		initializer.initialize(bundle, this);
		assertEquals(_short, Short.valueOf((short)50));
		
		bundle.put("_short", Short.valueOf((short)55));
		initializer.initialize(bundle, this);
		assertEquals(_short, Short.valueOf((short)55));
		
		bundle.put("_short", null);
		initializer.initialize(bundle, this);
		assertNull(_short);
		
		_short = (short)20;
		
		bundle.put("_short", "");
		initializer.initialize(bundle, this);
		assertNull(_short);
	}
	
	@Test(expected = InitializerException.class)
	public void testLongException() throws InitializerException {
		
		bundle
			.clearProperties()
			.put("_long", "true");
		
		initializer.initialize(bundle, this);
	}
	
	@Test
	public void testLong() throws InitializerException {
		
		bundle.clearProperties();
		initializer.initialize(bundle, this);
		assertEquals(_long, Long.valueOf(40L));
		
		bundle.put("_long", Long.valueOf(44L));
		initializer.initialize(bundle, this);
		assertEquals(_long, Long.valueOf(44L));
		
		bundle.put("_long", null);
		initializer.initialize(bundle, this);
		assertNull(_long);
		
		_long = 10L;
		
		bundle.put("_long", "");
		initializer.initialize(bundle, this);
		assertNull(_long);
	}
	
	@Test(expected = InitializerException.class)
	public void testByteException() throws InitializerException {
		
		bundle
			.clearProperties()
			.put("_byte", 200);
		
		initializer.initialize(bundle, this);
	}
	
	@Test
	public void testByte() throws InitializerException {
		
		bundle.clearProperties();
		initializer.initialize(bundle, this);
		assertEquals(_byte, Byte.valueOf((byte)30));
		
		bundle.put("_byte", Byte.valueOf((byte)33));
		initializer.initialize(bundle, this);
		assertEquals(_byte, Byte.valueOf((byte)33));
		
		bundle.put("_byte", null);
		initializer.initialize(bundle, this);
		assertNull(_byte);
		
		_byte = (byte)10;
		
		bundle.put("_byte", "");
		initializer.initialize(bundle, this);
		assertNull(_byte);
	}
	
	@Test(expected = InitializerException.class)
	public void testIntException() throws InitializerException {
		
		bundle
			.clearProperties()
			.put("integer", "abc");
		
		initializer.initialize(bundle, this);
	}
	
	@Test
	public void testInt() throws InitializerException {
		
		bundle.clearProperties();
		initializer.initialize(bundle, this);
		assertEquals(integer, Integer.valueOf(10));
		
		bundle.put("integer", Integer.valueOf(20));
		initializer.initialize(bundle, this);
		assertEquals(integer, Integer.valueOf(20));
		
		try { 
			bundle.put("integer", null);
			initializer.initialize(bundle, this);
			assertNull(integer);
		} catch (Exception e ) {
			e.printStackTrace();
		}
		
		integer = 1;
		
		bundle.put("integer", "");
		initializer.initialize(bundle, this);
		assertNull(integer);
	}
	
	@Test
	public void testBoolean() throws InitializerException {
		
		bundle.clearProperties();
		initializer.initialize(bundle, this);
		assertEquals(bool, Boolean.FALSE);
		
		bundle.put("bool", "true");
		initializer.initialize(bundle, this);
		assertEquals(bool, Boolean.TRUE);
		
		bundle.put("bool", null);
		initializer.initialize(bundle, this);
		assertNull(bool);
		
		bool = Boolean.TRUE;
		
		bundle.put("bool", "");
		initializer.initialize(bundle, this);
		assertNull(bool);
		
		bundle.put("bool", "dcdcdcdc");
		initializer.initialize(bundle, this);
		assertEquals(bool, Boolean.FALSE);
	}
	
	@Test
	public void testChar() throws InitializerException {
		
		bundle.clearProperties();
		initializer.initialize(bundle, this);
		assertEquals(_char, Character.valueOf('A'));
		
		bundle.put("_char", "");
		initializer.initialize(bundle, this);
		assertNull(_char);
		
		_char ='F';
		
		bundle.put("_char", null);
		initializer.initialize(bundle, this);
		assertNull(_char);
		
		bundle.put("_char", "Boom");
		initializer.initialize(bundle, this);
		assertEquals(_char, Character.valueOf('B'));
		
		bundle.put("_char", "C");
		initializer.initialize(bundle, this);
		assertEquals(_char, Character.valueOf('C'));
	}
	
}
