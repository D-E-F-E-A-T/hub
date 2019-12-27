package org.toy.ir.cfg.builder.ssaopt;

import org.toy.ir.code.CodeUnit;

public interface Constraint {
	boolean fails(CodeUnit s);
}