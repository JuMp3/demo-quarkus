package it.jump3.service;

import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import it.jump3.controller.model.UserDto;
import it.jump3.controller.model.UserFe;
import it.jump3.controller.model.UserResponse;
import it.jump3.dao.model.User;
import it.jump3.dao.repository.RoleRepository;
import it.jump3.dao.repository.UserRepository;
import it.jump3.enumz.BusinessError;
import it.jump3.enumz.UserStatusEnum;
import it.jump3.exception.CommonBusinessException;
import it.jump3.security.profile.Role;
import it.jump3.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Optional;

@ApplicationScoped
@Slf4j
public class UserService {

    @Inject
    UserRepository userRepository;

    @Inject
    RoleRepository roleRepository;

    @Inject
    BCryptHashProvider bCryptHashProvider;


    public UserFe getUser(String username) throws InvocationTargetException, IllegalAccessException {

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new CommonBusinessException(Integer.toString(BusinessError.IB_404_USER.code()),
                    String.format("User with username %s not found", username), Response.Status.NOT_FOUND);
        }

        UserFe userFe = new UserFe();
        BeanUtils.copyProperties(userFe, user);
        userFe.setRoles(new HashSet<>());
        user.getRoles().forEach(role -> userFe.getRoles().add(role.getCode().name()));

        return userFe;
    }

    public User newUser(UserDto userDto) throws InvocationTargetException, IllegalAccessException {

        User userToCheck = userRepository.findByUsername(userDto.getUsername());
        if (userToCheck != null) {
            throw new CommonBusinessException(Integer.toString(BusinessError.IB_409_USER.code()),
                    String.format("User with username %s yet exists", userDto.getUsername()), Response.Status.CONFLICT);
        }

        return createUser(userDto);
    }

    @Transactional
    public User createUser(UserDto userDto) throws InvocationTargetException, IllegalAccessException {

        User user = new User();
        BeanUtils.copyProperties(user, userDto);
        user.setPassword(bCryptHashProvider.hashPassword(userDto.getPassword()));
        if (user.getInsertTime() == null) user.setInsertTime(DateUtil.nowLocalDateTimeItaly());
        if (user.getStatus() == null) user.setStatus(UserStatusEnum.D);
        user.setRoles(new HashSet<>());

        for (String role : userDto.getRoles()) {
            Optional<it.jump3.dao.model.Role> dbRoleOpt = roleRepository.findByCode(Role.valueOf(role));
            if (dbRoleOpt.isEmpty()) {
                throw new CommonBusinessException(Integer.toString(BusinessError.IB_404_ROLE.code()),
                        String.format("Role %s not found", role), Response.Status.NOT_FOUND);
            }
            user.getRoles().add(dbRoleOpt.get());
        }

        userRepository.save(user);

        return user;
    }

    public UserResponse findUsers(Page page, Sort sort) {
        return userRepository.findUsers(page, sort);
    }

    public void deleteUser(String username) {
        userRepository.deleteUser(username);
    }
}
