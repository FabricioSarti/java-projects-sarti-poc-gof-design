package dev.sarti.PipelinePattern;

import java.math.BigDecimal;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PocReintegroValidation {
    public static void main(String[] args) {
        // Datos simulados
        Solicitud solicitud = new Solicitud(1L, "INTERIOR", new BigDecimal("3500"));
        Reintegro reintegro = new Reintegro(new BigDecimal("500"));
        List<Reintegro> reintegros = Arrays.asList(reintegro);

        Context context = new Context(solicitud, reintegros);

        // Crear reglas con builder DSL
        ValidationPipeline pipeline = ValidationPipelineBuilder.builder()
                .addRule("Monto no debe superar límite por tipo",
                        ctx -> MontoValidator.validarMontoPorTipo(ctx.getSolicitud().getTipo(),
                                ctx.getSolicitud().getMonto()))
                .addRule("Debe existir al menos un reintegro", ctx -> !ctx.getReintegros().isEmpty())
                .addRule("El monto del reintegro debe coincidir con la diferencia",
                        ctx -> ReintegroValidator.validarDiferencia(ctx.getSolicitud(), ctx.getReintegros()))
                .build();

        // Ejecutar validaciones
        ValidationResult result = pipeline.execute(context);

        // Mostrar resultado
        if (result.hasErrors()) {
            System.out.println("Errores de validación:");
            result.getErrors().forEach(System.out::println);
        } else {
            System.out.println("Todas las validaciones pasaron correctamente.");
        }
    }

    // DSL Builder
    public static class ValidationPipelineBuilder {
        private final List<ValidationRule> rules = new ArrayList<>();

        public static ValidationPipelineBuilder builder() {
            return new ValidationPipelineBuilder();
        }

        public ValidationPipelineBuilder addRule(String name, Predicate<Context> condition) {
            rules.add(new ValidationRule(name, condition));
            return this;
        }

        public ValidationPipeline build() {
            return new ValidationPipeline(rules);
        }
    }

    // Pipeline
    public static class ValidationPipeline {
        private final List<ValidationRule> rules;

        public ValidationPipeline(List<ValidationRule> rules) {
            this.rules = rules;
        }

        public ValidationResult execute(Context context) {
            List<String> errors = rules.stream()
                    .filter(rule -> !rule.getCondition().test(context))
                    .map(ValidationRule::getName)
                    .collect(Collectors.toList());
            return new ValidationResult(errors);
        }
    }

    // Rule
    public static class ValidationRule {
        private final String name;
        private final Predicate<Context> condition;

        public ValidationRule(String name, Predicate<Context> condition) {
            this.name = name;
            this.condition = condition;
        }

        public String getName() {
            return name;
        }

        public Predicate<Context> getCondition() {
            return condition;
        }
    }

    // Result
    public static class ValidationResult {
        private final List<String> errors;

        public ValidationResult(List<String> errors) {
            this.errors = errors;
        }

        public boolean hasErrors() {
            return !errors.isEmpty();
        }

        public List<String> getErrors() {
            return errors;
        }
    }

    // Domain objects
    public static class Solicitud {
        private final Long id;
        private final String tipo;
        private final BigDecimal monto;

        public Solicitud(Long id, String tipo, BigDecimal monto) {
            this.id = id;
            this.tipo = tipo;
            this.monto = monto;
        }

        public Long getId() {
            return id;
        }

        public String getTipo() {
            return tipo;
        }

        public BigDecimal getMonto() {
            return monto;
        }
    }

    public static class Reintegro {
        private final BigDecimal montoBoleta;

        public Reintegro(BigDecimal montoBoleta) {
            this.montoBoleta = montoBoleta;
        }

        public BigDecimal getMontoBoleta() {
            return montoBoleta;
        }
    }

    public static class Context {
        private final Solicitud solicitud;
        private final List<Reintegro> reintegros;

        public Context(Solicitud solicitud, List<Reintegro> reintegros) {
            this.solicitud = solicitud;
            this.reintegros = reintegros;
        }

        public Solicitud getSolicitud() {
            return solicitud;
        }

        public List<Reintegro> getReintegros() {
            return reintegros;
        }
    }

    // Validadores
    public static class MontoValidator {
        private static final Map<String, BigDecimal> limites = new HashMap<>();
        static {
            limites.put("INTERIOR", new BigDecimal("4000"));
            limites.put("EXTERIOR", new BigDecimal("8000"));
        }

        public static boolean validarMontoPorTipo(String tipo, BigDecimal monto) {
            return monto.compareTo(limites.getOrDefault(tipo, BigDecimal.ZERO)) <= 0;
        }
    }

    public static class ReintegroValidator {
        public static boolean validarDiferencia(Solicitud solicitud, List<Reintegro> reintegros) {
            BigDecimal suma = reintegros.stream()
                    .map(Reintegro::getMontoBoleta)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            return suma.compareTo(solicitud.getMonto()) == 0;
        }
    }
}
