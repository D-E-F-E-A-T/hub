package org.toy.deob.interproc;

import java.util.Set;

import org.apache.log4j.Logger;
import org.toy.context.AnalysisContext;
import org.toy.ir.cfg.ControlFlowGraph;
import org.toy.ir.code.Expr;
import org.toy.ir.code.Stmt;
import org.toy.ir.code.expr.invoke.Invocation;
import org.toy.asm.MethodNode;

/**
 * CallTracer which implements a DFS on the callgraph based on IR instructions.
 */
public class IRCallTracer extends CallTracer {

	private static final Logger LOGGER = Logger.getLogger(IRCallTracer.class);

	protected final AnalysisContext context;

	public IRCallTracer(AnalysisContext context) {
		super(context.getApplication(), context.getInvocationResolver());
		this.context = context;
	}

	@Override
	protected void traceImpl(MethodNode m) {
		ControlFlowGraph cfg = context.getIRCache().getFor(m);
		if (cfg == null) {
			throw new UnsupportedOperationException(
					String.format("Cannot trace, no cfg for %s (%d)", m, m.node.instructions.size()));
		}

		for (Stmt stmt : cfg.stmts()) {
			for (Expr c : stmt.enumerateOnlyChildren()) {
				if (c instanceof Invocation) {
					traceInvocation(m, (Invocation) c);
				}
			}
		}
	}

	private void traceInvocation(MethodNode m, Invocation invoke) {
		Set<MethodNode> targets = invoke.resolveTargets(resolver);

		if (targets.size() != 0) {
			for (MethodNode vtarg : targets) {
				trace(vtarg);
				processedInvocation(m, vtarg, invoke);
			}
		} else {
			LOGGER.error(String.format("can't resolve call to %s.%s %s%s%s", invoke.getOwner(),
					invoke.getName(), invoke.getDesc(), invoke.isStatic() ? "(static)" : "", invoke.isDynamic() ? "(dynamic)" : ""));
			LOGGER.error(String.format("   call from %s", m));
		}
	}
}
