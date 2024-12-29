package com.muzaffar.masjidfinder.service.user;

import com.muzaffar.masjidfinder.bot.model.TgUserDTO;
import com.muzaffar.masjidfinder.service.user.model.UserDTO;

public interface UserService {
    UserDTO getOrSave(TgUserDTO dto);
}
