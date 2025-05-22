package com.moneyminder.moneyminderusers.persistence.repositories;

import com.moneyminder.moneyminderusers.persistence.entities.GroupEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository  extends CrudRepository<GroupEntity, String>, JpaSpecificationExecutor<GroupEntity> {
    boolean existsById(String id);

    List<GroupEntity> findAllByIdIn(List<String> ids);

    List<GroupEntity> findAllByUsers_Username(String usernames);
}
