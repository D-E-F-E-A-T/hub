package org.toy.ir.code.stmt;

import org.toy.ir.code.CodeUnit;
import org.toy.ir.code.Expr;
import org.toy.ir.code.Stmt;
import org.toy.ir.codegen.BytecodeFrontend;
import org.toy.stdlib.util.TabbedStringWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ThrowStmt extends Stmt {

	private Expr expression;

	public ThrowStmt(Expr expression) {
		super(THROW);
		setExpression(expression);
	}

	public Expr getExpression() {
		return expression;
	}

	public void setExpression(Expr expression) {
		writeAt(expression, 0);
	}

	@Override
	public void onChildUpdated(int ptr) {
		if(ptr == 0) {
			expression = read(0);
		} else {
			raiseChildOutOfBounds(ptr);
		}
	}

	@Override
	public void toString(TabbedStringWriter printer) {
		printer.print("throw ");
		expression.toString(printer);
		printer.print(';');		
	}

	@Override
	public void toCode(MethodVisitor visitor, BytecodeFrontend assembler) {
		expression.toCode(visitor, assembler);
		visitor.visitInsn(Opcodes.ATHROW);		
	}

	@Override
	public boolean canChangeFlow() {
		return true;
	}

	@Override
	public ThrowStmt copy() {
		return new ThrowStmt(expression.copy());
	}

	@Override
	public boolean equivalent(CodeUnit s) {
		if(s instanceof ThrowStmt) {
			ThrowStmt thr = (ThrowStmt) s;
			return expression.equivalent(thr.expression);
		}
		return false;
	}
}