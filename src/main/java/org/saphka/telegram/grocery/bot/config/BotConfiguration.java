package org.saphka.telegram.grocery.bot.config;

import org.saphka.telegram.grocery.bot.adapter.TelegramLongPollingAdapter;
import org.saphka.telegram.grocery.bot.adapter.TelegramWebhookAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
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
    @Profile("prod")
    public Webhook telegramWebhook() {
        return new ServerlessWebhook();
    }

    @Bean
    @Profile("prod")
    public SetWebhook telegramWebhookInfo(@Value("${bot.webhook}") String webhookUrl) {
        var webhook = new SetWebhook();
        webhook.setUrl(webhookUrl);
        return webhook;
    }

    @Bean
    @Profile("prod")
    public TelegramBotsApi telegramBotsApiProd(Webhook telegramWebhook, SetWebhook telegramWebhookInfo, TelegramWebhookAdapter telegramWebhookAdapter) throws TelegramApiException {
        var botApi = new TelegramBotsApi(DefaultBotSession.class, telegramWebhook);
        botApi.registerBot(telegramWebhookAdapter, telegramWebhookInfo);
        return botApi;
    }

    @Bean
    @Profile("local")
    public TelegramBotsApi telegramBotsApiLocal(TelegramLongPollingAdapter longPollingAdapter) throws TelegramApiException {
        var botApi = new TelegramBotsApi(DefaultBotSession.class);
        botApi.registerBot(longPollingAdapter);
        return botApi;
    }
}
