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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;

import net.cactusthorn.initializer.InitProperties;
import net.cactusthorn.initializer.InitializerException;
import net.cactusthorn.initializer.annotations.Info;
import static net.cactusthorn.initializer.InitializerException.StandardError.*;

public class SimpleTypes implements ITypes {
	
	@Override
	public SimpleTypes clone() throws CloneNotSupportedException {
		return (SimpleTypes)super.clone();
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
		
		try {
			if (String.class.equals(fieldType) ) {
				
				return Value.of(empty ? null : propertyValue);
			} 
			if (StringBuffer.class.equals(fieldType) ) {
				
				return Value.of(empty ? null : new StringBuffer(propertyValue));
			}
			if (StringBuilder.class.equals(fieldType) ) {
				
				return Value.of(empty ? null : new StringBuilder(propertyValue));
			} 
			if (Integer.class.equals(fieldType) ) {
				
				return Value.of(empty ? null : new Integer(propertyValue));
			} 
			if (Boolean.class.equals(fieldType) ) {
				
				return Value.of(empty ? null : new Boolean(propertyValue));
			} 
			if (Character.class.equals(fieldType) ) {
				
				return Value.of(empty ? null : new Character(propertyValue.charAt(0)));
			} 
			if (Byte.class.equals(fieldType) ) {
				
				return Value.of(empty ? null : new Byte(propertyValue));
			} 
			if (Long.class.equals(fieldType) ) {
				
				return Value.of(empty ? null : new Long(propertyValue));
			} 
			if (Short.class.equals(fieldType) ) {
				
				return Value.of(empty ? null : new Short(propertyValue));
			} 
			if (Float.class.equals(fieldType) ) {
				
				return Value.of(empty ? null : new Float(propertyValue));
			} 
			if (Double.class.equals(fieldType) ) {
			
				return Value.of(empty ? null : new Double(propertyValue));
			}
			if (BigDecimal.class.equals(fieldType) ) {
				
				return Value.of(empty ? null : new BigDecimal(propertyValue));
			}
			if (BigInteger.class.equals(fieldType) ) {
				
				return Value.of(empty ? null : new BigInteger(propertyValue));
			}
			
			return Value.empty();
			
		} catch (Exception e) {
			throw new InitializerException(info, WRONG_VALUE, e);
		}	
	}

}
