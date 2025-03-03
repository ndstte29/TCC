package tg.codigo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class ControllerIndicadores {
    @GetMapping("/indicadores")
    public String indicadores() {
        return "indicadores"; // Nome do arquivo HTML sem a extensão
    }
}
