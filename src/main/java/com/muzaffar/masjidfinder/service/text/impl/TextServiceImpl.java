package com.muzaffar.masjidfinder.service.text.impl;

import com.muzaffar.masjidfinder.bot.enums.AdminCommand;
import com.muzaffar.masjidfinder.bot.enums.CallbackCommand;
import com.muzaffar.masjidfinder.bot.enums.Command;
import com.muzaffar.masjidfinder.domain.entity.Text;
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
    public TextDTO getAdminText(String command) {
        if (command.equals(AdminCommand.START.getText())) {
            return new TextDTO("""
                    *Assalomu alaykum\\!* \
                   
                    🌙 *Masjid Sari 🕌 🚶🏽‍♂️Adminlar uchun botimizga xush kelibsiz\\!*\
                   ️
                    *Botimizdan foydalanish uchun ro'yxatdan o'ting:*⏬""", true);
        }


        if (command.equals(AdminCommand.UNVERIFIED_MASAJID.getText())) {
            return new TextDTO("""
                    *Kerakli tugmani bosing*
                    """, true);
        }

        if (command.equals("location")) {
            return new TextDTO("""
                    Tasdiqlanmangan eng yaqin masjidlar ro'yxati: ⬇️
                    """, false);
        }

        return null;
    }

    @Override
    public TextDTO getWelcomeAdminText() {
        return new TextDTO("""
                🌙 *Hurmatli Admin️ 👨🏽‍💻 \\!*\
                \n *Siz botimizdan muvaffaqiyatli ro'yxatdan o'tdingiz\\!*\
                \n *Botimizdan foydalanish uchun o'zingizga kerakli tugmani bosing:*⏬
                """, true);
    }

    @Override
    public TextDTO getAdminMainMenu() {
        return new TextDTO("""
                *Botimizdan foydalanish uchun o'zingizga kerakli tugmani bosing:*⏬
                """, true);
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

    @Override
    public TextDTO getAdminSearchMasjidText() {
        return new TextDTO("""
                *Masjid nomini kiriting:* ⬇️
                """, true);
    }

    @Override
    public TextDTO getEnterUpdatedMasjidName(String masjidName) {
        return new TextDTO(
                String.format("""
                        *Iltimos %s masjidi uchun yangi nomni kiriting:* ⬇️""", masjidName),
                true);
    }

    @Override
    public TextDTO masjidNameSuccessfullyUpdated(String masjidName) {
        return new TextDTO(
                String.format("""
                        *Masjid nomi %s ga muvafaqqiyatli o'zgartirildi\\!*
                        """, masjidName), true
        );
    }


    @Override
    public TextDTO masjidIsVerified(String name) {
        return new TextDTO(
                String.format("""
                         %s *muvafaqqiyatli tasdiqlandi\\!*
                        """, name), true
        );
    }

    @Override
    public TextDTO updatePrayerTimesSample() {
        return new TextDTO("""
                *Iltimos yangilangan jamoat vaqtlarini quyidagi ko'rinishda kirgazing:*
                Bomdod: 04:10
                Peshin: 13:00
                Asr: 17:30
                Shom: 19:55
                Hufton: 21:30
                """, true);
    }

    @Override
    public TextDTO getAdminSendLocationText() {
        return new TextDTO("""
                *Iltimos o'z joylashuvingizni yuboring:* ⬇️
                """, true);
    }

    @Override
    public TextDTO getAdminNearFiveMasajid() {
        return new TextDTO("""
                *Sizning joylashuvingizga eng yaqin masjidlar:* ⏬
                """, true);
    }

    @Override
    public TextDTO getChoose() {
        return new TextDTO("""
                    *Kerakli tugmani bosing*
                    """, true);
    }

    @Override
    public TextDTO getAdminHasNoAssignedMasjid() {
        return new TextDTO("""
                *Sizga masjid biriktirilmagan*
                """, true);
    }

    @Override
    public TextDTO getAdminEnterMasjidIdText() {
        return new TextDTO("""
                *Iltimos masjid ID raqamini kiriting:* ⏬
                """, true);
    }

    @Override
    public TextDTO getInvalidIdText() {
        return new TextDTO("""
                *Notog'ri ID raqam kiritdingiz. Qaytadan urinib ko'ring*
                """, true);
    }

    @Override
    public TextDTO getAdminAbout() {
        return new TextDTO("""
                    *Admin botning maqsadi \\- foydalanuvchilar uchun masjidlarni, ulardagi namoz vaqtlarini yangilab turishga ko’mak berish\\!*\
                     
                     \n*Ishlab chiquvchisi \\- “Toshkent Inc”
                      \nBog’lanish uchun \\- https:\\/\\/t\\.me\\/BotOpsAdmin*
                    """, true);
    }

    @Override
    public TextDTO unauthorizedUser() {
        return new TextDTO("""
                *Tehnik xatolik\\. Iltimos adminlar bilan bog'laning*
                """, true);
    }

    private TextDTO getCached(String command) {

        if (true) {
            return getTextForTestEnvironment(command);
        }

        //TODO
        var textDTO = this.cache.get(command);

        if (Objects.isNull(textDTO) || textDTO.expiry().isBefore(LocalDateTime.now())) {
            var textOptional = textRepo.findByCommandButton(command);

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
                        Text::getCommandButton,
                        TextDTO::new,
                        (existing, replacement) -> existing,
                        HashMap::new
                ));
    }
}
