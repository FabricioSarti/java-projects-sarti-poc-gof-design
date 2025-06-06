package dev.sarti.PipelinePattern.advanced.validations;

import java.util.Collections;
import java.util.function.Predicate;

public class ValidationRule<T> {
    private final Predicate<T> predicate;
    private final String errorMessage;

    public ValidationRule(Predicate<T> predicate, String errorMessage) {
        this.predicate = predicate;
        this.errorMessage = errorMessage;
    }

    public ValidationResult apply(T target) {
        return predicate.test(target)
                ? ValidationResult.ok()
                : ValidationResult.fail(Collections.singletonList(errorMessage));
    }
}
