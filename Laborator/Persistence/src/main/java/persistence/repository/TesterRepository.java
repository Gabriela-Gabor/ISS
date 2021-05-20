package persistence.repository;

import model.Developer;
import model.Tester;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import persistence.IRepository;

import java.util.List;
import java.util.Properties;

public class TesterRepository implements IRepository<String, Tester> {

    private static SessionFactory sessionFactory;
    private JdbcUtils dbUtils;

    public TesterRepository(Properties props,SessionFactory sessionFactory) {
        dbUtils = new JdbcUtils(props);
        this.sessionFactory=sessionFactory;
    }

    @Override
    public Tester findOne(String id) {
        List<Tester> testers = null;
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                testers = session.createQuery("FROM Tester WHERE Username = :S", Tester.class).setParameter("S", id).list();
                System.out.println(testers);
                tx.commit();
            } catch (RuntimeException ex) {
                if (tx != null) {
                    tx.rollback();
                    ;
                }
            }
        }
        if (testers.size()>0)
            return testers.get(0);

        return null;
    }


    @Override
    public List<Tester> findAll() {
        return null;
    }

    @Override
    public void save(Tester entity) {

    }

    @Override
    public void delete(String id) {

    }

    @Override
    public void update(Tester entity) {

    }


}
