package tg.codigo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;
import tg.codigo.interfaces.Icontrolador;
import tg.codigo.models.Produtos;
import tg.codigo.models.Usuarios;
import tg.codigo.services.ServiceFornecedor;
import tg.codigo.services.ServiceProduto;
import tg.codigo.utils.PermissaoNegadaException;

@Controller
@RequestMapping("/produto")
public class ControllerProduto implements Icontrolador<Produtos, Long> {

    @Autowired
    private ServiceProduto serviceProduto;

    @Autowired
    private ServiceFornecedor serviceFornecedor;

    public ControllerProduto(ServiceProduto serviceProduto, ServiceFornecedor serviceFornecedor) {
        this.serviceProduto = serviceProduto;
        this.serviceFornecedor = serviceFornecedor;
    }

    @Override
    @GetMapping("/lista")
    public ModelAndView listarTodos() {
        ModelAndView mv = new ModelAndView("produto/lista");
        mv.addObject("produtos", serviceProduto.listarTodos());
        String alertaEstoque = serviceProduto.verificarEstoqueBaixo();
        if (alertaEstoque != null) {
            mv.addObject("alertaEstoque", alertaEstoque);
        }

        return mv;
    }

    @GetMapping("/novo")
    public ModelAndView getNovo() {
        ModelAndView mv = new ModelAndView("produto/novo");
        mv.addObject("produto", new Produtos());
        mv.addObject("fornecedor", serviceFornecedor.listarTodos());
        return mv;
    }

    @Override
    @PostMapping("/novo")
    public ModelAndView postNovo(@ModelAttribute Produtos produto, RedirectAttributes redirectAttributes) {
        try {
            serviceProduto.salvar(produto);
            redirectAttributes.addFlashAttribute("success", "Produto cadastrado com sucesso!");
            return new ModelAndView("redirect:/produto/lista");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            ModelAndView mv = new ModelAndView("produto/novo");
            mv.addObject("produto", produto);
            mv.addObject("fornecedor", serviceFornecedor.listarTodos());
            return mv;
        }
    }

    @Override
    @GetMapping("/editar/{id}")
    public ModelAndView editar(@PathVariable Long id) {
        ModelAndView mv = new ModelAndView("produto/editar");
        mv.addObject("produto", serviceProduto.localizar(id));
        mv.addObject("fornecedor", serviceFornecedor.listarTodos());
        return mv;
    }

    @PostMapping("/editar")
    public ModelAndView editar(@ModelAttribute Produtos produto,
            @RequestParam("proId") Long id,
            Model model,
            RedirectAttributes redirectAttributes) {
        produto.setProId(id);
        try {
            serviceProduto.Atualizar(produto);
            redirectAttributes.addFlashAttribute("success", "Produto atualizado com sucesso!");
            return new ModelAndView("redirect:/produto/lista");
        } catch (RuntimeException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("produto", produto);
            model.addAttribute("fornecedor", serviceFornecedor.listarTodos());
            return new ModelAndView("produto/editar");
        }
    }

    @Override
    @GetMapping("/excluir/{id}")
    public ModelAndView excluir(@PathVariable("id") Long atributo) {
        ModelAndView mv = new ModelAndView("produto/excluir");
        mv.addObject("produto", serviceProduto.localizar(atributo));
        return mv;
    }

    @Override
    @PostMapping("/excluir")

    public ModelAndView remover(@ModelAttribute Produtos produto,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {

            Usuarios usuarioLogado = (Usuarios) session.getAttribute("usuarioLogado");

            if (usuarioLogado == null) {
                throw new PermissaoNegadaException("Faça login para realizar esta operação.");
            }
            serviceProduto.excluirComPermissao(produto, usuarioLogado);
            redirectAttributes.addFlashAttribute("success", "Produto excluído com sucesso!");

        } catch (PermissaoNegadaException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao excluir produto: " + e.getMessage());
        }
        return new ModelAndView("redirect:/produto/lista");
    }
}