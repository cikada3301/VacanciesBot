package com.example.vacanciesbot.tgmessage.commands;

import com.example.vacanciesbot.contracts.input.message.MainCommandMessage;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component("/start")
public class StartCommand implements MainCommandMessage {

    @Override
    public SendMessage processAndRespond(Message message) {
        SendMessage response = new SendMessage();
        response.setChatId(message.getChatId());
        response.setText("Добро пожаловать в Vacancies Bot");
        return response;
    }

    @Override
    public String getDiscription() {
        return "Начало работы с ботом";
    }
}

