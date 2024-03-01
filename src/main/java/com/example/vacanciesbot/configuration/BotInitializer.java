package com.example.vacanciesbot.configuration;

import com.example.vacanciesbot.contracts.input.message.MainCommandMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Configuration
@Slf4j
@EnableScheduling
@RequiredArgsConstructor
public class BotInitializer {

    private final BotConfiguration bot;
    private final Map<String, MainCommandMessage> commandsMap;

    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {

        Set<Map.Entry<String, MainCommandMessage>> entries = commandsMap.entrySet();
        List<BotCommand> commands =
                entries
                        .stream()
                        .map(k -> new BotCommand(k.getKey(), k.getValue().getDiscription()))
                        .toList();
        bot.execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}

