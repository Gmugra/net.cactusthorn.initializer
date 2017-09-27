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
import static net.cactusthorn.initializer.annotations.InitPropertyPolicy.*;
import net.cactusthorn.initializer.Initializer;
import net.cactusthorn.initializer.InitializerException;
import net.cactusthorn.initializer.annotations.InitProperty;
import net.cactusthorn.initializer.annotations.InitPropertyName;
import net.cactusthorn.initializer.properties.InitProperties;
import net.cactusthorn.initializer.properties.InitPropertiesBuilder;

public class ArraysTest {
	
	InitPropertiesBuilder pb = new InitPropertiesBuilder();
	
	static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

	@InitProperty
	String[] sarr;
	
	@InitProperty(OPTIONAL)
	int[] iarr;
	
	@InitProperty
	BigInteger[] biarr;
	
	@InitProperty
	java.util.Date[] datearr;
	
	@InitPropertyName("FlOaT")
	float[] floatarr;
	
	@InitProperty(OPTIONAL)
	StringBuilder[] builder;

	@Test
	public void testEmptyAllowedInt() {
		
		new Initializer().initialize(pb.put("iarr", "").build(), this);
		assertEquals(0, iarr.length);
	}
	
	@Test
	public void testStringBuilder() {
		
		//space, yes
		new Initializer().initialize(pb.put("builder", " ").build(), this);
		assertEquals(1, builder.length);
	}
	
	@Test
	public void testEmptyNotAllowed() {
		
		try {
			new Initializer().initialize(pb.put("biarr", "").build(), this);
			fail();
		} catch (InitializerException e) {
			assertEquals(NOT_EMPTY_PROPERTY, e.getStandardError());
		}	
	}
	
	@Test
	public void testFloatArray() {
		
		float[] correct = new float[]{10.59f,99.45f,14.3475f,256.5678f};
		
		InitProperties prop = 
			pb.setValuesSeparator('.').put("FlOaT", " 10\\.59f . 99\\.45f . 14\\.3475f . 256\\.5678f").build();
		
		new Initializer().initialize(prop, this);
		
		assertArrayEquals(correct, floatarr, 0.0001f);	
	}
	
	@Test
	public void testWrongElementArray() {
		
		try {
			new Initializer().initialize(pb.put("iarr", "10,sss,30").build(), this);
			fail();
		} catch (InitializerException e ) {
			assertEquals(WRONG_VALUE_AT_POSITION,e.getStandardError());
		}
	}
	
	@Test
	public void testDateArray() throws ParseException {

		java.util.Date[] correct = new java.util.Date[]{SDF.parse("2017-10-10"),SDF.parse("2017-03-08"),SDF.parse("2017-11-15")};
		
		InitProperties prop = 
			pb.setValuesSeparator('|').put("datearr", "2017-10-10|2017-03-08|2017-11-15").build();
		
		new Initializer().initialize(prop, this);
		assertArrayEquals(correct, datearr);
	}
	
	@Test
	public void testBigIntegerArray() {
		
		BigInteger[] correct = new BigInteger[]{new BigInteger("10"),new BigInteger("100000"),new BigInteger("300000")};
		new Initializer().initialize(pb.put("biarr", "10,100000,300000").build(), this);
		assertArrayEquals(correct, biarr);
	}
	
	@Test
	public void testStringArray() {
		
		Initializer initializer = new Initializer();
		
		String[] correct = new String[]{"ssss","wwww","rrrr"};
		
		initializer.initialize(pb.put("sarr", "ssss,wwww,rrrr").build(), this);
		assertArrayEquals(correct, sarr);
		
		correct = new String[]{"ssss","wwww",null};
		initializer.initialize(pb.put("sarr", "ssss,wwww,").build(), this);
		assertArrayEquals(correct, sarr);
	}
	
	@Test
	public void testIntArray() {
		
		Initializer initializer = new Initializer();
		
		int[] correct = new int[]{10,20,30};
		initializer.initialize(pb.put("iarr", "10,20,30").build(), this);
		assertArrayEquals(correct, iarr);
		
		correct = new int[]{10,0,30};
		initializer.initialize(pb.put("iarr", "10,,30").build(), this);
		assertArrayEquals(correct, iarr);
	}
}
