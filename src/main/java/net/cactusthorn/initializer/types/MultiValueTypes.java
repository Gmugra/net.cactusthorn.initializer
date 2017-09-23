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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public abstract class MultiValueTypes implements ITypes {

	@Override
	public ITypes clone() throws CloneNotSupportedException {
		return (ITypes)super.clone();
	}
	
	/*
	 *  Drop Escaping only for \\ \, and \=
	 */
	private String deleteEscapingComma(String str ) {
		return str.replace("\\\\","\\").replace("\\,",",");
	}
	private String deleteEscapingCommaEquals(String str ) {
		return str.replace("\\\\","\\").replace("\\,",",").replace("\\=","=");
	}
	
	protected List<String> split(String str) {
		
		List<String> arr = new ArrayList<>();
		
		int pos = 0;
		int bs_couner = 0;
		for (int i = 0 ; i < str.length(); i++ ) {
			
			if (str.charAt(i) == ',' && bs_couner % 2 == 0  ) {
				arr.add(deleteEscapingComma(str.substring(pos, i) ) );
				pos = i + 1;
				bs_couner = 0;
			} else {
				if (str.charAt(i) == '\\') { 
					bs_couner++;
				} else {
					bs_couner = 0;
				}
			}
		}
		arr.add(deleteEscapingComma(str.substring(pos ) ) );
	
	    return arr;
	}
	
	protected List<StringsPair> splitPairs(String str) {
		
		List<StringsPair> arr = new ArrayList<>();
		
		String currentKey = null;
		String lastValue = null;
		
		int pos = 0;
		int bs_couner = 0;
		for (int i = 0 ; i < str.length(); i++ ) {
			if (str.charAt(i) == ',' && bs_couner % 2 == 0 ) {
				
				if (!"".equals(currentKey) ) {
					lastValue = deleteEscapingCommaEquals(str.substring(pos, i) );
					arr.add(StringsPair.of(currentKey,lastValue));
				}
				currentKey = null;
				
				pos = i + 1;
				bs_couner = 0;
			} else if (str.charAt(i) == '=' && bs_couner % 2 == 0 ) {
				
				if (currentKey != null && !currentKey.isEmpty() && lastValue == null ) {
					arr.add(StringsPair.of(currentKey));
				}
				currentKey = deleteEscapingCommaEquals(str.substring(pos, i) );
				lastValue = null;
				
				pos = i + 1;
				bs_couner = 0;
			} else {
				if (str.charAt(i) == '\\') { 
					bs_couner++;
				} else {
					bs_couner = 0;
				}
			}
		}
		if (!"".equals(currentKey) ) {
			arr.add(StringsPair.of(currentKey, deleteEscapingCommaEquals(str.substring(pos ) ) ) );
		}
	    return arr;
	}
	
	protected Class<?> getCollectionType(Type fieldGenericType ) {
		
		ParameterizedType parameterizedType = null;
		if (ParameterizedType.class.isAssignableFrom(fieldGenericType.getClass())) {
			parameterizedType = (ParameterizedType)fieldGenericType;
		}
		
		if (parameterizedType == null ) {
			return null;
		} 
		
		Type genericType = parameterizedType.getActualTypeArguments()[0];
		if ("?".equals(genericType.toString())) {
			return null;
		}
	
		Class<?> collectionType = (Class<?>) genericType;
		if (Object.class.equals(collectionType)) {
			return null;
		}
		
		return collectionType;
	}

}
