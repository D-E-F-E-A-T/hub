package org.toy.deob.passes.rename;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.toy.app.service.ApplicationClassSource;
import org.toy.app.service.InvocationResolver;
import org.toy.context.AnalysisContext;
import org.toy.deob.IPass;
import org.toy.deob.PassContext;
import org.toy.deob.PassResult;
import org.toy.deob.util.RenamingUtil;
import org.toy.ir.cfg.BasicBlock;
import org.toy.ir.cfg.ControlFlowGraph;
import org.toy.ir.code.Expr;
import org.toy.ir.code.Opcode;
import org.toy.ir.code.Stmt;
import org.toy.ir.code.expr.FieldLoadExpr;
import org.toy.ir.code.stmt.FieldStoreStmt;
import org.toy.asm.ClassNode;
import org.toy.asm.FieldNode;
import org.toy.asm.MethodNode;

public class FieldRenamerPass implements IPass {
	
	@Override
	public PassResult accept(PassContext pcxt) {		
		AnalysisContext cxt = pcxt.getAnalysis();
		Map<FieldNode, String> remapped = new HashMap<>();

//		int totalFields = 0;
		
//		int i = RenamingUtil.computeMinimum(totalFields);
		
		ApplicationClassSource source = cxt.getApplication();
		
		int i = RenamingUtil.numeric("aaaaa");
		
		for(ClassNode cn : source.iterate()) {
//			totalFields += cn.fields.size();
			for(FieldNode fn : cn.getFields()) {
				remapped.put(fn, RenamingUtil.createName(i++));
			}
		}
		
		InvocationResolver resolver = cxt.getInvocationResolver();
		
		for(ClassNode cn : source.iterate()) {
			for(MethodNode m : cn.getMethods()) {
				ControlFlowGraph cfg = cxt.getIRCache().getFor(m);
				
				for(BasicBlock b : cfg.vertices()) {
					for(Stmt stmt : b) {
						
						if(stmt.getOpcode() == Opcode.FIELD_STORE) {
							FieldStoreStmt fs = (FieldStoreStmt) stmt;
							
							FieldNode f = resolver.findField(fs.getOwner(), fs.getName(), fs.getDesc(), fs.getInstanceExpression() == null);
							
							if(f != null) {
								if(remapped.containsKey(f)) {
									fs.setName(remapped.get(f));
								} else if(mustMark(source, f.getOwner())) {
									System.err.println("  no remap for " + f + ", owner: " + f.getOwner());
								}
							} else {
								if(mustMark(source, fs.getOwner())) {
									System.err.println("  can't resolve field(set): " + fs.getOwner() + "." + fs.getName() + " " + fs.getDesc() + ", " + (fs.getInstanceExpression() == null));
								}
							}
						}
						
						for(Expr e : stmt.enumerateOnlyChildren()) {
							if(e.getOpcode() == Opcode.FIELD_LOAD) {
								FieldLoadExpr fl = (FieldLoadExpr) e;
								
								FieldNode f = resolver.findField(fl.getOwner(), fl.getName(), fl.getDesc(), fl.getInstanceExpression() == null);
								
								if(f != null) {
									if(remapped.containsKey(f)) {
										fl.setName(remapped.get(f));
									} else if(mustMark(source, f.getOwner())) {
										System.err.println("  no remap for " + f + ", owner: " + f.getOwner());
									}
								} else {
									if(mustMark(source, fl.getOwner())) {
										System.err.println("  can't resolve field(get): " + fl.getOwner() + "." + fl.getName() + " " + fl.getDesc() + ", " + (fl.getInstanceExpression() == null));
									}
								}
							}
						}
					}
				}
			}
		}
		
		for(Entry<FieldNode, String> e : remapped.entrySet()) {
			e.getKey().node.name = e.getValue();
		}
		
		System.out.printf("  Renamed %d fields.%n", remapped.size());

		return PassResult.with(pcxt, this).finished().make();
	}
	
	private boolean mustMark(ApplicationClassSource source, String owner) {
		ClassNode cn = source.findClassNode(owner);
		return cn == null || !source.isLibraryClass(owner);
	}
}
