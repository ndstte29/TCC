package tg.codigo.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import tg.codigo.interfaces.IService;
import tg.codigo.models.Usuarios;
import tg.codigo.repositories.RepositoryUsuario;
import tg.codigo.utils.CpfValidator;
import tg.codigo.utils.PermissaoNegadaException;

@Service
public class ServiceUsuario implements IService<Usuarios, Long> {

    @Autowired
    private RepositoryUsuario repositoryUsuario;

    // Constantes de permissão simplificadas
    private static final String PERMISSAO_ADMIN = "ADMIN";

    // Métodos da interface IService
    @Override
    public Usuarios salvar(Usuarios usuario) {
        // Validação do CPF
        if (!CpfValidator.isCpfValid(usuario.getUsuCpf())) {
            throw new RuntimeException("CPF inválido!");
        }

        // Validação do e-mail
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (usuario.getUsuEmail() == null || !usuario.getUsuEmail().matches(emailRegex)) {
            throw new RuntimeException("Formato de e-mail inválido!");
        }

        // Verifica se já existe usuário com o mesmo CPF (exceto o próprio usuário)
        Usuarios usuarioExistentePorCpf = repositoryUsuario.findByUsuCpf(usuario.getUsuCpf());
        if (usuarioExistentePorCpf != null && !usuarioExistentePorCpf.getUsuId().equals(usuario.getUsuId())) {
            throw new RuntimeException("Já existe um usuário cadastrado com este CPF.");
        }

        // Verifica se já existe usuário com o mesmo e-mail (exceto o próprio usuário)
        Optional<Usuarios> usuarioExistentePorEmail = repositoryUsuario.findByUsuEmail(usuario.getUsuEmail());
        if (usuarioExistentePorEmail.isPresent() &&
                !usuarioExistentePorEmail.get().getUsuId().equals(usuario.getUsuId())) {
            throw new RuntimeException("Já existe um usuário cadastrado com este e-mail.");
        }

        return repositoryUsuario.save(usuario);
    }

    @Override
    public Usuarios localizar(Long id) {
        return repositoryUsuario.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    @Override
    public void excluir(Usuarios usuario) {
        try {
            Objects.requireNonNull(usuario, "O objeto Usuário não pode ser nulo");
            Objects.requireNonNull(usuario.getUsuId(), "ID do usuário não pode ser nulo");

            if (!repositoryUsuario.existsById(usuario.getUsuId())) {
                throw new EmptyResultDataAccessException(
                        "Usuário com ID %d não encontrado".formatted(usuario.getUsuId()), 1);
            }

            repositoryUsuario.delete(usuario);

        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException(
                    "Não foi possível excluir o usuário pois existem registros vinculados.", e);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao excluir usuário: " + e.getMessage(), e);
        }
    }

    @Override
    public Usuarios Atualizar(Usuarios usuario) {
        if (repositoryUsuario.existsById(usuario.getUsuId())) {
            return repositoryUsuario.save(usuario);
        }
        throw new RuntimeException("Usuário não encontrado para atualização.");
    }

    @Override
    public List<Usuarios> listarTodos() {
        return repositoryUsuario.findAll();
    }

    public Usuarios salvarComPermissao(Usuarios usuario, Usuarios solicitante) {
    // Se o usuário sendo cadastrado for ADMIN, verifica se o solicitante é ADMIN
    if (PERMISSAO_ADMIN.equals(usuario.getUsuPermissao())) {
        verificarPermissao(solicitante, PERMISSAO_ADMIN); // Lança exceção se não for ADMIN
    }
    // Se o solicitante não for ADMIN, FORÇA a permissão para não-ADMIN
    else if (!PERMISSAO_ADMIN.equals(solicitante.getUsuPermissao())) {
        usuario.setUsuPermissao("FUNCIONARIO"); // Define como FUNCIONARIO (não-ADMIN)
    }
    return salvar(usuario);
}

    public Usuarios atualizarComPermissao(Usuarios usuario, Usuarios solicitante) {
        validarAutoPromocao(usuario, solicitante);
        verificarPermissaoParaEdicao(usuario, solicitante);
        return Atualizar(usuario);
    }

    public void excluirComPermissao(Usuarios usuario, Usuarios solicitante) {
        validarAutoExclusao(usuario, solicitante);
        verificarPermissaoParaExclusao(usuario, solicitante);
        excluir(usuario);
    }

    public List<Usuarios> listarTodosComPermissao(Usuarios solicitante) {
        verificarPermissao(solicitante, PERMISSAO_ADMIN);
        return listarTodos();
    }

    public Usuarios localizarComPermissao(Long id, Usuarios solicitante) {
        if (solicitante.getUsuId().equals(id)) {
            return localizar(id);
        }
        verificarPermissao(solicitante, PERMISSAO_ADMIN);
        return localizar(id);
    }

    private void verificarPermissao(Usuarios usuario, String permissaoRequerida) {
        if (usuario == null || usuario.getUsuPermissao() == null) {
            throw new PermissaoNegadaException("Usuário não autenticado");
        }

        if (PERMISSAO_ADMIN.equals(permissaoRequerida)) {
            if (!PERMISSAO_ADMIN.equals(usuario.getUsuPermissao())) {
                throw new PermissaoNegadaException("Acesso restrito a administradores");
            }
        }
    }

    private void validarAutoPromocao(Usuarios usuario, Usuarios solicitante) {
        if (!PERMISSAO_ADMIN.equals(solicitante.getUsuPermissao()) &&
                usuario.getUsuPermissao() != null &&
                !usuario.getUsuPermissao().equals(solicitante.getUsuPermissao())) {
            throw new PermissaoNegadaException("Você não tem permissão para alterar o registro");
        }
    }

    private void verificarPermissaoParaEdicao(Usuarios usuario, Usuarios solicitante) {
        if (!solicitante.getUsuId().equals(usuario.getUsuId())) {
            if (PERMISSAO_ADMIN.equals(usuario.getUsuPermissao())) {
                verificarPermissao(solicitante, PERMISSAO_ADMIN);
            } else {
                // Para editar funcionários, apenas admin pode editar outros funcionários
                verificarPermissao(solicitante, PERMISSAO_ADMIN);
            }
        }
    }

    private void validarAutoExclusao(Usuarios usuario, Usuarios solicitante) {
        if (solicitante.getUsuId().equals(usuario.getUsuId())) {
            throw new PermissaoNegadaException("Você não pode excluir sua própria conta");
        }
    }

    private void verificarPermissaoParaExclusao(Usuarios usuario, Usuarios solicitante) {
        if (PERMISSAO_ADMIN.equals(usuario.getUsuPermissao())) {
            verificarPermissao(solicitante, PERMISSAO_ADMIN);
        } else {
            // Para excluir funcionários, apenas admin pode excluir
            verificarPermissao(solicitante, PERMISSAO_ADMIN);
        }
    }

    // Método de autenticação
    public Usuarios autenticar(String email, String senha) {
        return repositoryUsuario.findByUsuEmail(email)
                .filter(usuario -> usuario.getUsuSenha().equals(senha))
                .orElseThrow(() -> new RuntimeException("Credenciais inválidas"));
    }
}