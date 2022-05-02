package com.github.gimazdo.archeocat.service;

import com.github.gimazdo.archeocat.util.ButtonNames;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.github.gimazdo.archeocat.util.ButtonNames.*;
import static com.github.gimazdo.archeocat.util.Messages.*;

public enum BotState {

    START {
        private BotState next;

        @Override
        public void handleInput(BotContext context) {
            switch (context.getInput().getMessage().getText()) {
                case INFO:
                    next = INFORMATION;
                    break;
                case I_LOST:
                    next = LOST;
                    break;
                case I_WANT_KNOW_MORE:
                    next = I_WANT_MORE;
                    break;
                case ButtonNames.ABOUT:
                    next = ABOUT;
                    break;
                case ButtonNames.DONATE:
                    next = DONATE;
                    break;
                case QUEST:
                    next = TEST;
                    break;
                case ButtonNames.ROUTE:
                    next = ROUTE;
                    break;
                default:
                    next = START;
                    sendMessage(context, "Неизвестное сообщение", getKeyBoard());
            }
        }

        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Выберите действие", getKeyBoard());
        }

        @Override
        public BotState nextState() {
            return next;
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
            keyboardButtons2.add(I_LOST);
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
    },

    ROUTE {
        BotState next;

        @Override
        public void handleInput(BotContext context) {
            switch (context.getInput().getMessage().getText()) {
                case ENTRANCE_MAIN:
                    next = MAIN;
                    break;
                case ENTRANCE_SHUVALOVSKY:
                    next = SHUVALOVSKY;
                    break;
                default:
                    next = START;
                    sendMessage(context, "Неизвестное сообщение", null);
            }
        }

        @Override
        public void enter(BotContext context) {
            ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
            KeyboardRow keyboardButtons1 = new KeyboardRow();
            KeyboardRow keyboardButtons2 = new KeyboardRow();
            List<KeyboardRow> keyboardRowList = new ArrayList<>();

            keyboardButtons1.add(ENTRANCE_MAIN);
            keyboardButtons2.add(ENTRANCE_SHUVALOVSKY);

            keyboardRowList.add(keyboardButtons1);
            keyboardRowList.add(keyboardButtons2);

            keyboard.setKeyboard(keyboardRowList);
            sendMessage(context, routeMessage,keyboard);
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },
    SHUVALOVSKY{
        BotState next;

        @Override
        public void handleInput(BotContext context) {
            switch (context.getInput().getMessage().getText()) {
                case LITE:
                    next = LITE_SHUVALOVSKY;
                    break;
                case MEDIUM:
                    next = MEDIUM_SHUVALOVSKY;
                    break;
                case HARD:
                    next = HARD_SHUVALOVSKY;
                    break;
                default:
                    next = START;
                    sendMessage(context, "Неизвестное сообщение", null);
            }
        }
        @Override
        public void enter(BotContext context) {
            ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
            KeyboardRow keyboardButtons1 = new KeyboardRow();
            KeyboardRow keyboardButtons2 = new KeyboardRow();
            List<KeyboardRow> keyboardRowList = new ArrayList<>();

            keyboardButtons1.add(LITE);
            keyboardButtons1.add(MEDIUM);
            keyboardButtons2.add(HARD);

            keyboardRowList.add(keyboardButtons1);
            keyboardRowList.add(keyboardButtons2);

            keyboard.setKeyboard(keyboardRowList);
            sendMessage(context, shuvalovskyStart,keyboard);
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },
    MAIN{
        BotState next;

        @Override
        public void handleInput(BotContext context) {
            switch (context.getInput().getMessage().getText()) {
                case LITE:
                    next = LITE_MAIN;
                    break;
                case MEDIUM:
                    next = MEDIUM_MAIN;
                    break;
                case HARD:
                    next = HARD_MAIN;
                    break;
                default:
                    next = START;
                    sendMessage(context, "Неизвестное сообщение", null);
            }
        }
        @Override
        public void enter(BotContext context) {
            ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
            KeyboardRow keyboardButtons1 = new KeyboardRow();
            KeyboardRow keyboardButtons2 = new KeyboardRow();
            List<KeyboardRow> keyboardRowList = new ArrayList<>();

            keyboardButtons1.add(LITE);
            keyboardButtons1.add(MEDIUM);
            keyboardButtons2.add(HARD);

            keyboardRowList.add(keyboardButtons1);
            keyboardRowList.add(keyboardButtons2);

            keyboard.setKeyboard(keyboardRowList);
            sendMessage(context, mainStart,keyboard);
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },
    LITE_SHUVALOVSKY(false){

        @Override
        public void enter(BotContext context) {
            sendMessage(context, choseLite, ReplyKeyboardMarkup.builder().clearKeyboard().build());
        }

        @Override
        public BotState nextState() {
            return START;
        }
    },
    MEDIUM_SHUVALOVSKY(false){

        @Override
        public void enter(BotContext context) {
            sendMessage(context, choseMedium, ReplyKeyboardMarkup.builder().clearKeyboard().build());
        }

        @Override
        public BotState nextState() {
            return START;
        }
    },
    HARD_SHUVALOVSKY(false){

        @Override
        public void enter(BotContext context) {
            sendMessage(context, choseHard, ReplyKeyboardMarkup.builder().clearKeyboard().build());
        }

        @Override
        public BotState nextState() {
            return START;
        }
    },
    LITE_MAIN(false){

        @Override
        public void enter(BotContext context) {
            sendMessage(context, choseLite, ReplyKeyboardMarkup.builder().clearKeyboard().build());
        }

        @Override
        public BotState nextState() {
            return START;
        }
    },
    MEDIUM_MAIN{
        @Override
        public void enter(BotContext context) {
            sendMessage(context, choseMedium, ReplyKeyboardMarkup.builder().clearKeyboard().build());
        }

        @Override
        public BotState nextState() {
            return START;
        }
    },
    HARD_MAIN{
        @Override
        public void enter(BotContext context) {
            sendMessage(context, choseHard, ReplyKeyboardMarkup.builder().clearKeyboard().build());
        }

        @Override
        public BotState nextState() {
            return START;
        }
    },
    INFORMATION {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Введите номер зала", ReplyKeyboardMarkup.builder().clearKeyboard().build());
        }

        @Override
        public void handleInput(BotContext context) {
            sendMessage(context, "Не реализовано", ReplyKeyboardMarkup.builder().clearKeyboard().build());
        }

        @Override
        public BotState nextState() {
            return START;
        }
    },
    LOST {
        @Override
        public void enter(BotContext context) {
            ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
            KeyboardRow keyboardButtons1 = new KeyboardRow();
            KeyboardRow keyboardButtons2 = new KeyboardRow();
            List<KeyboardRow> keyboardRowList = new ArrayList<>();

            keyboardButtons1.add(FIRST_FLOOR);
            keyboardButtons2.add(THIRD_FLOOR);

            keyboardRowList.add(keyboardButtons1);
            keyboardRowList.add(keyboardButtons2);

            keyboard.setKeyboard(keyboardRowList);
            sendMessage(context, "На каком вы этаже?", keyboard);
        }

        @Override
        public void handleInput(BotContext context) {

            switch (context.getInput().getMessage().getText()) {
                case FIRST_FLOOR:
                    InputFile inputFile = new InputFile(new File("E:\\MyProjects\\archeocat\\src\\main\\resources\\shema-ermitazha-1-etazh.jpg"));
                    sendPhoto(context, inputFile, ReplyKeyboardMarkup.builder().clearKeyboard().build());
                    break;
                case THIRD_FLOOR:
                    InputFile inputFile2 = new InputFile(new File("E:\\MyProjects\\archeocat\\src\\main\\resources\\karta-ermitazha-3-etazh.jpg"));
                    sendPhoto(context, inputFile2, ReplyKeyboardMarkup.builder().clearKeyboard().build());
                    break;
                default:
                    sendMessage(context, "Нет такого этажа", ReplyKeyboardMarkup.builder().clearKeyboard().build());
            }

        }

        @Override
        public BotState nextState() {
            return START;
        }
    },
    I_WANT_MORE(false) {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Не реализовано", ReplyKeyboardMarkup.builder().clearKeyboard().build());
        }

        @Override
        public BotState nextState() {
            return START;
        }
    },
    TEST(false) {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Не реализовано", ReplyKeyboardMarkup.builder().clearKeyboard().build());
        }

        @Override
        public BotState nextState() {
            return START;
        }
    },
    ABOUT {
        @Override
        public void enter(BotContext context) {
            ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
            KeyboardRow keyboardButtons1 = new KeyboardRow();
            KeyboardRow keyboardButtons2 = new KeyboardRow();
            List<KeyboardRow> keyboardRowList = new ArrayList<>();

            keyboardButtons1.add(HERMITAGE_TEAM);
            keyboardButtons2.add(HSE_TEAM);

            keyboardRowList.add(keyboardButtons1);
            keyboardRowList.add(keyboardButtons2);

            keyboard.setKeyboard(keyboardRowList);
            sendMessage(context, "Немного о людях. \n Выберете о чём вы хотите узнать подробнее", keyboard);
        }

        @Override
        public void handleInput(BotContext context) {
            switch (context.getInput().getMessage().getText()) {
                case HERMITAGE_TEAM:
                    sendMessage(context, "Тут будет о людях", ReplyKeyboardMarkup.builder().clearKeyboard().build());
                    break;
                case HSE_TEAM:
                    sendMessage(context, "Тут будет о людях", ReplyKeyboardMarkup.builder().clearKeyboard().build());
                    break;
                default:
                    sendMessage(context, "Нет такой кнопки", ReplyKeyboardMarkup.builder().clearKeyboard().build());
            }

        }

        @Override
        public BotState nextState() {
            return START;
        }
    },
    DONATE(false) {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Платить ей https://vk.com/makarushinalena", ReplyKeyboardMarkup.builder().clearKeyboard().build());
        }

        @Override
        public BotState nextState() {
            return START;
        }
    };

    private static BotState[] states;
    private final boolean inputNeeded;

    BotState() {
        this.inputNeeded = true;
    }

    BotState(boolean inputNeeded) {
        this.inputNeeded = inputNeeded;
    }

    public static BotState getInitialState() {
        return byId(0);
    }

    public static BotState byId(int id) {
        if (states == null) {
            states = BotState.values();
        }
        return states[id];
    }

    protected void sendMessage(BotContext context, String text, ReplyKeyboardMarkup keyboard) {
        SendMessage message = new SendMessage();
        message.setChatId(context.getInput().getMessage().getChatId().toString());
        if (!(text == null || text.isEmpty()))
            message.setText(text);
        message.setReplyMarkup(keyboard);

        try {
            context.getBot().execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    protected void sendPhoto(BotContext context, InputFile photo, ReplyKeyboardMarkup keyboard) {
        SendPhoto message = new SendPhoto();
        message.setChatId(context.getInput().getMessage().getChatId().toString());
        message.setPhoto(photo);
        message.setReplyMarkup(keyboard);

        try {
            context.getBot().execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    public boolean isInputNeeded() {
        return inputNeeded;
    }

    public void handleInput(BotContext context) {
        // do nothing by default
    }

    public abstract void enter(BotContext context);

    public abstract BotState nextState();
}
