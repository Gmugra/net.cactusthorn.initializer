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
package net.cactusthorn.initializer;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import net.cactusthorn.initializer.annotations.*;
import net.cactusthorn.initializer.types.*;
import static net.cactusthorn.initializer.InitializerException.StandardError.WRONG_VALUE;

public class CustomTypesTest {
	
	static class MySimple {
		
		boolean bool;
		int $int;
		
		public MySimple(boolean bool, int $int) {
			this.bool = bool;
			this.$int = $int;
		}

		@Override public String toString() { return bool + "|" + $int; }

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof MySimple) {
				MySimple that = (MySimple)obj;
				return bool == that.bool && $int == that.$int;
			}
			return false;
		}

		@Override public int hashCode() { return $int; }
	}
	
	static class MySimpleType implements ITypes {
		
		@Override
		public MySimpleType clone() throws CloneNotSupportedException {
			return (MySimpleType)super.clone();
		}

		@Override
		public Value<?> createObject(
				Class<?> fieldType, 
				Type fieldGenericType,
				Info info, 
				String propertyValue,
				InitProperties initProperties,
				Collection<ITypes> availableTypes) throws InitializerException {
			
			boolean empty = propertyValue.isEmpty();
			
			if (MySimple.class.equals(fieldType) ) {
				
				if(empty) {
					return Value._null();
				}
				
				String[] parts = propertyValue.split(",");
				
				try {
					return Value.of(new MySimple(Boolean.valueOf(parts[0] ), Integer.valueOf(parts[1] ) ) );
				} catch (Exception e) {
					throw new InitializerException(info, WRONG_VALUE, e);
				}
			} 
			
			return Value.empty();			
		}
	}
	
	static class MyMultiType extends MapTypes {
		
		@SuppressWarnings("unchecked")
		@Override
		protected Constructor<? extends Map<Object,Object>> getConstructor(Class<?> fieldType) {
			
			if (!ConcurrentHashMap.class.isAssignableFrom(fieldType) ) {
				return null;
			} 
			
			try {
				return (Constructor<? extends Map<Object,Object>>)ConcurrentHashMap.class.getConstructor();
			} catch (NoSuchMethodException|SecurityException e) {
				return null;
			}
		}
		
	}
	
	@InitProperty
	MySimple mySimple;
	
	@InitProperty
	MySimple[] mySimpleArray;
	
	@InitProperty
	SortedMap<String,MySimple> mySimpleMap;
	
	@InitProperty
	ConcurrentHashMap<String,MySimple> myConcurrentMap;

	
	@Test
	public void testAll() throws URISyntaxException, IOException {
		
		MySimple[] correctArray = new MySimple[]{new MySimple(true, 200),new MySimple(true, 300),new MySimple(false, 700)};
		
		Path path = Paths.get(getClass().getClassLoader().getResource("init-custom.properties").toURI());
		
		InitProperties prop = new InitPropertiesBuilder().setValuesSeparator('/').trimMultiValues(true).load(path).build();
		
		new Initializer()
			.addTypes(MySimpleType.class)
			.addTypes(MyMultiType.class)
			.initialize(prop, this);
		
		assertEquals(3, myConcurrentMap.size());
		assertEquals(3, mySimpleMap.size());
		assertArrayEquals(correctArray, mySimpleArray);
		assertEquals("true|1000", mySimple.toString());
	}
	
	@Test
	public void testConcurrentMap() {
		
		InitProperties prop = 
			new InitPropertiesBuilder()
			.setValuesSeparator('/')
			.trimMultiValues(true)
			.put("myConcurrentMap", "A = true,200 / B = true,300 / C = false,500")
			.build();
		
		new Initializer().addTypes(MySimpleType.class).addTypes(MyMultiType.class).initialize(prop, this);
		
		assertEquals(3, myConcurrentMap.size());
	}
	
	@Test
	public void testSimpleMap() {
		
		InitProperties prop = 
				new InitPropertiesBuilder()
				.setValuesSeparator('/')
				.trimMultiValues(true)
				.put("mySimpleMap", "A = true,200 / B = true,300 / C = false,500")
				.build();
		
		new Initializer().addTypes(MySimpleType.class).initialize(prop, this);
		
		assertEquals(3, mySimpleMap.size());
	}
	
	@Test
	public void testSimpleArray() {
		
		MySimple[] correct = new MySimple[]{new MySimple(true, 200),new MySimple(true, 300),new MySimple(false, 500)};
		
		InitProperties prop = 
			new InitPropertiesBuilder().setValuesSeparator('/').put("mySimpleArray", "true,200/true,300/false,500").build();
		
		new Initializer().addTypes(MySimpleType.class).initialize(prop, this);
		
		assertArrayEquals(correct, mySimpleArray);
	}
	
	@Test
	public void testSimpleClass() {
			
		InitProperties prop = new InitPropertiesBuilder().put("mySimple", "true,200").build();
		
		new Initializer().addTypes(MySimpleType.class).initialize(prop, this);
		
		assertEquals("true|200", mySimple.toString());
	}
	
	@Test
	public void testSimpleObject() throws CloneNotSupportedException {
		
		MySimpleType mySimpleType = new MySimpleType();
		
		InitProperties props = new InitPropertiesBuilder().put("mySimple", "true,200").build();
		new Initializer().addTypes(mySimpleType).initialize(props, this);
		assertEquals("true|200", mySimple.toString());
	}
}
