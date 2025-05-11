package tg.codigo.interfaces;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface Icontrolador<T, K> {

    public ModelAndView listarTodos();

    public ModelAndView getNovo();

    ModelAndView postNovo(T objeto, RedirectAttributes redirectAttributes);

    public ModelAndView editar(K atributo);

    public ModelAndView excluir(K atributo);

    public ModelAndView remover(T objeto, RedirectAttributes redirectAttributes);

}