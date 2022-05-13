package com.github.gimazdo.archeocat.service;

import com.github.gimazdo.archeocat.entity.Answer;
import com.github.gimazdo.archeocat.entity.Question;
import com.github.gimazdo.archeocat.entity.Room;
import com.github.gimazdo.archeocat.entity.User;
import com.github.gimazdo.archeocat.util.ButtonNames;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

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
                    context.getUser().setTestId(0L);
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
            sendMessage(context, routeMessage, keyboard);
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },
    SHUVALOVSKY {
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
            sendMessage(context, shuvalovskyStart, keyboard);
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },
    MAIN {
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
            sendMessage(context, mainStart, keyboard);
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },
    LITE_SHUVALOVSKY(false) {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, choseLite, ReplyKeyboardMarkup.builder().clearKeyboard().build());
        }

        @Override
        public BotState nextState() {
            return START;
        }
    },
    MEDIUM_SHUVALOVSKY(false) {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, choseMedium, ReplyKeyboardMarkup.builder().clearKeyboard().build());
        }

        @Override
        public BotState nextState() {
            return START;
        }
    },
    HARD_SHUVALOVSKY(false) {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, choseHard, ReplyKeyboardMarkup.builder().clearKeyboard().build());
        }

        @Override
        public BotState nextState() {
            return START;
        }
    },
    LITE_MAIN(false) {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, choseLite, ReplyKeyboardMarkup.builder().clearKeyboard().build());
        }

        @Override
        public BotState nextState() {
            return START;
        }
    },
    MEDIUM_MAIN {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, choseMedium, ReplyKeyboardMarkup.builder().clearKeyboard().build());
        }

        @Override
        public BotState nextState() {
            return START;
        }
    },
    HARD_MAIN {
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
        BotState next;

        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Введите номер зала", ReplyKeyboardMarkup.builder().clearKeyboard().build());
        }

        @Override
        public void handleInput(BotContext context) {
            try {
                Long roomId = Long.parseLong(context.getInput().getMessage().getText());
                Room room = context.getRoomService().getById(roomId);
                String messageText;
                if (room == null) {
                    messageText = NO_INFO_ABOUT_ROOM;
                } else {
                    if (room.getImage() != null) {
                        InputFile inputFile = new InputFile(new ByteArrayInputStream(room.getImage().getImage()), "info");
                        sendPhoto(context, inputFile, ReplyKeyboardMarkup.builder().clearKeyboard().build());
                    }
                    messageText = room.getDescription();
                }
                sendMessage(context, messageText, ReplyKeyboardMarkup.builder().clearKeyboard().build());
                next = START;
            } catch (NumberFormatException e) {
                next = INFORMATION;
                sendMessage(context, "Вы ввели не число", ReplyKeyboardMarkup.builder().clearKeyboard().build());
            }

        }

        @Override
        public BotState nextState() {
            return next;
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
                    InputFile inputFile = new InputFile(BotState.class.getClassLoader().getResourceAsStream("firstFloor.jpg"), "firstFloor.jpg");
                    sendPhoto(context, inputFile, ReplyKeyboardMarkup.builder().clearKeyboard().build());
                    break;
                case THIRD_FLOOR:
                    InputFile inputFile2 = new InputFile(BotState.class.getClassLoader().getResourceAsStream("thirdFloor.jpg"), "thirdFloor.jpg");
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
    TEST {
        BotState next;
        boolean last = false;

        @Override
        public void enter(BotContext context) {
            User user = context.getUser();
            Question question = context.getQuestionService().getNext(user.getTestId());
            user.setTestId(question.getIndex());
            context.getUserService().addUser(user);
            Long lastIndex = context.getQuestionService().getLastIndex();
            if (Objects.equals(question.getIndex(), lastIndex)) {
                last = true;
            }
            if (question.getImage() != null) {
                InputFile inputFile = new InputFile(new ByteArrayInputStream(question.getImage().getImage()), "question");
                sendPhoto(context, inputFile, ReplyKeyboardMarkup.builder().clearKeyboard().build());
            }
            if(question.isTextAnswer()){
                sendMessage(context, question.getDescription());
            }
            else{
                sendMessage(context, question.getDescription(), createKeyboard(question));
            }
        }

        @Override
        public void handleInput(BotContext context) {

            User user = context.getUser();
            Question question = context.getQuestionService().getNext(user.getTestId());
            Answer answer = question.getAnswerSet().stream().filter(Answer::isCorrect).findFirst().get();
            if (context.getInput().getMessage().getText().equals(answer.getText())) {
                if (last) {
                    next = TEST_FINISH;
                } else {
                    next = TEST;
                }
                user.setTestId(user.getTestId() + 1L);
                context.getUserService().addUser(user);
                if (question.getImageWin() != null) {
                    InputFile inputFile = new InputFile(new ByteArrayInputStream(question.getImageWin().getImage()), "answer");
                    sendPhoto(context, inputFile, ReplyKeyboardMarkup.builder().clearKeyboard().build());
                }
                    sendMessage(context, question.getTextIfWin(), ReplyKeyboardMarkup.builder().clearKeyboard().build());


            } else {
                next = TEST;
                sendMessage(context, ANSWER_INCORRECT, ReplyKeyboardMarkup.builder().clearKeyboard().build());
            }

        }

        @Override
        public BotState nextState() {
            return next;
        }

        private ReplyKeyboardMarkup createKeyboard(Question question) {

            ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
            if(question.isTextAnswer()){
                keyboard.setKeyboard(new ArrayList<>());
                return keyboard;
            }
            List<KeyboardRow> keyboardRowList = new ArrayList<>();
            final KeyboardRow[] keyboardButtons = {new KeyboardRow()};
            question.getAnswerSet().forEach(answer -> {
                keyboardButtons[0].add(answer.getText());
                keyboardRowList.add(keyboardButtons[0]);
                keyboardButtons[0] = new KeyboardRow();
            });
            keyboard.setKeyboard(keyboardRowList);
            return keyboard;
        }
    },
    TEST_FINISH(false) {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, YOU_PASS_TEST, ReplyKeyboardMarkup.builder().clearKeyboard().build());
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
        message.setParseMode("HTML");
        try {
            context.getBot().execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    protected void sendMessage(BotContext context, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(context.getInput().getMessage().getChatId().toString());
        if (!(text == null || text.isEmpty()))
            message.setText(text);
        message.setParseMode("HTML");
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
