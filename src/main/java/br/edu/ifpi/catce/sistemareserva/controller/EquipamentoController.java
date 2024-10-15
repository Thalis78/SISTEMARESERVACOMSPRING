package br.edu.ifpi.catce.sistemareserva.controller;

import br.edu.ifpi.catce.sistemareserva.model.EquipamentoModel;
import br.edu.ifpi.catce.sistemareserva.repository.EquipamentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class EquipamentoController {
    @Autowired
    EquipamentoRepository equipamentoRepository;

    @GetMapping("/equipamento")
    public String equipamento(){
        return "Equipamento/Equipamento";
    }


    @GetMapping("/cadastroEquipamento")
    public String cadastroEquipamento(Model model){
        model.addAttribute(new EquipamentoModel());
        return "Equipamento/Cadastrar";
    }

    @PostMapping("/cadastroRealizado")
    public String cadastroRealizado(EquipamentoModel equipamentoModel, RedirectAttributes redirectAttributes){
        equipamentoModel.setNomeEquipamento(equipamentoModel.getNomeEquipamento().toUpperCase());
        equipamentoRepository.save(equipamentoModel);
        redirectAttributes.addFlashAttribute("mensagem", "CADASTRADO COM SUCESSO!!!");
        redirectAttributes.addFlashAttribute("style", "mensagem alert alert-success");

        return "redirect:/cadastroEquipamento";

    }


    @GetMapping("/listEquipamento")
    public String listEquipamento(Model model){
        model.addAttribute(new EquipamentoModel());
        return "Equipamento/Listar";
    }
    @GetMapping("/listagemEquipamento")
    public String listagemEquipamento(@ModelAttribute EquipamentoModel equipamentoModel, Model model, RedirectAttributes redirectAttributes,@PageableDefault(size = 2) Pageable pageable){
        List<EquipamentoModel> equipamentos = equipamentoRepository.findByNomeEquipamento(equipamentoModel.getNomeEquipamento().toUpperCase());
        if(equipamentos.size() > 0){
            model.addAttribute(equipamentos);
        } else{
            redirectAttributes.addFlashAttribute("mensagem","NÃO EXISTE O EQUIPAMENTO OU NOME NÃO FOI ENCONTRADO");
            redirectAttributes.addFlashAttribute("style","mensagem alert alert-danger");
            return "redirect:/listEquipamento";
        }
        return "Equipamento/Listar";

    }


    @PostMapping("/deletarEquipamento")
    public String deletarEquipamento(@RequestParam("id") Integer id, RedirectAttributes redirectAttributes){
        equipamentoRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("mensagem","APAGADO COM SUCESSO!!!");
        redirectAttributes.addFlashAttribute("style","mensagem alert alert-success");
        return "redirect:/listEquipamento";
    }


    @GetMapping("/atualizarPorIdEqui")
    public String idRecebido(@RequestParam("id")Integer id,@RequestParam("nome")  String nome, @RequestParam("quant") int quant ,Model model){
        model.addAttribute("id",id);
        model.addAttribute("nome",nome.toUpperCase());
        model.addAttribute("quant",quant);
        return "Equipamento/Atualizar";
    }

    @PostMapping("/atualizarEquipamento")
    @Transactional
    public String atualizarEquipamento(@RequestParam("id") Integer id,@RequestParam("nome")  String nome, @RequestParam("quant") int quant, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("mensagem","ATUALIZADO COM SUCESSO!!!");
        redirectAttributes.addFlashAttribute("style","mensagem alert alert-success");
        equipamentoRepository.updateEquipamentoModelById(id,nome.toUpperCase(),quant);
        return "redirect:/listEquipamento";
    }


}
