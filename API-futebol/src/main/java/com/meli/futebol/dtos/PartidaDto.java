package com.meli.futebol.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class PartidaDto {
    private Long id;
    private Long idClube1;
    private Long idClube2;
    private Long idEstadio;
    private int golsClube1;
    private int golsClube2;
    private String resultado;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dataHora;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdClube1() {
        return idClube1;
    }

    public void setIdClube1(Long idClube1) {
        this.idClube1 = idClube1;
    }

    public Long getIdClube2() {
        return idClube2;
    }

    public void setIdClube2(Long idClube2) {
        this.idClube2 = idClube2;
    }

    public Long getIdEstadio() {
        return idEstadio;
    }

    public void setIdEstadio(Long idEstadio) {
        this.idEstadio = idEstadio;
    }

    public int getGolsClube1() {
        return golsClube1;
    }

    public void setGolsClube1(int golsClube1) {
        this.golsClube1 = golsClube1;
    }

    public int getGolsClube2() {
        return golsClube2;
    }

    public void setGolsClube2(int golsClube2) {
        this.golsClube2 = golsClube2;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}
