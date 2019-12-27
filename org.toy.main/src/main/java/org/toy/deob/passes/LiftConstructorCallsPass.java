package org.toy.deob.passes;

import org.toy.context.AnalysisContext;
import org.toy.deob.IPass;
import org.toy.deob.PassContext;
import org.toy.deob.PassResult;
import org.toy.flowgraph.edges.FlowEdge;
import org.toy.flowgraph.edges.FlowEdges;
import org.toy.ir.cfg.BasicBlock;
import org.toy.ir.cfg.ControlFlowGraph;
import org.toy.ir.code.Expr;
import org.toy.ir.code.Opcode;
import org.toy.ir.code.Stmt;
import org.toy.ir.code.expr.VarExpr;
import org.toy.ir.code.expr.invoke.InvocationExpr;
import org.toy.ir.locals.Local;
import org.toy.ir.utils.CFGUtils;
import org.toy.asm.ClassNode;
import org.toy.asm.MethodNode;

import java.util.Set;

public class LiftConstructorCallsPass implements Opcode, IPass {

	@Override
	public PassResult accept(PassContext pcxt) {
		AnalysisContext cxt = pcxt.getAnalysis();
		//int delta = 0;
		
		for(ClassNode cn : cxt.getApplication().iterate()) {
			for(MethodNode m : cn.getMethods()) {
				if(m.getName().equals("<init>")) {
					ControlFlowGraph cfg = cxt.getIRCache().getFor(m);
					if(tryLift(m, cfg)) {
						//delta++;
					}
				}
			}
		}
		
		return PassResult.with(pcxt, this).finished().make();
	}
	
	private boolean tryLift(MethodNode m, ControlFlowGraph cfg) {
		Local lvar0_0 = cfg.getLocals().get(0, 0, false);
		
		/* only contains synthetic copies */
		BasicBlock entry = cfg.getEntries().iterator().next();
		
		for(BasicBlock b : cfg.vertices()) {
			for(Stmt stmt : b) {
				for(Expr e : stmt.enumerateOnlyChildren()) {
					if(e.getOpcode() == INVOKE) {
						InvocationExpr invoke = (InvocationExpr) e;
						
						if(invoke.getOwner().equals(m.owner.node.superName) && invoke.getName().equals("<init>")) {
							assert(invoke.getCallType() != InvocationExpr.CallType.DYNAMIC);
							assert (invoke.getCallType() == InvocationExpr.CallType.SPECIAL);
							
							Expr p1 = invoke.getPhysicalReceiver();
							
							if(p1.getOpcode() == LOCAL_LOAD && ((VarExpr) p1).getLocal() == lvar0_0) {
								
								Set<FlowEdge<BasicBlock>> predsEdges = cfg.getReverseEdges(b);
								FlowEdge<BasicBlock> incoming;
								if(predsEdges.size() == 1 && ((incoming = predsEdges.iterator().next()).getType() == FlowEdges.IMMEDIATE) && incoming.src() == entry) {
									// BasicBlock liftBlock = new BasicBlock(cfg, cfg.vertices().size() + 1, new LabelNode());
									
									/* split the block before the invocation and 
									 * insert a new block. */
									// todo: convert to CFGUtils
									split(cfg, b, stmt);
									
									return true;
								} else {
									System.err.printf(" warn(nolift) for %s in %n%s%n", invoke, CFGUtils.printBlock(b));
									System.err.printf("  preds: %s%n", predsEdges);
								}
							} else {
								throw new IllegalStateException(String.format("broken super call: %s", invoke));
							}
						}
					}
				}
			}
		}
		
		return false;
	}
	
	private void split(ControlFlowGraph cfg, BasicBlock b, Stmt at) {
		BasicBlock newBlock = new BasicBlock(cfg);
		cfg.addVertex(newBlock);
		
		System.out.println(CFGUtils.printBlock(b));
		System.out.println("  to " + at);
		int index = b.indexOf(at) + 1;
		int size = b.size();
		for(int i=index; i < size; i++) {
			Stmt stmt = b.remove(index);
			newBlock.add(stmt);
		}
	}
}
