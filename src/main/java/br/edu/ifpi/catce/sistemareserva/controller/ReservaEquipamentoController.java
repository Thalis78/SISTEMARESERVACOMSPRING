package br.edu.ifpi.catce.sistemareserva.controller;

import br.edu.ifpi.catce.sistemareserva.model.EquipamentoModel;
import br.edu.ifpi.catce.sistemareserva.model.FuncionarioModel;
import br.edu.ifpi.catce.sistemareserva.model.ReservaEquipamentoModel;
import br.edu.ifpi.catce.sistemareserva.model.ReservaModel;
import br.edu.ifpi.catce.sistemareserva.repository.EquipamentoRepository;
import br.edu.ifpi.catce.sistemareserva.repository.FuncionarioRepository;
import br.edu.ifpi.catce.sistemareserva.repository.ReservaEquipamentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class ReservaEquipamentoController {
    @Autowired
    EquipamentoRepository equipamentoRepository;
    @Autowired
    FuncionarioRepository funcionarioRepository;
    @Autowired
    ReservaEquipamentoRepository reservaEquipamentoRepository;
    @Autowired
    private RestTemplateAutoConfiguration restTemplateAutoConfiguration;

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
    public String listReservaEqui(@RequestParam(defaultValue = "0") int page,Model model){
        return loadReservaEquipamento(page,model,null);
    }
    @GetMapping("/ListagemRealizadaReservaEqui")
    public String listagemRealizadaReservaEqui(@RequestParam(defaultValue = "0") int page,@ModelAttribute ReservaEquipamentoModel reservaEquipamentoModel, Model model,RedirectAttributes redirectAttributes) {
        String filter = reservaEquipamentoModel.getEquipamento().getNomeEquipamento() != null ? reservaEquipamentoModel.getEquipamento().getNomeEquipamento() : "";

        return loadReservaEquipamento(page,model,filter);
    }

    public String loadReservaEquipamento(int page,Model model,String filter){
        Pageable pageable = PageRequest.of(page,5);
        Page<ReservaEquipamentoModel> reservaEquipamentoPages;

        if(filter != null && !filter.isEmpty()){
            reservaEquipamentoPages = reservaEquipamentoRepository.findReservaEquipamentoByNomeEquipamento(filter,pageable);
            if(reservaEquipamentoRepository.findReservaEquipamentoByNomeEquipamento(filter,pageable).getSize() >= 5){
                model.addAttribute("totalItems",1);
            }
            System.out.println(reservaEquipamentoRepository.findReservaEquipamentoByNomeEquipamento("",pageable).getSize());
        }else{
            reservaEquipamentoPages = reservaEquipamentoRepository.findReservaEquipamentoByNomeEquipamento("",pageable);
            if(reservaEquipamentoRepository.findReservaEquipamentoByNomeEquipamento("",pageable).getSize() >= 5){
                model.addAttribute("totalItems",1);
            }
            System.out.println(reservaEquipamentoRepository.findReservaEquipamentoByNomeEquipamento("",pageable).getSize());
        }

        model.addAttribute("reservasEquipamentos", reservaEquipamentoPages.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", reservaEquipamentoPages.getTotalPages());


        List<Integer> pageNumbers = IntStream.range(0, reservaEquipamentoPages.getTotalPages())
                .boxed()
                .collect(Collectors.toList());

        model.addAttribute("pageNumbers",pageNumbers);
        model.addAttribute("reservaEquipamentoModel", new ReservaEquipamentoModel());
        model.addAttribute("filter", filter);

        return "ReservaEquipamento/Listar";
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
