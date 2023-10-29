package org.saphka.telegram.grocery.bot.config;

public class StateMachineStates {

    public static final String READY = "READY";
    public static final String PRODUCT_ADD = "PRODUCT_ADD";
    public static final String PRODUCT_REMOVE = "PRODUCT_REMOVE";
    public static final String SHARE_PENDING = "SHARE_PENDING";
    public static final String UNSHARE_PENDING = "UNSHARE_PENDING";

    private StateMachineStates() {
    }
}
