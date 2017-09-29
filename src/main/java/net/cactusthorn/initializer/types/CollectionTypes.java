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

import java.util.*;
import java.util.concurrent.*;
import java.beans.beancontext.*;
import java.lang.reflect.*;

import net.cactusthorn.initializer.InitializerException;
import net.cactusthorn.initializer.annotations.Info;
import net.cactusthorn.initializer.properties.InitProperties;

public class CollectionTypes extends MultiValueTypes {
	
	@Override
	public CollectionTypes clone() throws CloneNotSupportedException {
		return (CollectionTypes)super.clone();
	}
	
	@SuppressWarnings({ "unchecked"})
	protected Constructor<? extends Collection<Object>> getConstructor(Info info, Class<?> fieldType ) {
		
		Class<?> clazz = null;
		if (fieldType.isInterface() ) {
			
			if (List.class.isAssignableFrom(fieldType) ) {
				clazz = ArrayList.class;
			} else if (NavigableSet.class.isAssignableFrom(fieldType) ) {
				clazz = TreeSet.class;
			} else if (SortedSet.class.isAssignableFrom(fieldType) ) {
				clazz = TreeSet.class;
			} else if (Set.class.isAssignableFrom(fieldType) ) {
				clazz = HashSet.class;
			} else if (BlockingDeque.class.isAssignableFrom(fieldType) ) {
				clazz = LinkedBlockingDeque.class;
			} else if (TransferQueue.class.isAssignableFrom(fieldType) ) {
				clazz = LinkedTransferQueue.class;
			} else if (BlockingQueue.class.isAssignableFrom(fieldType) ) {
				clazz = LinkedBlockingDeque.class;
			} else if (Deque.class.isAssignableFrom(fieldType) ) {
				clazz = ArrayDeque.class;
			} else if (Queue.class.isAssignableFrom(fieldType) ) {
				clazz = LinkedList.class;
			} else if (BeanContextServices.class.isAssignableFrom(fieldType) ) {
				clazz = BeanContextServicesSupport.class;
			} else if (BeanContext.class.isAssignableFrom(fieldType) ) {
				clazz = BeanContextSupport.class;
			} else if (Collection.class.isAssignableFrom(fieldType) ) {
				clazz = ArrayList.class;
			} else {
				return null;
			}
		} else if ( !Modifier.isAbstract(fieldType.getModifiers() ) && Collection.class.isAssignableFrom(fieldType) ) {
			
			clazz = fieldType;
		} else {
			return null;
		}
		
		try {
			return (Constructor<? extends Collection<Object>>)clazz.getConstructor();
		} catch (NoSuchMethodException|SecurityException e) {
			throw new InitializerException(info, e);
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
			return  Value.empty();
		}
		
		Type[] genericTypes = parameterizedType.getActualTypeArguments();
		if (genericTypes.length != 1) {
			return Value.empty();
		}
		
		Constructor<? extends Collection<Object>> constructor = getConstructor(info, fieldType);
		if (constructor == null) {
			return Value.empty();
		}
		
		Class<?> collectionClass = getTypeClass(genericTypes[0]);
		if (collectionClass == null) {
			return Value.empty();
		}
		
		List<String> valueParts = initProperties.getSplitter().split(propertyValue);
		
		TypeValue typeValue = findType(info, valueParts.get(0), collectionClass, initProperties, availableTypes);
		if (typeValue == null) {
			return Value.empty();
		}
		
		Collection<Object> newCollection = null;
		try {
			newCollection = constructor.newInstance();
		} catch ( InvocationTargetException|IllegalAccessException|InstantiationException e ) {
			throw new InitializerException(info, e);
		}
		
		//Empty value = empty collection
		if (valueParts.size() == 1 && valueParts.get(0).isEmpty() ) {
			return Value.of(newCollection);
		}
		
		newCollection.add(typeValue.value.get() );
		
		for (int i = 1; i < valueParts.size(); i++ ) {
			Value<?> value = get(typeValue.type, info, valueParts.get(i), collectionClass, initProperties);
			newCollection.add(value.get() );
		}
		
		return Value.of(newCollection);
	}
}
