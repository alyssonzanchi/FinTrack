package br.edu.ifrs.fintrack.dao;

import br.edu.ifrs.fintrack.exception.DataAccessException;
import br.edu.ifrs.fintrack.exception.EntityNotFoundException;

import java.util.List;

public interface DAO<T> {
    boolean insert(T obj);
    boolean delete(int id);
    boolean update(T obj);
    List<T> list(int limit, int offset) throws DataAccessException;
    T get(int id) throws EntityNotFoundException, DataAccessException;
}
