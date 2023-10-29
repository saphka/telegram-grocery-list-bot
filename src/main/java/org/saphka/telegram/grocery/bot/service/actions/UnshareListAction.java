package org.saphka.telegram.grocery.bot.service.actions;

import org.saphka.telegram.grocery.bot.service.GroceryListService;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
public class UnshareListAction implements Action<String, String> {

    private final GroceryListService groceryListService;

    public UnshareListAction(GroceryListService groceryListService) {
        this.groceryListService = groceryListService;
    }

    @Override
    public void execute(StateContext<String, String> context) {
        var message = (Message) context.getMessageHeader("message");
        groceryListService.removeOwner(message.getFrom().getId(), message.getUserShared().getUserId());
    }
}
