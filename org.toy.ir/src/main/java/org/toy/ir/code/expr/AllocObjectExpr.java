package org.toy.ir.code.expr;

import org.toy.ir.code.CodeUnit;
import org.toy.ir.code.Expr;
import org.toy.ir.codegen.BytecodeFrontend;
import org.toy.stdlib.util.TabbedStringWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class AllocObjectExpr extends Expr {

	private Type type;

	public AllocObjectExpr(Type type) {
		super(ALLOC_OBJ);
		this.type = type;
	}

	@Override
	public Expr copy() {
		return new AllocObjectExpr(type);
	}

	@Override
	public Type getType() {
		return type;
	}
	
	public void setType(Type type) {
		this.type = type;
	}

	@Override
	public void onChildUpdated(int ptr) {
		raiseChildOutOfBounds(ptr);
	}
	
	@Override
	public Precedence getPrecedence0() {
		return Precedence.NEW;
	}

	@Override
	public void toString(TabbedStringWriter printer) {
		printer.print("new " + type.getClassName());		
	}

	@Override
	public void toCode(MethodVisitor visitor, BytecodeFrontend assembler) {
		visitor.visitTypeInsn(Opcodes.NEW, type.getInternalName());		
	}

	@Override
	public boolean canChangeFlow() {
		return false;
	}

	@Override
	public boolean equivalent(CodeUnit s) {
		return s instanceof AllocObjectExpr && type.equals(((AllocObjectExpr) s).type);
	}
}