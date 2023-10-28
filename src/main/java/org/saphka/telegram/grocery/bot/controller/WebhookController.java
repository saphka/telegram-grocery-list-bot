package org.saphka.telegram.grocery.bot.controller;

import org.saphka.telegram.grocery.bot.service.TelegramService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
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
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String handleUpdate(@RequestBody Update update) {
        service.processUpdate(update);
        return "Working";
    }
}
