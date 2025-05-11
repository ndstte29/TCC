package tg.codigo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import tg.codigo.interfaces.Icontrolador;
import tg.codigo.models.Fornecedor;
import tg.codigo.models.Usuarios;
import tg.codigo.services.ServiceFornecedor;

@Controller
@RequestMapping("/fornecedor")
public class ControllerFornecedor implements Icontrolador<Fornecedor, Long> {

    @Autowired
    private ServiceFornecedor serviceFornecedor;

    private HttpSession session;


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
    public ModelAndView postNovo(@ModelAttribute("fornecedor") Fornecedor fornecedor,  RedirectAttributes redirectAttributes) {
        serviceFornecedor.salvar(fornecedor);
        redirectAttributes.addFlashAttribute("success", "Fornecedor cadastrado com sucesso!");
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
        serviceFornecedor.Atualizar(fornecedor);
        mv = new ModelAndView("redirect:/fornecedor/lista");
    } catch (RuntimeException e) {
        mv = new ModelAndView("fornecedor/editar");
        mv.addObject("fornecedor", fornecedor);
        mv.addObject("erro", e.getMessage());
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
    public ModelAndView remover(@ModelAttribute("fornecedor") Fornecedor fornecedor,
                                RedirectAttributes redirectAttributes) {

        // Obtendo o usuário logado diretamente da sessão
        Usuarios usuarioLogado = (Usuarios) session.getAttribute("usuarioLogado");

        // Verifica se o usuário tem permissão de ADMIN
        if (usuarioLogado == null || !"ADMIN".equals(usuarioLogado.getUsuPermissao())) {
            redirectAttributes.addFlashAttribute("erro", "Você não tem permissão para excluir fornecedores.");
            return new ModelAndView("redirect:/fornecedor/lista");
        }

        try {
            // Chama o serviço para excluir o fornecedor
            serviceFornecedor.excluir(fornecedor);
            redirectAttributes.addFlashAttribute("success", "Fornecedor excluído com sucesso!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }

        return new ModelAndView("redirect:/fornecedor/lista");
    }
}
