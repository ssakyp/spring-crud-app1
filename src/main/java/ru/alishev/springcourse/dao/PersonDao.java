package ru.alishev.springcourse.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.alishev.springcourse.models.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //private static int PEOPLE_COUNT;
    //List<Person> people;

//    {
//        people = new ArrayList<>();
//        people.add(new Person(++PEOPLE_COUNT, "Bob", "bob@gmail.com", 23));
//        people.add(new Person(++PEOPLE_COUNT, "Tom", "tom@yahoo.kz", 34));
//        people.add(new Person(++PEOPLE_COUNT, "Jack", "jack@mail.ru", 26));
//        people.add(new Person(++PEOPLE_COUNT, "Andrew", "andr@com.ru", 19));
//
//    }

//    private static final String URL = "jdbc:postgresql://localhost:5432/first_db";
//    private static final String USERNAME = "postgres";
//    private static final String PASSWORD = "postgres";
//
//    private static Connection connection;
//
//    static {
//        try {
//            Class.forName("org.postgresql.Driver");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
//        } catch (SQLException throwables) {
//            throw new RuntimeException(throwables);
//        }
//    }




    public List<Person> index(){
        //return people;
//        List<Person> people = new ArrayList<>();
//
//        try {
//            Statement statement = connection.createStatement();
//            String SQL = "SELECT * FROM Person";
//            ResultSet resultSet =  statement.executeQuery(SQL);
//
//            while(resultSet.next()){
//                Person person = new Person();
//
//                person.setId(resultSet.getInt("id"));
//                person.setName(resultSet.getString("name"));
//                person.setAge(resultSet.getInt("age"));
//                person.setEmail(resultSet.getString("email"));
//
//                people.add(person);
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
        return jdbcTemplate.query("SELECT * FROM Person", new BeanPropertyRowMapper<>(Person.class));
    }

    public Person show(int id) {
        //return people.stream().filter(person -> person.getId() == id).findAny().orElse(null);
//        Person person = null;
//        try {
//            PreparedStatement preparedStatement =
//                    connection.prepareStatement("SELECT * FROM Person WHERE id=?");
//            preparedStatement.setInt(1, id);
//            ResultSet resultSet = preparedStatement.executeQuery();
//            resultSet.next();
//
//            person = new Person();
//            person.setId(resultSet.getInt("id"));
//            person.setName(resultSet.getString("name"));
//            person.setAge(resultSet.getInt("age"));
//            person.setEmail(resultSet.getString("email"));
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }

        return jdbcTemplate.query("SELECT * FROM Person WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny().orElse(null);
    }

    public void save(Person person){
//        person.setId(++PEOPLE_COUNT);
//        people.add(person);

//        try {
//            PreparedStatement preparedStatement =
//                    connection.prepareStatement("INSERT INTO Person VALUES(1, ?, ?, ?)");
//            preparedStatement.setString(1, person.getName());
//            preparedStatement.setInt(2, person.getAge());
//            preparedStatement.setString(3, person.getEmail());
////            Statement statement = connection.createStatement();
////            String SQL = "INSERT INTO Person VALUES(" + 1 + ",'" + person.getName() +
////                    "'," + person.getAge() + ",'" + person.getEmail() + "')";
//            // INSERT INTO Person VALUES(1, 'Tom', 12, 'vadfs@meail.gr')
//            //statement.executeUpdate(SQL);
//            preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
        jdbcTemplate.update("INSERT INTO Person VALUES(1, ?, ?, ?)", person.getName(), person.getAge(), person.getEmail());
    }

    public void update(int id, Person updatedPerson){
//        try {
//            PreparedStatement preparedStatement =
//                    connection.prepareStatement("UPDATE Person SET name=?, age=?, email=? WHERE id=?");
//            preparedStatement.setString(1, updatedPerson.getName());
//            preparedStatement.setInt(2, updatedPerson.getAge());
//            preparedStatement.setString(3, updatedPerson.getEmail());
//            preparedStatement.setInt(4, id);
//
//            preparedStatement.executeUpdate();
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }


//        Person personToBeUpdated = show(id);
//        personToBeUpdated.setName(updatedPerson.getName());
//        personToBeUpdated.setEmail(updatedPerson.getEmail());
//        personToBeUpdated.setAge(updatedPerson.getAge());

        jdbcTemplate.update("UPDATE Person SET name=?, age=?, email=? WHERE id=?", updatedPerson.getName(),
                updatedPerson.getAge(), updatedPerson.getEmail(), id);
    }

    public void delete(int id){
//        people.removeIf(p -> p.getId() == id);
//        try {
//            PreparedStatement preparedStatement =
//                    connection.prepareStatement("DELETE FROM Person WHERE id=?");
//            preparedStatement.setInt(1, id);
//
//            preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
        jdbcTemplate.update("DELETE FROM Person WHERE id=?", id);
    }


    ///////////////////////////////////////////
    //////Testing efficiency of batch update
    ///////////////////////////////////////////

public void testBatchUpdate(){
        List<Person> people = create1000People();

        long before = System.currentTimeMillis();

        jdbcTemplate.batchUpdate("INSERT INTO Person VALUES(?, ?, ?, ?)",
                new BatchPreparedStatementSetter(){

                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, people.get(i).getId());
                        ps.setString(2, people.get(i).getName());
                        ps.setInt(3, people.get(i).getAge());
                        ps.setString(4, people.get(i).getEmail());

                    }

                    @Override
                    public int getBatchSize() {
                        return people.size();
                    }
                });

        long after = System.currentTimeMillis();
    System.out.println("Batch update Time: "+(after-before));
}


    public void testMultipleUpdate(){
        List<Person> people = create1000People();

        long before = System.currentTimeMillis();

        for(Person person : people){
            jdbcTemplate.update("INSERT INTO Person VALUES(?, ?, ?, ?)", person.getId(),
                    person.getName(), person.getAge(), person.getEmail());
        }

        long after = System.currentTimeMillis();
        System.out.println("Ordinary update Time: " + (after-before));
    }
    private List<Person> create1000People(){
        List<Person> people = new ArrayList<>();

        for(int i = 0 ; i < 1000; i++){
            people.add(new Person(i, "Name" + i, "test" + i + "@mail.ru",30));
        }

        return people;
    }
}

