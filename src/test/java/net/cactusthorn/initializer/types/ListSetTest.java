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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import net.cactusthorn.initializer.InitProperties;
import net.cactusthorn.initializer.Initializer;
import net.cactusthorn.initializer.annotations.InitProperty;
import static net.cactusthorn.initializer.annotations.InitPropertyPolicy.*;

public class ListSetTest {

	static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
	
	Initializer initializer = new Initializer();
	InitProperties bundle = new InitProperties();
	
	@InitProperty
	ArrayList<String> arrayList;

	@InitProperty
	List<String> list;
	
	@InitProperty
	SortedSet<String> sortedSet;
	
	@InitProperty
	Set<String> set;
	
	@InitProperty
	AbstractList<String> abstractList;
	
	@InitProperty
	AbstractSequentialList<String> abstractSequentialList;
	
	@InitProperty
	LinkedList<String> linkedList;
	
	@InitProperty
	AbstractSet<String> abstractSet;
	
	@InitProperty
	HashSet<String> hashSet;
	
	@InitProperty
	LinkedHashSet<String> linkedHashSet;
	
	@InitProperty
	TreeSet<String> treeSet;

	@InitProperty(OPTIONAL)
	Set<java.util.Date> dateset;

	@Test
	public void testAllowedEmpty() throws ParseException {
		
		bundle.clear().put("dateset", "");
		initializer.initialize(bundle, this);
		assertEquals(0,dateset.size());
	}
	
	@Test
	public void testDateSet() throws ParseException {
		
		bundle.clear().put("dateset", "2017-10-10,2017-03-08,2017-11-15");
		initializer.initialize(bundle, this);
		assertEquals(3,dateset.size());
	}
	
	@Test
	public void testLinkedHashSet() {
		
		bundle.clear().put("linkedHashSet", "a,b,c,c,d").put("xxxx", "ddd");
		initializer.initialize(bundle, this);
		assertThat(linkedHashSet, hasItems("a","b","c","d"));
		assertEquals(4,linkedHashSet.size());
	}
	
	@Test
	public void testAbstractSet() {
		
		bundle.clear().put("abstractSet", "a,b,c,c,d");
		initializer.initialize(bundle, this);
		assertThat(abstractSet, hasItems("a","b","c","d"));
		assertEquals(4,abstractSet.size());
	}

	@Test
	public void testTreeSet() {
		
		bundle.clear().put("treeSet", "a,b,c,c,d");
		initializer.initialize(bundle, this);
		assertThat(treeSet, hasItems("a","b","c","d"));
		assertEquals(4,treeSet.size());
	}
	
	@Test
	public void testHashSet() {
		
		bundle.clear().put("hashSet", "a,b,c,c,d");
		initializer.initialize(bundle, this);
		assertThat(hashSet, hasItems("a","b","c","d"));
		assertEquals(4,hashSet.size());
	}
	
	@Test
	public void testLinkedList() {
		
		bundle.clear().put("linkedList", "a,b,c,d");
		initializer.initialize(bundle, this);
		assertThat(linkedList, hasItems("a","b","c","d"));
	}
	
	@Test
	public void testAbstractSequentialList() {

		bundle.clear().put("abstractSequentialList", "a,b,c,d");
		initializer.initialize(bundle, this);
		assertEquals(4,abstractSequentialList.size());
	}
	
	@Test
	public void testAbstractList() {

		bundle.clear().put("abstractList", "a,b,c,d");
		initializer.initialize(bundle, this);
		assertEquals(4,abstractList.size());
	}
	
	@Test
	public void testArrayList() {

		bundle.clear().put("arrayList", "a,b,c,d");
		initializer.initialize(bundle, this);
		assertThat(arrayList, hasItems("a","b","c","d"));
	}
	
	@Test
	public void testList() {

		bundle.clear().put("list", "a,b,c,d,d,d");
		initializer.initialize(bundle, this);
		assertThat(list, hasItems("a","b","c","d"));
		assertEquals(6,list.size());
	}
	
	@Test
	public void testSortedSet() {

		bundle.clear().put("sortedSet", "a,b,c,d,d,d");
		initializer.initialize(bundle, this);
		assertThat(sortedSet, hasItems("a","b","c","d"));
		assertEquals(4,sortedSet.size());
	}
	
	@Test
	public void testSet() {

		bundle.clear().put("set", "a,b,c,d,d,d");
		initializer.initialize(bundle, this);
		assertThat(set, hasItems("a","b","c","d"));
		assertEquals(4,set.size());
	}

}
