package tg.codigo.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tg.codigo.interfaces.Icontrolador;
import tg.codigo.models.Produtos;
import tg.codigo.services.ServiceFornecedor;
import tg.codigo.services.ServiceProduto;

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

    @GetMapping("/novo")
    public ModelAndView getNovo() {
        ModelAndView mv = new ModelAndView("produto/novo");
        mv.addObject("produto", new Produtos());
        mv.addObject("fornecedor", serviceFornecedor.listarTodos());
        return mv;
    }

    @Override
    @PostMapping("/novo")
    public ModelAndView postNovo(@ModelAttribute("produto") Produtos produto, RedirectAttributes redirectAttributes) {
        try {
            serviceProduto.salvar(produto);
            redirectAttributes.addFlashAttribute("success", "Produto cadastrado com sucesso!");
            return new ModelAndView("redirect:/produto/lista");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return new ModelAndView("redirect:/produto/novo");
        }
    }

    @Override
    @GetMapping("/editar/{id}")
    public ModelAndView editar(@PathVariable("id") Long id) {
        ModelAndView mv = new ModelAndView("produto/editar");
        mv.addObject("produto", serviceProduto.localizar(id));
        mv.addObject("fornecedor", serviceFornecedor.listarTodos());
        return mv;
    }

    @PostMapping("/editar")
    public ModelAndView editar(@ModelAttribute("produto") Produtos produto,
            @RequestParam("proId") Long id,
            Model model,
            RedirectAttributes redirectAttributes) {
        produto.setProId(id);
        try {
            serviceProduto.salvar(produto);
            redirectAttributes.addFlashAttribute("success", "Produto atualizado com sucesso!");
            return new ModelAndView("redirect:/produto/lista");
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
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

    @PostMapping("/excluir")
    @Override
    public ModelAndView remover(Produtos objeto, RedirectAttributes redirectAttributes) {
        try {
            serviceProduto.excluir(objeto);
            redirectAttributes.addFlashAttribute("success", "Produto excluído com sucesso!");
            return new ModelAndView("redirect:/produto/lista");
        } catch (RuntimeException e) {
            Produtos produto = serviceProduto.localizar(objeto.getProId());
            ModelAndView mv = new ModelAndView("produto/excluir");
            mv.addObject("produto", produto);
            mv.addObject("erro", "Erro ao excluir produto: " + e.getMessage());
            return mv;
        }
    }

    @Override
    @GetMapping("/lista")
    public ModelAndView listarTodos() {
        ModelAndView mv = new ModelAndView("produto/lista");
        mv.addObject("produtos", serviceProduto.listarTodos());
        return mv;
    }
}
