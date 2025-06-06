package dev.sarti.PipelinePattern.advanced.dto;

import java.math.BigDecimal;

public class ReintegroForm {
    private String tipo;
    private BigDecimal monto;
    private String estado;

    public ReintegroForm(String tipo, BigDecimal monto) {
        this.tipo = tipo;
        this.monto = monto;
    }

    public String getTipo() {
        return tipo;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
