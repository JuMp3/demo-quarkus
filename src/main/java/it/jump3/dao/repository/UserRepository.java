package it.jump3.dao.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import it.jump3.controller.model.UserDto;
import it.jump3.controller.model.UserResponse;
import it.jump3.dao.model.User;
import it.jump3.enumz.BusinessError;
import it.jump3.exception.CommonBusinessException;
import it.jump3.util.Utility;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.eclipse.microprofile.faulttolerance.Fallback;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@ApplicationScoped
@Slf4j
public class UserRepository implements PanacheRepositoryBase<User, String> {

    public Optional<User> findByName(String name) {
        return find("name", name).firstResultOptional();
    }

    @Transactional
    public void deleteUser(String username) {
        boolean deleted = this.deleteById(username);
        if (!deleted) {
            throw new CommonBusinessException(Integer.toString(BusinessError.IB_404_USER.code()),
                    String.format("User with username %s not found", username), Response.Status.NOT_FOUND);
        }
    }

    @Transactional
    public void deleteUser(User user) {
        if (this.isPersistent(user)) {
            // delete it
            this.delete(user);
        } else {
            throw new CommonBusinessException(Integer.toString(BusinessError.IB_404_USER.code()),
                    String.format("User with username %s not found", user.getUsername()), Response.Status.NOT_FOUND);
        }
    }

    public User findByUsername(String username) {
        return this.findById(username);
    }

    @Fallback(fallbackMethod = "findUsersFallback")
    public UserResponse findUsers(Page page, Sort sort) {

        Set<UserDto> users = new TreeSet<>();

        Long count = this.count();
        List<User> dbUsers = this.findAll(sort == null ? Sort.by("username") : sort).page(page).list();
        if (!CollectionUtils.isEmpty(dbUsers)) {
            dbUsers.forEach(user -> {
                UserDto userDto = new UserDto();
                try {
                    BeanUtils.copyProperties(userDto, user);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    log.error(e.getMessage());
                }
                users.add(userDto);
            });
        }

        UserResponse userResponse = new UserResponse();
        userResponse.setUsers(users);
        Utility.setPaginatedResponse(userResponse, count, page.size);

        return userResponse;
    }

    public UserResponse findUsersFallback(Page page, Sort sort) {

        log.warn("findUsersFallback call...");

        UserResponse userResponse = new UserResponse();
        userResponse.setUsers(new HashSet<>());
        Utility.setPaginatedResponse(userResponse, 0L, page.size);

        return userResponse;
    }

    @Transactional
    public void save(User user) {
        this.persist(user);
    }
}
