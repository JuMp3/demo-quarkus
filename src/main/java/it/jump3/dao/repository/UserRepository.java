package it.jump3.dao.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import it.jump3.controller.model.UserDto;
import it.jump3.controller.model.UserResponse;
import it.jump3.dao.model.User;
import it.jump3.enumz.BusinessError;
import it.jump3.enumz.UserStatusEnum;
import it.jump3.exception.CommonBusinessException;
import it.jump3.util.DateUtil;
import it.jump3.util.Utility;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

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

    public void newUser(UserDto userDto) throws InvocationTargetException, IllegalAccessException {

        User userToCheck = findByUsername(userDto.getUsername());
        if (userToCheck != null) {
            throw new CommonBusinessException(Integer.toString(BusinessError.IB_409_USER.code()),
                    String.format("User with username %s yet exists", userDto.getUsername()), Response.Status.CONFLICT);
        }

        createUser(userDto);
    }

    @Transactional
    public void createUser(UserDto userDto) throws InvocationTargetException, IllegalAccessException {

        User user = new User();
        BeanUtils.copyProperties(user, userDto);
        if (user.getInsertTime() == null) user.setInsertTime(DateUtil.nowLocalDateTimeItaly());
        if (user.getStatus() == null) user.setStatus(UserStatusEnum.D);

        this.persist(user);
    }
}
