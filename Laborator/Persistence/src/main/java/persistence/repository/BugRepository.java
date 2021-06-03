package persistence.repository;

import model.Bug;

import model.StatusType;
import observer.Observable;
import observer.Observer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import persistence.IRepository;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class BugRepository implements IRepository<String, Bug>, Observable {

    private static SessionFactory sessionFactory;
    private JdbcUtils dbUtils;


    public BugRepository(Properties props, SessionFactory sessionFactory) {
        dbUtils = new JdbcUtils(props);
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Bug findOne(String id) {
        List<Bug> bugs = null;
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                bugs = session.createQuery("FROM Bug WHERE Name= :S", Bug.class).setParameter("S", id).list();
                System.out.println(bugs);
                tx.commit();
            } catch (RuntimeException ex) {
                if (tx != null) {
                    tx.rollback();

                }
            }
        }
        if (bugs.size() > 0)
            return bugs.get(0);

        return null;

    }

    @Override
    public List<Bug> findAll() {
        List<Bug> bugs = null;
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                bugs = session.createQuery("FROM Bug", Bug.class).list();
                for (Bug b : bugs) {
                    System.out.println("Bug (" + b.getName() + ") : " + b.getDescription());
                }
                tx.commit();
            } catch (RuntimeException ex) {
                if (tx != null) {
                    tx.rollback();
                    ;
                }
            }
        }
        return bugs;
    }


    @Override
    public void save(Bug entity) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.save(entity);
                session.flush();
                tx.commit();
                notifyObservers();
            } catch (RuntimeException ex) {
                if (tx != null) {
                    tx.rollback();

                }
            }
        }

    }


    @Override
    public void delete(String id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();

                Bug bug = session.createQuery("FROM Bug WHERE Name= :S", Bug.class).setParameter("S", id)
                        .setMaxResults(1)
                        .uniqueResult();
                System.err.println("Delete ..." + bug.getName());
                session.delete(bug);
                tx.commit();
                notifyObservers();
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }

    }

    @Override
    public void update(Bug entity) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.update(entity);
                tx.commit();
                notifyObservers();
            } catch (RuntimeException ex) {
                if (tx != null) {
                    tx.rollback();

                }
            }
        }
    }

    public List<Bug> getFinishedBugs()
    {
        List<Bug> bugs=findAll().stream().filter(x->x.getStatusType()== StatusType.Finished).collect(Collectors.toList());
        return bugs;
    }

    public List<Bug> getUnfinishedBugs()
    {
        List<Bug> bugs=findAll().stream().filter(x->x.getStatusType()!= StatusType.Finished).collect(Collectors.toList());
        return bugs;
    }


    @Override
    public void addObserver(Observer e) {
        observers.add(e);

    }

    @Override
    public void removeObserver(Observer e) {

    }

    @Override
    public void notifyObservers() {
        observers.stream().forEach(x -> x.update());

    }
}
