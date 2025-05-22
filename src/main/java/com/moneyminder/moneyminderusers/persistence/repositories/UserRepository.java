package com.moneyminder.moneyminderusers.persistence.repositories;

import com.moneyminder.moneyminderusers.persistence.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository  extends CrudRepository<UserEntity, String>, JpaSpecificationExecutor<UserEntity> {
    boolean existsByUsername(String username);

    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByUsernameOrEmail(String username, String email);
    List<UserEntity> findAllByUsernameInOrEmailIn(List<String> usernames, List<String> emails);

    void deleteByUsername(String username);
}
