package org.toy.deob.intraproc;

import java.util.Set;

import org.toy.ir.code.CodeUnit;
import org.objectweb.asm.Type;

public interface ExceptionAnalysis {
	
	Set<Type> getPossibleUserThrowables(CodeUnit u);
	
	Set<Type> getForcedThrowables(CodeUnit u);
}