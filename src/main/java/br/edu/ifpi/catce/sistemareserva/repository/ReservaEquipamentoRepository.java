package br.edu.ifpi.catce.sistemareserva.repository;

import br.edu.ifpi.catce.sistemareserva.model.ReservaEquipamentoModel;
import br.edu.ifpi.catce.sistemareserva.model.ReservaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ReservaEquipamentoRepository extends JpaRepository<ReservaModel,Integer> {
    @Modifying
    @Query("UPDATE ReservaEquipamentoModel r SET r.equipamento.id_equipamento = :equipamento, r.funcionario.id= :func WHERE r.id_reserva = :id")
    void  updateReservaModelById_reserva(@Param("id") Integer id, @Param("equipamento") Integer equipamento, @Param("func") Integer func);

    @Query("SELECT r FROM ReservaEquipamentoModel r WHERE r.equipamento.nomeEquipamento LIKE %:nome")
    List<ReservaEquipamentoModel> findReservaModelByNomeEquipamento(@Param("nome") String nome);
}
