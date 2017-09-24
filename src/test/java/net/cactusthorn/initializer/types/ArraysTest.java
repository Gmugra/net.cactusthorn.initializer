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

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Test;

import static org.junit.Assert.*;
import static net.cactusthorn.initializer.InitializerException.StandardError.*;
import static net.cactusthorn.initializer.annotations.InitPropertyPolicy.OPTIONAL;
import net.cactusthorn.initializer.Initializer;
import net.cactusthorn.initializer.InitializerException;
import net.cactusthorn.initializer.ConfigPropertiesBundle;
import net.cactusthorn.initializer.annotations.InitProperty;
import net.cactusthorn.initializer.annotations.InitPropertyName;

public class ArraysTest {
	
	static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
	
	ConfigPropertiesBundle bundle = new ConfigPropertiesBundle("test");

	@InitProperty(OPTIONAL)
	String[] sarr;
	
	@InitProperty(OPTIONAL)
	int[] iarr;
	
	@InitProperty(OPTIONAL)
	BigInteger[] biarr;
	
	@InitProperty(OPTIONAL)
	java.util.Date[] datearr;
	
	@InitPropertyName("FlOaT")
	@InitProperty
	float[] floatarr;
	
	@Test
	public void testFloatArray() {
		
		Initializer initializer = new Initializer().setValuesSeparator('.');
		
		float[] correct = new float[]{10.59f,99.45f,14.3475f,256.5678f};
		bundle.clearProperties().put("FlOaT", " 10\\.59f . 99\\.45f . 14\\.3475f . 256\\.5678f");
		initializer.initialize(bundle, this);
		
		assertArrayEquals(correct, floatarr, 0.0001f);	
	}
	
	@Test
	public void testWrongElementArray() {
		
		Initializer initializer = new Initializer();
		
		bundle.clearProperties().put("iarr", "10,sss,30");
		try {
			initializer.initialize(bundle, this);
			fail();
		} catch (InitializerException e ) {
			assertEquals(WRONG_VALUE_AT_POSITION,e.getStandardError());
		}
	}
	
	@Test
	public void testDateArray() throws ParseException {

		Initializer initializer = new Initializer().setValuesSeparator('|');
		
		java.util.Date[] correct = new java.util.Date[]{SDF.parse("2017-10-10"),SDF.parse("2017-03-08"),SDF.parse("2017-11-15")};
		bundle.clearProperties().put("datearr", "2017-10-10|2017-03-08|2017-11-15");
		datearr = null;
		initializer.initialize(bundle, this);
		assertArrayEquals(correct, datearr);
	}
	
	@Test
	public void testBigIntegerArray() {
		
		Initializer initializer = new Initializer();
		
		BigInteger[] correct = new BigInteger[]{new BigInteger("10"),new BigInteger("100000"),new BigInteger("300000")};
		bundle.clearProperties().put("biarr", "10,100000,300000");
		biarr = null;
		initializer.initialize(bundle, this);
		assertArrayEquals(correct, biarr);
	}
	
	@Test
	public void testStringArray() {
		
		Initializer initializer = new Initializer();
		
		String[] correct = new String[]{"ssss","wwww","rrrr"};
		bundle.clearProperties().put("sarr", "ssss,wwww,rrrr");
		sarr = null;
		initializer.initialize(bundle, this);
		assertArrayEquals(correct, sarr);
		
		correct = new String[]{"ssss","wwww",null};
		bundle.clearProperties().put("sarr", "ssss,wwww,");
		sarr = null;
		initializer.initialize(bundle, this);
		assertArrayEquals(correct, sarr);
	}
	
	@Test
	public void testIntArray() {
		
		Initializer initializer = new Initializer();
		
		int[] correct = new int[]{10,20,30};
		bundle.clearProperties().put("iarr", "10,20,30");
		iarr = null;
		initializer.initialize(bundle, this);
		assertArrayEquals(correct, iarr);
		
		correct = new int[]{10,0,30};
		bundle.clearProperties().put("iarr", "10,,30");
		iarr = null;
		initializer.initialize(bundle, this);
		assertArrayEquals(correct, iarr);
	}
}
