package com.example.vacanciesbot.tgmessage.callbacks;

import com.example.vacanciesbot.contracts.input.message.CallbackActionMessage;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component("_authorize_candidate_message")
public class AuthorizeCallBackAction implements CallbackActionMessage {
    @Override
    public EditMessageText processAndRespond(String id, Message message, String[] currentCommand) {
        EditMessageText messageText = new EditMessageText();
        messageText.setText("Введите имя и фамилию кандидата");
        messageText.setMessageId(message.getMessageId());
        messageText.setChatId(message.getChatId());
        currentCommand[0] = "__authorize_candidate";
        currentCommand[1] = id;
        return messageText;
    }
}
