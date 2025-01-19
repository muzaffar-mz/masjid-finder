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
    public TextDTO getText(String command) {
        return getCached(command);
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

        if (command.equals(CallbackCommand.SET_MJ_AS_FAV.getText())) {
            return new TextDTO("""
                    *«{masjid}» «Mening Masjidlarim» ro’yxatiga qo'shildi\\!*\
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
