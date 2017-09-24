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
import static net.cactusthorn.initializer.InitializerException.StandardError.*;
import static net.cactusthorn.initializer.annotations.InitPropertyPolicy.*;

public class MapTest {
	
	InitProperties bundle = new InitProperties();
	
	@InitPropertyName("mapA")
	Map<String,Integer> simpleMap;

	@InitProperty
	Map<Integer,java.util.Date> intDateMap;
	
	@InitProperty
	Map<Integer,?> unsupported;
	
	@InitProperty
	IdentityHashMap<String,BigInteger> identMap;
	
	@InitProperty
	TreeMap<Integer,Float> tm;
	
	@InitProperty
	NavigableMap<String,Boolean> naviMap;
	
	@InitProperty
	SortedMap<String,Boolean> sortedMap;
	
	@InitProperty
	LinkedHashMap<String,Boolean> lhMap;

	@InitProperty
	HashMap<String,Boolean> hashMap;
	
	@InitProperty(OPTIONAL)
	AbstractMap<String,Boolean> abstractMap;

	@Test
	public void testNotAllowedEmpty() {
		
		try {
			bundle.clear().put("hashMap", "");
			new Initializer().initialize(bundle, this);
			fail();
		} catch (InitializerException e ) {
			assertEquals(NOT_EMPTY_PROPERTY,e.getStandardError());
		}
	}
	
	@Test
	public void testEmptyMap() {
		
		bundle.clear().put("abstractMap", "");
		new Initializer().initialize(bundle, this);
		assertEquals(0,abstractMap.size());
	}
	
	@Test
	public void testAbstractMap() {
		
		bundle.clear().put("abstractMap", "A=true,B=true,C=true");
		new Initializer().initialize(bundle, this);
		assertEquals(3,abstractMap.size());
	}
	
	@Test
	public void testHashMap() {
		
		bundle.clear().put("hashMap", "A=true,B=true,C=true");
		new Initializer().initialize(bundle, this);
		assertEquals(3,hashMap.size());
	}
	
	@Test
	public void testLinkedHashMap() {
		
		Initializer initializer = new Initializer();
		
		bundle.clear().put("lhMap", "A=true,B=true,C=true");
		initializer.initialize(bundle, this);
	
		assertEquals(3,lhMap.size());
	}
	
	@Test
	public void testSortedMap() {
		
		bundle.clear().put("sortedMap", "A=true,B=true,C=true");
		new Initializer().initialize(bundle, this);
		assertEquals(sortedMap.getClass(),TreeMap.class);
		assertEquals(3,sortedMap.size());
	}
	
	@Test
	public void testNaviMap() {
		
		bundle.clear().put("naviMap", "A=true,B=true,C=true");
		new Initializer().initialize(bundle, this);
		assertEquals(naviMap.getClass(),TreeMap.class);
		assertEquals(3,naviMap.size());
	}
	
	@Test
	public void testTreeMap() throws ParseException {
		
		Initializer initializer = new Initializer().setValuesSeparator('|').setPairSeparator('_').trimMultiValues(true);
		
		bundle.clear().put("tm", " 10_2017 | 20_300 | 30_44494 ");
		initializer.initialize(bundle, this);
		assertEquals(3,tm.size());
	}
	
	@Test
	public void testIdentMap() throws ParseException {
	
		bundle.clear().put("identMap", "A=2017,B=300,C=44494994944747474747474");
		new Initializer().initialize(bundle, this);
		assertEquals(3,identMap.size());
	}

	@Test
	public void testUnsupported() throws ParseException {
		
		try {
			bundle.clear().put("unsupported", "10=S");
			new Initializer().initialize(bundle, this);
			fail();
		} catch (InitializerException e ) {
			assertEquals(UNSUPPORTED_TYPE,e.getStandardError());
		}
	}
	
	@Test
	public void testDateSet() throws ParseException {
		
		bundle.clear().put("intDateMap", "10=2017-10-10,20=2017-03-08,30=2017-11-15");
		new Initializer().initialize(bundle, this);
		assertEquals(3,intDateMap.size());
	}
	
	@Test
	public void testSimpleMap() {
		
		bundle.clear().put("mapA", "a=10,b=20,c=30,AB\\=CD=5000");
		new Initializer().initialize(bundle, this);
		assertEquals(4,simpleMap.size());
	}
}
