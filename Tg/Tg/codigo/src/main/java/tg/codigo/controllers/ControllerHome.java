package tg.codigo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControllerHome {

    @GetMapping("/")
    public String redirecionarParaLogin() {
        return "redirect:/usuarios/login";
    }

    @GetMapping("/index")
    public String paginaHome() {
        return "redirect:/index";
    }
}
