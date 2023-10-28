package org.saphka.telegram.grocery.bot.service;

import org.saphka.telegram.grocery.bot.config.BotProperties;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

@Service
public class TelegramService extends DefaultAbsSender {
    public TelegramService(BotProperties properties) {
        super(new DefaultBotOptions(), properties.token());
    }

    @Override
    public <T extends Serializable, Method extends BotApiMethod<T>> CompletableFuture<T> executeAsync(Method method) {
        return sendApiMethodAsync(method);
    }
}
