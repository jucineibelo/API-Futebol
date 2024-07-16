package com.meli.futebol.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Partida {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "clube1_id")
    private Clube clube1;

    @ManyToOne
    @JoinColumn(name = "clube2_id")
    private Clube clube2;

    @ManyToOne
    @JoinColumn(name = "estadio_id")
    private Estadio estadio;
    private int golsClube1;
    private int golsClube2;
    private String resultado;
    private LocalDateTime dataHora;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Clube getClube1() {
        return clube1;
    }

    public void setClube1(Clube clube1) {
        this.clube1 = clube1;
    }

    public Clube getClube2() {
        return clube2;
    }

    public void setClube2(Clube clube2) {
        this.clube2 = clube2;
    }

    public Estadio getEstadio() {
        return estadio;
    }

    public void setEstadio(Estadio estadio) {
        this.estadio = estadio;
    }

    public double getGolsClube1() {
        return golsClube1;
    }

    public void setGolsClube1(int golsClube1) {
        this.golsClube1 = golsClube1;
    }

    public double getGolsClube2() {
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
