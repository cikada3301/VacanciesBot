package com.example.vacanciesbot.tgmessage.messages;

import com.example.vacanciesbot.contracts.input.message.AwaitingTextMessage;
import com.example.vacanciesbot.entity.Candidate;
import com.example.vacanciesbot.service.CandidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Map;

@Component("__authorize_candidate")
@RequiredArgsConstructor
public class AwaitAuthorizeCandidateMessage implements AwaitingTextMessage {

    @Value("${web.address}")
    private String baseUri;
    @Value("${hh.clientId}")
    private String applicationId;
    @Value("${web.redirectUri}")
    private String redirectUri;

    private final CandidateService candidateService;

    @Override
    public SendMessage processAndRespond(Message message, String[] currentCommand, Map<String, List<String>> vacancies) {

        SendMessage response = new SendMessage();

        String[] data = message.getText().split(" ");

        Candidate candidate = candidateService.findByNameAndLastname(data[0], data[1]);

        response.setText(String.join("", "Для авторизации пользователя необходимо активировать кандидата, перед этим нужно на hh.ru авторизоваться на аккаунт кандидата перейти по ссылке: \n"
                , baseUri, "/oauth/authorize?response_type=code&client_id="
                , applicationId, redirectUri.isBlank() ? "" : "&redirect_uri=" + redirectUri, "\n", "\n",
                "Затем отправьте полученный код и id резюме через пробел сюда"));
        response.setChatId(message.getChatId());

        currentCommand[0] = "__activate_code";
        currentCommand[1] = candidate.getId().toString();

        return response;
    }
}
