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
import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;
import net.cactusthorn.initializer.InitProperties;
import net.cactusthorn.initializer.Initializer;
import net.cactusthorn.initializer.InitializerException;
import net.cactusthorn.initializer.annotations.*;
import static net.cactusthorn.initializer.InitializerException.StandardError.UNSUPPORTED_TYPE;
import static net.cactusthorn.initializer.annotations.InitPropertyPolicy.*;

public class MapTest {
	
	InitProperties bundle = new InitProperties();
	
	@InitPropertyName("mapA")
	@InitProperty(OPTIONAL)
	Map<String,Integer> simpleMap;

	@InitProperty(OPTIONAL)
	Map<Integer,java.util.Date> intDateMap;
	
	@InitProperty(OPTIONAL)
	Map<Integer,?> unsupported;
	
	@InitProperty(OPTIONAL)
	IdentityHashMap<String,BigInteger> identMap;
	
	@InitProperty
	TreeMap<Integer,Float> tm;

	@Test
	public void testTreeMap() throws ParseException {
		
		Initializer initializer = new Initializer().setValuesSeparator('|').setPairSeparator('_').trimMultiValues(true);
		
		bundle.clear().put("tm", " 10_2017 | 20_300 | 30_44494 ");
		initializer.initialize(bundle, this);
	
		assertEquals(tm.size(),3);
	}
	
	@Test
	public void testIdentMap() throws ParseException {
		
		Initializer initializer = new Initializer();
		
		bundle.clear().put("identMap", "A=2017,B=300,C=44494994944747474747474");
		initializer.initialize(bundle, this);
		assertEquals(identMap.size(),3);
	}

	@Test
	public void testUnsupported() throws ParseException {
		
		Initializer initializer = new Initializer();
		
		try {
			bundle.clear().put("unsupported", "10=S");
			initializer.initialize(bundle, this);
			fail();
		} catch (InitializerException e ) {
			assertEquals(UNSUPPORTED_TYPE,e.getStandardError());
		}
	}
	
	@Test
	public void testDateSet() throws ParseException {
		
		Initializer initializer = new Initializer();
		
		bundle.clear().put("intDateMap", "10=2017-10-10,20=2017-03-08,30=2017-11-15");
		initializer.initialize(bundle, this);
		//System.out.println(intDateMap);
		assertEquals(intDateMap.size(),3);
	}
	
	@Test
	public void testSimpleMap() {
		
		Initializer initializer = new Initializer();
		
		bundle.clear().put("mapA", "a=10,b=20,c=30,AB\\=CD=5000");
		initializer.initialize(bundle, this);
		//System.out.println(simpleMap);
		assertEquals(simpleMap.size(),4);
	}
}
