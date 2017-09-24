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
import static net.cactusthorn.initializer.annotations.InitPropertyPolicy.OPTIONAL;

public class ListSetTest {

	static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
	
	Initializer initializer = new Initializer();
	InitProperties bundle = new InitProperties();
	
	@InitProperty(OPTIONAL)
	ArrayList<String> arrayList;

	@InitProperty(OPTIONAL)
	List<String> list;
	
	@InitProperty(OPTIONAL)
	SortedSet<String> sortedSet;
	
	@InitProperty(OPTIONAL)
	Set<String> set;
	
	@InitProperty(OPTIONAL)
	AbstractList<String> abstractList;
	
	@InitProperty(OPTIONAL)
	AbstractSequentialList<String> abstractSequentialList;
	
	@InitProperty(OPTIONAL)
	LinkedList<String> linkedList;
	
	@InitProperty(OPTIONAL)
	AbstractSet<String> abstractSet;
	
	@InitProperty(OPTIONAL)
	HashSet<String> hashSet;
	
	@InitProperty(OPTIONAL)
	LinkedHashSet<String> linkedHashSet;
	
	@InitProperty(OPTIONAL)
	TreeSet<String> treeSet;

	@InitProperty(OPTIONAL)
	Set<java.util.Date> dateset;
	
	@Test
	public void testDateSet() throws ParseException {
		
		/*HashSet<java.util.Date> correct = new HashSet<>();
		correct.add(SDF.parse("2017-10-10"));
		correct.add(SDF.parse("2017-03-08"));
		correct.add(SDF.parse("2017-11-15"));*/
		
		bundle.clear().put("dateset", "2017-10-10,2017-03-08,2017-11-15");
		dateset = null;
		initializer.initialize(bundle, this);
		assertEquals(dateset.size(),3);
	}
	
	@Test
	public void testLinkedHashSet() {
		
		linkedHashSet = null;
		bundle.clear().put("linkedHashSet", "a,b,c,c,d").put("xxxx", "ddd");
		initializer.initialize(bundle, this);
		assertThat(linkedHashSet, hasItems("a","b","c","d"));
		assertEquals(linkedHashSet.size(),4);
	}
	
	@Test
	public void testAbstractSet() {
		
		abstractSet = null;
		bundle.clear().put("abstractSet", "a,b,c,c,d");
		initializer.initialize(bundle, this);
		assertThat(abstractSet, hasItems("a","b","c","d"));
		assertEquals(abstractSet.size(),4);
	}

	@Test
	public void testTreeSet() {
		
		treeSet = null;
		bundle.clear().put("treeSet", "a,b,c,c,d");
		initializer.initialize(bundle, this);
		assertThat(treeSet, hasItems("a","b","c","d"));
		assertEquals(treeSet.size(),4);
	}
	
	@Test
	public void testHashSet() {
		
		hashSet = null;
		bundle.clear().put("hashSet", "a,b,c,c,d");
		initializer.initialize(bundle, this);
		assertThat(hashSet, hasItems("a","b","c","d"));
		assertEquals(hashSet.size(),4);
	}
	
	@Test
	public void testLinkedList() {
		
		linkedList = null;
		bundle.clear().put("linkedList", "a,b,c,d");
		initializer.initialize(bundle, this);
		assertThat(linkedList, hasItems("a","b","c","d"));
	}
	
	@Test
	public void testAbstractSequentialList() {
		
		abstractList = null;
		bundle.clear().put("abstractSequentialList", "a,b,c,d");
		initializer.initialize(bundle, this);
		assertEquals(abstractSequentialList.size(),4);
	}
	
	@Test
	public void testAbstractList() {
		
		abstractList = null;
		bundle.clear().put("abstractList", "a,b,c,d");
		initializer.initialize(bundle, this);
		assertEquals(abstractList.size(),4);
	}
	
	@Test
	public void testArrayList() {
		
		arrayList = null;
		bundle.clear().put("arrayList", "a,b,c,d");
		initializer.initialize(bundle, this);
		assertThat(arrayList, hasItems("a","b","c","d"));
	}
	
	@Test
	public void testList() {
		
		list = null;
		bundle.clear().put("list", "a,b,c,d,d,d");
		initializer.initialize(bundle, this);
		assertThat(list, hasItems("a","b","c","d"));
		assertEquals(list.size(),6);
	}
	
	@Test
	public void testSortedSet() {

		sortedSet = null;
		bundle.clear().put("sortedSet", "a,b,c,d,d,d");
		initializer.initialize(bundle, this);
		assertThat(sortedSet, hasItems("a","b","c","d"));
		assertEquals(sortedSet.size(),4);
	}
	
	@Test
	public void testSet() {
		
		set = null;
		bundle.clear().put("set", "a,b,c,d,d,d");
		initializer.initialize(bundle, this);
		assertThat(set, hasItems("a","b","c","d"));
		assertEquals(set.size(),4);
	}

}
