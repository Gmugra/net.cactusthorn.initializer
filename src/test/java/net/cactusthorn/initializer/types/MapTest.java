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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

import net.cactusthorn.initializer.Initializer;
import net.cactusthorn.initializer.InitializerException;
import net.cactusthorn.initializer.annotations.*;
import net.cactusthorn.initializer.properties.InitPropertiesBuilder;

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

	@InitProperty
	ConcurrentMap<String,String> cm;
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	public void testConcurrentMap() {
		
		new Initializer().initialize(pb.put("cm", "A=AA,B=>BB,C=CC").build(), this);
		assertEquals(ConcurrentHashMap.class,cm.getClass());
		assertEquals(3,cm.size());
	}
	
	@Test
	public void testNotAllowedEmpty() {
		
		expectedException.expect(InitializerException.class);
		expectedException.expectMessage(containsString("with not empty value"));
		
		new Initializer().initialize(pb.put("hashMap", "").build(), this);
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
		
		expectedException.expect(InitializerException.class);
		expectedException.expectMessage(containsString("has unsupported type"));
		
		new Initializer().initialize(pb.put("unsupported", "10=S").build(), this);
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
