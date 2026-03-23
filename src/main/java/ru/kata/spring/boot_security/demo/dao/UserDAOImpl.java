package ru.kata.spring.boot_security.demo.dao;



import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<User> allUsers() {
        TypedQuery<User> query = entityManager.createQuery("select u from User u", User.class);
        return query.getResultList();
    }

    @Override
    public void add(User user) {
        entityManager.persist(user);
    }

    @Override
    public void delete(User user) {
        User managedUser = entityManager.find(User.class, user.getId());
        if (managedUser != null) {
            entityManager.remove(managedUser);
        }
    }

    @Override
    public void edit(User user) {
        entityManager.merge(user);
    }

    @Override
    public User getById(int id) {
        return entityManager.find(User.class,id);
    }

    @Override
    public User findByUsername(String username) {
        TypedQuery<User> query = entityManager.createQuery("select u from User u left join fetch u.roles " +
                "where u.username = :username", User.class);
        query.setParameter("username", username);
        List<User> users = query.getResultList();
        if(users.isEmpty()) {
            return null;
        } else {
           return users.get(0);
        }
    }
}
