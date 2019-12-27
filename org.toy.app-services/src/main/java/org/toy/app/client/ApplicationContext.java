package org.toy.app.client;

import java.util.Set;

import org.toy.asm.MethodNode;

public interface ApplicationContext {

	Set<MethodNode> getEntryPoints();
}
