package dev.sarti.PipelinePattern.advanced.command;

import java.util.List;

public class PersistCommand<T> implements Command {
    private final T entity;
    private final List<T> database; // simulación de BD
    private boolean inserted = false;

    public PersistCommand(T entity, List<T> database) {
        this.entity = entity;
        this.database = database;
    }

    @Override
    public boolean execute() {
        try {
            database.add(entity);
            inserted = true;
            System.out.println("Entidad persistida: " + entity);
            return true;
        } catch (Exception e) {
            System.err.println("Error al persistir: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void rollback() {
        if (inserted) {
            database.remove(entity);
            System.out.println("Rollback de persistencia: entidad removida de BD simulada.");
        }
    }
}