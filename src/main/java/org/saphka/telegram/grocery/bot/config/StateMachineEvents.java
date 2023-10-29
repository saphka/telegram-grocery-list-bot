package org.saphka.telegram.grocery.bot.config;

public class StateMachineEvents {

    public static final String COMMAND_PREFIX = "COMMAND_";

    public static final String COMMAND_ADD = COMMAND_PREFIX + "ADD";
    public static final String COMMAND_REMOVE = COMMAND_PREFIX + "REMOVE";
    public static final String COMMAND_CLEAR = COMMAND_PREFIX + "CLEAR";
    public static final String COMMAND_SHARE = COMMAND_PREFIX + "SHARE";
    public static final String COMMAND_UNSHARE = COMMAND_PREFIX + "UNSHARE";
    public static final String COMMAND_LIST = COMMAND_PREFIX + "LIST";
    public static final String TEXT_INPUT = "TEXT_INPUT";
    public static final String USER_INPUT = "USER_INPUT";

    private StateMachineEvents() {
    }
}
