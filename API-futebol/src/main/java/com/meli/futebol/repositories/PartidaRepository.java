package com.meli.futebol.repositories;

import com.meli.futebol.models.Partida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface PartidaRepository extends JpaRepository<Partida, Long> {

    @Query("select p from Partida p where (:nome is null or p.clube1.nome = :nome or p.clube2.nome = :nome) and (:estadio is null or p.estadio.nome = :estadio)")
    Page<Partida> findByFilters(@Param("nome") String nome, @Param("estadio") String estadio, Pageable pageable);

    @Query(value = " SELECT EXISTS(SELECT 1 " +
            " FROM partida p " +
            " WHERE DATE(p.data_hora) < :dataCriacao " +
            " AND (:id = p.clube1_id OR :id = p.clube2_id)) AS dataInvalida ", nativeQuery = true)
    int existsByDataHoraBeforeAndClubeIds(@Param("dataCriacao") LocalDate dataCriacao, @Param("id") Long id);

    @Query(value = " SELECT EXISTS(SELECT 1 " +
            " FROM partida p " +
            " WHERE (p.clube1_id = :clube1Id OR p.clube2_id = :clube2Id) " +
            " AND p.data_hora BETWEEN :dataHora AND DATE_ADD(:dataHora, INTERVAL 48 HOUR)) AS existePartida", nativeQuery = true)
    int existsMatchesByClub(@Param("clube1Id") Long clube1Id, @Param("clube2Id") Long clube2Id, @Param("dataHora") LocalDateTime dataHora);

    @Query(value = "SELECT EXISTS(SELECT 1 " +
            "FROM partida p " +
            "WHERE p.estadio_id = :estadioId " +
            "AND p.data_hora BETWEEN :dataHora AND DATE_ADD(:dataHora, INTERVAL 24 HOUR)) AS existePartidaByEstadio", nativeQuery = true)
    int existsMatchesByEstadio(@Param("estadioId") Long estadioId, @Param("dataHora") LocalDateTime dataHora);

}
