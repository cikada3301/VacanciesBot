package com.example.vacanciesbot.tgmessage.commands;

import com.example.vacanciesbot.contracts.input.message.MainCommandMessage;
import com.example.vacanciesbot.service.CandidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component("/update")
@RequiredArgsConstructor
public class UpdateCommand implements MainCommandMessage {

    private final CandidateService candidateService;

    @Override
    public SendMessage processAndRespond(Message message) {

        candidateService.update();

        SendMessage response = new SendMessage();
        response.setChatId(message.getChatId());
        response.setText("Данные из таблицы Notion были обновлены в бд");
        return response;
    }

    @Override
    public String getDiscription() {
        return "Синхронизация данных таблицы и локальной бд";
    }
}
