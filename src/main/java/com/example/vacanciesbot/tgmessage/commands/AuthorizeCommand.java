package com.example.vacanciesbot.tgmessage.commands;

import com.example.vacanciesbot.contracts.input.message.MainCommandMessage;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component("/authorize")
public class AuthorizeCommand implements MainCommandMessage {
    @Override
    public SendMessage processAndRespond(Message message) {
        SendMessage response = new SendMessage();
        response.setText("Нажмите кнопку ввести имя для ввода имени и фамилии кандидата для авторизации");
        response.setChatId(message.getChatId());

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> button = new ArrayList<>();

        InlineKeyboardButton b = new InlineKeyboardButton();
        b.setText("Ввести имя");
        b.setCallbackData("_authorize_candidate_message");
        button.add(b);
        buttons.add(button);

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        markupInLine.setKeyboard(buttons);
        response.setReplyMarkup(markupInLine);
        return response;
    }

    @Override
    public String getDiscription() {
        return "Авторизовать HH-аккаунт кандидата для откликов";
    }
}
