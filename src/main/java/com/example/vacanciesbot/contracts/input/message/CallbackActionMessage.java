package com.example.vacanciesbot.contracts.input.message;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface CallbackActionMessage {
    EditMessageText processAndRespond(String id, Message message, String[] currentCommand);

}
