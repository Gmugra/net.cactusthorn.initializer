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
package net.cactusthorn.initializer.annotations;;

public enum InitPropertyPolicy {

	REQUIRED, //required, but CAN BE empty
	OPTIONAL, 
	NOT_EMPTY, //not requited, but if present MUST BE not empty (default policy)
	REQUIRED_NOT_EMPTY
}
