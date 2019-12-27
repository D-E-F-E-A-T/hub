package org.toy.deob.passes;

import java.util.Map.Entry;

import org.toy.context.AnalysisContext;
import org.toy.deob.IPass;
import org.toy.deob.PassContext;
import org.toy.deob.PassResult;
import org.toy.ir.cfg.ControlFlowGraph;
import org.toy.stdlib.collections.graph.GraphUtils;
import org.toy.asm.MethodNode;

public class DetectIrreducibleFlowPass implements IPass {

	@Override
	public String getId() {
		return "Detect-Irreducible-Flow";
	}
	
	@Override
	public PassResult accept(PassContext pcxt) {
		AnalysisContext cxt = pcxt.getAnalysis();
		for(Entry<MethodNode, ControlFlowGraph> e : cxt.getIRCache().entrySet()) {
			MethodNode mn = e.getKey();
			ControlFlowGraph cfg = e.getValue();
			
			if(!GraphUtils.isReducibleGraph(cfg, cfg.getEntries().iterator().next())) {
				return PassResult.with(pcxt, this).fatal(new IllegalStateException(String.format("%s contains irreducible loop", mn))).make();
			}
		}
		return PassResult.with(pcxt, this).finished().make();
	}
}
