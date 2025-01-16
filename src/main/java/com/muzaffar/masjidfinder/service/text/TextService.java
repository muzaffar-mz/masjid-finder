package com.muzaffar.masjidfinder.service.text;

import com.muzaffar.masjidfinder.service.text.model.TextDTO;

public interface TextService {

    TextDTO getText(String command);

    TextDTO temporaryUnavailable();

    TextDTO unrecognised();
}
