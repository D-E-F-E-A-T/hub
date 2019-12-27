package org.toy.ir.code.expr.invoke;

import org.toy.app.service.InvocationResolver;
import org.toy.ir.code.Expr;
import org.toy.stdlib.collections.CollectionUtils;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.toy.asm.MethodNode;

import java.util.HashSet;
import java.util.Set;

public class StaticInvocationExpr extends InvocationExpr {
	public StaticInvocationExpr(Expr[] args, String owner, String name, String desc) {
		super(CallType.STATIC, args, owner, name, desc);
	}

	@Override
	public StaticInvocationExpr copy() {
		return new StaticInvocationExpr(copyArgs(), getOwner(), getName(), getDesc());
	}

	@Override
	public boolean isStatic() {
		return true;
	}

	@Override
	public boolean isDynamic() {
		return false;
	}

	@Override
	protected void generateCallCode(MethodVisitor visitor) {
		visitor.visitMethodInsn(Opcodes.INVOKESTATIC, getOwner(), getName(), getDesc(), getCallType() == CallType.INTERFACE);
	}
	
	@Override
	public Expr[] getPrintedArgs() {
		Expr[] result = new Expr[getArgumentExprs().length];
		System.arraycopy(getArgumentExprs(), 0, result, 0, getArgumentExprs().length);
		return result;
	}
	
	@Override
	public Set<MethodNode> resolveTargets(InvocationResolver res) {
		return resolveStaticCall(res, getOwner(), getName(), getDesc());
	}

	public static Set<MethodNode> resolveStaticCall(InvocationResolver res, String owner, String name, String desc) {
		return CollectionUtils.asCollection(HashSet::new, res.resolveStaticCall(owner, name, desc));
	}
}
