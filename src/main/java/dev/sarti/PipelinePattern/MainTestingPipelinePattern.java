package dev.sarti.PipelinePattern;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MainTestingPipelinePattern {

    public static void main(String[] args) {
        // Simulamos datos
        Solicitud solicitud = new Solicitud("INTERIOR", new BigDecimal("4500"));
        ReintegroForm reintegro = new ReintegroForm(new BigDecimal("-50"), "SPECIAL");

        // Creamos validadores específicos
        ValidationEngine<Solicitud> solicitudEngine = new ValidationEngine<>(SolicitudRules.rulesFromService());
        ValidationEngine<ReintegroForm> reintegroEngine = new ValidationEngine<>(ReintegroRules.rules());

        // Ejecutamos validaciones
        ValidationResult solicitudResult = solicitudEngine.validate(solicitud);
        ValidationResult reintegroResult = reintegroEngine.validate(reintegro);

        // Mostramos resultados
        System.out.println("Validaciones de Solicitud:");
        solicitudResult.getErrors().forEach(System.out::println);

        System.out.println("\nValidaciones de Reintegro:");
        reintegroResult.getErrors().forEach(System.out::println);
    }

    // Motor de validaciones reutilizable y funcional
    public static class ValidationEngine<T> {
        private final List<ValidationRule<T>> rules;

        public ValidationEngine(List<ValidationRule<T>> rules) {
            this.rules = rules;
        }

        public ValidationResult validate(T input) {
            List<String> errors = rules.stream()
                    .map(rule -> rule.apply(input))
                    .filter(result -> !result.isValid())
                    .map(ValidationResult::getMessage)
                    .collect(Collectors.toList());
            return errors.isEmpty() ? ValidationResult.ok() : ValidationResult.fail(errors);
        }
    }

    // Resultado de validación con múltiples errores
    public static class ValidationResult {
        private final boolean valid;
        private final List<String> messages;

        private ValidationResult(boolean valid, List<String> messages) {
            this.valid = valid;
            this.messages = messages;
        }

        public static ValidationResult ok() {
            return new ValidationResult(true, Collections.emptyList());
        }

        public static ValidationResult fail(List<String> messages) {
            return new ValidationResult(false, messages);
        }

        public boolean isValid() {
            return valid;
        }

        public List<String> getErrors() {
            return messages;
        }

        public String getMessage() {
            return messages.isEmpty() ? null : messages.get(0);
        }
    }

    // Representa una regla funcional
    public static class ValidationRule<T> {
        private final Function<T, ValidationResult> rule;

        public ValidationRule(Function<T, ValidationResult> rule) {
            this.rule = rule;
        }

        public ValidationResult apply(T input) {
            return rule.apply(input);
        }
    }

    // Simulación de modelo de Solicitud
    public static class Solicitud {
        private final String tipo;
        private final BigDecimal monto;

        public Solicitud(String tipo, BigDecimal monto) {
            this.tipo = tipo;
            this.monto = monto;
        }

        public String getTipo() {
            return tipo;
        }

        public BigDecimal getMonto() {
            return monto;
        }
    }

    // Reglas para Solicitud
    public static class SolicitudRules {

        public static List<ValidationRule<Solicitud>> rulesFromService() {
            return Arrays.asList(
                    new ValidationRule<>(s -> s.getMonto().compareTo(BigDecimal.ZERO) > 0
                            ? ValidationResult.ok()
                            : ValidationResult.fail(Collections.singletonList("El monto debe ser mayor a 0"))),

                    new ValidationRule<>(s -> {
                        BigDecimal limite = obtenerLimiteDesdeServicio(s.getTipo());
                        return s.getMonto().compareTo(limite) <= 0
                                ? ValidationResult.ok()
                                : ValidationResult.fail(
                                        Collections.singletonList("El monto supera el límite permitido de " + limite));
                    }));
        }

        private static BigDecimal obtenerLimiteDesdeServicio(String tipo) {
            if ("INTERIOR".equalsIgnoreCase(tipo))
                return new BigDecimal("4000");
            if ("EXTERIOR".equalsIgnoreCase(tipo))
                return new BigDecimal("8000");
            return BigDecimal.ZERO;
        }
    }

    // Simulación de formulario de reintegro
    public static class ReintegroForm {
        private final BigDecimal monto;
        private final String tipo;

        public ReintegroForm(BigDecimal monto, String tipo) {
            this.monto = monto;
            this.tipo = tipo;
        }

        public BigDecimal getMonto() {
            return monto;
        }

        public String getTipo() {
            return tipo;
        }
    }

    // Reglas para formulario de reintegro
    public static class ReintegroRules {
        public static List<ValidationRule<ReintegroForm>> rules() {
            return Arrays.asList(
                    new ValidationRule<>(r -> r.getMonto() != null && r.getMonto().compareTo(BigDecimal.ZERO) > 0
                            ? ValidationResult.ok()
                            : ValidationResult
                                    .fail(Collections.singletonList("El monto del reintegro debe ser mayor a cero"))),

                    new ValidationRule<>(r -> "NORMAL".equalsIgnoreCase(r.getTipo())
                            ? ValidationResult.ok()
                            : ValidationResult.fail(Collections.singletonList("Tipo de reintegro no permitido"))));
        }
    }
}
