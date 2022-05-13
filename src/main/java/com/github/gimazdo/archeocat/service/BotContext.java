package com.github.gimazdo.archeocat.service;

import com.github.gimazdo.archeocat.entity.User;
import org.telegram.telegrambots.meta.api.objects.Update;

public class BotContext {
    private final RoomService roomService;
    private final QuestionService questionService;
    private final UserService userService;
    private final TgBot bot;
    private final Update input;
    private final User user;

    public static BotContext of(TgBot bot, User user, Update text, RoomService roomService, QuestionService questionService, UserService userService) {
        return new BotContext(bot, user, text, roomService, questionService, userService);
    }

    private BotContext(TgBot bot, User user, Update input, RoomService roomService, QuestionService questionService, UserService userService) {
        this.bot = bot;
        this.input = input;
        this.user = user;
        this.roomService = roomService;
        this.questionService = questionService;
        this.userService = userService;
    }

    public TgBot getBot() {
        return bot;
    }

    public Update getInput() {
        return input;
    }

    public RoomService getRoomService() {
        return roomService;
    }

    public User getUser() {
        return user;
    }

    public QuestionService getQuestionService() {
        return questionService;
    }

    public UserService getUserService() {
        return userService;
    }
}
