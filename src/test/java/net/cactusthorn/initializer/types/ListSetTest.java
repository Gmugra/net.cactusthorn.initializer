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

import net.cactusthorn.initializer.InitPropertiesBuilder;
import net.cactusthorn.initializer.Initializer;
import net.cactusthorn.initializer.annotations.InitProperty;
import static net.cactusthorn.initializer.annotations.InitPropertyPolicy.*;

public class ListSetTest {

	InitPropertiesBuilder pb = new InitPropertiesBuilder();
	
	static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
	
	Initializer initializer = new Initializer();
	
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
		
		initializer.initialize(pb.put("dateset", "").build(), this);
		assertEquals(0,dateset.size());
	}
	
	@Test
	public void testDateSet() throws ParseException {

		initializer.initialize(pb.put("dateset", "2017-10-10,2017-03-08,2017-11-15").build(), this);
		assertEquals(3,dateset.size());
	}
	
	@Test
	public void testLinkedHashSet() {
		
		initializer.initialize(pb.put("linkedHashSet", "a,b,c,c,d").put("xxxx", "ddd").build(), this);
		assertThat(linkedHashSet, hasItems("a","b","c","d"));
		assertEquals(4,linkedHashSet.size());
	}
	
	@Test
	public void testAbstractSet() {
		
		initializer.initialize(pb.put("abstractSet", "a,b,c,c,d").build(), this);
		assertThat(abstractSet, hasItems("a","b","c","d"));
		assertEquals(4,abstractSet.size());
	}

	@Test
	public void testTreeSet() {
		
		initializer.initialize(pb.put("treeSet", "a,b,c,c,d").build(), this);
		assertThat(treeSet, hasItems("a","b","c","d"));
		assertEquals(4,treeSet.size());
	}
	
	@Test
	public void testHashSet() {
		
		initializer.initialize(pb.put("hashSet", "a,b,c,c,d").build(), this);
		assertThat(hashSet, hasItems("a","b","c","d"));
		assertEquals(4,hashSet.size());
	}
	
	@Test
	public void testLinkedList() {

		initializer.initialize(pb.put("linkedList", "a,b,c,d").build(), this);
		assertThat(linkedList, hasItems("a","b","c","d"));
	}
	
	@Test
	public void testAbstractSequentialList() {

		initializer.initialize(pb.put("abstractSequentialList", "a,b,c,d").build(), this);
		assertEquals(4,abstractSequentialList.size());
	}
	
	@Test
	public void testAbstractList() {

		initializer.initialize(pb.put("abstractList", "a,b,c,d").build(), this);
		assertEquals(4,abstractList.size());
	}
	
	@Test
	public void testArrayList() {

		initializer.initialize(pb.put("arrayList", "a,b,c,d").build(), this);
		assertThat(arrayList, hasItems("a","b","c","d"));
	}
	
	@Test
	public void testList() {

		initializer.initialize(pb.put("list", "a,b,c,d,d,d").build(), this);
		assertThat(list, hasItems("a","b","c","d"));
		assertEquals(6,list.size());
	}
	
	@Test
	public void testSortedSet() {

		initializer.initialize(pb.put("sortedSet", "a,b,c,d,d,d").build(), this);
		assertThat(sortedSet, hasItems("a","b","c","d"));
		assertEquals(4,sortedSet.size());
	}
	
	@Test
	public void testSet() {

		initializer.initialize(pb.put("set", "a,b,c,d,d,d").build(), this);
		assertThat(set, hasItems("a","b","c","d"));
		assertEquals(4,set.size());
	}

}
