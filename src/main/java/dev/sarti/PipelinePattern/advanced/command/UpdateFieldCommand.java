package dev.sarti.PipelinePattern.advanced.command;

public class UpdateFieldCommand<T> implements Command {
    private final T target;
    private final String field;
    private final Object newValue;
    private Object previousValue;

    public UpdateFieldCommand(T target, String field, Object newValue) {
        this.target = target;
        this.field = field;
        this.newValue = newValue;
    }

    @Override
    public boolean execute() {
        try {
            java.lang.reflect.Field f = target.getClass().getDeclaredField(field);
            f.setAccessible(true);
            previousValue = f.get(target);
            f.set(target, newValue);
            System.out.println("Campo " + field + " actualizado a " + newValue);
            return true;
        } catch (Exception e) {
            System.err.println("Error actualizando campo: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void rollback() {
        try {
            java.lang.reflect.Field f = target.getClass().getDeclaredField(field);
            f.setAccessible(true);
            f.set(target, previousValue);
            System.out.println("Rollback de campo " + field + " a valor " + previousValue);
        } catch (Exception e) {
            System.err.println("Error en rollback del campo: " + e.getMessage());
        }
    }
}