package org.toy.ir.code.stmt.copy;

import org.toy.ir.code.CodeUnit;
import org.toy.ir.code.Expr;
import org.toy.ir.code.expr.PhiExpr;
import org.toy.ir.code.expr.VarExpr;

public class CopyVarStmt extends AbstractCopyStmt {

	public CopyVarStmt(VarExpr variable, Expr expression) {
		super(LOCAL_STORE, variable, expression);
	}
	
	public CopyVarStmt(VarExpr variable, Expr expression, boolean synthetic) {
		super(LOCAL_STORE, variable, expression, synthetic);
		assert (!(expression instanceof PhiExpr));
	}

	@Override
	public CopyVarStmt copy() {
		return new CopyVarStmt(getVariable().copy(), getExpression().copy(), isSynthetic());
	}

	@Override
	public boolean equivalent(CodeUnit s) {
		if(s instanceof CopyVarStmt) {
			CopyVarStmt copy = (CopyVarStmt) s;
			return getExpression().equivalent(copy.getExpression()) && getVariable().equivalent(copy.getVariable());
		}
		return false;
	}
}
