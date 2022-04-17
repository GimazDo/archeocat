package com.github.gimazdo.archeocat.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public enum BotState {

    START {
        private BotState next;

        @Override
        public void handleInput(BotContext context) {
            switch (context.getInput().getMessage().getText()) {
                case "ИНФО":
                    next = INFORMATION;
                    break;
                case "Я ПОТЕРЯЛСЯ":
                    next = LOST;
                    break;
                case "Я ХОЧУ ЗНАТЬ БОЛЬШЕ":
                    next = I_WANT_MORE;
                    break;
                case "О ФЕСТИВАЛЕ":
                    next = ABOUT;
                    break;
                case "ДОНАТЫ":
                    next = DONATE;
                    break;
                case "ТЕСТ":
                    next = TEST;
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

            keyboardButtons1.add("ИНФО");
            keyboardButtons2.add("Я ПОТЕРЯЛСЯ");
            keyboardButtons3.add("Я ХОЧУ ЗНАТЬ БОЛЬШЕ");
            keyboardButtons4.add("О ФЕСТИВАЛЕ");
            keyboardButtons5.add("ДОНАТЫ");
            keyboardButtons.add("ТЕСТ");

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

            keyboardButtons1.add("1");
            keyboardButtons2.add("3");

            keyboardRowList.add(keyboardButtons1);
            keyboardRowList.add(keyboardButtons2);

            keyboard.setKeyboard(keyboardRowList);
            sendMessage(context, "На каком вы этаже?", keyboard);
        }

        @Override
        public void handleInput(BotContext context) {

            switch (context.getInput().getMessage().getText()) {
                case "1":
                    InputFile inputFile = new InputFile(new File("E:\\MyProjects\\archeocat\\src\\main\\resources\\shema-ermitazha-1-etazh.jpg"));
                    sendPhoto(context, inputFile, ReplyKeyboardMarkup.builder().clearKeyboard().build());
                    break;
                case "3":
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
        public void handleInput(BotContext context) {
            // do nothing by default
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

            keyboardButtons1.add("Команда Эрмитажа");
            keyboardButtons2.add("Команда нашего проекта ВШЭ");

            keyboardRowList.add(keyboardButtons1);
            keyboardRowList.add(keyboardButtons2);

            keyboard.setKeyboard(keyboardRowList);
            sendMessage(context, "Здесь какая-то вводная инфа", keyboard);
        }

        @Override
        public void handleInput(BotContext context) {
            switch (context.getInput().getMessage().getText()) {
                case "Команда Эрмитажа":
                    sendMessage(context, "Тут будет о людях", ReplyKeyboardMarkup.builder().clearKeyboard().build());
                    break;
                case "Команда нашего проекта ВШЭ":
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
