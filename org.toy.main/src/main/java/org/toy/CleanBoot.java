package org.toy;

import org.toy.context.IRCache;
import org.toy.ir.algorithms.BoissinotDestructor;
import org.toy.ir.algorithms.LocalsReallocator;
import org.toy.ir.cfg.ControlFlowGraph;
import org.toy.ir.codegen.ControlFlowGraphDumper;
import org.toy.ir.utils.CFGUtils;
import org.toy.asm.ClassHelper;
import org.toy.asm.InsnListUtils;
import org.toy.asm.ClassNode;
import org.toy.asm.MethodNode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class CleanBoot {

    public static void main(String[] args) throws Exception {
        ClassNode cn = ClassHelper.create(new FileInputStream(new File("res", "BiteCode.class")));
        IRCache irFactory = new IRCache();
        for (MethodNode mn : cn.getMethods()) {
            ControlFlowGraph cfg = irFactory.getNonNull(mn);

            System.out.println(cfg);
            CFGUtils.easyDumpCFG(cfg, "pre-destruct");
            cfg.verify();

            BoissinotDestructor.leaveSSA(cfg);

            CFGUtils.easyDumpCFG(cfg, "pre-reaalloc");
            LocalsReallocator.realloc(cfg);
            CFGUtils.easyDumpCFG(cfg, "post-reaalloc");
            System.out.println(cfg);
            cfg.verify();
            System.out.println("Rewriting " + mn.getName());
            (new ControlFlowGraphDumper(cfg, mn)).dump();
            System.out.println(InsnListUtils.insnListToString(mn.node.instructions));
        }
        ClassHelper.dump(cn, new FileOutputStream(new File("out", "Bad.class")));
    }
}
