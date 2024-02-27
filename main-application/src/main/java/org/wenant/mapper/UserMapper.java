package org.wenant.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.wenant.domain.dto.AuthenticationDto;
import org.wenant.domain.dto.RegistrationDto;
import org.wenant.domain.dto.UserDto;
import org.wenant.domain.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "role", constant = "USER")
    @Mapping(target = "id", expression = "java(null)")
    User registrationDtoToUser(RegistrationDto registrationDto);


    AuthenticationDto userToAuthenticationDto(User user);


    UserDto userToUserDto(User user);

    List<UserDto> userListToUserDtoList(List<User> userList);

}
