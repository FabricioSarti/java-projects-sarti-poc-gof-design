package dev.sarti.PipelinePattern.advanced.command;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class CommandPipeline {

    private final List<Command> executed = new ArrayList<>();
    private final List<Command> commands = new ArrayList<>();

    public CommandPipeline add(Command cmd) {
        commands.add(cmd);
        return this;
    }

    public boolean execute() {
        for (Command cmd : commands) {
            if (!cmd.execute()) {
                return false;
            }
            executed.add(cmd);
        }
        return true;
    }

    public void rollback() {
        ListIterator<Command> it = executed.listIterator(executed.size());
        while (it.hasPrevious()) {
            it.previous().rollback();
        }
    }
}
