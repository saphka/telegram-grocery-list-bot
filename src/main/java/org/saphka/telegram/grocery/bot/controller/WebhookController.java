package org.saphka.telegram.grocery.bot.controller;

import org.saphka.telegram.grocery.bot.service.TelegramUpdateProcessor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@RequestMapping("${bot.path}")
@Profile("prod")
public class WebhookController {
    private final TelegramUpdateProcessor telegramUpdateProcessor;

    public WebhookController(TelegramUpdateProcessor telegramUpdateProcessor) {
        this.telegramUpdateProcessor = telegramUpdateProcessor;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String handleUpdate(@RequestBody Update update) {
        telegramUpdateProcessor.processUpdate(update);
        return "Working";
    }
}
