package org.toy.ir.code.expr;

import org.toy.ir.code.CodeUnit;
import org.toy.ir.code.Expr;
import org.toy.ir.codegen.BytecodeFrontend;
import org.toy.stdlib.util.TabbedStringWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public class CaughtExceptionExpr extends Expr {

	private Type type;
	
	private CaughtExceptionExpr(Type type) {
		super(CATCH);
		this.type = type;
	}

	public CaughtExceptionExpr(String type) {
		super(CATCH);
		if (type == null) {
			type = "Ljava/lang/Throwable;";
		} else {
			type = "L" + type + ";";
		}
		this.type = Type.getType(type);
	}

	@Override
	public Expr copy() {
		return new CaughtExceptionExpr(type);
	}
	
	public void setType(Type type) {
		this.type = type;
	}

	@Override
	public Type getType() {
		return type;
	}

	@Override
	public void onChildUpdated(int ptr) {
		raiseChildOutOfBounds(ptr);
	}

	@Override
	public void toString(TabbedStringWriter printer) {
		printer.print("catch()");
	}

	@Override
	public void toCode(MethodVisitor visitor, BytecodeFrontend assembler) {
		
	}

	@Override
	public boolean canChangeFlow() {
		return false;
	}

	@Override
	public boolean equivalent(CodeUnit s) {
		if(s.getOpcode() == CATCH) {
			CaughtExceptionExpr e = (CaughtExceptionExpr) s;
			return type.equals(e.type);
		} else {
			return false;
		}
	}
}