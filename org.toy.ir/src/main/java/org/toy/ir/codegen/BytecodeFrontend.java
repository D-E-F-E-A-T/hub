package org.toy.ir.codegen;

import org.toy.ir.cfg.BasicBlock;
import org.toy.ir.cfg.ControlFlowGraph;
import org.objectweb.asm.Label;

public interface BytecodeFrontend {
	Label getLabel(BasicBlock b);

	ControlFlowGraph getGraph();
}
