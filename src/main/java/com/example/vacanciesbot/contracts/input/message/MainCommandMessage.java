package com.example.vacanciesbot.contracts.input.message;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface MainCommandMessage {
    SendMessage processAndRespond(Message message);

    String getDiscription();
}
