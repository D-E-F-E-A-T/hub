package org.toy.ir.code.stmt;

import org.toy.ir.code.CodeUnit;
import org.toy.ir.code.Stmt;
import org.toy.ir.codegen.BytecodeFrontend;
import org.toy.stdlib.util.TabbedStringWriter;
import org.objectweb.asm.MethodVisitor;

public class NopStmt extends Stmt {
	public NopStmt() {
		super(NOP);
	}

	@Override
	public void onChildUpdated(int ptr) {
		raiseChildOutOfBounds(ptr);
	}

	@Override
	public void toString(TabbedStringWriter printer) {
		printer.print("nop;");
	}

	@Override
	public void toCode(MethodVisitor visitor, BytecodeFrontend assembler) {
	}

	@Override
	public boolean canChangeFlow() {
		return false;
	}
	
	@Override
	public NopStmt copy() {
		return new NopStmt();
	}

	@Override
	public boolean equivalent(CodeUnit s) {
		return s instanceof NopStmt;
	}
}