package com.example.vacanciesbot.configuration;

import com.example.vacanciesbot.contracts.input.message.AwaitingTextMessage;
import com.example.vacanciesbot.contracts.input.message.CallbackActionMessage;
import com.example.vacanciesbot.contracts.input.message.CallbackMenuMessage;
import com.example.vacanciesbot.contracts.input.message.MainCommandMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class BotConfiguration extends TelegramLongPollingBot {

    @Value("${bot.name}")
    private String name;

    @Value("${bot.token}")
    private String token;

    private final Map<String, MainCommandMessage> commandsMap;

    private final Map<String, CallbackMenuMessage> callbackMenuMap;

    private final Map<String, CallbackActionMessage> callbackActionMap;

    private final Map<String, AwaitingTextMessage> awaitingTextMap;

    private final String[] currentAction = new String[2];

    private Map<String, List<String>> vacancies = new HashMap<>();

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return token;
    }


    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            checkMessage(update);
        } else if (update.hasCallbackQuery()) {
            checkCallback(update);
        }
    }

    private void checkMessage(Update update) {
        MainCommandMessage mainCommandMessage = commandsMap.get(update.getMessage().getText());
        if (mainCommandMessage == null && this.currentAction[0] == null) {
            sendMessage(new SendMessage(String.valueOf(update.getMessage().getChatId()), "Команда не поддерживается"));
        } else if (mainCommandMessage == null && currentAction[0] != null) {
            SendMessage message = awaitingTextMap.get(currentAction[0]).processAndRespond(update.getMessage(), currentAction, vacancies);
            sendMessage(message);
        } else {
            clearAwaitingTextCommand();
            SendMessage message = mainCommandMessage.processAndRespond(update.getMessage());
            sendMessage(message);
        }
    }

    private void checkCallback(Update update) {
        clearAwaitingTextCommand();

        Message message = (Message) update.getCallbackQuery().getMessage();

        String[] data = update.getCallbackQuery().getData().split("::");
        if (data.length < 2) {
            data = new String[]{data[0], ""};
        }
        if (callbackActionMap.containsKey(data[0])) {
            EditMessageText messageText = callbackActionMap.get(data[0]).processAndRespond(data[1], message, currentAction);
            editMessage(messageText);
        } else if (callbackMenuMap.containsKey(data[0])) {
            EditMessageText messageText = callbackMenuMap.get(data[0]).processAndRespond(data[1], message);
            editMessage(messageText);
        } else
            sendMessage(new SendMessage(String.valueOf(message.getChatId()), "Команда не поддерживается"));
    }

    private void sendMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void editMessage(EditMessageText messageText) {
        try {
            execute(messageText);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void clearAwaitingTextCommand() {
        this.currentAction[0] = null;
        this.currentAction[1] = null;
    }
}
