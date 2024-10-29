package br.edu.ifpi.catce.sistemareserva.controller;

import br.edu.ifpi.catce.sistemareserva.model.EquipamentoModel;
import br.edu.ifpi.catce.sistemareserva.repository.EquipamentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    // O CONTROLADOR RETORNA A PRIMEIRA PÁGINA COM TODOS OS EQUIPAMENTOS COM LOTE DE 5 POR VEZ.
    @GetMapping("/listEquipamento")
    public String listEquipamento(@RequestParam(defaultValue = "0") int page, Model model) {
        return loadEquipamentoPage(page, model, null);
    }

    //CONTEM DADOS DO FORMULÁRIO, ESPECIFICAMENTE O NOME DO EQUIPAMENTO PARA FILTRAGEM.
    @GetMapping("/listagemEquipamento")
    public String listagemEquipamento(@ModelAttribute EquipamentoModel equipamentoModel,
                                      Model model, RedirectAttributes redirectAttributes,
                                      @RequestParam(defaultValue = "0") int page) {
        String filter = equipamentoModel.getNomeEquipamento() != null
                ? equipamentoModel.getNomeEquipamento().toUpperCase()
                : "";

        // PASSA A PÁGINA ATUAL COM O FILTRO;
        return loadEquipamentoPage(page, model, filter);
    }

    // VAI MOSTRAR TODOS OS RESULTADOS PAGINADO EM LOTES DE 5 POR VEZ.
    // LÓGICA USADA PARA FORMATAR OS DADOS DA VISUALIZAÇÃO, INDEPENDETEMENTE DE O USUÁRIO ESTAR FILTRANDO OU NÃO.
    private String loadEquipamentoPage(int page, Model model, String filter) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<EquipamentoModel> equipamentosPage;

        if (filter != null && !filter.isEmpty()) {
            equipamentosPage = equipamentoRepository.findByNomeEquipamento(filter, pageable);
        } else {
            equipamentosPage = equipamentoRepository.findAll(pageable);
        }

        model.addAttribute("equipamentos", equipamentosPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", equipamentosPage.getTotalPages());

        List<Integer> pageNumbers = IntStream.range(0, equipamentosPage.getTotalPages())
                .boxed()
                .collect(Collectors.toList());
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("equipamentoModel", new EquipamentoModel());
        model.addAttribute("filter", filter);

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
