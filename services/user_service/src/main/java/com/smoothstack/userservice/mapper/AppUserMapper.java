package com.smoothstack.userservice.mapper;

import com.smoothstack.userservice.dto.UserResponseDTO;
import com.smoothstack.userservice.model.AppUser;
import com.smoothstack.userservice.dto.AppUserDTO;
import org.mapstruct.*;

// The componentModel = "spring" attribute makes the generated mapper a Spring bean,
// allowing it to be injected into other Spring components.
@Mapper(componentModel = "spring")
public interface AppUserMapper {
    AppUserDTO userToUserDTO(AppUser appUser);
    AppUser userDTOToUser(AppUserDTO userDTO);

    @Mapping(target = "userId", ignore = true) // Ignoring the ID field
    void updateUserFromDto(AppUserDTO dto, @MappingTarget AppUser user);  // updates an existing user with params

    UserResponseDTO userToUserResponseDTO(AppUser appUser);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDtoIgnoringNull(AppUserDTO dto, @MappingTarget AppUser user);
}


