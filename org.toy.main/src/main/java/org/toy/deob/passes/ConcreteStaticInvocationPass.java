package org.toy.deob.passes;

import org.toy.app.service.InvocationResolver;
import org.toy.context.AnalysisContext;
import org.toy.deob.IPass;
import org.toy.deob.PassContext;
import org.toy.deob.PassResult;
import org.toy.ir.cfg.BasicBlock;
import org.toy.ir.cfg.ControlFlowGraph;
import org.toy.ir.code.Expr;
import org.toy.ir.code.Opcode;
import org.toy.ir.code.Stmt;
import org.toy.ir.code.expr.invoke.InvocationExpr;
import org.toy.asm.ClassNode;
import org.toy.asm.MethodNode;

public class ConcreteStaticInvocationPass implements IPass {

	@Override
	public PassResult accept(PassContext pcxt) {
		AnalysisContext cxt = pcxt.getAnalysis();
		int fixed = 0;
		
		InvocationResolver resolver = cxt.getInvocationResolver();
		
		for(ClassNode cn : cxt.getApplication().iterate()) {
			for(MethodNode mn : cn.getMethods()) {
				ControlFlowGraph cfg = cxt.getIRCache().getFor(mn);
				
				for(BasicBlock b : cfg.vertices()) {
					for(Stmt stmt : b) {
						for(Expr e : stmt.enumerateOnlyChildren()) {
							if(e.getOpcode() == Opcode.INVOKE) {
								InvocationExpr invoke = (InvocationExpr) e;
								
								if(invoke.getCallType() == InvocationExpr.CallType.STATIC) {
									MethodNode invoked = resolver.resolveStaticCall(invoke.getOwner(), invoke.getName(), invoke.getDesc());
									
									if(invoked != null) {
										if(!invoked.getOwner().equals(invoke.getOwner())) {
											invoke.setOwner(invoked.getOwner());
											fixed++;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		System.out.printf("  corrected %d dodgy static calls.%n", fixed);
		
		return PassResult.with(pcxt, this).finished().make();
	}
}
