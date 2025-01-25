package com.muzaffar.masjidfinder.service.text.impl;

import com.muzaffar.masjidfinder.bot.enums.CallbackCommand;
import com.muzaffar.masjidfinder.bot.enums.Command;
import com.muzaffar.masjidfinder.domain.repository.TextRepo;
import com.muzaffar.masjidfinder.service.text.TextService;
import com.muzaffar.masjidfinder.service.text.model.TextDTO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TextServiceImpl implements TextService {

    private final TextRepo textRepo;
    private Map<String, TextDTO> cache;

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
    public TextDTO getFoundMasajidText() {
        return new TextDTO("""
                *Sizning so'ro'vingiz bo'yicha topilgan Masjidlar:* ⬇️
                """, true);
    }

    @Override
    public TextDTO getYouCanTryAgain() {
        return new TextDTO("""
                *Agar siz qidirgan masjid topilmagan bo'lsa boshqa nom bilan qaytadan urunib ko'ring:* ⬇️
                """, true);
    }

    @Override
    public TextDTO getText(String command) {
        return getCached(command);
    }

    @Override
    public TextDTO getText(String command, Boolean isAllowed) {
        //TODO
        // TEMPORARY SOLUTION

        if (isAllowed) {
            return new TextDTO("""
                    🔍 *Iltimos, Masjid nomini kiriting:* ⬇️
                    """, true);
        }

        return new TextDTO("""
                *Iltimos, keyinroq urunib ko'ring*
                """, true);
    }

    @Override
    public TextDTO getNoMasajidFoundText() {
        return new TextDTO("""
                *Sizning so'rovingizga mos keladigan Masjid topilmadi\\.* 
                \n*Qaytadan boshqa nom bilan urunib ko'ring:* ⬇️
                """, true);
    }

    @PostConstruct
    private void init() {
        this.cache = new HashMap<>();
        cache.putAll(getAll());
    }

    @Override
    public void reloadTexts() {
        this.cache.clear();
        this.cache.putAll(getAll());
    }

    private TextDTO getCached(String command) {

        if (true) {
            return getTextForTestEnvironment(command);
        }

        //TODO
        var textDTO = this.cache.get(command);

        if (Objects.isNull(textDTO) || textDTO.expiry().isBefore(LocalDateTime.now())) {
            var textOptional = textRepo.findByCommandButton(Command.valueOf(command));

            if (textOptional.isEmpty()) {
                return unrecognised();
            }

            textDTO = new TextDTO(textOptional.get());
            this.cache.put(command, textDTO);
        }
        return textDTO;
    }

    private TextDTO getTextForTestEnvironment(String command) {
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
                    *Eng yaqin Masjidlarni 🕌 va Jamoat Namozlarni 🕐 ko'rish uchun joylashuvni yuboring* 📿 
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

        if (command.equals(CallbackCommand.SET_MJ_AS_FAV.getText())) {
            return new TextDTO("""
                    *«{masjid}» «Mening Masjidlarim» ro’yxatiga qo'shildi\\!*\
                    """, true);
        }

        if (command.equals(CallbackCommand.REMOVE_FROM_MJ_AS_FAV.getText())) {
            return new TextDTO("""
                    *«{masjid}» «Mening Masjidlarim» ro’yxatiga chiqarildi\\!*\
                    """, true);
        }

        if (command.equals(Command.COMMUNITY_PRAYER_TIMES.getText())) {
            return new TextDTO("""
                    *Masjid namoz vaqtlarini olish uchun kerakli tugmani bosing* ⏬
                    """, true);
        }

        if (command.equals(Command.SEARCH.getText())) {
            return new TextDTO("""
                    *🔍 Iltimos Masjid nomini kiriting: ⬇️
                    """, true);
        }

        return temporaryUnavailable();
    }

    private Map<String, TextDTO> getAll() {
        return textRepo.findAll()
                .stream()
                .collect(Collectors.toMap(
                        text -> text.getCommandButton().getText(),
                        TextDTO::new,
                        (existing, replacement) -> existing,
                        HashMap::new
                ));
    }
}
