package fr.insa.PPC_TP.controllers;

import fr.insa.PPC_TP.classes.Person;
import fr.insa.PPC_TP.interfaces.PersonRepository;
import fr.insa.PPC_TP.services.AnnuaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.Optional;

@Controller
public class AnnuaireController
{
    @Autowired
    AnnuaireService annuaireService;

    @Autowired
    PersonRepository personRepository;

    /*----- RECHERCHE -----*/

    @GetMapping("/annuaire/recherche")
    public String recherche(Model model, @RequestParam (name = "name", required = false, defaultValue = "*") String name)
    {
        if(name.equals("*") )
        {
            model.addAttribute("entries", annuaireService.getAll() );
        }
        else
        {
            model.addAttribute("entries", annuaireService.getFromName(name) );
        }
        return "annuaire";
    }

    /*----- AJOUT -----*/

    @GetMapping("/ajouter")
    public String ajouter(Model model)
    {
        return "ajouter";
    }

    /*@PostMapping("/annuaire/ajouter")
    public String ajouter(Model model,
                          @RequestParam(name = "name") String name,
                          @RequestParam(name = "surname") String surname,
                          @RequestParam(name = "phone") String phone,
                          @RequestParam(name = "city") String city)
    {
        annuaireService.addPerson(new Person(annuaireService.getAll().size() , name, surname, phone, city) );
        model.addAttribute("entries", annuaireService.getAll() );
        return "annuaire";
    }*/

    @PostMapping("/annuaire/ajouter")
    public String ajouter(Model model,
                          @RequestParam(name = "name") String name,
                          @RequestParam(name = "surname") String surname,
                          @RequestParam(name = "phone") String phone,
                          @RequestParam(name = "city") String city)
    {
        this.personRepository.save(new Person(name, surname, phone, city) );
        return "redirect:/annuaire/recherche";
    }

    /*----- SUPPRESSION -----*/

    @GetMapping("/annuaire/supprimer/{id}")
    public String supprimer(Model model, @PathVariable Long id)
    {
        annuaireService.deleteFromId(id);
        model.addAttribute("entries", annuaireService.getAll() );
        return "redirect:/annuaire/recherche";
    }

    /*----- MODIFICATION -----*/

    @GetMapping("/modifier/{id}")
    public String modifier(Model model, @PathVariable Long id)
    {
        model.addAttribute("entry", annuaireService.getFromId(id) );
        return "modifier";
    }

    /*@PostMapping("/annuaire/modifier")
    public String modifier(Model model,
                           @RequestParam(name = "id") Long id,
                           @RequestParam(name = "name") String name,
                           @RequestParam(name = "surname") String surname,
                           @RequestParam(name = "phone") String phone,
                           @RequestParam(name = "city") String city)
    {
        annuaireService.addPerson(new Person(id, name, surname, phone, city));
        model.addAttribute("entries", annuaireService.getAll() );
        return "redirect:/annuaire/recherche";
    }*/

    @PostMapping("/annuaire/modifier")
    public String modifier(Model model,
                           @RequestParam(name = "id") Long id,
                           @RequestParam(name = "name") String name,
                           @RequestParam(name = "surname") String surname,
                           @RequestParam(name = "phone") String phone,
                           @RequestParam(name = "city") String city)
    {
        Optional<Person> person = this.annuaireService.getFromId(id);
        if(person.isPresent() )
        {
            person.get().setName(name);
            person.get().setSurname(surname);
            person.get().setPhone(phone);
            person.get().setCity(city);
            personRepository.save(person.get() );
        }
        return "redirect:/annuaire/recherche";
    }
}
