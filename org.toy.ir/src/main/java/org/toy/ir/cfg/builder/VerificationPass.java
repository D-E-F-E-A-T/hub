//package org.toy.ir.cfg.builder;
//
//import org.toy.ir.cfg.BasicBlock;
//import org.toy.ir.code.Stmt;
//
//public class VerificationPass extends ControlFlowGraphBuilder.BuilderPass {
//
//	private final String prev;
//
//	public VerificationPass(ControlFlowGraphBuilder builder, String prev) {
//		super(builder);
//		this.prev = prev;
//	}
//	
//	@Override
//	public void run() {
//		for (BasicBlock b : builder.graph.vertices()) {
//			for (Stmt s : b) {
//				s.spew("");
//				try {
//					s.checkConsistency();
//				} catch (RuntimeException e) {
//					throw new RuntimeException(s.toString() + " in #" + b.getId() + "\n  After " + prev, e);
//				}
//			}
//		}
//	}
//}
