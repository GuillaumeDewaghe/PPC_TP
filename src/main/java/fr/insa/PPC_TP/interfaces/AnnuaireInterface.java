package fr.insa.PPC_TP.interfaces;

import fr.insa.PPC_TP.classes.Person;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface AnnuaireInterface
{
    public Collection<Person> getAll();
    public Optional<Person> getFromId(Long id);
    public List<Person> getFromName(String name);
    public boolean deleteFromId(Long id);
    public void addPerson(Person p);
}
