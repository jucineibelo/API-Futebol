package com.meli.futebol.repositories;

import com.meli.futebol.models.Partida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PartidaRepository extends JpaRepository<Partida, Long> {
    @Query("select p from Partida p where (:nome is null or p.clubeCasa.nome = :nome or p.clubeVisitante.nome = :nome) and (:estadio is null or p.estadio.nome = :estadio)")
    Page<Partida> findByFilters(@Param("nome") String nome, @Param("estadio") String estadio, Pageable pageable);
}
