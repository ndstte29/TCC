package tg.codigo.interfaces;

import java.util.List;

public interface IService<T, K> {
    T salvar(T objeto);
    List<T> listarTodos();
    T localizar(K atributo);
    void excluir(T objeto);
}