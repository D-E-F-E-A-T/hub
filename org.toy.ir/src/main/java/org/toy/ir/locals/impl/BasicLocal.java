package org.toy.ir.locals.impl;

import org.toy.ir.locals.Local;

public class BasicLocal extends Local {

	public BasicLocal(int index) {
		this(index, false);
	}
	
	public BasicLocal(int index, boolean stack) {
		super(index, stack);
	}
}
