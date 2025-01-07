package com.muzaffar.masjidfinder.service.text;

import com.muzaffar.masjidfinder.bot.enums.CallbackCommand;
import com.muzaffar.masjidfinder.bot.enums.Command;
import com.muzaffar.masjidfinder.service.text.model.TextDTO;

public interface TextService {

    TextDTO getText(String command);
}
