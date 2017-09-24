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

import net.cactusthorn.initializer.types.*;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	NameTest.class,
	PolicyTest.class,
	ExceptionTest.class,
	SplitTest.class,
	PrimitivesTest.class,
	SimplesTest.class,
	DateTimeTest.class,
	ArraysTest.class,
	ListSetTest.class,
	MapTest.class,
	CustomTypesTest.class
})
public class AllTests {
}
