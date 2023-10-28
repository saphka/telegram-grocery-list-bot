package org.saphka.telegram.grocery.bot.service.actions;

import org.saphka.telegram.grocery.bot.service.TelegramService;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButtonRequestUser;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Service
public class RequestUserAction implements Action<String, String> {

    private final TelegramService telegramService;

    public RequestUserAction(TelegramService telegramService) {
        this.telegramService = telegramService;
    }

    @Override
    public void execute(StateContext<String, String> context) {
        var message = (Message) context.getMessageHeader("message");

        var msg = new SendMessage();
        msg.setChatId(message.getChatId());
        msg.setText("Please select user to share list with");

        var shareButton = new KeyboardButton();
        shareButton.setText("Select user");

        var requestUserParams = new KeyboardButtonRequestUser();
        requestUserParams.setRequestId("1");
        requestUserParams.setUserIsBot(false);
        shareButton.setRequestUser(requestUserParams);

        var keyboardRow = new KeyboardRow();
        keyboardRow.add(shareButton);

        var replyMarkup = new ReplyKeyboardMarkup();
        replyMarkup.setKeyboard(List.of(keyboardRow));
        msg.setReplyMarkup(replyMarkup);

        try {
            telegramService.execute(msg);
        } catch (TelegramApiException e) {
            throw new IllegalStateException(e);
        }
    }
}
