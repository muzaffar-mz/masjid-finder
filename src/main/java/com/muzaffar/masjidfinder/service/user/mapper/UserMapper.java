package com.muzaffar.masjidfinder.service.user.mapper;

import com.muzaffar.masjidfinder.bot.model.TgUserDTO;
import com.muzaffar.masjidfinder.domain.entity.User;
import com.muzaffar.masjidfinder.service.user.model.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "role", constant = "USER")
    User toUser(String phone);

    @Mapping(target = "defaultMasjidId", source = "user.defaultMasjid.id")
    UserDTO toUserDTO(User user);


    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "role", constant = "USER")
    User toUser(TgUserDTO userDTO);
}
