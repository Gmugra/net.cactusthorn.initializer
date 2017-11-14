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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import net.cactusthorn.initializer.InitializerException;
import net.cactusthorn.initializer.annotations.Info;
import net.cactusthorn.initializer.properties.InitProperties;

public class MapTypes extends MultiValueTypes {

	@Override
	public MapTypes clone() throws CloneNotSupportedException {
		return (MapTypes)super.clone();
	}
	
	@SuppressWarnings("unchecked")
	protected Constructor<? extends Map<Object,Object>> getConstructor(Class<?> fieldType) {
		
		Class<?> clazz = null;
		if (fieldType.isInterface() ) {
			
			if (ConcurrentNavigableMap.class.isAssignableFrom(fieldType) )  {
				clazz = ConcurrentSkipListMap.class;
			} else if (ConcurrentMap.class.isAssignableFrom(fieldType) )  {
				clazz = ConcurrentHashMap.class;
			} else if (NavigableMap.class.isAssignableFrom(fieldType) ) {
				clazz = TreeMap.class;
			} else if (SortedMap.class.isAssignableFrom(fieldType) ) {
				clazz = TreeMap.class;
			} else if (Map.class.isAssignableFrom(fieldType) ) {
				clazz = HashMap.class;
			} else {
				return null;
			}
		} else if ( !Modifier.isAbstract(fieldType.getModifiers() ) && Map.class.isAssignableFrom(fieldType) ) {
			
			clazz = fieldType;
		} else {
			return null;
		}
		
		try {
			return (Constructor<? extends Map<Object,Object>>)clazz.getConstructor();
		} catch (NoSuchMethodException|SecurityException e) {
			return null;
		}
	}
	
	@Override
	public Value<?> createObject(
		Class<?> fieldType, 
		Type fieldGenericType,
		Info info, 
		String propertyValue, 
		InitProperties initProperties, 
		Collection<ITypes> availableTypes) throws InitializerException {
		
		if (availableTypes == null || availableTypes.isEmpty() || fieldGenericType == null) {
			return Value.empty();
		}
		
		ParameterizedType parameterizedType = null;
		if (ParameterizedType.class.isAssignableFrom(fieldGenericType.getClass())) {
			parameterizedType = (ParameterizedType)fieldGenericType;
		} else {
			return Value.empty();
		}
		
		Type[] genericTypes = parameterizedType.getActualTypeArguments();
		if (genericTypes.length != 2) {
			return Value.empty();
		}
		
		Constructor<? extends Map<Object,Object>> constructor = getConstructor(fieldType);
		if (constructor == null) {
			return Value.empty();
		}
		
		Class<?> keyClass = getTypeClass(genericTypes[0]);
		if (keyClass == null) {
			return Value.empty();
		}
		
		Class<?> valueClass = getTypeClass(genericTypes[1]);
		if (valueClass == null) {
			return Value.empty();
		}
		
		List<StringsPair> pairs = initProperties.getSplitter().splitPairs(propertyValue);
		
		Map<Object,Object> newMap = null;
		try {
			newMap = constructor.newInstance();
		} catch ( InvocationTargetException|IllegalAccessException|InstantiationException e ) {
			throw new InitializerException(info, e);
		}
		
		//Empty value = empty map
		if (pairs.size() == 0 ) {
			return Value.of(newMap);
		}
		
		TypeValue keyTypeValue = findType(info, pairs.get(0).getKey(), keyClass, initProperties, availableTypes);
		if (keyTypeValue == null) {
			return Value.empty();
		}
		
		TypeValue valueTypeValue = findType(info, pairs.get(0).getValue(), valueClass, initProperties, availableTypes);
		if (valueTypeValue == null) {
			return Value.empty();
		}
		
		newMap.put(keyTypeValue.value.get(), valueTypeValue.value.get());
		
		for (int i = 1; i < pairs.size(); i++ ) {
			Value<?> key = get(keyTypeValue.type, info, pairs.get(i).getKey(), i, keyClass, initProperties);
			Value<?> value = get(valueTypeValue.type, info, pairs.get(i).getValue(), i, valueClass, initProperties);
			newMap.put(key.get(), value.get());
		}
		
		return Value.of(newMap);
	}
}
