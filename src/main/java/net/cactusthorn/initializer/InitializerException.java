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
package net.cactusthorn.initializer;

import net.cactusthorn.initializer.annotations.Info;


public class InitializerException extends RuntimeException {
	
	public enum StandardError {
		
		WRONG_VALUE ("Property \"{property}\"({bandle}) contain wrong value for the field \"{field}\" of the class {class}"),
		UNEXPECTED("Unexpected exception"),
		UNSUPPORTED_TYPE("The field \"{field}\" of the class {class} has unsupported type {type}"),
		REQUIRED_PROPERTY("The field \"{field}\" of the class {class} require property \"{property}\"({bandle})"),
		NOT_EMPTY_PROPERTY("The field \"{field}\" of the class {class} accept only property \"{property}\"({bandle}) with not empty value"),
	    UNPARSEABLE_DATETIME("Property \"{property}\"({bandle}) for the field \"{field}\" of the class {class} contain unparseable datetime value"),
		WRONG_VALUE_AT_POSITION("Property \"{property}\"({bandle}) contain wrong value at position:{position} for the field \"{field}\" of the class {class}");
		
		private String messageTemplate;
		
		private StandardError(String messageTemplate) {
			this.messageTemplate = messageTemplate;
		}
	}
	
	private static final long serialVersionUID = 0L;
	
	private static String LS = System.getProperty("line.separator");
	
	private StandardError standardError;
	
	private Exception rootСause; 
	
	//I know about String.format, but I hate unreadable place-holders
	private static String format(Info info, String messageTemplate, int position ) {
		
		if (info == null ) {
			return messageTemplate.replace("{position}", String.valueOf(position));
		}
		
		return
			messageTemplate
				.replace("{field}", info.getField().getName())
				.replace("{property}", info.getName())
				.replace("{bandle}", info.getConfigBundleName())
				.replace("{class}", info.getFieldClass().getName())	
				.replace("{type}", info.getField().getGenericType().getTypeName())
				.replace("{position}", String.valueOf(position));
	}
	
	public InitializerException(Info info, String messageTemplate, Exception e, int position ) {
		 super(format(info,messageTemplate + (e == null?"":LS + '\t' + e.toString() ), position) );
		 if (e != null ) {
			 this.setStackTrace(e.getStackTrace());
			 rootСause = e;
		 }
	}
	     
	public InitializerException(Info info, String messageTemplate) {
		 this(info,messageTemplate,null,0);
	}
	
	public InitializerException(Info info, StandardError standardError, Exception e, int position) {
		 this(info, standardError.messageTemplate, e, position );
		 this.standardError = standardError;
	}
	
	public InitializerException(Info info, StandardError standardError, Exception e) {
		 this(info, standardError, e, 0 );
	}
	
	public InitializerException(Info info, StandardError standardError, int position) {
		 this(info, standardError, null, position );
	}
	
	public InitializerException(Info info, StandardError standardError) {
        this(info, standardError, null, 0);
    }
    
    public InitializerException(Info info, Exception e) {
    	this(info, StandardError.UNEXPECTED, e, 0);
    }
    
    public InitializerException(Info info, Exception e, int position) {
    	this(info, StandardError.UNEXPECTED, e, position);
    }
	
	public StandardError getStandardError() {
		return standardError;
	}
	public Exception getRootСause() {
		return rootСause;
	}
}
