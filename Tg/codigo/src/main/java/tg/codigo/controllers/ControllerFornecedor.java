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
import tg.codigo.utils.PermissaoNegadaException;

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

    @GetMapping("/novo")
    public ModelAndView getNovo() {
        ModelAndView mv = new ModelAndView("fornecedor/novo");
        mv.addObject("fornecedor", new Fornecedor());
        return mv;
    }

    @PostMapping("/novo")
    public ModelAndView postNovo(@ModelAttribute Fornecedor fornecedor,
            RedirectAttributes redirectAttributes) {
        try {
            serviceFornecedor.salvar(fornecedor);
            redirectAttributes.addFlashAttribute("success", "Fornecedor cadastrado com sucesso!");
            return new ModelAndView("redirect:/fornecedor/lista");
        } catch (RuntimeException e) {
            ModelAndView mv = new ModelAndView("fornecedor/novo");
            mv.addObject("fornecedor", fornecedor);
            mv.addObject("erro", e.getMessage());
            return mv;
        }
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editar(@PathVariable Long id) {
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
    public ModelAndView postEditar(
            @ModelAttribute Fornecedor fornecedor,
            RedirectAttributes redirectAttributes) {

        ModelAndView mv;
        try {
            serviceFornecedor.Atualizar(fornecedor);
            redirectAttributes.addFlashAttribute("success", "Fornecedor atualizado com sucesso!");
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
    public ModelAndView excluir(@PathVariable Long id) {
        ModelAndView mv = new ModelAndView("fornecedor/excluir");
        Fornecedor fornecedor = serviceFornecedor.localizar(id);
        if (fornecedor != null) {
            mv.addObject("fornecedor", fornecedor);
        } else {
            mv.setViewName("redirect:/fornecedor/lista");
        }
        return mv;
    }

    @Override
    @PostMapping("/excluir")
    public ModelAndView remover(@ModelAttribute Fornecedor fornecedor,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            Usuarios usuarioLogado = (Usuarios) session.getAttribute("usuarioLogado");

            if (usuarioLogado == null) {
                throw new PermissaoNegadaException("Faça login para realizar esta operação.");
            }

            serviceFornecedor.excluirComPermissao(fornecedor, usuarioLogado);
            redirectAttributes.addFlashAttribute("success", "Fornecedor excluído com sucesso!");

        } catch (PermissaoNegadaException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao excluir fornecedor: " + e.getMessage());
        }

        return new ModelAndView("redirect:/fornecedor/lista");
    }
}