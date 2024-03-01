package com.example.vacanciesbot.contracts.input.message;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Map;

public interface AwaitingTextMessage {
    SendMessage processAndRespond(Message message, String[] id, Map<String, List<String>> vacancies);
}
