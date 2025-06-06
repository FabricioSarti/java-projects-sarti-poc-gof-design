package dev.sarti.PipelinePattern.advanced.command;

public class LogCommand implements Command {
    private final String message;

    public LogCommand(String message) {
        this.message = message;
    }

    @Override
    public boolean execute() {
        System.out.println("[LOG]: " + message);
        return true;
    }

    @Override
    public void rollback() {
        System.out.println("Rollback del log: " + message);
    }
}