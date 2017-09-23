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

import java.lang.reflect.Type;
import java.util.List;

import net.cactusthorn.initializer.InitializerException;
import net.cactusthorn.initializer.annotations.Info;
import static net.cactusthorn.initializer.InitializerException.StandardError.*;

public class PrimitiveTypes implements ITypes {
	
	@Override
	public PrimitiveTypes clone() throws CloneNotSupportedException {
		return (PrimitiveTypes)super.clone();
	}

	@Override
	public Value<?> createObject(Class<?> fieldType, Type fieldGenericType, Info info, String propertyValue, List<ITypes> availableTypes) throws InitializerException {
		
		boolean empty = propertyValue.isEmpty();
		
		try {
			
			if (int.class.equals(fieldType) ) {
				
				return Value.of(empty ? 0 : Integer.valueOf(propertyValue) );
			}
			if (boolean.class.equals(fieldType) ) {
				
				return Value.of(empty ? false : Boolean.valueOf(propertyValue));
			} 
			if (char.class.equals(fieldType) ) {
				
				return Value.of(empty ? Character.MIN_VALUE : propertyValue.charAt(0));
			} 
			if (byte.class.equals(fieldType) ) {
				
				return Value.of(empty ? (byte)0 : Byte.valueOf(propertyValue));
			} 
			if (long.class.equals(fieldType) ) {
				
				return Value.of(empty ? 0L : Long.valueOf(propertyValue));
			} 
			if (short.class.equals(fieldType) ) {
				
				return Value.of(empty ? (short)0 : Short.valueOf(propertyValue));
			}
			if (float.class.equals(fieldType) ) {
				
				return Value.of(empty ? 0.0f : Float.valueOf(propertyValue));
			}
			if (double.class.equals(fieldType) ) {
			
				return Value.of(empty ? 0.0d : Double.valueOf(propertyValue));
			}
		
			return Value.empty();
			
		} catch (Exception e) {
			throw new InitializerException(info, WRONG_VALUE, e);
		}
	}
}
