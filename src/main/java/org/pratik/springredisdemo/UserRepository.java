package org.pratik.springredisdemo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserScore,Long> {
    Optional<UserScore> findByUsername(String username);
}
