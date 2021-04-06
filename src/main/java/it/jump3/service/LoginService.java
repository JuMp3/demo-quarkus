package it.jump3.service;

import it.jump3.controller.model.LoginRequest;
import it.jump3.dao.model.Role;
import it.jump3.dao.model.User;
import it.jump3.dao.repository.UserRepository;
import it.jump3.enumz.BusinessError;
import it.jump3.enumz.UserStatusEnum;
import it.jump3.exception.CommonBusinessException;
import it.jump3.security.TokenProvider;
import it.jump3.user.UserInfo;
import it.jump3.util.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.lang.reflect.InvocationTargetException;
import java.util.stream.Collectors;

@ApplicationScoped
@Slf4j
public class LoginService {

    @Inject
    TokenProvider tokenProvider;

    @Inject
    UserRepository userRepository;

    @Inject
    BCryptHashProvider bCryptHashProvider;


    public String login(LoginRequest loginRequest) throws InvocationTargetException, IllegalAccessException {

        User user = userRepository.findByUsername(loginRequest.getUsername());
        if (user == null) {
            throw new CommonBusinessException(Integer.toString(BusinessError.IB_404_USER.code()),
                    String.format("User %s not found", loginRequest.getUsername()), Response.Status.UNAUTHORIZED);
        }

        if (!bCryptHashProvider.checkPassword(loginRequest.getPassword(), user.getPassword())) {
            throw new CommonBusinessException(Integer.toString(BusinessError.IB_401_WRONG_PSW.code()),
                    "Wrong Password", Response.Status.UNAUTHORIZED);
        }

        if (UserStatusEnum.D.equals(user.getStatus())) {
            throw new CommonBusinessException(Integer.toString(BusinessError.IB_403_USER_DISABLED.code()),
                    String.format("User %s disabled", loginRequest.getUsername()), Response.Status.FORBIDDEN);
        }

        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userInfo, user);
        userInfo.setRoles(user.getRoles().stream()
                .map(Role::getCode)
                .collect(Collectors.toSet()));

        return tokenProvider.generateToken(userInfo);
    }

    public UserInfo getUserInfoFromToken(String token) {
        return tokenProvider.getUserInfoFromToken(TokenUtils.getToken(token));
    }
}
