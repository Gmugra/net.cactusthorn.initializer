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
package net.cactusthorn.initializer.properties;

import static org.junit.Assert.*;

import java.util.List;

import net.cactusthorn.initializer.properties.Splitter;
import net.cactusthorn.initializer.types.StringsPair;

import org.junit.Test;

public class SplitTest {

	Splitter splitter = new Splitter(',','=',false);
	
	@Test
	public void testPairMultiEquals() {
		
		String correct = "[{aa:}, {bb:20}, {cc:}, {xxx:345}]";
		List<StringsPair> result = splitter.splitPairs("aa====bb,bb=20,cc==we\\,er,xxx=345"); 
		//System.out.println(result);
		assertEquals(correct, result.toString());
	}
		
	@Test
	public void testPairSimpleEscaping() {
		
		String correct = "[{aa=bb:cc}, {bb:20}, {cc:we,er}, {xxx:345}]";
		List<StringsPair> result = splitter.splitPairs("aa\\=bb=cc,bb=20,cc=we\\,er,xxx=345"); 
		assertEquals(correct, result.toString());
	}
		
	@Test
	public void testSimplePair() {
		
		String correct = "[{aa:10}, {bb:20}, {cc:weer}, {xxx:345}]";
		List<StringsPair> result = splitter.splitPairs("aa=10,bb=20,cc=weer,xxx=345"); 
		assertEquals(correct, result.toString());
	}
		
	@Test
	public void testEmpty() {
		
		String[] correct = new String[]{""};
		List<String> result = splitter.split(""); 
		assertArrayEquals(correct, result.toArray());
	}
		
	@Test
	public void testSimple() {
	
		String[] correct = new String[]{"abc","drf","dft","gfff"};
		List<String> result = splitter.split("abc,drf,dft,gfff"); 
		assertArrayEquals(correct, result.toArray());
	}
	
	@Test
	public void testEscape() {
	
		String[] correct = new String[]{"abc,drf","dft","gfff"};
		List<String> result = splitter.split("abc\\,drf,dft,gfff"); 
		//System.out.println(Arrays.asList(result));
		assertArrayEquals(correct, result.toArray());
	}
	
	@Test
	public void testEscapeSlash() {
		
		String[] correct = new String[]{"abc\\","drf","dft,gfff"};
		List<String> result = splitter.split("abc\\\\,drf,dft\\,gfff");
		//System.out.println(Arrays.asList(result));
		assertArrayEquals(correct, result.toArray());
	}
	
	@Test
	public void testMegaSlash() {
		
		String[] correct = new String[]{"\\\\","\\"};
		List<String> result = splitter.split("\\\\\\\\,\\\\"); 
		assertArrayEquals(correct, result.toArray());
	}

	@Test
	public void testComma() {
		
		String[] correct = new String[]{"",""};
		List<String> result = splitter.split(","); 
		assertArrayEquals(correct, result.toArray());
	}
	
	@Test
	public void testMegaComma() {
		
		String[] correct = new String[]{"ABC","","","DEF",""};
		List<String> result = splitter.split("ABC,,,DEF,"); 
		assertArrayEquals(correct, result.toArray());
	}
	
	@Test
	public void testSingle() {
		
		String[] correct = new String[]{"ABC"};
		List<String> result = splitter.split("ABC");
		assertArrayEquals(correct, result.toArray());
	}
}
