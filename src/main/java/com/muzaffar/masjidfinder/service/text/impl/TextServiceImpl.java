package com.muzaffar.masjidfinder.service.text.impl;

import com.muzaffar.masjidfinder.bot.enums.Command;
import com.muzaffar.masjidfinder.domain.entity.Text;
import com.muzaffar.masjidfinder.domain.entity.enums.InterfaceLanguage;
import com.muzaffar.masjidfinder.domain.repository.TextRepo;
import com.muzaffar.masjidfinder.service.text.TextService;
import com.muzaffar.masjidfinder.service.text.model.CachedTextDTO;
import com.muzaffar.masjidfinder.service.text.model.TextDTO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TextServiceImpl implements TextService {

    private final TextRepo textRepo;
    private final Map<String, CachedTextDTO> textCache = new HashMap<>();

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
                    Assalomu alaykum, hurmatli *{username}*\\! \
                    
                    *Masjid Sari* botimizga xush kelibsiz\\!\
                    
                    Eng yaqin Masjid Sari borish uchun joylashuvni yuboring""", true);
        }

        return null;
    }

    @PostConstruct
    private void initializeMap() {
        reloadTexts();
    }

    private void reloadTexts() {
        textCache.clear();
        textRepo.findAll().forEach(textEntity-> {
            InterfaceLanguage language = textEntity.getLanguage();
            Command commandButton = textEntity.getCommandButton();
            String message = textEntity.getMessage();
            Boolean isFormatted = textEntity.getIsFormatted();
            LocalDateTime expirationTime = LocalDateTime.now().plusDays(30);
            textCache.put(String.valueOf(commandButton), new CachedTextDTO(message, isFormatted, expirationTime));
        });
    }


    private CachedTextDTO getCachedText(String command) {
        CachedTextDTO cachedText = textCache.get(command);

        if (cachedText == null || cachedText.expiration().isBefore(LocalDateTime.now())) {
            Text textEntity = textRepo.findByCommand(Command.valueOf(command))
                    .orElseThrow(() -> new RuntimeException("Text not found for command: " + command));
            String text = textEntity.getMessage();
            Boolean isFormatted = textEntity.getIsFormatted();
            LocalDateTime expiration = LocalDateTime.now().plusDays(30);
            cachedText = new CachedTextDTO(text, isFormatted, expiration);
            textCache.put(command, cachedText);
        }
        return cachedText;
    }

}
