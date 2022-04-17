package com.github.gimazdo.archeocat.service;

import com.github.gimazdo.archeocat.entity.User;
import org.telegram.telegrambots.meta.api.objects.Update;

public class BotContext {
    private final TgBot bot;
    private final Update input;
    private final User user;
    public static BotContext of(TgBot bot, User user, Update text) {
        return new BotContext(bot, user,text);
    }

    private BotContext(TgBot bot, User user, Update input) {
        this.bot = bot;
        this.input = input;
        this.user = user;
    }

    public TgBot getBot() {
        return bot;
    }

    public Update getInput() {
        return input;
    }
}
