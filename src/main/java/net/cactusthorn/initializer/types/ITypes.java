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

import java.lang.reflect.*;
import java.util.List;

import net.cactusthorn.initializer.InitializerException;
import net.cactusthorn.initializer.annotations.Info;

public interface ITypes extends Cloneable {
	
	ITypes clone() throws CloneNotSupportedException;
	
	default ITypes addDateTimeFormatPattern(String formatPattern) {
		return this;
	}
	
	default ITypes setValuesSeparator(char separator) {
		return this;
	}
	
	default ITypes setPairSeparator(char separator) {
		return this;
	}
	
	default ITypes trimMultiValues(boolean trimMultiValues) {
		return this;
	}
	
	default boolean setObject(Object object, Field field, Info info, String propertyValue, List<ITypes> availableTypes) 
		throws InitializerException {
		
		Value<?> newValue = createObject(field.getType(), field.getGenericType(), info, propertyValue, availableTypes);
		if (!newValue.isPresent()) {
			return false;
		}
		
		boolean accessible = field.isAccessible();
		field.setAccessible(true);
		try {
			field.set(object, newValue.get());
			field.setAccessible(accessible);
		} catch (IllegalArgumentException|IllegalAccessException e) {
			field.setAccessible(accessible);
			throw new InitializerException(info, e);
		}
		
		return true;
	}
	
	Value<?> createObject(Class<?> fieldType, Type fieldGenericType, Info info, String propertyValue, List<ITypes> availableTypes) throws InitializerException;
	
}
