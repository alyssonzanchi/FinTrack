package br.edu.ifrs.fintrack.dao;

import br.edu.ifrs.fintrack.exception.DataAccessException;
import br.edu.ifrs.fintrack.exception.DatabaseConnectionException;
import br.edu.ifrs.fintrack.exception.DatabaseIntegrityException;
import br.edu.ifrs.fintrack.exception.EntityNotFoundException;

import java.util.List;

public interface DAO<T> {
    boolean insert(T obj) throws DatabaseConnectionException, DataAccessException;
    boolean delete(int id) throws EntityNotFoundException, DatabaseIntegrityException;
    boolean update(T obj) throws EntityNotFoundException, DataAccessException;
    List<T> list(int limit, int offset) throws DataAccessException;
    T get(int id) throws EntityNotFoundException, DataAccessException;
}
