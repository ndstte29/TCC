package tg.codigo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import tg.codigo.models.Fornecedor;
import tg.codigo.services.ServiceFornecedor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/fornecedor")
public class ControllerFornecedor {
    
    @Autowired
    private ServiceFornecedor sfor;

    public ControllerFornecedor(ServiceFornecedor sfor){
        this.sfor = sfor;
    }

    @GetMapping("/novo")
    public ModelAndView getNovo() {
        ModelAndView mv = new ModelAndView("fornecedor/novo");
        mv.addObject("fornecedor", new Fornecedor());
        return mv;
    }

    @PostMapping("/novo")
    public String postNovo(@ModelAttribute("fornecedor") Fornecedor forn) {
        try{
        sfor.salvar(forn);
        }catch (Exception e){
            return "redirect:/index";
        }
        return "redirect:/fornecedor/novo";
    }

    @GetMapping("/lista")
    public ModelAndView listarfornecedores() {
        ModelAndView mv = new ModelAndView("fornecedor/lista");
        mv.addObject("fornecedor", sfor.listarTodos());
        return mv;
    }
}
