package dev.sarti.PipelinePattern.advanced.command;

import java.util.ArrayList;
import java.util.List;

import dev.sarti.PipelinePattern.advanced.validations.ValidationResult;
import dev.sarti.PipelinePattern.advanced.validations.ValidationRule;

public class ValidateCommand<T> implements Command {

    private final T target;
    private final List<ValidationRule<T>> rules;
    private final List<String> errors = new ArrayList<>();

    public ValidateCommand(T target, List<ValidationRule<T>> rules) {
        this.target = target;
        this.rules = rules;
    }

    @Override
    public boolean execute() {
        errors.clear();
        for (ValidationRule<T> rule : rules) {
            ValidationResult result = rule.apply(target);
            if (!result.isValid()) {
                errors.addAll(result.getErrors());
            }
        }
        if (!errors.isEmpty()) {
            System.out.println("Errores de validación: " + errors);
        }
        return errors.isEmpty();
    }

    @Override
    public void rollback() {
        System.out.println("Rollback de validación: nada que deshacer.");
    }

}
