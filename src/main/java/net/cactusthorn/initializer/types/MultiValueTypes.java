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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import net.cactusthorn.initializer.InitializerException;
import net.cactusthorn.initializer.annotations.Info;

public abstract class MultiValueTypes implements ITypes {

	private char valuesSep = ',';
	private String valuesSepAsStr = ",";
	private String valuesSepAsEscStr = "\\,";
	
	private char pairSep = '=';
	private String pairSepAsStr = "=";
	private String pairSepAsEscStr = "\\=";
	
	private boolean trimMultiValues;
	
	@Override
	public ITypes clone() throws CloneNotSupportedException {
		return (ITypes)super.clone();
	}
	
	@Override
	public ITypes setValuesSeparator(char separator) {
		this.valuesSep = separator;
		this.valuesSepAsStr = String.valueOf(this.valuesSep );
		this.valuesSepAsEscStr = "\\" + this.valuesSep;
		return this;
	}
	
	@Override
	public ITypes setPairSeparator(char separator) {
		this.pairSep = separator;
		this.pairSepAsStr = String.valueOf(this.pairSep );
		this.pairSepAsEscStr = "\\" + this.pairSep;
		return this;
	}
	
	@Override
	public ITypes trimMultiValues(boolean trimMultiValues) {
		this.trimMultiValues = trimMultiValues;
		return this;
	}
	
	/*
	 *  Drop Escaping only for \\ \, and \=
	 */
	//TODO need something more clever: too many replaces
	private String deleteEscapingComma(String str ) {
		String result = str.replace("\\\\","\\").replace(valuesSepAsEscStr, valuesSepAsStr);
		return trimMultiValues ? result.trim() : result; 
	}
	private String deleteEscapingCommaEquals(String str ) {
		String result = str.replace("\\\\","\\").replace(valuesSepAsEscStr, valuesSepAsStr).replace(pairSepAsEscStr,pairSepAsStr);
		return trimMultiValues ? result.trim() : result; 
	}
	
	protected List<String> split(String str) {
		
		List<String> arr = new ArrayList<>();
		
		int pos = 0;
		int bs_couner = 0;
		for (int i = 0 ; i < str.length(); i++ ) {
			
			if (str.charAt(i) == valuesSep && bs_couner % 2 == 0  ) {
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
			if (str.charAt(i) == valuesSep && bs_couner % 2 == 0 ) {
				
				if (!"".equals(currentKey) ) {
					lastValue = deleteEscapingCommaEquals(str.substring(pos, i) );
					arr.add(StringsPair.of(currentKey,lastValue));
				}
				currentKey = null;
				
				pos = i + 1;
				bs_couner = 0;
			} else if (str.charAt(i) == pairSep && bs_couner % 2 == 0 ) {
				
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
		if (!"".equals(currentKey) && currentKey != null  ) {
			arr.add(StringsPair.of(currentKey, deleteEscapingCommaEquals(str.substring(pos ) ) ) );
		}
	    return arr;
	}
	
	protected Class<?> getTypeClass(Type genericType ) {
		
		if ("?".equals(genericType.getTypeName() ) ) {
			return null;
		}
	
		Class<?> genericTypeClass = (Class<?>) genericType;
		if (Object.class.equals(genericTypeClass)) {
			return null;
		}
		
		return genericTypeClass;
	}

	protected class TypeValue {
		protected ITypes type;
		protected Value<?> value;
		protected TypeValue(ITypes type, Value<?> value) {
			this.type = type;
			this.value = value;
		}
	}
	
	protected TypeValue findType(Info info, String value, Class<?> genericClass, List<ITypes> availableTypes) throws InitializerException {
		
		try {
			for (ITypes type : availableTypes) {	
				Value<?> created = type.createObject(genericClass, null, info, value, null);
				if (created.isPresent() ) {
					return new TypeValue(type, created);
				}
			}
			return null;
		} catch (InitializerException cie ) {
			if (cie.getStandardError() == WRONG_VALUE) {
				throw new InitializerException (info, WRONG_VALUE_AT_POSITION, cie.getRootСause(), 0);
			}
			throw cie;
		}
	}
	
	protected Value<?> get(ITypes type, Info info, String value, Class<?> genericClass) throws InitializerException {
		
		try {
			return type.createObject(genericClass, null, info, value, null);
		} catch (InitializerException cie ) {
			if (cie.getStandardError() == WRONG_VALUE) {
				throw new InitializerException (info, WRONG_VALUE_AT_POSITION, cie.getRootСause(), 0);
			}
			throw cie;
		}
	}
}
