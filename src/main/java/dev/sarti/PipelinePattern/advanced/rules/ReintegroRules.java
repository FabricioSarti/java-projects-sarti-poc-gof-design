package dev.sarti.PipelinePattern.advanced.rules;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import dev.sarti.PipelinePattern.advanced.dto.ReintegroForm;
import dev.sarti.PipelinePattern.advanced.validations.ValidationRule;

public class ReintegroRules {

        public static List<ValidationRule<ReintegroForm>> rules() {
                return Arrays.asList(
                                new ValidationRule<ReintegroForm>(
                                                r -> r.getMonto() != null
                                                                && r.getMonto().compareTo(BigDecimal.ZERO) > 0,
                                                "El monto del reintegro debe ser mayor a cero"),
                                new ValidationRule<ReintegroForm>(
                                                r -> "NORMAL".equalsIgnoreCase(r.getTipo()),
                                                "Tipo de reintegro no permitido"));
        }
}
