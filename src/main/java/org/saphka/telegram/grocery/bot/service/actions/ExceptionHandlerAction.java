package org.saphka.telegram.grocery.bot.service.actions;

import org.saphka.telegram.grocery.bot.service.TelegramService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
public class ExceptionHandlerAction implements Action<String, String> {

    private static final Logger log = LoggerFactory.getLogger(ExceptionHandlerAction.class);

    private final TelegramService telegramService;

    public ExceptionHandlerAction(TelegramService telegramService) {
        this.telegramService = telegramService;
    }

    @Override
    public void execute(StateContext<String, String> context) {
        Exception e = context.getException();
        if (e != null) {
            log.error("Error occurred when processing update {}", context.getMessageHeader("update"), e);

            var message = (Message) context.getMessageHeader("message");
            SendMessage msg = new SendMessage();
            msg.setChatId(message.getChatId());
            msg.setText("Unknown error occurred. Try again later");
            telegramService.executeAsync(msg);
        }
    }
}
