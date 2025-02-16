package tg.codigo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
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
    public ModelAndView postNovo(@ModelAttribute("produto") Produtos pro) {
        ModelAndView mv;
        serviceProduto.salvar(pro);
        mv = new ModelAndView("redirect:/produto/lista");
        return mv;
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
    public ModelAndView editar(@ModelAttribute("produto") Produtos produto, @RequestParam("proId") Long id) {
        produto.setProId(id);
        serviceProduto.salvar(produto);
        ModelAndView mv = new ModelAndView("redirect:/produto/lista");
        return mv;
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