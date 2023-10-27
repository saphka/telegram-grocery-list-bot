package org.saphka.telegram.grocery.bot.adapter;

import org.saphka.telegram.grocery.bot.config.BotProperties;
import org.saphka.telegram.grocery.bot.service.TelegramService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@Profile("local")
public class TelegramLongPollingAdapter extends TelegramLongPollingBot {

    private static final Logger log = LoggerFactory.getLogger(TelegramLongPollingAdapter.class);

    private final BotProperties properties;
    private final TelegramService service;

    public TelegramLongPollingAdapter(BotProperties properties, TelegramService service) {
        super(properties.token());
        this.properties = properties;
        this.service = service;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            this.execute(service.processUpdate(update));
        } catch (TelegramApiException e) {
            log.error("Error processing update {}", update, e);
        }
    }

    @Override
    public String getBotUsername() {
        return properties.username();
    }
}
