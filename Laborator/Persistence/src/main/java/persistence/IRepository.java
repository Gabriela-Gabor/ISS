package persistence;

import model.Entity;

import java.util.List;

public interface IRepository<ID, E extends Entity<ID>> {

    public E findOne(ID id);

    public List<E> findAll();

    public void save(E entity);

    public void delete(ID id);

    public void update(E entity);

}
