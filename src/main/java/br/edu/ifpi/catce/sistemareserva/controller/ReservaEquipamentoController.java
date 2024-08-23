package br.edu.ifpi.catce.sistemareserva.controller;

import br.edu.ifpi.catce.sistemareserva.model.EquipamentoModel;
import br.edu.ifpi.catce.sistemareserva.model.FuncionarioModel;
import br.edu.ifpi.catce.sistemareserva.model.ReservaEquipamentoModel;
import br.edu.ifpi.catce.sistemareserva.repository.EquipamentoRepository;
import br.edu.ifpi.catce.sistemareserva.repository.FuncionarioRepository;
import br.edu.ifpi.catce.sistemareserva.repository.ReservaEquipamentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ReservaEquipamentoController {
    @Autowired
    EquipamentoRepository equipamentoRepository;
    @Autowired
    FuncionarioRepository funcionarioRepository;
    @Autowired
    ReservaEquipamentoRepository reservaEquipamentoRepository;

    @GetMapping("/reservaEquipamento")
    public String reservaEquipamento(){
        return "ReservaEquipamento/Reservaequipamento";
    }

    @GetMapping("/cadastroReservaEqui")
    public String cadastroReservaEqui(Model model) {
        List<EquipamentoModel> equipamentos = equipamentoRepository.findAll();
        List<FuncionarioModel> funcionarios = funcionarioRepository.findAll();

        ReservaEquipamentoModel reservaEquipamentoModel = new ReservaEquipamentoModel();
        model.addAttribute("reservaEquipamentoModel", reservaEquipamentoModel);
        model.addAttribute("equipamentoModelList", equipamentos);
        model.addAttribute("funcionarioModelList", funcionarios);

        return "ReservaEquipamento/Cadastrar";
    }

    @PostMapping("/cadastroRealizadoReservaEqui")
    public String cadastroRealizadoReservaEqui(ReservaEquipamentoModel reservaEquipamentoModel, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("mensagem","CADASTRADO COM SUCESSO!!!");
        redirectAttributes.addFlashAttribute("style","mensagem alert alert-success");
        reservaEquipamentoRepository.save(reservaEquipamentoModel);
        return "redirect:/cadastroReservaEqui";
    }

    @GetMapping("/listReservaEqui")
    public String listReservaEqui(Model model){
        model.addAttribute(new ReservaEquipamentoModel());
        return "ReservaEquipamento/Listar";
    }
    @PostMapping("/ListagemRealizadaReservaEqui")
    public String listagemRealizadaReservaEqui(@ModelAttribute ReservaEquipamentoModel reservaEquipamentoModel, Model model,RedirectAttributes redirectAttributes) {
        String nomeEquipamento = reservaEquipamentoModel.getEquipamento().getNomeEquipamento().toUpperCase();

        List<ReservaEquipamentoModel> reservas = reservaEquipamentoRepository.findReservaModelByNomeEquipamento(nomeEquipamento);

        if (!reservas.isEmpty()) {
            model.addAttribute("reservas", reservas);
            return "ReservaEquipamento/Listar";
        } else {
            redirectAttributes.addFlashAttribute("mensagem","NOME DE EQUIPAMENTO NÃO ENCOTRADO OU NÃO EXISTE NENHUMA RESERVA COM EQUIPAMENTO!!!");
            redirectAttributes.addFlashAttribute("style","mensagem alert alert-danger");
            return "redirect:/listReservaEqui";
        }
    }
    @PostMapping("/deletarReservaEqui")
    public String deletarReservaEqui(@RequestParam("id") Integer id, RedirectAttributes redirectAttributes){
        reservaEquipamentoRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("mensagem","APAGADO COM SUCESSO!!!");
        redirectAttributes.addFlashAttribute("style","mensagem alert alert-success");

        return "redirect:/listReservaEqui";
    }
    @GetMapping("atualizarPorIdReservaEquipamento")
    public String atualizarPorIdReservaEquipamento(@RequestParam("id") Integer id, Model model){
        model.addAttribute("id",id);
        List<EquipamentoModel> equipamentos= equipamentoRepository.findAll();
        List<FuncionarioModel> funcionarios = funcionarioRepository.findAll();

        model.addAttribute("equipamentosModelList", equipamentos);
        model.addAttribute("funcionarioModelList", funcionarios);
        return "ReservaEquipamento/Atualizar";

    }
    @PostMapping("atualizarReservaEquipamento")
    @Transactional
    public String atualizarReservaEspaco(@RequestParam("id") Integer id,@RequestParam("equipamento") Integer equipamento,@RequestParam("func") Integer func,RedirectAttributes redirectAttributes){
        reservaEquipamentoRepository.updateReservaModelById_reserva(id,equipamento,func);
        redirectAttributes.addFlashAttribute("mensagem","ATUALIZADO COM SUCESSO!!!");
        redirectAttributes.addFlashAttribute("style","mensagem alert alert-success");
        return "redirect:/listReservaEqui";
    }
}
