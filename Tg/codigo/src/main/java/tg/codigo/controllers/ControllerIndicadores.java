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
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class ControllerIndicadores {

    @Autowired
    private ServiceIndicador serviceIndicador;
    
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @GetMapping("/indicadores")
    public ModelAndView indicadores(
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate endDate) {

        // Definir intervalo padrão (últimos 30 dias)
        if (endDate == null) {
            endDate = LocalDate.now();
        }
        if (startDate == null) {
            startDate = endDate.minusDays(5);
        }

        List<Venda> vendasFiltradas = serviceIndicador.buscarPorData(startDate, endDate);

        int totalSaidas = vendasFiltradas.stream()
                .filter(v -> "saida".equalsIgnoreCase(v.getVenAcao()))
                .mapToInt(Venda::getVenQuantidade)
                .sum();

        int totalEntrada = vendasFiltradas.stream()
                .filter(v -> "entrada".equalsIgnoreCase(v.getVenAcao()))
                .mapToInt(Venda::getVenQuantidade)
                .sum();

        ModelAndView mv = new ModelAndView("indicadores");
        mv.addObject("venda", vendasFiltradas);
        mv.addObject("totalSaidas", totalSaidas);
        mv.addObject("totalEntrada", totalEntrada);
        mv.addObject("startDate", startDate.format(dateFormatter));
        mv.addObject("endDate", endDate.format(dateFormatter));
        mv.addObject("startDateRaw", startDate);
        mv.addObject("endDateRaw", endDate);
        
        return mv;
    }
}