package com.auth.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.auth.persistence.entity.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, String> {

}
