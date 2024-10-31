package br.edu.ifpi.catce.sistemareserva.controller;

import br.edu.ifpi.catce.sistemareserva.model.*;
import br.edu.ifpi.catce.sistemareserva.repository.*;
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
public class ReservaEspacoController {
    @Autowired
    EspacoRepository espacoRepository;
    @Autowired
    FuncionarioRepository funcionarioRepository;
    @Autowired
    ReservaEspacoRepository reservaEspacoRepository;

    @GetMapping("/reservaEspaco")
    public String reservaEspaco(){

        return "ReservaEspaco/Reservaespaco";
    }

    @GetMapping("/cadastroReservaEsp")
    public String cadastroReservaEqui(Model model) {
        ReservaEspacoModel reservaEspacoModel = new ReservaEspacoModel();

        List<EspacoModel> espacos= espacoRepository.findAll();
        List<FuncionarioModel> funcionarios = funcionarioRepository.findAll();
        model.addAttribute("reservaEspacoModel", reservaEspacoModel);
        model.addAttribute("espacoModelList", espacos);
        model.addAttribute("funcionarioModelList", funcionarios);

        return "ReservaEspaco/Cadastrar";
    }

    @PostMapping("/cadastroRealizadoReservaEsp")
    public String cadastroRealizadoReservaEsp(ReservaEspacoModel reservaEspacoModel, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("mensagem","CADASTRADO COM SUCESSO!!!");
        redirectAttributes.addFlashAttribute("style","mensagem alert alert-success");
        reservaEspacoRepository.save(reservaEspacoModel);
        return "redirect:/cadastroReservaEsp";
    }

    @GetMapping("/listReservaEsp")
    public String listReservaEqui(Model model){
        model.addAttribute(new ReservaEspacoModel());
        return "ReservaEspaco/Listar";
    }
    @GetMapping("/ListagemRealizadaReservaEsp")
    public String listagemRealizadaReservaEsp(@ModelAttribute ReservaEspacoModel reservaEspacoModel, Model model,RedirectAttributes redirectAttributes) {
        String nomeEspaco = reservaEspacoModel.getEspaco().getNomeEspaco();

        List<ReservaEspacoModel> reservas = reservaEspacoRepository.findReservaEspacoByNomeEspaco(nomeEspaco);

        if (!reservas.isEmpty()) {
            model.addAttribute("reservasEsp", reservas);
            return "ReservaEspaco/Listar";
        } else {
            redirectAttributes.addFlashAttribute("mensagem","NOME DE ESPAÇO NÃO ENCOTRADO OU NÃO EXISTE RESERVA COM ESPAÇO!!!");
            redirectAttributes.addFlashAttribute("style","mensagem alert alert-danger");
            return "redirect:/listReservaEsp";
        }
    }

    @PostMapping("/deletarReservaEsp")
    public String deletarReservaEsp(@RequestParam("id") Integer id,RedirectAttributes redirectAttributes){
        reservaEspacoRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("mensagem","APAGADO COM SUCESSO!!!");
        redirectAttributes.addFlashAttribute("style","mensagem alert alert-success");
        return "redirect:/listReservaEsp";
    }

    @GetMapping("atualizarPorIdReservaEspaco")
    public String atualizarPorIdReservaEspaco(@RequestParam("id") Integer id, Model model){
        model.addAttribute("id",id);
        List<EspacoModel> espacos= espacoRepository.findAll();
        List<FuncionarioModel> funcionarios = funcionarioRepository.findAll();

        model.addAttribute("espacoModelList", espacos);
        model.addAttribute("funcionarioModelList", funcionarios);
        return "ReservaEspaco/Atualizar";

    }
    @PostMapping("atualizarReservaEspaco")
    @Transactional
    public String atualizarReservaEspaco(@RequestParam("id") Integer id,@RequestParam("espaco") Integer espaco,@RequestParam("func") Integer func,RedirectAttributes redirectAttributes){
        reservaEspacoRepository.updateReservaEspacoModelById_reserva(id,espaco,func);
        redirectAttributes.addFlashAttribute("mensagem","ATUALIZADO COM SUCESSO!!!");
        redirectAttributes.addFlashAttribute("style","mensagem alert alert-success");
        return "redirect:/listReservaEsp";
    }


}
