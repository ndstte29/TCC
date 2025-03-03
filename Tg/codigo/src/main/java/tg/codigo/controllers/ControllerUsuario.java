package tg.codigo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import tg.codigo.interfaces.Icontrolador;
import tg.codigo.models.Usuarios;
import tg.codigo.services.ServiceUsuario;

@Controller
@RequestMapping("/usuarios")
public class ControllerUsuario implements Icontrolador<Usuarios, Long> {

    @Autowired
    private ServiceUsuario serviceUsuario;

    @Override
    @GetMapping("/lista")
    public ModelAndView listarTodos() {
        ModelAndView mv = new ModelAndView("usuarios/lista");
        mv.addObject("usuarios", serviceUsuario.listarTodos());
        return mv;
    }

    // Exibir formulário para criar um novo usuário
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
    public ModelAndView postNovo(@ModelAttribute("usuarios") Usuarios usuarios) {
        serviceUsuario.salvar(usuarios);
        return new ModelAndView("redirect:/usuarios/lista");
    }

    // Exibir formulário para editar um usuário
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

    // Atualizar um usuário existente
    @PostMapping("/editar")
    public ModelAndView editar(@ModelAttribute("usuario") Usuarios usuario) {
        serviceUsuario.salvar(usuario);
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
            mv.setViewName("redirect:/usuarios/lista"); // Redireciona se o usuário não for encontrado
        }
        return mv;
    }

    // Confirmar exclusão de um usuário
    @PostMapping("/excluir")
    @Override
    public ModelAndView remover(@ModelAttribute("usuarios") Usuarios usuarios) {
        ModelAndView mv;
        try {
            serviceUsuario.excluir(usuarios);
            mv = new ModelAndView("redirect:/usuarios/lista");
        } catch (RuntimeException e) {
            mv = new ModelAndView("usuarios/excluir");
            mv.addObject("usuarios", usuarios);
            mv.addObject("erro", e.getMessage());
        }
        return mv;
    }
}