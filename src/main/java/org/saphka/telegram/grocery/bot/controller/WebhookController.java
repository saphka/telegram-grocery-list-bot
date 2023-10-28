package org.saphka.telegram.grocery.bot.controller;

import org.saphka.telegram.grocery.bot.service.TelegramUpdateProcessor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@RequestMapping("/api/v1/callback")
@Profile("prod")
public class WebhookController {
    private final TelegramUpdateProcessor telegramUpdateProcessor;

    public WebhookController(TelegramUpdateProcessor telegramUpdateProcessor) {
        this.telegramUpdateProcessor = telegramUpdateProcessor;
    }

    @PostMapping
    public String handleUpdate(@RequestBody Update update) {
        telegramUpdateProcessor.processUpdate(update);
        return "Working";
    }
}
