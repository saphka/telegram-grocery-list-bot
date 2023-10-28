package org.saphka.telegram.grocery.bot.config;

public class StateMachineEvents {

    public static final String COMMAND_PREFIX = "COMMAND_";

    public static final String COMMAND_PRODUCT = COMMAND_PREFIX + "PRODUCT";
    public static final String COMMAND_SHARE = COMMAND_PREFIX + "SHARE";
    public static final String COMMAND_LIST = COMMAND_PREFIX + "LIST";
    public static final String TEXT_INPUT = "TEXT_INPUT";
    public static final String USER_INPUT = "USER_INPUT";

    private StateMachineEvents() {
    }
}
