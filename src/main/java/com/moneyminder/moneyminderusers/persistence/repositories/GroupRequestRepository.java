package com.moneyminder.moneyminderusers.persistence.repositories;

import com.moneyminder.moneyminderusers.persistence.entities.GroupRequestEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRequestRepository  extends CrudRepository<GroupRequestEntity, String>, JpaSpecificationExecutor<GroupRequestEntity> {
    boolean existsById(String id);

    List<GroupRequestEntity> findAllByIdIn(List<String> ids);
    List<GroupRequestEntity> findAllByRequestedUser_Username(String username);
    List<GroupRequestEntity> findAllByRequestingUser_Username(String username);
}
