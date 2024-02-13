package com.example.myjobfinder.telegramBot.service;

import com.example.myjobfinder.authorizer.Authorizer;
import com.example.myjobfinder.model.Vacancy;
import com.example.myjobfinder.model.VacancyRepository;
import com.example.myjobfinder.parser.Parser;
import com.example.myjobfinder.responder.Responder;
import com.example.myjobfinder.telegramBot.config.BotConfig;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Optional;

@Component
@AllArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig botConfig;

    @Autowired
    private Authorizer authorizer;

    @Autowired
    private Parser parser;

    @Autowired
    private Responder responder;

    @Autowired
    VacancyRepository vacancyRepository;

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

        if(update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText){
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "/commands":
                    break;
                case "/parse":
                    parser.getVacancies();
                    break;
                case "/authorize":
                    String authorizeLink = authorizer.authorize();
                    sendMessage(chatId, authorizeLink);
                    break;
                case "/respondWithoutTest":
                    Iterable<Vacancy> vacancyIterable = vacancyRepository.findAll();
                    for (Vacancy vacancy : vacancyIterable)
                    {
                        if (!vacancy.getHastest())
                        {
                            String message = vacancy.getId().toString() + ": " +
                                    responder.respondVacancy(vacancy, authorizer.getConfig().accessToken);
                            sendMessage(chatId, message);
                        }
                    }
                    break;

            }
        }

    }

    private void startCommandReceived(Long chatId, String name) {
        String answer = "Hi, " + name + ", nice to meet you!\n" +
                "Type \"commands\" to view list of available commands";
        sendMessage(chatId, answer);
    }

    private void sendMessage(Long chatId, String textToSend){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}