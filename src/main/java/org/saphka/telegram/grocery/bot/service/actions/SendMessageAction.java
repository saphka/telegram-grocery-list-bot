package org.saphka.telegram.grocery.bot.service.actions;

import org.saphka.telegram.grocery.bot.config.StateMachineEvents;
import org.saphka.telegram.grocery.bot.config.StateMachineStates;
import org.saphka.telegram.grocery.bot.service.TelegramService;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class SendMessageAction implements Action<String, String> {

    private final TelegramService telegramService;

    public SendMessageAction(TelegramService telegramService) {
        this.telegramService = telegramService;
    }

    @Override
    public void execute(StateContext<String, String> context) {
        var text = getMessageText(context);
        if (text != null) {
            var message = (Message) context.getMessageHeader("message");
            var msg = new SendMessage();
            msg.setChatId(message.getChatId());
            msg.setText(text);
            var removeKeyboard = new ReplyKeyboardRemove();
            removeKeyboard.setRemoveKeyboard(true);
            msg.setReplyMarkup(removeKeyboard);
            try {
                telegramService.execute(msg);
            } catch (TelegramApiException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    private String getMessageText(StateContext<String, String> context) {
        return switch (context.getTarget().getId()) {
            case StateMachineStates.PRODUCT_ADD, StateMachineStates.PRODUCT_REMOVE -> "Enter products";
            case StateMachineStates.READY -> switch (context.getSource().getId()) {
                case StateMachineStates.PRODUCT_ADD -> "Products added";
                case StateMachineStates.PRODUCT_REMOVE -> "Products removed";
                case StateMachineStates.SHARE_PENDING -> "Grocery list shared";
                case StateMachineStates.UNSHARE_PENDING -> "Grocery list unshared";
                case StateMachineStates.READY -> switch (context.getEvent()) {
                    case StateMachineEvents.COMMAND_LIST -> "Here is your list";
                    case StateMachineEvents.COMMAND_CLEAR -> "List cleared";
                    default -> null;
                };
                default -> null;
            };
            default -> null;
        };
    }
}
