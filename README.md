# net.cactusthorn.initializer

Initializer is Java library that can be used to dynamically initialize class attributes.

It's typical task to initialize some classes based on text data from, for example, configuration files.

Idea is, that configuration properties present in short and human readable way, 
when even multi-value properties are stored in one line. 
And then data is using to intitialize classes with complex types without any effort from the developer.

## Simple example
Let say we have the class:
```java
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Map;

import net.cactusthorn.initializer.annotations.*;
import static net.cactusthorn.initializer.annotations.InitPropertyPolicy.*;

/*
 * All what need to do is mark attributes with @InitProperty or @InitPropertyName annotations. That is all.
 */
public class MyClass {

	@InitPropertyName("check") private boolean isAvailable;
	
	@InitProperty private BigInteger[] values;
	
	@InitProperty(OPTIONAL) private Map<String,java.util.Date> dates;

	@Override
	public String toString() {
		return dates.toString() + " <-> " + isAvailable + " <-> " + Arrays.asList(values).toString();
	}	
}
```
Now Lets initialize it:
```java
import java.util.HashMap;
import java.util.Map;

import net.cactusthorn.initializer.Initializer;

public class MyInit {
	
	public static void main(String... args) {
		
		Map<String,String> prop = new HashMap<>();
		prop.put("check", "true");
		prop.put("values", "1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 7406529596973765");
		prop.put("dates", "first=2017-09-17T11:16:50+01:00 , second=2017-10-05 , third=2017-09-17T11:16:50");
		
		MyClass myClass =  new MyClass();
		
		InitProperties props = new InitPropertiesBuilder().setValuesSeparator(',').trimMultiValues(true).from(prop).build();
	
		new Initializer().initialize(props, myClass);
		
		System.out.println(myClass.toString());
	}
}
```
## Beans example
Properties:
```
test-bean.date = 2017-09-17T11:16:50+01:00
test-bean.map = A=10, B=20, C=30 
test-bean.sub-test-bean.name = Super Name
test-bean.sub-test-bean.values = 1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000
simple=SIMPLE
```
Test:
```java
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import net.cactusthorn.initializer.annotations.*;

public class BeanTest {

	static class SubTestBean {
		@InitProperty String name;
		@InitProperty int[] values;
	}

	static class TestBean {
		@InitProperty java.util.Date date;
		@InitProperty Map<String, Integer> map;
		@InitBean("sub-test-bean") SubTestBean subTestBean;
	}
	
	@InitBean("test-bean") TestBean testBean;
	
	@InitProperty String simple;
	
	@Test
	public void testBean() throws URISyntaxException, IOException {
		
		Path path = Paths.get(getClass().getClassLoader().getResource("init-bean.properties").toURI());
		
		new Initializer().trimMultiValues(true).initialize(InitProperties.load(path), this);
		
		assertEquals("Super Name", testBean.subTestBean.name);
		assertEquals(9, testBean.subTestBean.values.length);
		assertEquals(3, testBean.map.size());
		assertEquals("SIMPLE", simple);
	}
}
```
## Custom types example
Properties:
```
myConcurrentMap = A = true,200 / B = true,300 / C = false,500
mySimpleMap = C = true,200 / D = true,300 / F = false,500
mySimpleArray = true,200/true,300/false,700
mySimple = true,1000
```
Test:
```java
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
		public Value<?> createObject(Class<?> fieldType, Type fieldGenericType, Info info, 
			String propertyValue, List<ITypes> availableTypes) throws InitializerException {
			
			boolean empty = propertyValue.isEmpty();
			
			if (MySimple.class.equals(fieldType) ) {
				
				if(empty) {
					return Value._null();
				}
				
				String[] parts = propertyValue.split(",");
				
				try {
					return Value.of(
						new MySimple(
								Boolean.valueOf(parts[0] ), 
								Integer.valueOf(parts[1] ) ) 
						);
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
				return 
					(Constructor<? extends Map<Object,Object>>)
						ConcurrentHashMap.class.getConstructor();
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
		
		MySimple[] correctArray = 
			new MySimple[]{
					new MySimple(true, 200),
					new MySimple(true, 300),
					new MySimple(false, 700)
			};
		
		Path path = Paths.get(getClass().getClassLoader().getResource("init-custom.properties").toURI());
		
		new Initializer()
			.addTypes(MySimpleType.class)
			.addTypes(MyMultiType.class)
			.setValuesSeparator('/')
			.trimMultiValues(true)
			.initialize(InitProperties.load(path), this);
		
		assertEquals(3, myConcurrentMap.size());
		assertEquals(3, mySimpleMap.size());
		assertArrayEquals(correctArray, mySimpleArray);
		assertEquals("true|1000", mySimple.toString());
	}
}
```

## What it can initialize for the moment?
* all primitive and simple object types, StringBuffer, StringBuilder, BigDecimal, BigInteger, java.util.Date, java.sql.Date, java.util.Calendar  
* one-dimensional arrays of any type which described above
* basic Lists & Sets with generic of any types which described above
* basic Maps with generic of any types which described above as key or value
* customizing date/time patterns
* requirements policies for properties
* naming -> @InitPropertyName
* beans initialization (recursive) -> @InitBean
* initializaton by environment variables -> @InitEnvVariable 
* extendable for support new types, and new type will work with array, beans and coollection
* Check unit tests - a lot of examples there

## What it can NOT initialize
* globally : attributes based on **not static** inner classes

## License
Initializer is released under the BSD 2-Clause License
```
Copyright (C) 2017, Alexei Khatskevich
All rights reserved.

Licensed under the BSD 2-clause (Simplified) License (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
 
http://opensource.org/licenses/BSD-2-Clause
```