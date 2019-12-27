package org.toy.deob.passes;

import java.util.ListIterator;
import java.util.Set;

import org.toy.deob.IPass;
import org.toy.deob.PassContext;
import org.toy.deob.PassResult;
import org.toy.asm.ClassNode;
import org.toy.asm.MethodNode;

public class CallgraphPruningPass implements IPass {

	@Override
	public String getId() {
		return "CG-Prune";
	}

	@Override
	public PassResult accept(PassContext cxt) {
		int delta = 0;

		Set<MethodNode> active = cxt.getAnalysis().getIRCache().getActiveMethods();
		for(ClassNode cn : cxt.getAnalysis().getApplication().iterate()) {
			ListIterator<MethodNode> lit = cn.getMethods().listIterator();
			while(lit.hasNext()) {
				MethodNode m = lit.next();
				if(!active.contains(m)) {
					lit.remove();
					delta++;
				}
			}
		}
		
		System.out.println("Removed " + delta + " dead methods.");
		
		return PassResult.with(cxt, this).finished().make();
	}
}
