package org.saphka.telegram.grocery.bot.service.actions;

import org.saphka.telegram.grocery.bot.service.GroceryListService;
import org.saphka.telegram.grocery.bot.service.TelegramService;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class ListProductsAction implements Action<String, String> {

    private final GroceryListService groceryListService;
    private final TelegramService telegramService;

    public ListProductsAction(GroceryListService groceryListService, TelegramService telegramService) {
        this.groceryListService = groceryListService;
        this.telegramService = telegramService;
    }

    @Override
    public void execute(StateContext<String, String> context) {
        var message = (Message) context.getMessageHeader("message");
        var msg = new SendMessage();
        msg.setChatId(message.getChatId());
        msg.setText(String.join("\n",
                groceryListService.getOrCreateList(message.getFrom().getId()).products()));
        try {
            telegramService.execute(msg);
        } catch (TelegramApiException e) {
            throw new IllegalStateException(e);
        }
    }
}
