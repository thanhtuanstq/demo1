package vinh.pro.book.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vinh.pro.book.entity.Role;
import vinh.pro.book.enums.RoleNameType;

import java.util.Optional;


@Repository
public interface RoleRepository
        extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleNameType roleName);
}
