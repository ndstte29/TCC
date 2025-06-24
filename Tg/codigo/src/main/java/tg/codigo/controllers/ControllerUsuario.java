package tg.codigo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tg.codigo.interfaces.Icontrolador;
import tg.codigo.models.Usuarios;
import tg.codigo.services.ServiceUsuario;
import tg.codigo.utils.PermissaoNegadaException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/usuarios")
public class ControllerUsuario implements Icontrolador<Usuarios, Long> {

    @Autowired
    private ServiceUsuario serviceUsuario;

    @Override
    @GetMapping("/novo")
    public ModelAndView getNovo() {
        ModelAndView mv = new ModelAndView("usuarios/novo");
        mv.addObject("usuarios", new Usuarios());
        return mv;
    }

    @Override
    @PostMapping("/novo")
    public ModelAndView postNovo(
            @ModelAttribute("usuarios") Usuarios usuario,
            RedirectAttributes redirectAttributes) {
        // Obtém o request sem injetar no parâmetro
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        try {
            // 1. Recupera usuário da sessão
            HttpSession session = request.getSession(false);
            Usuarios usuarioLogado = (session != null)
                    ? (Usuarios) session.getAttribute("usuarioLogado")
                    : null;

            // 2. Validações
            if (usuarioLogado == null) {
                throw new PermissaoNegadaException("Faça login para cadastrar usuários.");
            }

            if ("ADMIN".equals(usuario.getUsuPermissao()) && !"ADMIN".equals(usuarioLogado.getUsuPermissao())) {
                throw new PermissaoNegadaException("Apenas ADMINS podem cadastrar outros ADMINS.");
            }

            // 3. Lógica principal
            serviceUsuario.salvar(usuario);
            redirectAttributes.addFlashAttribute("success", "Usuário cadastrado com sucesso!");
            return new ModelAndView("redirect:/usuarios/lista");

        } catch (PermissaoNegadaException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return new ModelAndView("redirect:/usuarios/novo");
        } catch (RuntimeException e) {
            ModelAndView mv = new ModelAndView("usuarios/novo");
            mv.addObject("usuarios", usuario);
            mv.addObject("erro", e.getMessage());
            return mv;
        }
    }

    @Override
    @GetMapping("/lista")
    public ModelAndView listarTodos() {

        ModelAndView mv = new ModelAndView("usuarios/lista");
        mv.addObject("usuarios", serviceUsuario.listarTodos());
        return mv;
    }

    @Override
    @GetMapping("/editar/{id}")
    public ModelAndView editar(@PathVariable Long id) {
        ModelAndView mv = new ModelAndView("usuarios/editar");
        Usuarios usuario = serviceUsuario.localizar(id);
        mv.addObject("usuarios", usuario);
        return mv;
    }

    @Override
    @GetMapping("/excluir/{id}")
    public ModelAndView excluir(@PathVariable Long id) {
        ModelAndView mv = new ModelAndView("usuarios/excluir");
        Usuarios usuario = serviceUsuario.localizar(id);
        mv.addObject("usuarios", usuario);
        return mv;
    }

    @Override
    @PostMapping("/excluir")

    public ModelAndView remover(@ModelAttribute Usuarios usuarios,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {

            Usuarios usuarioLogado = (Usuarios) session.getAttribute("usuarioLogado");

            if (usuarioLogado == null) {

                throw new PermissaoNegadaException("Faça login para realizar esta operação.");
            }

            serviceUsuario.excluirComPermissao(usuarios, usuarioLogado);
            redirectAttributes.addFlashAttribute("success", "Usuário excluído com sucesso!");

        } catch (PermissaoNegadaException e) {

            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao excluir usuário: " + e.getMessage());
        }

        return new ModelAndView("redirect:/usuarios/lista");
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("usuarios", new Usuarios());
        return "usuarios/login";
    }

    @PostMapping("/login")
    public ModelAndView login(@ModelAttribute Usuarios usuarios,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            Usuarios usuarioAutenticado = serviceUsuario.autenticar(usuarios.getUsuEmail(), usuarios.getUsuSenha());
            session.setAttribute("usuarioLogado", usuarioAutenticado);
            redirectAttributes.addFlashAttribute("success", "Bem-vindo, " + usuarioAutenticado.getUsuNome() + "!");
            return new ModelAndView("redirect:/vendas/lista");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro", "Credenciais inválidas!");
            return new ModelAndView("redirect:/usuarios/login");
        }
    }

    @GetMapping("/logout")
    public ModelAndView logout(HttpSession session) {
        session.invalidate();
        return new ModelAndView("redirect:/usuarios/login");
    }

    @PostMapping("/editar")
    public ModelAndView postEditar(@ModelAttribute("usuarios") Usuarios usuario,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        Usuarios usuarioLogado = (Usuarios) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return new ModelAndView("redirect:/usuarios/login");
        }

        try {
            serviceUsuario.atualizarComPermissao(usuario, usuarioLogado);
            redirectAttributes.addFlashAttribute("success", "Usuário atualizado com sucesso!");
            return new ModelAndView("redirect:/usuarios/lista");
        } catch (PermissaoNegadaException e) {
            redirectAttributes.addFlashAttribute("erro", "Acesso negado: " + e.getMessage());
            return new ModelAndView("redirect:/usuarios/lista");
        } catch (RuntimeException e) {
            ModelAndView mv = new ModelAndView("usuarios/editar");
            mv.addObject("usuarios", usuario);
            mv.addObject("erro", e.getMessage());
            return mv;
        }
    }

    @GetMapping("/lista-com-permissao")
    public ModelAndView listarComPermissao(HttpSession session, RedirectAttributes redirectAttributes) {
        Usuarios usuarioLogado = (Usuarios) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            redirectAttributes.addFlashAttribute("erro", "Faça login para continuar");
            return new ModelAndView("redirect:/usuarios/login");
        }

        try {
            ModelAndView mv = new ModelAndView("usuarios/lista");
            mv.addObject("usuarios", serviceUsuario.listarTodosComPermissao(usuarioLogado));
            mv.addObject("usuarioLogado", usuarioLogado);
            return mv;
        } catch (PermissaoNegadaException e) {
            redirectAttributes.addFlashAttribute("erro", "Acesso negado: " + e.getMessage());
            return new ModelAndView("redirect:/usuarios/lista");
        }
    }
}