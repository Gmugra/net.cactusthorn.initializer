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

import static net.cactusthorn.initializer.InitializerException.StandardError.WRONG_VALUE;
import static net.cactusthorn.initializer.InitializerException.StandardError.WRONG_VALUE_AT_POSITION;

import java.util.*;
import java.lang.reflect.*;

import net.cactusthorn.initializer.InitializerException;
import net.cactusthorn.initializer.annotations.Info;

public class ListSetTypes  extends MultiValueTypes {
	
	@Override
	public ListSetTypes clone() throws CloneNotSupportedException {
		return (ListSetTypes)super.clone();
	}

	private ITypes set(ITypes initializer, List<Object> list, Info info, Class<?> collectionType, List<String> valueParts, int position) throws InitializerException {
		
		try {
			
			Value<?> created = initializer.createObject(collectionType, null, info, valueParts.get(position), null);
			if (created.isPresent() ) {
				
				list.add(created.get());
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
	
	private Constructor<?> getConstructor(Class<?> fieldType ) {
		
		Class<?> clazz = null;
		if (fieldType.isInterface() ) {
			
			if (List.class.isAssignableFrom(fieldType) ) {
				clazz = ArrayList.class;
			} else if (SortedSet.class.isAssignableFrom(fieldType) ) {
				clazz = TreeSet.class;
			} else if (Set.class.isAssignableFrom(fieldType) ) {
				clazz = HashSet.class;
			} else {
				return null;
			}
		} else {
		
			if (AbstractList.class.equals(fieldType) ) {
				clazz = ArrayList.class;
			} else if (AbstractSequentialList.class.equals(fieldType) ) {
				clazz = LinkedList.class;
			} else if (ArrayList.class.equals(fieldType) ) {
				clazz = ArrayList.class;
			} else if (LinkedList.class.equals(fieldType) ) {
				clazz = LinkedList.class;
			} else if (AbstractSet.class.equals(fieldType) ) {
				clazz = HashSet.class;
			} else if (HashSet.class.equals(fieldType) ) {
				clazz = HashSet.class;
			} else if (LinkedHashSet.class.equals(fieldType) ) {
				clazz = LinkedHashSet.class;
			} else if (TreeSet.class.equals(fieldType) ) {
				clazz = TreeSet.class;
			} else {
				return null;
			}
		}
	
		try {
			return clazz.getConstructor(Collection.class);
		} catch (NoSuchMethodException|SecurityException e) {
			return null;
		}
	}
	
	@Override
	public Value<?> createObject(Class<?> fieldType, Type fieldGenericType, Info info, String propertyValue, List<ITypes> availableTypes) throws InitializerException {
		
		if (availableTypes == null || availableTypes.isEmpty() || fieldGenericType == null) {
			return Value.empty();
		}
	
		Class<?> collectionType = getCollectionType(fieldGenericType);
		if (collectionType == null) {
			return Value.empty();
		}
		
		Constructor<?> constructor = getConstructor(fieldType);
		if (constructor == null) {
			return Value.empty();
		}
		
		List<String> valueParts = split(propertyValue);
		
		List<Object> list = new ArrayList<>();
		
		ITypes initializer = null;
		for (ITypes simple : availableTypes) {					
			
			initializer = set(simple, list, info, collectionType, valueParts, 0);
			if (initializer != null ) {
				break;
			}
		}
		if (initializer == null ) {
			return Value.empty();
		}
		
		for (int i = 1; i < valueParts.size(); i++ ) {
			set(initializer, list, info, collectionType, valueParts, i);
		}
	
		try {
			return Value.of(constructor.newInstance(list));
		} catch ( InvocationTargetException|IllegalAccessException|InstantiationException e ) {
			throw new InitializerException(info, e);
		}
		
	}
}
