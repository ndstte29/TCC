package tg.codigo.interfaces;

import org.springframework.web.servlet.ModelAndView;

public interface Icontrolador<T, K> {

    public ModelAndView listarTodos();

    public ModelAndView getNovo();

    public ModelAndView postNovo(T objeto);

    public ModelAndView editar(K atributo); // Mantenha apenas este método

    public ModelAndView excluir(K atributo);

    public ModelAndView remover(T objeto);

}