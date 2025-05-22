package com.moneyminder.moneyminderusers.persistence.repositories;

import com.moneyminder.moneyminderusers.persistence.entities.SessionEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository  extends CrudRepository<SessionEntity, String>, JpaSpecificationExecutor<SessionEntity> {
    boolean existsById(String id);

    List<SessionEntity> findAllByIdIn(List<String> ids);
}
