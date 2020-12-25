package web.dao;


import web.model.User;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao {

  @PersistenceContext
  private EntityManager em;

    @Override
    public void add(User user) {
        em.persist(user);
    }

    @Override
    public List<User> listUsers() {

        Query query = em.createQuery("SELECT user FROM User user ");
        List<User> users = (List<User>) query.getResultList();
        return users;
    }

    @Override
    public User getUser(long id) {
        User user = em.find(User.class, id);
        return user;
    }

    @Override
    public void deleteUser(User user) {
        em.remove(user);
    }

    @Override
    public void updateUser(User user) {
        User u = em.find(User.class, user.getId());
        em.detach(u);
        u.setUsername(user.getUsername());
        u.setPassword(user.getPassword());
        u.setRoles(user.getRoles());
        em.merge(u);
    }

    @Override
    public User getUserByName(String name) {

        Query query = em.createQuery("SELECT user FROM User user where user.username = :n")
                .setParameter("n", name);
        User user = (User) query.getSingleResult();
        return user;
    }


}

