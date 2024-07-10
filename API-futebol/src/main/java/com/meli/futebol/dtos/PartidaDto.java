package com.meli.futebol.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class PartidaDto {

    private Long idClubeCasa;
    private Long idClubeVisitante;
    private Long idEstadio;
    private String resultado;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dataHora;

    public Long getIdClubeCasa() {
        return idClubeCasa;
    }

    public void setIdClubeCasa(Long idClubeCasa) {
        this.idClubeCasa = idClubeCasa;
    }

    public Long getIdclubeVisitante() {
        return idClubeVisitante;
    }

    public void setIdclubeVisitante(Long idclubeVisitante) {
        this.idClubeVisitante = idclubeVisitante;
    }

    public Long getIdEstadio() {
        return idEstadio;
    }

    public void setIdEstadio(Long idEstadio) {
        this.idEstadio = idEstadio;
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
