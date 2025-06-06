package dev.sarti.PipelinePattern.advanced;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import dev.sarti.PipelinePattern.advanced.command.CommandPipeline;
import dev.sarti.PipelinePattern.advanced.command.EventCommand;
import dev.sarti.PipelinePattern.advanced.command.LogCommand;
import dev.sarti.PipelinePattern.advanced.command.PersistCommand;
import dev.sarti.PipelinePattern.advanced.command.UpdateFieldCommand;
import dev.sarti.PipelinePattern.advanced.command.ValidateCommand;
import dev.sarti.PipelinePattern.advanced.dto.ReintegroForm;
import dev.sarti.PipelinePattern.advanced.rules.ReintegroRules;

public class MainTestingAdvancedPipeline {
    public static void main(String[] args) {
        ReintegroForm form = new ReintegroForm("NORMAL", new BigDecimal("0"));
        List<ReintegroForm> fakeDb = new ArrayList<>();

        // Pipeline de comandos
        CommandPipeline pipeline = new CommandPipeline();
        pipeline
                .add(new PersistCommand<>(form, fakeDb))
                .add(new ValidateCommand<ReintegroForm>(form, ReintegroRules.rules()))
                .add(new UpdateFieldCommand<>(form, "estado", "VALIDADO"))
                .add(new LogCommand("Formulario validado correctamente"))
                .add(new EventCommand("Disparando evento de validación exitosa"));

        // Ejecutar
        boolean success = pipeline.execute();

        if (!success) {
            System.out.println("Falló una regla. Haciendo rollback de todas las acciones...");
            pipeline.rollback();
        }
    }
}
