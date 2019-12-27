package org.toy.ir.code.expr;

import org.toy.ir.TypeUtils;
import org.toy.ir.code.CodeUnit;
import org.toy.ir.code.Expr;
import org.toy.ir.codegen.BytecodeFrontend;
import org.toy.stdlib.util.TabbedStringWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class NewArrayExpr extends Expr {

	private Expr[] bounds;
	private Type type;

	public NewArrayExpr(Expr[] bounds, Type type) {
		super(NEW_ARRAY);
		this.bounds = bounds;
		this.type = type;
		for (int i = 0; i < bounds.length; i++) {
			writeAt(bounds[i], i);
		}
		
//		if(type.getSort() == Type.ARRAY) {
//			throw new RuntimeException(type.toString());
//		}
	}

	public int getDimensions() {
		return bounds.length;
	}
	
	public Expr[] getBounds() {
		return bounds;
	}

	@Override
	public Expr copy() {
		Expr[] bounds = new Expr[this.bounds.length];
		for (int i = 0; i < bounds.length; i++)
			bounds[i] = this.bounds[i].copy();
		return new NewArrayExpr(bounds, type);
	}

	@Override
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Override
	public void onChildUpdated(int ptr) {
		if(ptr >= 0 && ptr < bounds.length) {
			bounds[ptr] = read(ptr);
		} else {
			raiseChildOutOfBounds(ptr);
		}
	}
	
	@Override
	public Precedence getPrecedence0() {
		return Precedence.ARRAY_ACCESS;
	}

	// TODO: redo type to element type.
	@Override
	public void toString(TabbedStringWriter printer) {
		printer.print("new " + type.getElementType().getClassName());
		for (int dim = 0; dim < type.getDimensions(); dim++) {
			printer.print('[');
			if (dim < bounds.length) {
				bounds[dim].toString(printer);
			}
			printer.print(']');
		}
	}

	@Override
	public void toCode(MethodVisitor visitor, BytecodeFrontend assembler) {
		for (int i = 0; i < bounds.length; i++) {
			bounds[i].toCode(visitor, assembler);
			int[] cast = TypeUtils.getPrimitiveCastOpcodes(bounds[i].getType(), Type.INT_TYPE);
			for (int a = 0; a < cast.length; a++)
				visitor.visitInsn(cast[a]);
		}

		if (type.getDimensions() != 1) {
			visitor.visitMultiANewArrayInsn(type.getDescriptor(), bounds.length);
		} else {
			Type element = type.getElementType();
			if (element.getSort() == Type.OBJECT || element.getSort() == Type.METHOD) {
				visitor.visitTypeInsn(Opcodes.ANEWARRAY, element.getInternalName());
			} else {
				visitor.visitIntInsn(Opcodes.NEWARRAY, TypeUtils.getPrimitiveArrayOpcode(type));
			}
		}
	}

	@Override
	public boolean canChangeFlow() {
		return false;
	}
	
	@Override
	public boolean equivalent(CodeUnit s) {
		if(s instanceof NewArrayExpr) {
			NewArrayExpr e = (NewArrayExpr) s;
			if(e.bounds.length != bounds.length) {
				return false;
			}
			for(int i=0; i < bounds.length; i++) {
				if(!bounds[i].equivalent(e.bounds[i])) {
					return false;
				}
			}
			return type.equals(e.type);
		}
		return false;
	}
}