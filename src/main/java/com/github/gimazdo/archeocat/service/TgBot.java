package com.github.gimazdo.archeocat.service;

import com.github.gimazdo.archeocat.entity.User;
import com.github.gimazdo.archeocat.util.ButtonNames;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

import static com.github.gimazdo.archeocat.util.ButtonNames.*;
import static com.github.gimazdo.archeocat.util.Messages.startMessage;


@Component
@Slf4j
@RequiredArgsConstructor
public class TgBot extends TelegramLongPollingBot {

    private final RoomService roomService;
    private final UserService userService;
    private final QuestionService questionService;
    @Value("${bot.username}")
    private String username;
    @Value("${bot.token}")
    private String token;


    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage())
            return;
        log.info("Reserved new message {}", update.getMessage());
        final Long chatId = update.getMessage().getChatId();
        User user = userService.findByChatId(chatId);
        BotContext context;
        BotState state;
        if (update.getMessage().getText() != null && update.getMessage().getText().equals("/start")) {
            sendGreetingMessage(update.getMessage().getChatId().toString());
            if(user!=null) {
                user.setStateId(0);
                user.setTestId(0L);
                userService.addUser(user);
            }
            return;
        }
        if (update.getMessage().getText() != null && update.getMessage().getText().equals(TO_MAIN_PAGE)){
            user.setStateId(0);
            user.setTestId(0L);
            userService.addUser(user);
            context = BotContext.of(this, user, update,roomService,questionService, userService);
            state = BotState.byId(user.getStateId());
            state.enter(context);
            return;
        }
        if (user == null) {
            state = BotState.getInitialState();

            user = new User(chatId, state.ordinal(), 0L);
            userService.addUser(user);

            context = BotContext.of(this, user, update,roomService,questionService, userService);
            state.enter(context);

        } else {
            context = BotContext.of(this, user, update,roomService, questionService, userService);
            state = BotState.byId(user.getStateId());
        }
        state.handleInput(context);
        do {
            state = state.nextState();
            state.enter(context);
        } while (!state.isInputNeeded());
        user = userService.findByChatId(user.getChatId());
        user.setStateId(state.ordinal());

        userService.addUser(user);

    }

    private void sendGreetingMessage(String chatId) {
         SendMessage sendMessage = SendMessage.builder().chatId(chatId).text(startMessage).parseMode("HTML").replyMarkup(getKeyBoard()).build();

        try {
            this.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    private ReplyKeyboardMarkup getKeyBoard() {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        KeyboardRow keyboardButtons1 = new KeyboardRow();
        KeyboardRow keyboardButtons2 = new KeyboardRow();
        KeyboardRow keyboardButtons3 = new KeyboardRow();
        KeyboardRow keyboardButtons4 = new KeyboardRow();
        KeyboardRow keyboardButtons5 = new KeyboardRow();
        KeyboardRow keyboardButtons = new KeyboardRow();
        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        keyboardButtons1.add(INFO);
        keyboardButtons2.add(FESTIVAL_PROGRAM);
        keyboardButtons3.add(I_WANT_KNOW_MORE);
        keyboardButtons4.add(ButtonNames.ABOUT);
        keyboardButtons5.add(ButtonNames.DONATE);
        keyboardButtons.add(QUEST);
        keyboardButtons.add(ButtonNames.ROUTE);

        keyboardRowList.add(keyboardButtons2);
        keyboardRowList.add(keyboardButtons);
        keyboardRowList.add(keyboardButtons1);
        keyboardRowList.add(keyboardButtons3);
        keyboardRowList.add(keyboardButtons4);
        keyboardRowList.add(keyboardButtons5);

        keyboard.setKeyboard(keyboardRowList);
        return keyboard;
    }

}
