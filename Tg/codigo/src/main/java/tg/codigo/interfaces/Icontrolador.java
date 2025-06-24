package tg.codigo.interfaces;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession; // Adicione esta importação!

public interface Icontrolador<T, K> {

    public ModelAndView listarTodos();

    public ModelAndView getNovo();

    ModelAndView postNovo(T objeto, RedirectAttributes redirectAttributes);

    public ModelAndView editar(K atributo);

    public ModelAndView excluir(K atributo); // Este método 'excluir' está com um 'K atributo' que não é o objeto T

    // Método 'remover' atualizado com HttpSession
    // Agora o método no ControllerFornecedor poderá usar @Override
    public ModelAndView remover(T objeto, HttpSession session, RedirectAttributes redirectAttributes);

}