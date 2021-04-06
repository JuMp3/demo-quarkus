package it.jump3.dao.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import it.jump3.dao.model.Role;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
@Slf4j
public class RoleRepository implements PanacheRepositoryBase<Role, Long> {

    public Optional<Role> findByCode(it.jump3.security.profile.Role code) {
        return find("code", code).firstResultOptional();
    }
}
