package com.example.vacanciesbot.tgmessage.messages;

import com.example.vacanciesbot.contracts.input.message.AwaitingTextMessage;
import com.example.vacanciesbot.service.VacanciesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("__find_vacancies")
@RequiredArgsConstructor
public class AwaitFindVacanciesMessage implements AwaitingTextMessage {

    private final VacanciesService vacanciesService;

    @Override
    public SendMessage processAndRespond(Message message, String[] currentCommand, Map<String, List<String>> vacancies) {

        SendMessage response = new SendMessage();

        String type = message.getText();

        if (type.equalsIgnoreCase("старые")) {
            Map<String, String> oldVacancies = vacanciesService.getOldVacancies(currentCommand[1]);

            StringBuilder result = new StringBuilder("Старые вакансии:");
            setTextMessageWithVacancies(currentCommand, vacancies, response, oldVacancies, result);

        } else if (type.equalsIgnoreCase("новые")) {
            Map<String, String> newVacancies = vacanciesService.getNewVacancies(currentCommand[1]);

            StringBuilder result = new StringBuilder("Новые вакансии:");
            setTextMessageWithVacancies(currentCommand, vacancies, response, newVacancies, result);
        }

        response.setChatId(message.getChatId());

        currentCommand[0] = "__response_on_vacancy";

        return response;
    }

    private void setTextMessageWithVacancies(String[] currentCommand, Map<String, List<String>> vacancies, SendMessage response, Map<String, String> oldVacancies, StringBuilder result) {
        if (!oldVacancies.isEmpty()) {
            result.append('\n');

            List<String> idVacancies = new ArrayList<>();

            oldVacancies.entrySet().stream()
                    .limit(10)
                    .forEach((entry) -> {
                        result.append(entry.getKey()).append(" : ").append(entry.getValue()).append('\n');
                        idVacancies.add(entry.getValue());
                    });

            vacancies.put(currentCommand[1], idVacancies);

            response.setText(String.join("", result.toString(), "\n", "\n", "Введите на какие вакансии откликнуться (Все / Перечислить номера, например 1, 2, 5)"));
        }

        response.setText("На данный момент новых вакансий нет");
    }
}
