# net.cactusthorn.initializer

Initializer is Java library that can be used to dynamically initialize class attributes.

It's typical task to initialize some classes based on text data from, for example, configuration files.

Idea is, that configuration properties present in short and human readable way, 
when even multi-value properties are stored in one line. 
And then data is using to intitialize classes with complex types without any effort from the developer.

## Very simple example
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

	@InitPropertyName("check")
	private boolean isAvailable;
	
	@InitProperty
	private BigInteger[] values;
	
	@InitProperty(OPTIONAL)
	private Map<String,java.util.Date> dates;

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
	
	private static final Initializer initializer = new Initializer().setValuesSeparator(',').trimMultiValues(true);
	
	public static void main(String... args) {
		
		Map<String,String> prop = new HashMap<>();
		prop.put("check", "true");
		prop.put("values", "1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 7406529596973765");
		prop.put("dates", "first=2017-09-17T11:16:50+01:00 , secod=2017-10-05 , third=2017-09-17T11:16:50");
		
		MyClass myClass =  new MyClass();
	
		initializer.initialize(prop, myClass);
		
		System.out.println(myClass.toString());
	}
}
```
## What it can initialize for the moment?
1. all primitive and simple object types, StringBuffer, StringBuilder, BigDecimal, BigInteger, java.util.Date, java.sql.Date, java.util.Calendar  
2. one-dimensional arrays of any type which described above
3. basic Lists & Sets with generic of any types which described above
4. basic Maps with generic of any types which described above as key or value
5. Customizing date/time formats
6. Requirements policies for properties
7. Naming (@InitPropertyName)
8. thread safe
9. Easy extendable for new types
10. Check unit tests - a lot of examples there.

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