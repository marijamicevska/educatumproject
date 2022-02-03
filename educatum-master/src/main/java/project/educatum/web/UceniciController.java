package project.educatum.web;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import project.educatum.model.Nastavnici;
import project.educatum.model.Predmeti;
import project.educatum.model.Ucenici;
import project.educatum.service.PredmetiService;
import project.educatum.service.UceniciService;
import project.educatum.service.ZainteresiraniZaService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping(path = "/ucenici", method = {RequestMethod.POST, RequestMethod.DELETE, RequestMethod.GET})
public class UceniciController {

    private final UceniciService uceniciService;
    private final PredmetiService predmetiService;
    private final ZainteresiraniZaService zainteresiraniZaService;

    public UceniciController(UceniciService uceniciService, PredmetiService predmetiService, ZainteresiraniZaService zainteresiraniZaService) {
        this.uceniciService = uceniciService;
        this.predmetiService = predmetiService;
        this.zainteresiraniZaService = zainteresiraniZaService;
    }

    @PostMapping("/zaintesesiranZaPredmet")
    public String zaintesesiranZaPredmet(@RequestParam String predmetId,
                                         HttpServletRequest request,
                                         @DateTimeFormat(iso= DateTimeFormat.ISO.DATE)LocalDate date) {
        UserDetails user = (UserDetails) request.getSession().getAttribute("user");
        String username = user.getUsername();
        Ucenici u = uceniciService.findByEmail(username);
        zainteresiraniZaService.addSubjectStudent(Integer.valueOf(predmetId),u.getId(),date );
        return "listaNastavnicPredmet.html";
    }
}
