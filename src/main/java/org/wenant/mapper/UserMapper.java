package org.wenant.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.wenant.domain.dto.AuthenticationDto;
import org.wenant.domain.dto.RegistrationDto;
import org.wenant.domain.dto.UserDto;
import org.wenant.domain.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);


    @Mapping(target = "role", constant = "USER")
    @Mapping(target = "id", expression = "java(null)")
    User registrationDtoToUser(RegistrationDto registrationDto);


    @Mapping(target = "role", source = "user.role", qualifiedByName = "mapRole")
    AuthenticationDto userToAuthenticationDto(User user);


    UserDto userToUserDto(User user);

    List<UserDto> userListToUserDtoList(List<User> userList);

    @Named("mapRole")
    default AuthenticationDto.Role mapRole(User.Role role) {
        return AuthenticationDto.Role.valueOf(role.name());
    }

    default User.Role mapRole(String role) {
        return User.Role.valueOf(role);
    }


}
