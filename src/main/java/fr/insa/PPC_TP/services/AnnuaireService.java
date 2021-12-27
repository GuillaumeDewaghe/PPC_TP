package fr.insa.PPC_TP.services;

import fr.insa.PPC_TP.classes.Person;
import fr.insa.PPC_TP.interfaces.AnnuaireInterface;
import fr.insa.PPC_TP.interfaces.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AnnuaireService implements AnnuaireInterface
{
    /*Map<Integer, Person> hm;

    public AnnuaireService()
    {
        hm = new HashMap<Integer,Person>();
        hm.put(0, new Person(0,"Guillaume", "Dewaghe","0606060606", "Marchiennes"));
        hm.put(1, new Person(1,"Steven", "Helin","0707070707", "Anzin"));
        hm.put(2, new Person(2,"Aymeric", "LeFeyer","0808080808", "Lille"));
    }

    @Override
    public Collection<Person> getAll()
    {
        return hm.values();
    }

    @Override
    public Person getFromId(int id)
    {
        return hm.get(id);
    }

    @Override
    public List<Person> getFromName(String name)
    {
        List<Person> listPersons = new ArrayList<>();
        for (Person p: hm.values() )
        {
            if(p.getName().equals(name))
            {
                listPersons.add(p);
            }
        }
        return listPersons;
    }

    @Override
    public boolean deleteFromId(int id)
    {
        hm.remove(id);
        System.out.println(hm);
        return true;
    }

    @Override
    public void addPerson(Person p)
    {
        hm.put( (int) p.getId(), p);
    }*/
    @Autowired
    PersonRepository personRepository;

    public AnnuaireService()
    {

    }

    @Override
    public Collection<Person> getAll()
    {
        return (Collection<Person>) personRepository.findAll();
    }

    @Override
    public Optional<Person> getFromId(Long id)
    {
        return personRepository.findById(id);
    }

    @Override
    public List<Person> getFromName(String name)
    {
        List<Person> listPersons = new ArrayList<>();
        for (Person p: personRepository.findAll() )
        {
            if(p.getName().equals(name))
            {
                listPersons.add(p);
            }
        }
        return listPersons;
    }

    @Override
    public boolean deleteFromId(Long id)
    {
        personRepository.deleteById(id);
        System.out.println(personRepository);
        return true;
    }

    @Override
    public void addPerson(Person p)
    {
        personRepository.save(p);
    }
}
