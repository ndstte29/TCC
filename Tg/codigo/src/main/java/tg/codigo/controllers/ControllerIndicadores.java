package tg.codigo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import tg.codigo.models.Venda;
import tg.codigo.services.ServiceIndicador;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ControllerIndicadores {

    @Autowired
    private ServiceIndicador serviceIndicador;

    @GetMapping("/indicadores")
    public ModelAndView indicadores(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<Venda> vendasFiltradas = serviceIndicador.buscarPorData(startDate, endDate);

        int totalSaidas = vendasFiltradas.stream()
                .filter(v -> "saida".equalsIgnoreCase(v.getVenAcao()))
                .mapToInt(Venda::getVenQuantidade)
                .sum();

        int totalEntradas = vendasFiltradas.stream()
                .filter(v -> "entrada".equalsIgnoreCase(v.getVenAcao()))
                .mapToInt(Venda::getVenQuantidade)
                .sum();

        ModelAndView mv = new ModelAndView("indicadores");
        mv.addObject("venda", vendasFiltradas);
        mv.addObject("totalSaidas", totalSaidas);
        mv.addObject("totalEntradas", totalEntradas);
        mv.addObject("startDate", startDate);
        mv.addObject("endDate", endDate);
        return mv;
    }
}
