package tg.codigo.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;
import tg.codigo.interfaces.Icontrolador;
import tg.codigo.models.Usuarios;
import tg.codigo.services.ServiceUsuario;

@Controller
@RequestMapping("/usuarios")
public class ControllerUsuario implements Icontrolador<Usuarios, Long> {

    @Autowired
    private ServiceUsuario serviceUsuario;

    @GetMapping("/login")
    public String showLoginForm(HttpSession session, Model model) {
        Boolean loginError = (Boolean) session.getAttribute("loginError");
        if (loginError != null && loginError) {
            model.addAttribute("showAlert", true);
            session.removeAttribute("loginError");
        }
        return "usuarios/login";  // Retorna a página de login
    }

    @Override
    @GetMapping("/lista")
    public ModelAndView listarTodos() {
        ModelAndView mv = new ModelAndView("usuarios/lista");
        mv.addObject("usuarios", serviceUsuario.listarTodos());
        return mv;
    }

    @Override
    @GetMapping("/novo")
    public ModelAndView getNovo() {
        ModelAndView mv = new ModelAndView("usuarios/novo");
        mv.addObject("usuarios", new Usuarios());
        return mv;
    }

    // Salvar um novo usuário
    @Override
    @PostMapping("/novo")
    public ModelAndView postNovo(@ModelAttribute("usuarios") Usuarios usuarios, RedirectAttributes redirectAttributes) {
        serviceUsuario.salvar(usuarios);
        redirectAttributes.addFlashAttribute("success", "Usuario cadastrado com sucesso!");
        return new ModelAndView("redirect:/usuarios/lista");
    }

    @Override
    @GetMapping("/editar/{id}")
    public ModelAndView editar(@PathVariable("id") Long id) {
        ModelAndView mv = new ModelAndView("usuarios/editar");
        Usuarios usuario = serviceUsuario.localizar(id);
        if (usuario != null) {
            mv.addObject("usuarios", usuario);
        } else {
            mv.setViewName("redirect:/usuarios/lista"); // Redireciona se o usuário não for encontrado
        }
        return mv;
    }

    @PostMapping("/editar")
    public ModelAndView editar(
            @ModelAttribute("usuario") Usuarios usuario,
            RedirectAttributes redirectAttributes) {

        serviceUsuario.salvar(usuario);
        redirectAttributes.addFlashAttribute("success", "Usuário atualizado com sucesso!");
        return new ModelAndView("redirect:/usuarios/lista");
    }

    // Exibir confirmação de exclusão de um usuário
    @Override
    @GetMapping("/excluir/{id}")
    public ModelAndView excluir(@PathVariable("id") Long id) {
        ModelAndView mv = new ModelAndView("usuarios/excluir");
        Usuarios usuarios = serviceUsuario.localizar(id);
        if (usuarios != null) {
            mv.addObject("usuarios", usuarios);
        } else {
            mv.setViewName("redirect:/usuarios/lista");
        }
        return mv;
    }

    @PostMapping("/excluir")
    @Override
    public ModelAndView remover(@ModelAttribute("usuarios") Usuarios usuarios, RedirectAttributes redirectAttributes) {
        ModelAndView mv;
        try {
            serviceUsuario.excluir(usuarios);
            redirectAttributes.addFlashAttribute("success", "Usuário excluído com sucesso!");
            mv = new ModelAndView("redirect:/usuarios/lista");
        } catch (RuntimeException e) {
            mv = new ModelAndView("usuarios/excluir");
            mv.addObject("usuarios", usuarios);
            mv.addObject("erro", e.getMessage());
        }
        return mv;
    }

    @GetMapping("/esqueci-senha")
    public ModelAndView mostrarEsqueciSenha() {
        return new ModelAndView("usuarios/esqueci-senha");
    }

    @PostMapping("/solicitar-redefinicao")
public ModelAndView solicitarRedefinicao(@RequestParam String email, HttpSession session) {
    ModelAndView mv = new ModelAndView("usuarios/esqueci-senha");

    try {
        serviceUsuario.criarTokenRedefinicao(email);  // <- ponto crítico
        session.setAttribute("emailRecuperacao", email);
        mv.addObject("mensagem", "Instruções enviadas para seu e-mail");
    } catch (RuntimeException e) {
        mv.addObject("erro", e.getMessage());
    }

    return mv;
}


    @GetMapping("/redefinir-senha")
    public ModelAndView mostrarRedefinirSenha(@RequestParam String token) {
        ModelAndView mv = new ModelAndView();
        if (serviceUsuario.validarToken(token)) {
            mv.setViewName("usuarios/redefinir-senha");
            mv.addObject("token", token);
        } else {
            mv.setViewName("redirect:/usuarios/esqueci-senha");
            mv.addObject("erro", "Link inválido ou expirado");
        }
        return mv;
    }

    @PostMapping("/atualizar-senha")
    public ModelAndView atualizarSenha(
            @RequestParam String token,
            @RequestParam String novaSenha,
            @RequestParam String confirmacaoSenha) {

        ModelAndView mv = new ModelAndView();

        if (!novaSenha.equals(confirmacaoSenha)) {
            mv.setViewName("usuarios/redefinir-senha");
            mv.addObject("erro", "As senhas não coincidem");
            mv.addObject("token", token);
            return mv;
        }

        try {
            serviceUsuario.atualizarSenha(token, novaSenha);
            mv.setViewName("redirect:/usuarios/login");
            mv.addObject("mensagem", "Senha alterada com sucesso!");
        } catch (RuntimeException e) {
            mv.setViewName("usuarios/redefinir-senha");
            mv.addObject("erro", e.getMessage());
            mv.addObject("token", token);
        }

        return mv;
    }
}