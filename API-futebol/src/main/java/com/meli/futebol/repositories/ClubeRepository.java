package com.meli.futebol.repositories;

import com.meli.futebol.models.Clube;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface ClubeRepository extends JpaRepository<Clube, Long> {

    @Query("select c from Clube c where (:nome is null or c.nome = :nome) and (:estado is null or c.siglaEstado = :estado) and (:ativo is null or c.ativo = :ativo)")
    Page<Clube> listByFilters(@Param("nome") String nome, @Param("estado") String siglaEstado, @Param("ativo") Boolean ativo, Pageable pageable);

    @Query("select case when count(c) > 0 then true else false end from Clube c where c.nome = :nome and c.siglaEstado = :siglaEstado")
    boolean existsByNome(@Param("nome") String nome, @Param("siglaEstado") String siglaEstado);

    @Query("select case when count(c) > 0 then true else false end from Clube c where c.nome = :nome and c.siglaEstado = :siglaEstado and c.id != :id")
    boolean existsByNomeAndSiglaEstadoIgnoringId(@Param("nome") String nome, @Param("siglaEstado") String siglaEstado, @Param("id") Long id);

    @Query("select case when count(c) > 0 then true else false end from Clube c where c.dataCriacao > :dataCriacao and c.id = :id")
    boolean existsClubByDateInvalid(@Param("dataCriacao") LocalDate dataCriacao, @Param("id") Long id);

}
