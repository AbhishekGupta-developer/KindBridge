package com.myorganisation.CareEmoPilot.repository;

import com.myorganisation.CareEmoPilot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
