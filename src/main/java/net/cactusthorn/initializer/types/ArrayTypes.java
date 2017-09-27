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

import java.util.Collection;
import java.util.List;
import java.lang.reflect.Array;
import java.lang.reflect.Type;

import net.cactusthorn.initializer.InitProperties;
import net.cactusthorn.initializer.InitializerException;
import net.cactusthorn.initializer.annotations.Info;

public class ArrayTypes extends MultiValueTypes {
		
	@Override
	public ArrayTypes clone() throws CloneNotSupportedException {
		return (ArrayTypes)super.clone();
	}
	
	@Override
	public Value<?> createObject(
		Class<?> fieldType, 
		Type fieldGenericType,
		Info info, 
		String propertyValue, 
		InitProperties initProperties, 
		Collection<ITypes> availableTypes) throws InitializerException {
		
		if (availableTypes == null || availableTypes.isEmpty() || !fieldType.isArray() ) {
			return Value.empty();
		}
		
		Class<?> arrayType = fieldType.getComponentType();
		
		if (arrayType.isArray() ) {
			//support only one-dimensional arrays
			return Value.empty();
		}
		
		List<String> valueParts = initProperties.getSplitter().split(propertyValue);
		
		//Empty value = 0 size array
		if (valueParts.size() == 1 && valueParts.get(0).isEmpty() ) {
			return Value.of(Array.newInstance(arrayType, 0 ) );
		}
		
		TypeValue typeValue = findType(info, valueParts.get(0), arrayType, initProperties, availableTypes);
		if (typeValue == null) {
			return Value.empty();
		}
		
		Object array = Array.newInstance(arrayType, valueParts.size());
		Array.set(array, 0, typeValue.value.get());
		
		for (int i = 1; i < valueParts.size(); i++ ) {
			Value<?> value = get(typeValue.type, info, valueParts.get(i), arrayType, initProperties);
			Array.set(array, i, value.get());
		}
		
		return Value.of(array);
	}
}
