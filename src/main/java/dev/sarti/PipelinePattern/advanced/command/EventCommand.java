package dev.sarti.PipelinePattern.advanced.command;

public class EventCommand implements Command {
    private final String event;

    public EventCommand(String event) {
        this.event = event;
    }

    @Override
    public boolean execute() {
        System.out.println("[EVENTO]: " + event);
        return true;
    }

    @Override
    public void rollback() {
        System.out.println("Rollback del evento: " + event);
    }
}