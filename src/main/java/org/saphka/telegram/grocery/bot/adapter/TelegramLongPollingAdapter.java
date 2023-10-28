package org.saphka.telegram.grocery.bot.adapter;

import org.saphka.telegram.grocery.bot.config.BotProperties;
import org.saphka.telegram.grocery.bot.service.TelegramUpdateProcessor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@Profile("local")
public class TelegramLongPollingAdapter extends TelegramLongPollingBot {

    private final BotProperties properties;
    private final TelegramUpdateProcessor telegramUpdateProcessor;

    public TelegramLongPollingAdapter(BotProperties properties, TelegramUpdateProcessor telegramUpdateProcessor) {
        super(properties.token());
        this.properties = properties;
        this.telegramUpdateProcessor = telegramUpdateProcessor;
    }

    @Override
    public void onUpdateReceived(Update update) {
        telegramUpdateProcessor.processUpdate(update);
    }

    @Override
    public String getBotUsername() {
        return properties.username();
    }
}
