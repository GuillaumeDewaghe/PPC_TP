package fr.insa.PPC_TP.interfaces;

import fr.insa.PPC_TP.classes.Person;
import org.springframework.data.repository.CrudRepository;

/**
 * Va permettre de se connecter à la base de données
 */
public interface PersonRepository extends CrudRepository<Person, Long>
{

}
