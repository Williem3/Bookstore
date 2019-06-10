package com.bookstore.dao;

import com.bookstore.entity.security.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleDAO  extends CrudRepository<Role, Long> {

    Role findByName(String name);
}
