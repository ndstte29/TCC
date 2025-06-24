package tg.codigo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import tg.codigo.interfaces.Icontrolador;
import tg.codigo.models.Venda;
import tg.codigo.services.ServiceProduto;
import tg.codigo.services.ServiceUsuario;
import tg.codigo.services.ServiceVenda;

@Controller
@RequestMapping("/vendas")
public class ControllerVenda implements Icontrolador<Venda, Long> {

    @Autowired
    private ServiceVenda serviceVenda;

    @Autowired
    private ServiceProduto serviceProduto;

    @Autowired
    private ServiceUsuario serviceUsuario;

    @Override
    @GetMapping("/saida")
    public ModelAndView getNovo() {
        ModelAndView mv = new ModelAndView("vendas/saida");
        mv.addObject("venda", new Venda());
        mv.addObject("produto", serviceProduto.listarTodos());
        mv.addObject("usuario", serviceUsuario.listarTodos());
        return mv;
    }

    @Override
    @PostMapping("/saida")
    public ModelAndView postNovo(@ModelAttribute Venda venda, RedirectAttributes redirectAttributes) {
        try {
            serviceVenda.salvar(venda);
            List<String> alertas = serviceVenda.getAlertasEstoque();

            String mensagem = venda.getVenAcao().equals("entrada")
                    ? "Entrada de produto registrada com sucesso!"
                    : "Saída de produto registrada com sucesso!";
            redirectAttributes.addFlashAttribute("success", mensagem);

            if (!alertas.isEmpty()) {
                redirectAttributes.addFlashAttribute("alertasEstoque", alertas);
            }

            return new ModelAndView("redirect:/vendas/lista");

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return new ModelAndView("redirect:/vendas/saida");
        }
    }

    @GetMapping("/lista")
    public ModelAndView listarTodos() {
        ModelAndView mv = new ModelAndView("vendas/lista");
        mv.addObject("venda", serviceVenda.listarTodos());
        return mv;
    }

    @Override
    public ModelAndView editar(Long atributo) {
        throw new UnsupportedOperationException("Unimplemented method 'editar'");
    }

    @Override
    public ModelAndView excluir(Long atributo) {
        throw new UnsupportedOperationException("Unimplemented method 'excluir'");
    }

    @Override
    public ModelAndView remover(Venda objeto, HttpSession session, RedirectAttributes redirectAttributes) {
        throw new UnsupportedOperationException("Unimplemented method 'remover'");
    }
}