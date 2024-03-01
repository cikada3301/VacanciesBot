package com.example.vacanciesbot.tgmessage.messages;

import com.example.vacanciesbot.contracts.input.hh.CreateHHAuthCodeUseCase;
import com.example.vacanciesbot.contracts.input.message.AwaitingTextMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Map;

@Component("__activate_code")
@RequiredArgsConstructor
public class AwaitActivateCodeMessage implements AwaitingTextMessage {

    private final CreateHHAuthCodeUseCase createHHAuthCodeUseCase;

    @Override
    public SendMessage processAndRespond(Message message, String[] currentCommand, Map<String, List<String>> vacancies) {
        SendMessage response = new SendMessage();

        String result = createHHAuthCodeUseCase.getAuthToken(message.getText(), currentCommand[1]);

        if (result.equals("Auth")) {
            response.setText("Кандидат авторизован");
        } else {
            response.setText(result);
        }

        currentCommand[0] = null;
        currentCommand[1] = null;

        return response;
    }
}
