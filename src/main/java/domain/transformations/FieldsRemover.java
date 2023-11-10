package domain.transformations;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import java.util.List;

public class FieldsRemover extends ClassVisitor {

    private final List<String> fieldsToRemove;

    public FieldsRemover(int api, ClassVisitor cv, List<String> fieldsToRemove) {
        super(api, cv);
        this.fieldsToRemove = fieldsToRemove;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        if (fieldsToRemove.contains(name)) {
            return null;
        }
        return super.visitField(access, name, descriptor, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        return new CustomMethodVisitor(api, mv, fieldsToRemove);
    }

    private static class CustomMethodVisitor extends MethodVisitor {
        private final List<String> fieldsToRemove;

        public CustomMethodVisitor(int api, MethodVisitor mv, List<String> fieldsToRemove) {
            super(api, mv);
            this.fieldsToRemove = fieldsToRemove;
        }

        @Override
        public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
            if (fieldsToRemove.contains(name)) {
                return;
            }
            super.visitFieldInsn(opcode, owner, name, descriptor);
        }
    }
}
