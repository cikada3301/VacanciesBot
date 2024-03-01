package com.example.vacanciesbot.tgmessage.callbacks;

import com.example.vacanciesbot.contracts.input.message.CallbackMenuMessage;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component("_candidate_name")
public class CandidateCallBackMenu implements CallbackMenuMessage {
    @Override
    public EditMessageText processAndRespond(String id, Message message) {
        EditMessageText messageText = new EditMessageText();
//        messageText.setText("Выберите действие");
//        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
//        List<InlineKeyboardButton> button = new ArrayList<>();
//        InlineKeyboardButton b = new InlineKeyboardButton();
//        b.setText("Введите имя");
//        b.setCallbackData("_candidate_name_message");
//
//        button.add(b);
//        buttons.add(button);
//
//        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
//        markupInLine.setKeyboard(buttons);
//
//        messageText.setMessageId(message.getMessageId());
//        messageText.setChatId(message.getChatId());
//        messageText.setReplyMarkup(markupInLine);
        return messageText;
    }
}
