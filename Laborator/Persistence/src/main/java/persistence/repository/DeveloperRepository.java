package persistence.repository;

import model.Developer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import persistence.IRepository;

import java.util.List;
import java.util.Properties;

public class DeveloperRepository implements IRepository<String, Developer> {

    private static SessionFactory sessionFactory;
    private JdbcUtils dbUtils;

    public DeveloperRepository(Properties props,SessionFactory sessionFactory) {
        dbUtils = new JdbcUtils(props);
        this.sessionFactory=sessionFactory;
    }

    @Override
    public Developer findOne(String id) {
        List<Developer> developers = null;
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                developers = session.createQuery("FROM Developer WHERE Username = :S", Developer.class).setParameter("S", id).list();
                System.out.println(developers);
                tx.commit();
            } catch (RuntimeException ex) {
                if (tx != null) {
                    tx.rollback();
                    ;
                }
            }
        }
        if(developers.size()>0)
            return developers.get(0);

        return null;


    }

    @Override
    public List<Developer> findAll() {
        return null;
    }

    @Override
    public void save(Developer entity) {

    }

    @Override
    public void delete(String id) {

    }

    @Override
    public void update(Developer entity) {

    }


}
