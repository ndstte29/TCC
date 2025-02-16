package tg.codigo.interfaces;

public interface IService <T,K> {

    public T salvar(T objeto);

    public  T localizar(K atributo);

    public void excluir (T objeto);

}