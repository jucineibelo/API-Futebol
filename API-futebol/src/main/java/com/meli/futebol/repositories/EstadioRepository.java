package com.meli.futebol.repositories;

import com.meli.futebol.models.Estadio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EstadioRepository extends JpaRepository<Estadio, Long> {

    @Query("select e from Estadio e where (:nome is null or e.nome = :nome)")
    Page<Estadio> findByNome(@Param("nome") String nome, Pageable pageable);

}
