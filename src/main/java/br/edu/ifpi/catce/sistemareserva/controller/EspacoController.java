package br.edu.ifpi.catce.sistemareserva.controller;

import br.edu.ifpi.catce.sistemareserva.model.EspacoModel;
import br.edu.ifpi.catce.sistemareserva.repository.EspacoRepository;
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
public class EspacoController {
    @Autowired
    EspacoRepository espacoRepository;

    @GetMapping("/espaco")
    public String espaco(){
        return "Espaco/Espaco";
    }

    @GetMapping("/cadastroEspaco")
    public String cadastroEspaco(Model model){
        model.addAttribute(new EspacoModel());
        return "Espaco/Cadastrar";
    }

    @PostMapping("/cadastroRealizadoEsp")
    public String cadastroRealizadoEsp(EspacoModel espacoModel, RedirectAttributes redirectAttributes){
        espacoModel.setNomeEspaco(espacoModel.getNomeEspaco().toUpperCase());
        espacoRepository.save(espacoModel);
        redirectAttributes.addFlashAttribute("mensagem","CADASTRADO COM SUCESSO!!!");
        redirectAttributes.addFlashAttribute("style","mensagem alert alert-success");
        return "redirect:/cadastroEspaco";
    }

    @GetMapping("/listEspaco")
    public String listEspaco(Model model){
        model.addAttribute(new EspacoModel());
        return "Espaco/Listar";
    }
    @PostMapping("/listagemEspaco")
    public String listagemEspaco(@ModelAttribute EspacoModel espacoModel, Model model,RedirectAttributes redirectAttributes){
        List<EspacoModel> espacos = espacoRepository.findByNomeEspaco(espacoModel.getNomeEspaco().toUpperCase());
        if(espacos.size() > 0){
            model.addAttribute(espacos);
        } else{
            redirectAttributes.addFlashAttribute("mensagem","NÃO EXISTE O ESPAÇO OU O NOME NÃO FOI ENCONTRADO ");
            redirectAttributes.addFlashAttribute("style","mensagem alert alert-danger");
            return "redirect:/listEspaco";
        }
        return "Espaco/Listar";
    }
    @PostMapping("/deletarEspaco")
    public String deletarEspaco(@RequestParam("id") Integer id, RedirectAttributes redirectAttributes){
        espacoRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("mensagem","APAGADO COM SUCESSO!!!");
        redirectAttributes.addFlashAttribute("style","mensagem alert alert-success");

        return "redirect:/listEspaco";
    }

    @GetMapping("/atualizarPorIdEsp")
    public String atualizarPorIdEsp(@RequestParam("id") Integer id,@RequestParam("nome") String nome, Model model){
        model.addAttribute("id",id);
        model.addAttribute("nome",nome);
        return "Espaco/Atualizar";
    }

    @PostMapping("/atualizarEspaco")
    @Transactional
    public String atualizarEspaco(@RequestParam("id") Integer id,@RequestParam("nome") String nome,@RequestParam("status") String status, RedirectAttributes redirectAttributes){
        espacoRepository.updateEspacoById(id,nome.toUpperCase(),status);
        redirectAttributes.addFlashAttribute("mensagem","ATUALIZADO COM SUCESSO!!!");
        redirectAttributes.addFlashAttribute("style","mensagem alert alert-success");
        return "redirect:/listEspaco";
    }


}
