package tg.codigo.interfaces;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface Icontrolador<T, K> {

    public ModelAndView listarTodos();

    public ModelAndView getNovo();

    ModelAndView postNovo(T objeto, RedirectAttributes redirectAttributes); // Adicione RedirectAttributes

    public ModelAndView editar(K atributo); // Mantenha apenas este método

    public ModelAndView excluir(K atributo);

    public ModelAndView remover(T objeto);

}