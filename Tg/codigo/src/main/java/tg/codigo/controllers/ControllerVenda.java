package tg.codigo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import tg.codigo.interfaces.Icontrolador;
import tg.codigo.models.Venda;
import tg.codigo.services.ServiceVenda;

@Controller
@RequestMapping("/vendas")
public class ControllerVenda implements Icontrolador<Venda, Long> {

    @Autowired
    private ServiceVenda serviceVenda;

    @Override
    @GetMapping("/lista")
    public ModelAndView listarTodos() {
        ModelAndView mv = new ModelAndView("vendas/lista");
        mv.addObject("vendas", serviceVenda.listarTodos());
        return mv;
    }

    @Override
    @GetMapping("/novo")
    public ModelAndView getNovo() {
        ModelAndView mv = new ModelAndView("vendas/novo");
        mv.addObject("venda", new Venda());
        return mv;
    }

    @Override
    @PostMapping("/novo")
    public ModelAndView postNovo(@ModelAttribute("venda") Venda venda) {
        serviceVenda.salvar(venda);
        return new ModelAndView("redirect:/vendas/lista");
    }

    @Override
    @GetMapping("/editar/{id}")
    public ModelAndView editar(@PathVariable("id") Long id) {
        ModelAndView mv = new ModelAndView("vendas/editar");
        Venda venda = serviceVenda.localizar(id);
        if (venda != null) {
            mv.addObject("venda", venda);
        } else {
            mv.setViewName("redirect:/vendas/lista");
        }
        return mv;
    }

    @PostMapping("/editar")
    public ModelAndView editar(@ModelAttribute("venda") Venda venda) {
        serviceVenda.salvar(venda);
        return new ModelAndView("redirect:/vendas/lista");
    }

    @Override
    @GetMapping("/excluir/{id}")
    public ModelAndView excluir(@PathVariable("id") Long id) {
        ModelAndView mv = new ModelAndView("vendas/excluir");
        Venda venda = serviceVenda.localizar(id);
        if (venda != null) {
            mv.addObject("venda", venda);
        } else {
            mv.setViewName("redirect:/vendas/lista");
        }
        return mv;
    }

    @PostMapping("/excluir")
    @Override
    public ModelAndView remover(@ModelAttribute("venda") Venda venda) {
        ModelAndView mv;
        try {
            serviceVenda.excluir(venda);
            mv = new ModelAndView("redirect:/vendas/lista");
        } catch (RuntimeException e) {
            mv = new ModelAndView("vendas/excluir");
            mv.addObject("venda", venda);
            mv.addObject("erro", e.getMessage());
        }
        return mv;
    }
}