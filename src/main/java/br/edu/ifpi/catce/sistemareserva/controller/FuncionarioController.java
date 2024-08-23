package br.edu.ifpi.catce.sistemareserva.controller;

import br.edu.ifpi.catce.sistemareserva.model.FuncionarioModel;
import br.edu.ifpi.catce.sistemareserva.repository.FuncionarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class FuncionarioController {
    private final FuncionarioRepository funcionarioRepository;

    public FuncionarioController(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    @GetMapping("/funcionario")
    public String funcionario(){
        return "Funcionario/Funcionario";
    }

    @GetMapping("/cadastroFuncionario")
    public String cadastroFuncionario(Model model){
        model.addAttribute(new FuncionarioModel());
        return "Funcionario/Cadastrar";
    }

    @PostMapping("/cadastroRealizadoFunc")
    public String cadastroRealizadoFunc(FuncionarioModel funcionarioModel, RedirectAttributes redirectAttributes){
        funcionarioModel.setNomeFuncionario(funcionarioModel.getNomeFuncionario().toUpperCase());
        funcionarioRepository.save(funcionarioModel);
        redirectAttributes.addFlashAttribute("mensagem","CADASTRADO COM SUCESSO!!!");
        redirectAttributes.addFlashAttribute("style","mensagem alert alert-success");
        return "redirect:/cadastroFuncionario";
    }

    @GetMapping("/listFunc")
    public String listagemFuncionario(Model model){
        model.addAttribute(new FuncionarioModel());
        return "Funcionario/Listar";
    }
    @PostMapping("/ListagemFuncionario")
    public String listagemFuncionario(@ModelAttribute FuncionarioModel funcionarioModel, Model model, RedirectAttributes redirectAttributes){
        List<FuncionarioModel> funcionarios = funcionarioRepository.findByNomeFuncionario(funcionarioModel.getNomeFuncionario().toUpperCase());
        if(funcionarios.size() > 0){
            model.addAttribute(funcionarios);
        } else{
            redirectAttributes.addFlashAttribute("mensagem","NÃO EXISTE O FUNCIONARIO OU O NOME NÃO FOI ECONTRADO");
            redirectAttributes.addFlashAttribute("style","mensagem alert alert-danger");
            return "redirect:/listFunc";
        }
        return "Funcionario/Listar";
    }
    @PostMapping("/deletarFuncionario")
    public String deletarFuncionario(@RequestParam("id") Integer id, RedirectAttributes redirectAttributes){
        funcionarioRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("mensagem","APAGADO COM SUCESSO!!!");
        redirectAttributes.addFlashAttribute("style","mensagem alert alert-success");

        return "redirect:/listFunc";
    }

    @GetMapping("/atualizarPorIdFunc")
    public String atualizarPorIdFunc(@RequestParam("id") Integer id, @RequestParam("nome") String nome, Model model){
        model.addAttribute("id",id);
        model.addAttribute("nome",nome.toUpperCase());
        return "Funcionario/Atualizar";
    }

    @PostMapping("atualizarFunc")
    @Transactional
    public String atualizarFunc(@RequestParam("id") Integer id, @RequestParam("nome") String nome, @RequestParam("cargo") String cargo,RedirectAttributes redirectAttributes){
        funcionarioRepository.updateFuncionarioModelById(id,nome.toUpperCase(),cargo);
        redirectAttributes.addFlashAttribute("mensagem","ATUALIZADO COM SUCESSO!!!");
        redirectAttributes.addFlashAttribute("style","mensagem alert alert-success");
        return "redirect:/listFunc";
    }

}
