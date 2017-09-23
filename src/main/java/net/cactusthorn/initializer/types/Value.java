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

public final class Value<T> {
	
	private static final Value<?> EMPTY = new Value<>();
	private static final Value<?> NULL = new Value<>(null);

	private final T newValue;
	private boolean isPresent;
	
	private Value() {
		newValue = null;
	}
	
	private Value(T newValue) {
		this.newValue = newValue;
		isPresent = true;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Value<T> of(T newValue ) {
		return newValue == null ? (Value<T>)NULL : new Value<>(newValue);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Value<T> empty() {
		return (Value<T>)EMPTY;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Value<T> _null() {
		return (Value<T>)NULL;
	}
	
	public T get() {
		return newValue;
	}
	
	public boolean isPresent() {
		return isPresent;
	}
}
