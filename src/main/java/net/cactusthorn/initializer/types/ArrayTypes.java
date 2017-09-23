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

import java.util.List;
import java.lang.reflect.Array;
import java.lang.reflect.Type;

import net.cactusthorn.initializer.InitializerException;
import net.cactusthorn.initializer.annotations.Info;
import static net.cactusthorn.initializer.InitializerException.StandardError.*;

public class ArrayTypes extends MultiValueTypes {
		
	@Override
	public ArrayTypes clone() throws CloneNotSupportedException {
		return (ArrayTypes)super.clone();
	}

	private ITypes set(ITypes initializer, Object array, Info info, Class<?> arrayType, List<String> valueParts, int position) throws InitializerException {
		
		try {
			
			Value<?> created = initializer.createObject(arrayType, null, info, valueParts.get(position), null);
			if (created.isPresent() ) {
				Array.set(array, position, created.get());
				return initializer;
			}
			return null;
		} catch (InitializerException cie ) {
			if (cie.getStandardError() == WRONG_VALUE) {
				throw new InitializerException (info, WRONG_VALUE_AT_POSITION, cie.getRootСause(), position);
			}
			throw cie;
		}
	}
	
	@Override
	public Value<?> createObject(Class<?> fieldType, Type fieldGenericType, Info info, String propertyValue, List<ITypes> availableTypes) throws InitializerException {
	
		if (availableTypes == null || availableTypes.isEmpty() || !fieldType.isArray() ) {
			return Value.empty();
		}
		
		Class<?> arrayType = fieldType.getComponentType();
		
		if (arrayType.isArray() ) {
			//support only one-dimensional arrays
			return Value.empty();
		}
		
		List<String> valueParts = split(propertyValue);
		
		Object array = Array.newInstance(arrayType, valueParts.size());
		
		ITypes initializer = null;
		for (ITypes simple : availableTypes) {					
			
			initializer = set(simple, array, info, arrayType, valueParts, 0);
			if (initializer != null ) {
				break;
			}
		}
		if (initializer == null ) {
			return Value.empty();
		}
		
		for (int i = 1; i < valueParts.size(); i++ ) {
			set(initializer, array, info, arrayType, valueParts, i);
		}
		
		return Value.of(array);
	}
}
