package ru.alishev.springcourse.dao;

import org.springframework.stereotype.Component;
import ru.alishev.springcourse.models.Person;

import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDao {
    private static int PEOPLE_COUNT;
    List<Person> people;

    {
        people = new ArrayList<>();
        people.add(new Person(++PEOPLE_COUNT, "Bob", "bob@gmail.com", 23));
        people.add(new Person(++PEOPLE_COUNT, "Tom", "tom@yahoo.kz", 34));
        people.add(new Person(++PEOPLE_COUNT, "Jack", "jack@mail.ru", 26));
        people.add(new Person(++PEOPLE_COUNT, "Andrew", "andr@com.ru", 19));

    }

    public List<Person> index(){
        return people;
    }

    public Person show(int id) {
        return people.stream().filter(person -> person.getId() == id).findAny().orElse(null);
    }

    public void save(Person person){
        person.setId(++PEOPLE_COUNT);
        people.add(person);
    }

    public void update(int id, Person updatedPerson){
        Person personToBeUpdated = show(id);
        personToBeUpdated.setName(updatedPerson.getName());
        personToBeUpdated.setEmail(updatedPerson.getEmail());
        personToBeUpdated.setAge(updatedPerson.getAge());
    }

    public void delete(int id){
        people.removeIf(p -> p.getId() == id);
    }
}
