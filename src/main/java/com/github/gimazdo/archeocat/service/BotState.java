package com.github.gimazdo.archeocat.service;

import com.github.gimazdo.archeocat.entity.Answer;
import com.github.gimazdo.archeocat.entity.Question;
import com.github.gimazdo.archeocat.entity.Room;
import com.github.gimazdo.archeocat.entity.User;
import com.github.gimazdo.archeocat.util.ButtonNames;
import com.github.gimazdo.archeocat.util.Messages;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
                case FESTIVAL_PROGRAM:
                    next = PROGRAM;
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
    },
    ROUTE {
        BotState next;

        @Override
        public void handleInput(BotContext context) {
            switch (context.getInput().getMessage().getText()) {
                case ROUTE_ENTRANCE_MAIN_BUTTON:
                    next = MAIN;
                    break;
                case ROUTE_ENTRANCE_SHUVALOVSKY_BUTTON:
                    next = SHUVALOVSKY;
                    break;
                case MAP_BUTTON:
                    InputFile inputFile = new InputFile(BotState.class.getClassLoader().getResourceAsStream("Map.png"), "cap.png");
                    sendPhoto(context, inputFile, ReplyKeyboardMarkup.builder().build());
                    next = START;
                    break;
                default:
                    next = START;
                    sendMessage(context, "Неизвестное сообщение");
            }
        }

        @Override
        public void enter(BotContext context) {
            ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
            KeyboardRow keyboardButtons1 = new KeyboardRow();
            KeyboardRow keyboardButtons2 = new KeyboardRow();
            KeyboardRow keyboardButtons3 = new KeyboardRow();
            KeyboardRow keyboardButtons4 = new KeyboardRow();
            List<KeyboardRow> keyboardRowList = new ArrayList<>();

            keyboardButtons3.add(MAP_BUTTON);
            keyboardButtons1.add(ROUTE_ENTRANCE_MAIN_BUTTON);
            keyboardButtons2.add(ROUTE_ENTRANCE_SHUVALOVSKY_BUTTON);
            keyboardButtons4.add(TO_MAIN_PAGE);

            keyboardRowList.add(keyboardButtons3);
            keyboardRowList.add(keyboardButtons1);
            keyboardRowList.add(keyboardButtons2);
            keyboardRowList.add(keyboardButtons4);
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
                case QUEST_ROUTE_BUTTON:
                    next = MEDIUM_SHUVALOVSKY;
                    break;
                case HARD:
                    next = HARD_SHUVALOVSKY;
                    break;
                case BACK:
                    next = ROUTE;
                    break;
                default:
                    next = START;
                    sendMessage(context, "Неизвестное сообщение");
            }
        }

        @Override
        public void enter(BotContext context) {
            ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
            KeyboardRow keyboardButtons1 = new KeyboardRow();
            KeyboardRow keyboardButtons2 = new KeyboardRow();
            KeyboardRow keyboardButtons3 = new KeyboardRow();
            List<KeyboardRow> keyboardRowList = new ArrayList<>();

            keyboardButtons1.add(LITE);
            keyboardButtons1.add(HARD);
            keyboardButtons2.add(QUEST_ROUTE_BUTTON);
            keyboardButtons3.add(BACK);

            keyboardRowList.add(keyboardButtons2);
            keyboardRowList.add(keyboardButtons1);
            keyboardRowList.add(keyboardButtons3);

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
                    next = LITE_MAIN_START;
                    break;
                case QUEST_ROUTE_BUTTON:
                    next = QUEST_ROUTE_1;
                    break;
                case HARD:
                    next = HARD_MAIN_START;
                    break;
                case BACK:
                    next = ROUTE;
                    break;
                default:
                    next = START;
                    sendMessage(context, "Неизвестное сообщение");
            }
        }

        @Override
        public void enter(BotContext context) {
            ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
            KeyboardRow keyboardButtons1 = new KeyboardRow();
            KeyboardRow keyboardButtons2 = new KeyboardRow();
            KeyboardRow keyboardButtons3 = new KeyboardRow();
            List<KeyboardRow> keyboardRowList = new ArrayList<>();

            keyboardButtons1.add(LITE);
            keyboardButtons1.add(HARD);
            keyboardButtons2.add(QUEST_ROUTE_BUTTON);
            keyboardButtons3.add(TO_MAIN_PAGE);

            keyboardRowList.add(keyboardButtons2);
            keyboardRowList.add(keyboardButtons1);
            keyboardRowList.add(keyboardButtons3);

            keyboard.setKeyboard(keyboardRowList);
            sendMessage(context, mainStart, keyboard);
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },
    LITE_SHUVALOVSKY() {
        BotState next;

        @Override
        public void enter(BotContext context) {
            ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
            KeyboardRow keyboardButtons1 = new KeyboardRow();
            KeyboardRow keyboardButtons2 = new KeyboardRow();
            List<KeyboardRow> keyboardRowList = new ArrayList<>();

            keyboardButtons1.add(NEXT_BUTTON);
            keyboardButtons2.add(TO_MAIN_PAGE);

            keyboardRowList.add(keyboardButtons1);
            keyboardRowList.add(keyboardButtons2);

            keyboard.setKeyboard(keyboardRowList);
            sendMessage(context, LITE_SHUVALOVSKY_START_MESSAGE, keyboard);
        }

        @Override
        public void handleInput(BotContext context) {
            switch (context.getInput().getMessage().getText()) {
                case NEXT_BUTTON:
                    next = LITE_SHUVALOVSKY_2;
                    break;
                default:
                    next = START;
                    sendMessage(context, "Неизвестное сообщение");
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },
    LITE_SHUVALOVSKY_2() {
        BotState next;

        @Override
        public void enter(BotContext context) {
            ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
            KeyboardRow keyboardButtons1 = new KeyboardRow();
            KeyboardRow keyboardButtons2 = new KeyboardRow();
            List<KeyboardRow> keyboardRowList = new ArrayList<>();

            keyboardButtons1.add(NEXT_BUTTON);
            keyboardButtons2.add(TO_MAIN_PAGE);

            keyboardRowList.add(keyboardButtons1);
            keyboardRowList.add(keyboardButtons2);

            keyboard.setKeyboard(keyboardRowList);
            sendMessage(context, LITE_SHUVALOVSKY_2_MESSAGE, keyboard);
        }

        @Override
        public void handleInput(BotContext context) {
            switch (context.getInput().getMessage().getText()) {
                case NEXT_BUTTON:
                    next = LITE_SHUVALOVSKY_3;
                    break;
                default:
                    next = START;
                    sendMessage(context, "Неизвестное сообщение");
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },
    LITE_SHUVALOVSKY_3() {
        BotState next;

        @Override
        public void enter(BotContext context) {
            ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
            KeyboardRow keyboardButtons1 = new KeyboardRow();
            KeyboardRow keyboardButtons2 = new KeyboardRow();
            List<KeyboardRow> keyboardRowList = new ArrayList<>();

            keyboardButtons1.add(NEXT_BUTTON);
            keyboardButtons2.add(TO_MAIN_PAGE);

            keyboardRowList.add(keyboardButtons1);
            keyboardRowList.add(keyboardButtons2);

            keyboard.setKeyboard(keyboardRowList);
            sendMessage(context, LITE_SHUVALOVSKY_3_MESSAGE, keyboard);
        }

        @Override
        public void handleInput(BotContext context) {
            switch (context.getInput().getMessage().getText()) {
                case NEXT_BUTTON:
                    next = LITE_SHUVALOVSKY_4;
                    break;
                default:
                    next = START;
                    sendMessage(context, "Неизвестное сообщение");
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },
    LITE_SHUVALOVSKY_4() {
        BotState next;

        @Override
        public void enter(BotContext context) {
            ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
            KeyboardRow keyboardButtons1 = new KeyboardRow();
            KeyboardRow keyboardButtons2 = new KeyboardRow();
            List<KeyboardRow> keyboardRowList = new ArrayList<>();

            keyboardButtons1.add(NEXT_BUTTON);
            keyboardButtons2.add(TO_MAIN_PAGE);

            keyboardRowList.add(keyboardButtons1);
            keyboardRowList.add(keyboardButtons2);

            keyboard.setKeyboard(keyboardRowList);
            sendMessage(context, LITE_SHUVALOVSKY_4_MESSAGE, keyboard);
        }

        @Override
        public void handleInput(BotContext context) {
            switch (context.getInput().getMessage().getText()) {
                case NEXT_BUTTON:
                    next = LITE_SHUVALOVSKY_5;
                    break;
                default:
                    next = START;
                    sendMessage(context, "Неизвестное сообщение");
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },
    LITE_SHUVALOVSKY_5(false) {
        BotState next;

        @Override
        public void enter(BotContext context) {
            sendMessage(context, LITE_SHUVALOVSKY_5_MESSAGE);
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
    HARD_SHUVALOVSKY() {
        BotState next;

        @Override
        public void enter(BotContext context) {
            ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
            KeyboardRow keyboardButtons1 = new KeyboardRow();
            KeyboardRow keyboardButtons2 = new KeyboardRow();
            KeyboardRow keyboardButtons3 = new KeyboardRow();
            KeyboardRow keyboardButtons4 = new KeyboardRow();
            List<KeyboardRow> keyboardRowList = new ArrayList<>();

            keyboardButtons1.add(FIRST_FLOOR);
            keyboardButtons2.add(SECOND_FLOOR);
            keyboardButtons3.add(THIRD_FLOOR);
            keyboardButtons4.add(ROUTES);

            keyboardRowList.add(keyboardButtons1);
            keyboardRowList.add(keyboardButtons2);
            keyboardRowList.add(keyboardButtons3);
            keyboardRowList.add(keyboardButtons4);

            keyboard.setKeyboard(keyboardRowList);
            sendMessage(context, SHUVALOVSKY_HARD_START_MESSAGE, keyboard);
        }

        @Override
        public void handleInput(BotContext context) {
            switch (context.getInput().getMessage().getText()) {
                case FIRST_FLOOR:
                    next = FIRST_FLOOR_SHUVALOVSKY;
                    break;
                case SECOND_FLOOR:
                    next = SECOND_FLOOR_SHUVALOVSKY;
                    break;
                case THIRD_FLOOR:
                    next = THIRD_FLOOR_SHUVALOVSKY;
                    break;
                case ROUTES:
                    next = ROUTE;
            }

        }

        @Override
        public BotState nextState() {
            return next;
        }
    },
    FIRST_FLOOR_SHUVALOVSKY() {
        BotState next;

        @Override
        public void enter(BotContext context) {
            ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
            KeyboardRow keyboardButtons1 = new KeyboardRow();
            KeyboardRow keyboardButtons2 = new KeyboardRow();
            KeyboardRow keyboardButtons3 = new KeyboardRow();
            List<KeyboardRow> keyboardRowList = new ArrayList<>();

            keyboardButtons1.add(FIRST_FLOOR_NEW_HERMITAGE_BUTTON);
            keyboardButtons2.add(FIRST_FLOOR_WINTER_CASTLE_BUTTON);
            keyboardButtons3.add(BACK);

            keyboardRowList.add(keyboardButtons1);
            keyboardRowList.add(keyboardButtons2);
            keyboardRowList.add(keyboardButtons3);

            keyboard.setKeyboard(keyboardRowList);
            sendMessage(context, "Выберите действие", keyboard);
        }

        @Override
        public void handleInput(BotContext context) {
            switch (context.getInput().getMessage().getText()) {
                case FIRST_FLOOR_NEW_HERMITAGE_BUTTON:
                    next = FIRST_FLOOR_NEW_HERMITAGE;
                    break;
                case FIRST_FLOOR_WINTER_CASTLE_BUTTON:
                    next = FIRST_FLOOR_WINTER_CASTLE;
                    break;
                case BACK:

                default:
                    next = START;
            }

        }

        @Override
        public BotState nextState() {
            return next;
        }
    },
    FIRST_FLOOR_NEW_HERMITAGE(false) {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, FIRST_FLOOR_NEW_HERMITAGE_MESSAGE);
        }

        @Override
        public BotState nextState() {
            return START;
        }
    },
    FIRST_FLOOR_WINTER_CASTLE(false) {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, FIRST_FLOOR_WINTER_CASTLE_MESSAGE);
        }

        @Override
        public BotState nextState() {
            return START;
        }
    },
    SECOND_FLOOR_SHUVALOVSKY(false) {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, SECOND_FLOOR_MESSAGE_SHUVALOVSKY);
        }

        @Override
        public BotState nextState() {
            return START;
        }
    },
    THIRD_FLOOR_SHUVALOVSKY(false) {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, THIRD_FLOOR_MESSAGE_SHUVALOVSKY);
        }

        @Override
        public BotState nextState() {
            return START;
        }
    },
    QUEST_ROUTE_1() {
        BotState next;

        @Override
        public void enter(BotContext context) {
            ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
            KeyboardRow keyboardButtons1 = new KeyboardRow();
            KeyboardRow keyboardButtons2 = new KeyboardRow();
            List<KeyboardRow> keyboardRowList = new ArrayList<>();

            keyboardButtons1.add(NEXT_BUTTON);
            keyboardButtons2.add(TO_MAIN_PAGE);

            keyboardRowList.add(keyboardButtons1);
            keyboardRowList.add(keyboardButtons2);

            keyboard.setKeyboard(keyboardRowList);
            sendMessage(context, QUEST_ROUTE_1_MESSAGE, keyboard);
        }

        @Override
        public void handleInput(BotContext context) {
            switch (context.getInput().getMessage().getText()) {
                case NEXT_BUTTON:
                    next = QUEST_ROUTE_2;
                    break;
                default:
                    next = START;
                    sendMessage(context, "Неизвестное сообщение");
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },
    QUEST_ROUTE_2() {
        BotState next;

        @Override
        public void enter(BotContext context) {
            ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
            KeyboardRow keyboardButtons1 = new KeyboardRow();
            KeyboardRow keyboardButtons2 = new KeyboardRow();
            List<KeyboardRow> keyboardRowList = new ArrayList<>();

            keyboardButtons1.add(NEXT_BUTTON);
            keyboardButtons2.add(TO_MAIN_PAGE);

            keyboardRowList.add(keyboardButtons1);
            keyboardRowList.add(keyboardButtons2);

            keyboard.setKeyboard(keyboardRowList);
            sendMessage(context, QUEST_ROUTE_2_MESSAGE, keyboard);
        }

        @Override
        public void handleInput(BotContext context) {
            switch (context.getInput().getMessage().getText()) {
                case NEXT_BUTTON:
                    next = QUEST_ROUTE_3;
                    break;
                default:
                    next = START;
                    sendMessage(context, "Неизвестное сообщение");
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },
    QUEST_ROUTE_3() {
        BotState next;

        @Override
        public void enter(BotContext context) {
            ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
            KeyboardRow keyboardButtons1 = new KeyboardRow();
            KeyboardRow keyboardButtons2 = new KeyboardRow();
            List<KeyboardRow> keyboardRowList = new ArrayList<>();

            keyboardButtons1.add(NEXT_BUTTON);
            keyboardButtons2.add(TO_MAIN_PAGE);

            keyboardRowList.add(keyboardButtons1);
            keyboardRowList.add(keyboardButtons2);

            keyboard.setKeyboard(keyboardRowList);
            sendMessage(context, QUEST_ROUTE_3_MESSAGE, keyboard);
        }

        @Override
        public void handleInput(BotContext context) {
            switch (context.getInput().getMessage().getText()) {
                case NEXT_BUTTON:
                    next = QUEST_ROUTE_4;
                    break;
                default:
                    next = START;
                    sendMessage(context, "Неизвестное сообщение");
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },
    QUEST_ROUTE_4(false) {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, QUEST_ROUTE_4_MESSAGE);
        }

        @Override
        public BotState nextState() {
            return START;
        }
    },
    LITE_MAIN_START {
        BotState next;

        @Override
        public void enter(BotContext context) {
            ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
            KeyboardRow keyboardButtons1 = new KeyboardRow();
            KeyboardRow keyboardButtons2 = new KeyboardRow();
            List<KeyboardRow> keyboardRowList = new ArrayList<>();

            keyboardButtons1.add(START_BUTTON);
            keyboardButtons2.add(BACK);

            keyboardRowList.add(keyboardButtons1);
            keyboardRowList.add(keyboardButtons2);

            keyboard.setKeyboard(keyboardRowList);
            sendMessage(context, MAIN_LITE_START_MESSAGE, keyboard);
        }

        @Override
        public void handleInput(BotContext context) {
            switch (context.getInput().getMessage().getText()) {
                case START_BUTTON:
                    next = LITE_MAIN_1;
                    break;
                case BACK:
                    next = MAIN;
                    break;
                default:
                    next = START;
                    sendMessage(context, "Неизвестное сообщение");
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },
    LITE_MAIN_1() {
        BotState next;

        @Override
        public void enter(BotContext context) {
            ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
            KeyboardRow keyboardButtons1 = new KeyboardRow();
            KeyboardRow keyboardButtons2 = new KeyboardRow();
            List<KeyboardRow> keyboardRowList = new ArrayList<>();

            keyboardButtons1.add(NEXT_BUTTON);
            keyboardButtons2.add(TO_MAIN_PAGE);

            keyboardRowList.add(keyboardButtons1);
            keyboardRowList.add(keyboardButtons2);

            keyboard.setKeyboard(keyboardRowList);
            sendMessage(context, MAIN_LITE_1_MESSAGE, keyboard);
        }

        @Override
        public void handleInput(BotContext context) {
            switch (context.getInput().getMessage().getText()) {
                case NEXT_BUTTON:
                    next = LITE_MAIN_2;
                    break;
                default:
                    next = START;
                    sendMessage(context, "Неизвестное сообщение");
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },
    LITE_MAIN_2() {
        BotState next;

        @Override
        public void enter(BotContext context) {
            ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
            KeyboardRow keyboardButtons1 = new KeyboardRow();
            KeyboardRow keyboardButtons2 = new KeyboardRow();
            List<KeyboardRow> keyboardRowList = new ArrayList<>();

            keyboardButtons1.add(NEXT_BUTTON);
            keyboardButtons2.add(TO_MAIN_PAGE);

            keyboardRowList.add(keyboardButtons1);
            keyboardRowList.add(keyboardButtons2);

            keyboard.setKeyboard(keyboardRowList);
            sendMessage(context, MAIN_LITE_2_MESSAGE, keyboard);
        }

        @Override
        public void handleInput(BotContext context) {
            switch (context.getInput().getMessage().getText()) {
                case NEXT_BUTTON:
                    next = LITE_MAIN_3;
                    break;
                default:
                    next = START;
                    sendMessage(context, "Неизвестное сообщение");
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },
    LITE_MAIN_3(false) {
        BotState next;

        @Override
        public void enter(BotContext context) {
            sendMessage(context, MAIN_LITE_3_MESSAGE);
        }


        @Override
        public BotState nextState() {
            return START;
        }
    },
    HARD_MAIN_START {
        BotState next;

        @Override
        public void enter(BotContext context) {
            ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
            KeyboardRow keyboardButtons1 = new KeyboardRow();
            KeyboardRow keyboardButtons2 = new KeyboardRow();
            List<KeyboardRow> keyboardRowList = new ArrayList<>();

            keyboardButtons1.add(START_BUTTON);
            keyboardButtons2.add(BACK);

            keyboardRowList.add(keyboardButtons1);
            keyboardRowList.add(keyboardButtons2);

            keyboard.setKeyboard(keyboardRowList);
            sendMessage(context, MAIN_HARD_START_MESSAGE, keyboard);
        }

        @Override
        public void handleInput(BotContext context) {
            switch (context.getInput().getMessage().getText()) {
                case START_BUTTON:
                    next = HARD_MAIN_1;
                    break;
                case BACK:
                    next = MAIN;
                    break;
                default:
                    next = START;
                    sendMessage(context, "Неизвестное сообщение");
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },
    HARD_MAIN_1 {
        BotState next;

        @Override
        public void enter(BotContext context) {
            ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
            KeyboardRow keyboardButtons1 = new KeyboardRow();
            KeyboardRow keyboardButtons2 = new KeyboardRow();
            List<KeyboardRow> keyboardRowList = new ArrayList<>();

            keyboardButtons1.add(NEXT_BUTTON);
            keyboardButtons2.add(TO_MAIN_PAGE);

            keyboardRowList.add(keyboardButtons1);
            keyboardRowList.add(keyboardButtons2);

            keyboard.setKeyboard(keyboardRowList);
            sendMessage(context, MAIN_HARD_1_MESSAGE, keyboard);
        }

        @Override
        public void handleInput(BotContext context) {
            switch (context.getInput().getMessage().getText()) {
                case NEXT_BUTTON:
                    next = HARD_MAIN_2;
                    break;
                default:
                    next = START;
                    sendMessage(context, "Неизвестное сообщение");
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },
    HARD_MAIN_2 {
        BotState next;

        @Override
        public void enter(BotContext context) {
            ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
            KeyboardRow keyboardButtons1 = new KeyboardRow();
            KeyboardRow keyboardButtons2 = new KeyboardRow();
            List<KeyboardRow> keyboardRowList = new ArrayList<>();

            keyboardButtons1.add(NEXT_BUTTON);
            keyboardButtons2.add(TO_MAIN_PAGE);

            keyboardRowList.add(keyboardButtons1);
            keyboardRowList.add(keyboardButtons2);

            keyboard.setKeyboard(keyboardRowList);
            sendMessage(context, MAIN_HARD_2_MESSAGE, keyboard);
        }

        @Override
        public void handleInput(BotContext context) {
            switch (context.getInput().getMessage().getText()) {
                case NEXT_BUTTON:
                    next = HARD_MAIN_3;
                    break;
                default:
                    next = START;
                    sendMessage(context, "Неизвестное сообщение");
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },
    HARD_MAIN_3 {
        BotState next;

        @Override
        public void enter(BotContext context) {
            ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
            KeyboardRow keyboardButtons1 = new KeyboardRow();
            KeyboardRow keyboardButtons2 = new KeyboardRow();
            List<KeyboardRow> keyboardRowList = new ArrayList<>();

            keyboardButtons1.add(NEXT_BUTTON);
            keyboardButtons2.add(TO_MAIN_PAGE);

            keyboardRowList.add(keyboardButtons1);
            keyboardRowList.add(keyboardButtons2);

            keyboard.setKeyboard(keyboardRowList);
            sendMessage(context, MAIN_HARD_3_MESSAGE, keyboard);
        }

        @Override
        public void handleInput(BotContext context) {
            switch (context.getInput().getMessage().getText()) {
                case NEXT_BUTTON:
                    next = HARD_MAIN_4;
                    break;
                default:
                    next = START;
                    sendMessage(context, "Неизвестное сообщение");
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },
    HARD_MAIN_4(false) {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, MAIN_HARD_4_MESSAGE);
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
                Optional<Room> roomopt = context.getRoomService().getById(roomId);
                String messageText;
                if (roomopt.isEmpty()) {
                    messageText = NO_INFO_ABOUT_ROOM;
                } else {
                    Room room = roomopt.get();
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
    PROGRAM {
        @Override
        public void enter(BotContext context) {
            ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
            KeyboardRow keyboardButtons1 = new KeyboardRow();
            KeyboardRow keyboardButtons2 = new KeyboardRow();
            List<KeyboardRow> keyboardRowList = new ArrayList<>();

            keyboardButtons1.add(FIRST_DAY);
            keyboardButtons2.add(SECOND_DAY);

            keyboardRowList.add(keyboardButtons1);
            keyboardRowList.add(keyboardButtons2);

            keyboard.setKeyboard(keyboardRowList);
            sendMessage(context, "Выберите день", keyboard);
        }

        @Override
        public void handleInput(BotContext context) {

            switch (context.getInput().getMessage().getText()) {
                case FIRST_DAY:
                    sendMessage(context, FIRST_DAY_MESSAGE, ReplyKeyboardMarkup.builder().clearKeyboard().build());
                    break;
                case SECOND_DAY:
                    sendMessage(context, SECOND_DAY_MESSAGE, ReplyKeyboardMarkup.builder().clearKeyboard().build());
                    break;
                default:
                    sendMessage(context, "Нужно было нажать кнопку!!", ReplyKeyboardMarkup.builder().clearKeyboard().build());
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
            sendMessage(context, I_WANT_MORE_MESSAGE_1);
            sendMessage(context, I_WANT_MORE_MESSAGE_2);
            sendMessage(context, I_WANT_MORE_MESSAGE_3);
        }

        @Override
        public BotState nextState() {
            return START;
        }
    },
    TEST {
        BotState next;
        boolean last = false;
        int cnt = 0;
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
            if (question.getImage().getImage().length > 0) {
                InputFile inputFile = new InputFile(new ByteArrayInputStream(question.getImage().getImage()), "question");
                if (question.isTextAnswer()) {
                    sendPhoto(context, inputFile, null, question.getDescription());
                    sendMessage(context, "<i>Введите ответ в текстовое поле</i>");
                } else {
                    sendPhoto(context, inputFile, createKeyboard(question), question.getDescription());
                }
                sendPhoto(context, inputFile, createKeyboard(question), question.getDescription());
            } else {
                if (question.isTextAnswer()) {
                    sendMessage(context, question.getDescription());
                    sendMessage(context, "<i>Введите ответ в текстовое поле</i>");
                } else {
                    sendMessage(context, question.getDescription(), createKeyboard(question));
                }
            }
            if(cnt>=2){
                switch (question.getIndex().intValue()){
                    case 2: sendMessage(context, "<i>Подсказка: Исида</i>", createKeyboard(question));
                    break;
                    case 3: sendMessage(context, "<i>Подсказка: Мирмекийский</i>", createKeyboard(question));
                        break;
                    case 8: sendMessage(context, "<i>Подсказка: М.П. Грязнов, С.И. Руденко</i>", createKeyboard(question));
                        break;
                    case 10: sendMessage(context,  "<i>Подсказка: Туркестанская</i>", createKeyboard(question));
                        break;
                }
            }

        }

        @Override
        public void handleInput(BotContext context) {

            User user = context.getUser();
            Question question = context.getQuestionService().getNext(user.getTestId());
            List<Answer> correctAnswers = question.getAnswerSet().stream().filter(Answer::isCorrect).collect(Collectors.toList());
            cnt ++;
            if (correctAnswers.stream().anyMatch(p -> p.getText().equals(context.getInput().getMessage().getText()))) {
                if (last) {
                    next = TEST_FINISH;
                } else {
                    next = TEST;
                }
                cnt=0;
                user.setTestId(user.getTestId() + 1L);
                context.getUserService().addUser(user);
                if (question.getImageWin().getImage().length > 0) {
                    InputFile inputFile = new InputFile(new ByteArrayInputStream(question.getImageWin().getImage()), "answer");
                    sendPhoto(context, inputFile, ReplyKeyboardMarkup.builder().clearKeyboard().build(), question.getTextIfWin());
                } else {
                    sendMessage(context, question.getTextIfWin(), ReplyKeyboardMarkup.builder().clearKeyboard().build());
                }
            } else {
                next = this;
                sendMessage(context, ANSWER_INCORRECT, ReplyKeyboardMarkup.builder().clearKeyboard().build());
            }

        }

        @Override
        public BotState nextState() {
            return next;
        }

        private ReplyKeyboardMarkup createKeyboard(Question question) {
            ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
            if (question.isTextAnswer()) {
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
    ABOUT(false) {
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
            InputFile inputFile = new InputFile(BotState.class.getClassLoader().getResourceAsStream("ABOUT_FESTIVAL.jpg"), "pic.jpg");
            sendPhoto(context, inputFile, keyboard);
            sendMessage(context, ABOUT_FESTIVAL_MESSAGE);
        }

        @Override
        public BotState nextState() {
            return START;
        }
    },
    DONATE(false) {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, DONATE_MESSAGE, ReplyKeyboardMarkup.builder().clearKeyboard().build());
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
        keyboard.setOneTimeKeyboard(true);
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
        sendPhoto(context, photo, keyboard, null);
    }

    protected void sendPhoto(BotContext context, InputFile photo, ReplyKeyboardMarkup keyboard, String caption) {
        SendPhoto message = new SendPhoto();
        message.setChatId(context.getInput().getMessage().getChatId().toString());
        message.setPhoto(photo);
        message.setParseMode("HTML");

        if (keyboard != null) {
            keyboard.setOneTimeKeyboard(true);
            message.setReplyMarkup(keyboard);
        }
        if (caption != null && !caption.isEmpty())
            message.setCaption(caption);
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
