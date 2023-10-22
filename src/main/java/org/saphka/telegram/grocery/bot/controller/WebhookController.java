package org.saphka.telegram.grocery.bot.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@RequestMapping("${bot.path}")
public class WebhookController {
    @PostMapping
    public BotApiMethod<?> handleUpdate(@RequestBody Update update) {
        var msg = new SendMessage();
        msg.setChatId(update.getMessage().getChatId());
        msg.setText("Hello, world!");
        return msg;
    }
}
