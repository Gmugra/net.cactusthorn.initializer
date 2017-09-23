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

public class StringsPair {

	private String key;
	private String value;
	
	private StringsPair(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	public static StringsPair of(String key, String value) {
		return new StringsPair(key, value);
	}
	
	public static StringsPair of(String key) {
		return new StringsPair(key, "");
	}
	
	public String key() {
		return key;
	}
	
	public String value() {
		return value;
	}
	
	@Override
	public String toString() {
		return "{"+ key + ":" + value +"}";
	}
}
