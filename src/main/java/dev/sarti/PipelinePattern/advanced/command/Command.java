package dev.sarti.PipelinePattern.advanced.command;

public interface Command {
    boolean execute();

    void rollback();
}
