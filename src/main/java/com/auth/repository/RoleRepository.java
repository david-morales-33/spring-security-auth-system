package com.auth.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.auth.persistence.entity.RoleEntity;

@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, Integer> {
    List<RoleEntity> findRoleEntitiesByRoleEnumIn(List<String> roleList);
}
