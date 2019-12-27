package org.toy.deob.passes;

import org.toy.app.service.ApplicationClassSource;
import org.toy.context.AnalysisContext;
import org.toy.deob.IPass;
import org.toy.deob.PassContext;
import org.toy.deob.PassResult;
import org.toy.deob.intraproc.ExceptionAnalysis;
import org.toy.flowgraph.ExceptionRange;
import org.toy.ir.TypeUtils;
import org.toy.ir.cfg.BasicBlock;
import org.toy.ir.cfg.ControlFlowGraph;
import org.toy.ir.code.Stmt;
import org.toy.ir.utils.CFGUtils;
import org.toy.stdlib.collections.graph.GraphUtils;
import org.toy.stdlib.collections.graph.algorithms.TarjanSCC;
import org.objectweb.asm.Type;
import org.toy.asm.ClassNode;
import org.toy.asm.MethodNode;

import java.util.*;

public class DemoteRangesPass implements IPass {
	
	private static final String ERROR_CLASS = Error.class.getName().replace(".", "/");
	private static final String RTE_CLASS = RuntimeException.class.getName().replace(".", "/");
	
	@Override
	public PassResult accept(PassContext pcxt) {
		AnalysisContext cxt = pcxt.getAnalysis();
		for(ClassNode cn : cxt.getApplication().iterate()) {
			for(MethodNode m : cn.getMethods()) {
				ControlFlowGraph cfg = cxt.getIRCache().getFor(m);
				if(cfg.getRanges().size() > 0) {
					process(cxt.getApplication(), cfg, cxt.getExceptionAnalysis(cfg));
				}
			}
		}
		throw new UnsupportedOperationException("not done");
	}

	private void process(ApplicationClassSource app, ControlFlowGraph cfg, ExceptionAnalysis analysis) {
		TarjanSCC<BasicBlock> sccComputor = new TarjanSCC<>(cfg);
		for(BasicBlock b : cfg.vertices()) {
			if(sccComputor.low(b) == -1) {
				sccComputor.search(b);
			}
		}
		
		Map<BasicBlock, List<BasicBlock>> sccs = new HashMap<>();
		for(List<BasicBlock> l : sccComputor.getComponents()) {
			for(BasicBlock e : l) {
				if(sccs.containsKey(e)) {
					throw new IllegalStateException();
				} else {
					sccs.put(e, l);
				}
			}
		}
		
		for(ExceptionRange<BasicBlock> er : cfg.getRanges()) {
			/* go through the blocks in code order and
			 * try to promote them one at a time. if we
			 * can't promote the current block, stop
			 * trying for this range (breaking up the
			 * range causes more problems than it solves). */
			
			/* if the handler catches */
			for(BasicBlock b : er.getNodes()) {
				Set<Type> canThrow = new HashSet<>();
				
				List<BasicBlock> comp = new ArrayList<>();
				if(sccs.containsKey(b)) {
					comp.addAll(sccs.get(b));
				} else {
					comp.add(b);
				}
				for(BasicBlock e : comp) {
					for(Stmt stmt : e) {
						canThrow.addAll(analysis.getPossibleUserThrowables(stmt));
					}
				}
				
				
				if(!catchesAny(app, er.getTypes(), canThrow)) {
					if(comp.size() > 1) {
						System.out.println("promote: " + GraphUtils.toNodeArray(comp));
						for(BasicBlock e : comp) {
							System.out.println(CFGUtils.printBlock(e));
						}
						System.out.println(" canThrow: " + canThrow);
						System.out.println(" catching: " + er.getTypes());
						System.out.println();
						System.out.println();
						System.out.println();
						return;
					}
				} else {
					break;
				}
			}
		}
	}
	
	private boolean catchesAny(ApplicationClassSource app, Set<Type> catching, Set<Type> throwing) {
		for(Type _net : catching) {
			String net = _net.getInternalName();
			
			for(Type ball : throwing) {
				
				if(ball == TypeUtils.ANY) {
					return true;
				}
				
				ClassNode t = app.findClassNode(ball.getInternalName());
				for(;;) {
					if(t == null) {
						break;
					}
					
					if(t.getName().equals(net)) {
						return true;
					} else {
						// TODO: add flag for app support
						// and if we don't have a class
						// assume it does catch.
						t = app.findClassNode(t.node.superName);
					}
				}
			}
		}
		
		return false;
	}
}
