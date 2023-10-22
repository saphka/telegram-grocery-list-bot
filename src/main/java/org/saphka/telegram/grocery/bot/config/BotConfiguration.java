package org.saphka.telegram.grocery.bot.config;

import org.saphka.telegram.grocery.bot.service.TelegramApiService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.Webhook;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.updatesreceivers.ServerlessWebhook;

@Configuration
@EnableConfigurationProperties(BotProperties.class)
public class BotConfiguration {

    @Bean
    public Webhook telegramWebhook() {
        return new ServerlessWebhook();
    }

    @Bean
    public TelegramBotsApi telegramBotsApi(Webhook telegramWebhook, TelegramApiService apiService) throws TelegramApiException {
        var botApi = new TelegramBotsApi(DefaultBotSession.class, telegramWebhook);
        botApi.registerBot(apiService, new SetWebhook());
        return botApi;
    }
}
