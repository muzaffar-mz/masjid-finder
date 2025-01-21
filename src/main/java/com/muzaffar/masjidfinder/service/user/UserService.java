package com.muzaffar.masjidfinder.service.user;

import com.muzaffar.masjidfinder.bot.model.TgUserDTO;
import com.muzaffar.masjidfinder.service.user.model.UserDTO;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface UserService {
    UserDTO getOrSaveByTgUserDTO(TgUserDTO dto);

    UserDTO getUser(TgUserDTO user);

    boolean isChatEnabled(Long userId);

    void userUpdateChatsState(Update update, boolean b);
}
