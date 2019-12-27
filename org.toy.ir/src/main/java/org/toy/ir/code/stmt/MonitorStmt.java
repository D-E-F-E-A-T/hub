package org.toy.ir.code.stmt;

import org.toy.ir.code.CodeUnit;
import org.toy.ir.code.Expr;
import org.toy.ir.code.Stmt;
import org.toy.ir.codegen.BytecodeFrontend;
import org.toy.stdlib.util.TabbedStringWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class MonitorStmt extends Stmt {

	public enum MonitorMode {
		ENTER, EXIT;
	}

	private Expr expression;
	private MonitorMode mode;

	public MonitorStmt(Expr expression, MonitorMode mode) {
		super(MONITOR);
		this.mode = mode;
		setExpression(expression);
	}

	public void setExpression(Expr expression) {
		writeAt(expression, 0);
	}

	public Expr getExpression() {
		return expression;
	}

	public MonitorMode getMode() {
		return mode;
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
		printer.print(mode == MonitorMode.ENTER ? "MONITORENTER" : "MONITOREXIT");
		printer.print('(');
		expression.toString(printer);
		printer.print(')');
		printer.print(';');		
	}

	@Override
	public void toCode(MethodVisitor visitor, BytecodeFrontend assembler) {
		expression.toCode(visitor, assembler);
		visitor.visitInsn(mode == MonitorMode.ENTER ? Opcodes.MONITORENTER : Opcodes.MONITOREXIT);		
	}

	@Override
	public boolean canChangeFlow() {
		return false;
	}

	@Override
	public MonitorStmt copy() {
		return new MonitorStmt(expression.copy(), mode);
	}

	@Override
	public boolean equivalent(CodeUnit s) {
		if(s instanceof MonitorStmt) {
			MonitorStmt mon = (MonitorStmt) s;
			return mode == mon.mode && expression.equivalent(mon.expression);
		}
		return false;
	}
}