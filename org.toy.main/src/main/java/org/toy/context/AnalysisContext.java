package org.toy.context;

import org.toy.app.client.ApplicationContext;
import org.toy.app.service.ApplicationClassSource;
import org.toy.app.service.InvocationResolver;
import org.toy.deob.dataflow.DataFlowAnalysis;
import org.toy.deob.intraproc.ExceptionAnalysis;
import org.toy.ir.cfg.ControlFlowGraph;

public interface AnalysisContext {

	ApplicationClassSource getApplication();
	
	InvocationResolver getInvocationResolver();
	
	ExceptionAnalysis getExceptionAnalysis(ControlFlowGraph cfg);
	
	IRCache getIRCache();
	
	ApplicationContext getApplicationContext();

	DataFlowAnalysis getDataflowAnalysis();
}
