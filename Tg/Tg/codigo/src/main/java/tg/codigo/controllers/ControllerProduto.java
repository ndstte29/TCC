package tg.codigo.controllers;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ModelAndView postNovo(
            @ModelAttribute("produto") Produtos produto,
            RedirectAttributes redirectAttributes) {
        serviceProduto.salvar(produto);
        redirectAttributes.addFlashAttribute("success", "Produto cadastrado com sucesso!");
        return new ModelAndView("redirect:/produto/lista");
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
    public ModelAndView editar(@ModelAttribute("produto") Produtos produto, @RequestParam("proId") Long id,
            Model model) {
        produto.setProId(id);

        // Validação: Verificar se o estoque máximo não é menor que o estoque mínimo
        if (produto.getProEstoquemaximo() < produto.getProEstoqueminimo()) {
            model.addAttribute("errorMessage", "O estoque máximo não pode ser menor que o estoque mínimo.");
            model.addAttribute("produto", produto); // Repassar o produto com os dados
            model.addAttribute("fornecedor", serviceFornecedor.listarTodos()); // Repassar fornecedores
            return new ModelAndView("produto/editar"); // Volta para a tela de edição com o erro
        }

        // Validação: Verificar se a data de vencimento é no passado
        if (produto.getProVencimento().isBefore(LocalDate.now())) {
            model.addAttribute("errorMessage", "A data de vencimento não pode ser anterior à data atual.");
            model.addAttribute("produto", produto); // Repassar o produto com os dados
            model.addAttribute("fornecedor", serviceFornecedor.listarTodos()); // Repassar fornecedores
            return new ModelAndView("produto/editar"); // Volta para a tela de edição com o erro
        }

        // Se as validações passarem, salvar o produto
        serviceProduto.salvar(produto);

        // Redireciona para a lista de produtos
        return new ModelAndView("redirect:/produto/lista");
    }

    @Override
    @GetMapping("/excluir/{id}")
    public ModelAndView excluir(@PathVariable("id") Long atributo) {
        ModelAndView mv = new ModelAndView("/produto/excluir.html");
        mv.addObject("produto", serviceProduto.localizar(atributo));
        return mv;
    }

    @PostMapping("/excluir")
    @Override
    public ModelAndView remover(Produtos objeto) {
        ModelAndView mv;
        try {
            serviceProduto.excluir(objeto);
            mv = new ModelAndView("redirect:/produto/lista");
        } catch (RuntimeException e) {
            mv = new ModelAndView("produto/excluir"); // Exibe a página de confirmação com erro
            Produtos produto = serviceProduto.localizar(objeto.getProId());
            mv.addObject("produto", produto);
            mv.addObject("erro", e.getMessage()); // Exibe a mensagem de erro
        }
        return mv;
    }

    @Override
    @GetMapping("/lista")
    public ModelAndView listarTodos() {
        ModelAndView mv = new ModelAndView("produto/lista");
        mv.addObject("produtos", serviceProduto.listarTodos());
        return mv;
    }
}