package br.edu.ifpi.catce.sistemareserva.repository;

import br.edu.ifpi.catce.sistemareserva.model.EspacoModel;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EspacoRepository extends JpaRepository<EspacoModel, Integer> {

    @Modifying
    @Query("UPDATE EspacoModel e SET e.nomeEspaco = :nome, e.status = :status WHERE e.id_espaco = :id")
    void updateEspacoById(@Param("id") Integer id, @Param("nome") String nome, @Param("status") String status);

    @Query("SELECT e FROM EspacoModel e WHERE e.nomeEspaco LIKE %:nome")
    List<EspacoModel> findByNomeEspaco(@Param("nome") String nome);
}
