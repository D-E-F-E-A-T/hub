package org.toy.deob.intraproc.eval;

import org.toy.ir.cfg.ControlFlowGraph;
import org.toy.ir.code.Expr;
import org.toy.ir.locals.Local;
import org.toy.stdlib.collections.taint.TaintableSet;

/**
 * Provides possible values a local could hold in a CFG.
 */
public interface LocalValueResolver {
	/**
	 *
	 * @param cfg Method to provide value relevant for
	 * @param l Local to provide value for
	 * @return Taintable set of possible values the local could represent
	 */
	TaintableSet<Expr> getValues(ControlFlowGraph cfg, Local l);
}
