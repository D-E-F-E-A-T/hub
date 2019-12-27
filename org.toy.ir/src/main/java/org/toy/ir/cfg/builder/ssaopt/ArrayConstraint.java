package org.toy.ir.cfg.builder.ssaopt;

import org.toy.ir.code.CodeUnit;

public class ArrayConstraint implements Constraint {
	
	@Override
	public boolean fails(CodeUnit s) {
		return true;
	}
}