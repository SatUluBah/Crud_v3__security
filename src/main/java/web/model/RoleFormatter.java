package web.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import web.dao.RoleDao;

import javax.persistence.EntityManager;
import java.text.ParseException;
import java.util.Locale;


public class RoleFormatter implements Formatter<Role> {

    private EntityManager entityManager;
    @Autowired
    public RoleFormatter(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    @Override
    public Role parse(String stringWithId, Locale locale) throws ParseException {
        return entityManager.find(Role.class, Long.parseLong(stringWithId));
    }

    @Override
    public String print(Role role, Locale locale) {

        return String.valueOf(role.getId());
    }
}