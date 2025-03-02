package tg.codigo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import tg.codigo.interfaces.Icontrolador;
import tg.codigo.models.ProdutoVenda;
import tg.codigo.services.ServiceProdutoVenda;

@Controller
@RequestMapping("/produtoVenda")
public class ControllerProdutoVenda implements Icontrolador<ProdutoVenda, Long> {

    @Autowired
    private ServiceProdutoVenda serviceProdutoVenda;

    @Override
    @GetMapping("/lista")
    public ModelAndView listarTodos() {
        ModelAndView mv = new ModelAndView("produtoVenda/lista");
        mv.addObject("produtoVendas", serviceProdutoVenda.listarTodos());
        return mv;
    }

    @Override
    @GetMapping("/novo")
    public ModelAndView getNovo() {
        ModelAndView mv = new ModelAndView("produtoVenda/novo");
        mv.addObject("produtoVenda", new ProdutoVenda());
        return mv;
    }

    @Override
    @PostMapping("/novo")
    public ModelAndView postNovo(@ModelAttribute("produtoVenda") ProdutoVenda produtoVenda) {
        serviceProdutoVenda.salvar(produtoVenda);
        return new ModelAndView("redirect:/produtoVenda/lista");
    }

    @Override
    @GetMapping("/editar/{id}")
    public ModelAndView editar(@PathVariable("id") Long id) {
        ModelAndView mv = new ModelAndView("produtoVenda/editar");
        ProdutoVenda produtoVenda = serviceProdutoVenda.localizar(id);
        if (produtoVenda != null) {
            mv.addObject("produtoVenda", produtoVenda);
        } else {
            mv.setViewName("redirect:/produtoVenda/lista");
        }
        return mv;
    }

    @PostMapping("/editar")
    public ModelAndView editar(@ModelAttribute("produtoVenda") ProdutoVenda produtoVenda) {
        serviceProdutoVenda.salvar(produtoVenda);
        return new ModelAndView("redirect:/produtoVenda/lista");
    }

    @Override
    @GetMapping("/excluir/{id}")
    public ModelAndView excluir(@PathVariable("id") Long id) {
        ModelAndView mv = new ModelAndView("produtoVenda/excluir");
        ProdutoVenda produtoVenda = serviceProdutoVenda.localizar(id);
        if (produtoVenda != null) {
            mv.addObject("produtoVenda", produtoVenda);
        } else {
            mv.setViewName("redirect:/produtoVenda/lista");
        }
        return mv;
    }

    @PostMapping("/excluir")
    @Override
    public ModelAndView remover(@ModelAttribute("produtoVenda") ProdutoVenda produtoVenda) {
        ModelAndView mv;
        try {
            serviceProdutoVenda.excluir(produtoVenda);
            mv = new ModelAndView("redirect:/produtoVenda/lista");
        } catch (RuntimeException e) {
            mv = new ModelAndView("produtoVenda/excluir");
            mv.addObject("produtoVenda", produtoVenda);
            mv.addObject("erro", e.getMessage());
        }
        return mv;
    }
}