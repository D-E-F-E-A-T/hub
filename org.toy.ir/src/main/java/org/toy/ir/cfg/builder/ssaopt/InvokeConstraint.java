package org.toy.ir.cfg.builder.ssaopt;

import org.toy.ir.code.CodeUnit;
import org.toy.ir.code.Opcode;

public class InvokeConstraint implements Constraint {
	@Override
	public boolean fails(CodeUnit s) {
		int op = s.getOpcode();
		return ConstraintUtil.isInvoke(op) || op == Opcode.FIELD_STORE || op == Opcode.ARRAY_STORE;
	}
}