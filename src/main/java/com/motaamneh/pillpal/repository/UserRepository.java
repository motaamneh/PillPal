package com.motaamneh.pillpal.repository;

import com.motaamneh.pillpal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {


}
