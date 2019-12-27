package org.toy.ir.algorithms;

import org.toy.ir.locals.Local;

import java.util.Set;

public interface Liveness<N> {

	Set<Local> in(N n);
	
	Set<Local> out(N n);
}