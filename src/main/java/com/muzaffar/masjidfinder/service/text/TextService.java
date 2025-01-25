package com.muzaffar.masjidfinder.service.text;

import com.muzaffar.masjidfinder.service.text.model.TextDTO;

public interface TextService {

    TextDTO getText(String command);
    TextDTO getText(String command, Boolean isAllowed);

    TextDTO temporaryUnavailable();

    TextDTO unrecognised();

    void reloadTexts();

    TextDTO getNoMasajidFoundText();

    TextDTO getFoundMasajidText();

    TextDTO getYouCanTryAgain();
}
