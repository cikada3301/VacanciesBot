package com.example.vacanciesbot.tgmessage.messages;

import com.example.vacanciesbot.contracts.input.message.AwaitingTextMessage;
import com.example.vacanciesbot.service.VacanciesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Map;

@Component("__response_on_vacancy")
@RequiredArgsConstructor
public class AwaitResponseOnVacancy implements AwaitingTextMessage {

    private final VacanciesService vacanciesService;

    @Override
    public SendMessage processAndRespond(Message message, String[] currentCommand, Map<String, List<String>> vacancies) {

        SendMessage sendMessage = new SendMessage();

        sendMessage.setText("Кандидат на данные вакансии был откликнут");
        sendMessage.setChatId(message.getChatId());

        String result = vacanciesService.responseOnVacancies(currentCommand[1], vacancies, message.getText());

        if (result.equals("NotAuth")) {
            sendMessage.setText("Кандидат не авторизован в боте, чтобы авторизовать введите /authorize");
        }

        vacancies.remove(currentCommand[1]);
        currentCommand[0] = null;
        currentCommand[1] = null;

        return sendMessage;
    }
}
