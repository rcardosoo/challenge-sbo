package com.triade.simple.core.user.service.mapper;

import com.triade.simple.core.user.domain.User;
import com.triade.simple.core.user.domain.Authority;
import com.triade.simple.core.user.service.dto.AdminUserDTO;
import com.triade.simple.core.user.service.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<UserDTO, User> {

    @Mapping(source = "authorities", target = "authorities", qualifiedByName = "authoritiesToStrings")
    AdminUserDTO userToAdminUserDTO(User user);

    @Mapping(source = "authorities", target = "authorities", qualifiedByName = "authoritiesFromStrings")
    User userDTOToUser(AdminUserDTO userDTO);

    List<User> userDTOsToUsers(List<AdminUserDTO> userDTOs);

    default User userFromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }


    @Named("authoritiesToStrings")
    default Set<String> authoritiesToStrings(Set<Authority> authorities) {
        return authorities.stream().map(Authority::getName).collect(Collectors.toSet());
    }

    @Named("authoritiesFromStrings")
    default Set<Authority> authoritiesFromStrings(Set<String> authoritiesAsString) {
        Set<Authority> authorities = new HashSet<>();

        if (authoritiesAsString != null) {
            authorities = authoritiesAsString.stream().map(string -> {
                Authority auth = new Authority();
                auth.setName(string);
                return auth;
            }).collect(Collectors.toSet());
        }

        return authorities;
    }


}
