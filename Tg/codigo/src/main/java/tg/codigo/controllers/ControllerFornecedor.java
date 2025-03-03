package tg.codigo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import tg.codigo.interfaces.Icontrolador;
import tg.codigo.models.Fornecedor;
import tg.codigo.services.ServiceFornecedor;

@Controller
@RequestMapping("/fornecedor")
public class ControllerFornecedor implements Icontrolador<Fornecedor, Long> {

    @Autowired
    private ServiceFornecedor serviceFornecedor;

    @Override
    @GetMapping("/lista")
    public ModelAndView listarTodos() {
        ModelAndView mv = new ModelAndView("fornecedor/lista");
        mv.addObject("fornecedor", serviceFornecedor.listarTodos());
        return mv;
    }

    @Override
    @GetMapping("/novo")
    public ModelAndView getNovo() {
        ModelAndView mv = new ModelAndView("fornecedor/novo");
        mv.addObject("fornecedor", new Fornecedor());
        return mv;
    }

    @Override
    @PostMapping("/novo")
    public ModelAndView postNovo(@ModelAttribute("fornecedor") Fornecedor fornecedor) {
        serviceFornecedor.salvar(fornecedor);
        return new ModelAndView("redirect:/fornecedor/lista");
    }

    @Override
    @GetMapping("/editar/{id}")
    public ModelAndView editar(@PathVariable("id") Long id) {
        ModelAndView mv = new ModelAndView("fornecedor/editar");
        Fornecedor fornecedor = serviceFornecedor.localizar(id);
        if (fornecedor != null) {
            mv.addObject("fornecedor", fornecedor);
        } else {
            mv.setViewName("redirect:/fornecedor/lista");
        }
        return mv;
    }

    @PostMapping("/editar")
    public ModelAndView postEditar(@ModelAttribute("fornecedor") Fornecedor fornecedor) {
    ModelAndView mv;
    try {
        // Certifique-se de que a atualização está funcionando corretamente
        serviceFornecedor.Atualizar(fornecedor);  // Chama o método Atualizar do service
        mv = new ModelAndView("redirect:/fornecedor/lista");  // Redireciona para a lista
    } catch (RuntimeException e) {
        mv = new ModelAndView("fornecedor/editar");
        mv.addObject("fornecedor", fornecedor);
        mv.addObject("erro", e.getMessage());  // Exibe o erro se houver algum problema
    }
    return mv;
}


    @Override
    @GetMapping("/excluir/{id}")
    public ModelAndView excluir(@PathVariable("id") Long id) {
        ModelAndView mv = new ModelAndView("fornecedor/excluir");
        Fornecedor fornecedor = serviceFornecedor.localizar(id);
        if (fornecedor != null) {
            mv.addObject("fornecedor", fornecedor);
        } else {
            mv.setViewName("redirect:/fornecedor/lista");
        }
        return mv;
    }

    @PostMapping("/excluir")
    @Override
    public ModelAndView remover(@ModelAttribute("fornecedor") Fornecedor fornecedor) {
        ModelAndView mv;
        try {
            serviceFornecedor.excluir(fornecedor);
            mv = new ModelAndView("redirect:/fornecedor/lista");
        } catch (RuntimeException e) {
            mv = new ModelAndView("fornecedor/excluir");
            mv.addObject("fornecedor", fornecedor);
            mv.addObject("erro", e.getMessage());
        }
        return mv;
    }
}
