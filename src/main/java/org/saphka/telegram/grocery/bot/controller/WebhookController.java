package org.saphka.telegram.grocery.bot.controller;

import org.saphka.telegram.grocery.bot.service.TelegramService;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@RequestMapping("${bot.path}")
@Profile("prod")
public class WebhookController {
    private final TelegramService service;

    public WebhookController(TelegramService service) {
        this.service = service;
    }

    @PostMapping
    public BotApiMethod<?> handleUpdate(@RequestBody Update update) {
        return service.processUpdate(update);
    }
}
