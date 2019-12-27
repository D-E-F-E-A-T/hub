package org.toy.context;

import org.toy.ir.cfg.ControlFlowGraph;
import org.toy.ir.cfg.builder.ControlFlowGraphBuilder;
import org.toy.ir.code.CodeUnit;
import org.toy.stdlib.collections.map.KeyedValueCreator;
import org.toy.stdlib.collections.map.NullPermeableHashMap;
import org.toy.stdlib.util.JavaDesc;
import org.toy.asm.MethodNode;

import java.util.Set;
import java.util.stream.Stream;

public class IRCache extends NullPermeableHashMap<MethodNode, ControlFlowGraph> {
	private static final long serialVersionUID = 1L;
	
	public IRCache(KeyedValueCreator<MethodNode, ControlFlowGraph> creator) {
		super(creator);
	}
	
	public IRCache() {
		this(ControlFlowGraphBuilder::build);
	}
	
	public ControlFlowGraph getFor(MethodNode m) {
		return getNonNull(m);
	}

	public MethodNode findMethod(JavaDesc jd) {
		return getActiveMethods().stream().filter(mn -> mn.getJavaDesc().equals(jd)).findFirst().orElseGet(null);
	}
	
	public Set<MethodNode> getActiveMethods() {
		return keySet();
	}

	public Stream<CodeUnit> allExprStream() {
		return values().stream().flatMap(ControlFlowGraph::allExprStream);
	}
}
