package com.tesis.urbe.user.repository;

import com.tesis.urbe.user.entity.UserEntity;
import org.springframework.data.repository.ListCrudRepository;

public interface UserRepository extends ListCrudRepository<UserEntity, Integer> {
}
