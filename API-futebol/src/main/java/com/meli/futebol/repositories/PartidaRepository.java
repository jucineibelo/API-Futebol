package com.meli.futebol.repositories;

import com.meli.futebol.models.Partida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;

public interface PartidaRepository extends JpaRepository<Partida, Long> {

    @Query("select p from Partida p where (:nome is null or p.clube1.nome = :nome or p.clube2.nome = :nome) and (:estadio is null or p.estadio.nome = :estadio)")
    Page<Partida> findByFilters(@Param("nome") String nome, @Param("estadio") String estadio, Pageable pageable);

    @Query(value = "SELECT CASE " +
            "    WHEN EXISTS ( " +
            "        SELECT 1 " +
            "        FROM Partida p " +
            "        WHERE DATE(p.data_hora) < :dataCriacao " +
            "          AND (:id = p.clube_casa_id OR :id = p.clube_visitante_id) " +
            "    ) THEN 1 " +
            "    ELSE 0 " +
            "END AS resultado", nativeQuery = true)
    int existsByDataHoraBeforeAndClubeIds(@Param("dataCriacao") LocalDate dataCriacao, @Param("id") Long id);





}
