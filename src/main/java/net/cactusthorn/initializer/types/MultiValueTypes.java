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
import java.util.Collection;

import net.cactusthorn.initializer.InitProperties;
import net.cactusthorn.initializer.InitializerException;
import net.cactusthorn.initializer.annotations.Info;

public abstract class MultiValueTypes implements ITypes {
	
	@Override
	public ITypes clone() throws CloneNotSupportedException {
		return (ITypes)super.clone();
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
	
	protected TypeValue findType(
		Info info, 
		String value, 
		Class<?> genericClass,
		InitProperties initProperties,
		Collection<ITypes> availableTypes) throws InitializerException {
		
		try {
			for (ITypes type : availableTypes) {	
				Value<?> created = type.createObject(genericClass, null, info, value, initProperties, null);
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
	
	protected Value<?> get(ITypes type, Info info, String value, Class<?> genericClass, InitProperties initProperties) throws InitializerException {
		
		try {
			return type.createObject(genericClass, null, info, value, initProperties, null);
		} catch (InitializerException cie ) {
			if (cie.getStandardError() == WRONG_VALUE) {
				throw new InitializerException (info, WRONG_VALUE_AT_POSITION, cie.getRootСause(), 0);
			}
			throw cie;
		}
	}
}
