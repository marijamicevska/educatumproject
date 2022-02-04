package project.educatum.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.educatum.model.Admini;
import project.educatum.model.Predmeti;
import project.educatum.service.AdminiService;
import project.educatum.service.NastavniciService;
import project.educatum.service.PredmetiService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final PredmetiService predmetiService;
    private final AdminiService adminiService;
    private final NastavniciService nastavniciService;
    private static String UPLOADED_FOLDER = "C://Users//Acer//Desktop//dok//";

    public HomeController(PredmetiService predmetiService, AdminiService adminiService, NastavniciService nastavniciService) {
        this.predmetiService = predmetiService;
        this.adminiService = adminiService;
        this.nastavniciService = nastavniciService;
    }

    @GetMapping
    public String getHomePage() {
        return "home";
    }

    @GetMapping("/izberiPredmet")
    public String choose(@RequestParam(required = false) String ime, Model model) {
        List<Predmeti> predmeti;
        if (ime == null) {
            predmeti = this.predmetiService.findAll();
        } else {
            predmeti = this.predmetiService.findAllByNameLike(ime);
        }
        model.addAttribute("predmeti", predmeti);
        return "izberiPredmeti.html";
    }

    @GetMapping("/document")
    public String document() {
        return "prikaziDokument.html";
    }

    @PostMapping("/upload")
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Внесете задолжително документ");
            return "redirect:/home/potvrda";
        }

        try {

            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);

            redirectAttributes.addFlashAttribute("message",
                    "Ви благодариме за регистрацијата! Ќе добиете известување на e-mail кога вашиот профил ќе виде активиран.");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/home/potvrda";
    }

    @GetMapping("/potvrda")
    public String potvrdaZaNastavnik() {
        return "potvrdaNastavnik.html";
    }

    @GetMapping("/add")
    public String dodadiPredmet(Model model) {
        List<Admini> adminiList = this.adminiService.listAll();
        model.addAttribute("adminiList", adminiList);
        return "dodadiForma.html";
    }

    @PostMapping("/izberiPredmet")
    public String kreirajPredmet(@RequestParam String ime,
                                 @RequestParam List<Integer> idAdmin) {
        this.predmetiService.create(ime, idAdmin);
        return "redirect:/izberiPredmet";
    }

    @GetMapping("/slusajPredmet")
    public String slusajPredmet(@RequestParam(required = false)String ime, Model model) {
        List<Predmeti> listaPredmeti;
        if (ime == null) {
            listaPredmeti = this.predmetiService.findAll();
        } else {
            listaPredmeti = this.predmetiService.findAllByNameLike(ime);
        }
        model.addAttribute("listaPredmeti", listaPredmeti);
        return "slusajPredmet";
    }

}
