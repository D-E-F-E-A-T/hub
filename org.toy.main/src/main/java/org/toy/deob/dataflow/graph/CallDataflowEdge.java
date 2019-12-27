package org.toy.deob.dataflow.graph;


import org.toy.ir.code.expr.invoke.InvocationExpr;

/**
 * Represents data flow out of a method as its return value.
 */
public class CallDataflowEdge extends DataflowEdge {
    public CallDataflowEdge(DataflowVertex src, DataflowVertex dst, InvocationExpr via) {
        super(src, dst, via);
    }

    @Override
    public DataflowType getType() {
        return DataflowType.CALL;
    }
}
