package fr.insa.PPC_TP.controllers;

import fr.insa.PPC_TP.classes.Person;
import fr.insa.PPC_TP.interfaces.PersonRepository;
import fr.insa.PPC_TP.services.AnnuaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

//@Controller
@RestController
public class AnnuaireController
{
    @Autowired
    AnnuaireService annuaireService;

    @Autowired
    PersonRepository personRepository;

    /*--------------------------------------------------------------------------- MODEL ---------------------------------------------------------------------------*/

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

    /*--------------------------------------------------------------------------- REST ---------------------------------------------------------------------------*/

    @GetMapping("/entree")
    public ResponseEntity<Collection<Person> > recherche()
    {
        return ResponseEntity.status(HttpStatus.OK).body(annuaireService.getAll() );
    }

    @GetMapping("/entree/{id}")
    public ResponseEntity<Person> rechercheParId(@PathVariable Long id)
    {
        var personneID = annuaireService.getFromId(id);
        if(personneID.isPresent() )
        {
            return ResponseEntity.status(HttpStatus.OK).body(personneID.get() );
        }
        else
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/entree")
    public ResponseEntity<Collection<Person> > ajouter(@RequestParam(name = "name") String name,
                                                      @RequestParam(name = "surname") String surname,
                                                      @RequestParam(name = "phone") String phone,
                                                      @RequestParam(name = "city") String city)
    {
        var personnes = annuaireService;
        if(personnes.getFromName(name).isEmpty() )
        {
            this.personRepository.save(new Person(name, surname, phone, city) );
            return ResponseEntity.status(HttpStatus.CREATED).body(personnes.getAll() );
        }
        else
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @DeleteMapping("/entree/{id}")
    public ResponseEntity<Collection<Person> > supprimer(@PathVariable Long id)
    {
        var personnes = annuaireService;
        if(personnes.getFromId(id).isPresent() )
        {
            personnes.deleteFromId(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        else
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/entree")
    public ResponseEntity<Person> modifier(@RequestParam(name = "id") Long id,
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
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        else
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /*--------------------------------------------------------------------------- KAFKA ---------------------------------------------------------------------------*/

    /**
     * Cette méthode est appelée à chaque fois qu'un nouveau message est ajouté dans la file
     * On estime que les attributs sont séparés par ";"
     * @param s File provenant de Kafka
     */
    @KafkaListener(id = "myId", topics = "quickstart-events")
    public void ajoutPersonneKafka(String s)
    {
        String[] sTemp = s.split(";");
        Person p = new Person(sTemp[0], sTemp[1], sTemp[2], sTemp[3]);
        annuaireService.addPerson(p);
    }
}