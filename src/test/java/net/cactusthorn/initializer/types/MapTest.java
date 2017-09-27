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

import net.cactusthorn.initializer.InitPropertiesBuilder;
import net.cactusthorn.initializer.Initializer;
import net.cactusthorn.initializer.InitializerException;
import net.cactusthorn.initializer.annotations.*;
import static net.cactusthorn.initializer.InitializerException.StandardError.*;
import static net.cactusthorn.initializer.annotations.InitPropertyPolicy.*;

public class MapTest {
	
	InitPropertiesBuilder pb = new InitPropertiesBuilder();
	
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
			new Initializer().initialize(pb.put("hashMap", "").build(), this);
			fail();
		} catch (InitializerException e ) {
			assertEquals(NOT_EMPTY_PROPERTY,e.getStandardError());
		}
	}
	
	@Test
	public void testEmptyMap() {
		
		new Initializer().initialize(pb.put("abstractMap", "").build(), this);
		assertEquals(0,abstractMap.size());
	}
	
	@Test
	public void testAbstractMap() {
		
		new Initializer().initialize(pb.put("abstractMap", "A=true,B=true,C=true").build(), this);
		assertEquals(3,abstractMap.size());
	}
	
	@Test
	public void testHashMap() {
		
		new Initializer().initialize(pb.put("hashMap", "A=true,B=true,C=true").build(), this);
		assertEquals(3,hashMap.size());
	}
	
	@Test
	public void testLinkedHashMap() {
		
		new Initializer().initialize(pb.put("lhMap", "A=true,B=true,C=true").build(), this);
		assertEquals(3,lhMap.size());
	}
	
	@Test
	public void testSortedMap() {
		
		new Initializer().initialize(pb.put("sortedMap", "A=true,B=true,C=true").build(), this);
		assertEquals(sortedMap.getClass(),TreeMap.class);
		assertEquals(3,sortedMap.size());
	}
	
	@Test
	public void testNaviMap() {
		
		new Initializer().initialize(pb.put("naviMap", "A=true,B=true,C=true").build(), this);
		assertEquals(naviMap.getClass(),TreeMap.class);
		assertEquals(3,naviMap.size());
	}
	
	@Test
	public void testTreeMap() throws ParseException {
		
		pb.setValuesSeparator('|').setPairSeparator('_').trimMultiValues(true).put("tm", " 10_2017 | 20_300 | 30_44494 ");
		new Initializer().initialize(pb.build(), this);
		assertEquals(3,tm.size());
	}
	
	@Test
	public void testIdentMap() throws ParseException {
	
		new Initializer().initialize(pb.put("identMap", "A=2017,B=300,C=44494994944747474747474").build(), this);
		assertEquals(3,identMap.size());
	}

	@Test
	public void testUnsupported() throws ParseException {
		
		try {
			new Initializer().initialize(pb.put("unsupported", "10=S").build(), this);
			fail();
		} catch (InitializerException e ) {
			assertEquals(UNSUPPORTED_TYPE,e.getStandardError());
		}
	}
	
	@Test
	public void testDateSet() throws ParseException {
		
		new Initializer().initialize(pb.put("intDateMap", "10=2017-10-10,20=2017-03-08,30=2017-11-15").build(), this);
		assertEquals(3,intDateMap.size());
	}
	
	@Test
	public void testSimpleMap() {
		
		new Initializer().initialize(pb.put("mapA", "a=10,b=20,c=30,AB\\=CD=5000").build(), this);
		assertEquals(4,simpleMap.size());
	}
}
