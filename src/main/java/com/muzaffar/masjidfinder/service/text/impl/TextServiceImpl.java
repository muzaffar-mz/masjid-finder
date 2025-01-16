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
    public TextDTO temporaryUnavailable() {
        return new TextDTO("""
                    *Hurmatli foydalanuvchi\\!*\
                    \n*Bu tugma voqtinchalik faol emas\\.*\
                    \n*Noqulayliklar uchun uzr so'raymiz\\!*\
                """, true);
    }

    @Override
    public TextDTO unrecognised() {
        return new TextDTO("""
                    *Hurmatli foydalanuvchi\\!*\
                    \n*Siz yuborgan so'rov topilmadi\\.*\
                    \n*Noqulayliklar uchun uzr so'raymiz\\!*\
                """, true);
    }

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

        if (command.equals("location")) {
            // TODO
            return new TextDTO("""
                    *Iltimos qulay masjidni tanlang:*
                    """, true);
        }

        if (command.equals(Command.CLOSEST_MASJID.getText())) {
            return new TextDTO("""
                    *Eng yaqin Masjidlarni 🕌 va Namoat Namozlarni 🕐 ko'rish uchun joylashuvni yuboring* 📿 
                    """, true);
        }

        if (command.equals(Command.BACK.getText())) {
            return new TextDTO("""
                    🌙 *Masjid Sari*  🕌 🚶🏽‍
                    *O'zingizga kerakli tugmani bosing:*⏬
                    """, true);
        }

        if (command.equals(Command.FAVORITES.getText())) {
            return new TextDTO("""
                    💚🕌 *«Mening Masjidlarim» ro’yxati:*
                    """, true);
        }

        if (command.equals(Command.ABOUT.getText())) {
            return new TextDTO("""
                    *Botning maqsadi \\- foydalanuvchiga yaqin masjidlarni, ularga olib boruvchi yo’llarni, ulardagi namoz vaqtlarini topishda ko’mak berish\\!*\
                     
                     \n*Ishlab chiquvchisi \\- “Toshkent Inc”
                      \nBog’lanish uchun \\- 998 \\(90\\) 123\\-45\\-67 \\/ feedback@toshkentinc\\.com*
                    """, true);
        }

        return temporaryUnavailable();
    }
}
