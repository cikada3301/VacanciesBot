package com.example.vacanciesbot.tgmessage.messages;

import com.example.vacanciesbot.contracts.input.message.AwaitingTextMessage;
import com.example.vacanciesbot.entity.Candidate;
import com.example.vacanciesbot.service.CandidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Map;

@Component("__find_candidate")
@RequiredArgsConstructor
public class AwaitCandidateNameMessage implements AwaitingTextMessage {

    private final CandidateService candidateService;

    @Override
    public SendMessage processAndRespond(Message message, String[] currentCommand, Map<String, List<String>> vacancies) {

        SendMessage response = new SendMessage();

        String[] data = message.getText().split(" ");

        Candidate candidate = candidateService.findByNameAndLastname(data[0], data[1]);

        response.setText("Введите тип вакансии (старые/новые)");
        response.setChatId(message.getChatId());

        currentCommand[0] = "__find_vacancies";
        currentCommand[1] = candidate.getId().toString();

        return response;
    }
}
