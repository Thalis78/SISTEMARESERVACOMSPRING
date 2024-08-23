package br.edu.ifpi.catce.sistemareserva.repository;

import br.edu.ifpi.catce.sistemareserva.model.FuncionarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface FuncionarioRepository extends JpaRepository<FuncionarioModel,Integer> {

    @Modifying
    @Query("UPDATE FuncionarioModel f SET f.nomeFuncionario = :nome, f.cargo = :cargo WHERE f.id = :id")
    void  updateFuncionarioModelById(@Param("id") Integer id, @Param("nome") String nome, @Param("cargo") String cargo);

    @Modifying
    @Query("SELECT f FROM FuncionarioModel f WHERE f.nomeFuncionario LIKE %:nome")
    List<FuncionarioModel> findByNomeFuncionario(@Param("nome") String nome);
}
