package com.meli.futebol.repositories;

import com.meli.futebol.models.Clube;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClubeRepository extends JpaRepository<Clube, Long> {

    @Query("select c from Clube c where (:nome is null or c.nome = :nome) and (:estado is null or c.siglaEstado = :estado) and (:ativo is null or c.ativo = :ativo)")
    Page<Clube> listByFilters(@Param("nome") String nome, @Param("estado") String siglaEstado, @Param("ativo") Boolean ativo, Pageable pageable);
}
