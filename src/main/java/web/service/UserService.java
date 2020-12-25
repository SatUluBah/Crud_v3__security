package web.service;

import web.model.User;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface UserService {
    void add(User user);
    List<User> listUsers();
    User getUser(long id);
    void deleteUser(User user);
    void updateUser(User user);
    User getUserByName(String name);

}