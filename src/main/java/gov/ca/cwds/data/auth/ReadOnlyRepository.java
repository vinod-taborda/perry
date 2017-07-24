package gov.ca.cwds.data.auth;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.io.Serializable;

/**
 * Created by dmitry.rudenko on 7/19/2017.
 */
@NoRepositoryBean
public interface ReadOnlyRepository<T, ID extends Serializable> extends Repository<T, ID> {

  T findOne(ID id);

  Iterable<T> findAll();
}