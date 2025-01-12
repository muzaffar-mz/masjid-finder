package com.muzaffar.masjidfinder.service.text.impl;

import com.muzaffar.masjidfinder.bot.enums.Command;
import com.muzaffar.masjidfinder.service.text.TextService;
import com.muzaffar.masjidfinder.service.text.model.TextDTO;
import org.springframework.stereotype.Service;

@Service
public class TextServiceImpl implements TextService {

    //TODO
    // Abdulloh
    // 1. Create Text entity class
    // 2. Create TextRepo interface
    // 3. Create HashMap variable with key String and Value CachedTextDTO (can be found in model folder)
    // 4. Upon initiation of this class (@PostConstruct) all texts from the repository should be retrieved and loaded into a map
    //      Expiration time is LocalDateTime.now() + 30 days
    // 5. private method that gets CachedTextDTO, checks whether expired, if not then returns CachedTextDTO, otherwise
    //      gets text from repo using <<command>> field
    // 6. another public method that reloads all texts from the repo and puts into hashmap



    @Override
    public TextDTO getText(String command) {

        //TEMPORARY SOLUTION
        if (command.equals("/start")) {
            return new TextDTO("""
                    *Assalomu alaykum\\!* \
                   
                    🌙 *Masjid Sari  🕌 🚶🏽‍♂️ botimizga xush kelibsiz\\!*\
                   ️
                    *Botimizdan foydalanish uchun o'zingizga kerakli tugmani bosing:*⏬""", true);
        }

        if (command.equals(Command.CLOSEST_MASJID.getText())) {
            return new TextDTO("""
                    *Eng yaqin Masjidlarni 🕌 va Namoat Namozlarni 🕐 ko'rish uchun joylashuvni yuboring* 📿 
                    """, true);
        }

        return null;
    }
}
