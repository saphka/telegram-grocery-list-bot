package org.saphka.telegram.grocery.bot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.data.redis.RedisStateMachineContextRepository;
import org.springframework.statemachine.data.redis.RedisStateMachinePersister;
import org.springframework.statemachine.persist.RepositoryStateMachinePersist;

import java.util.Set;

@Configuration
@EnableStateMachineFactory
public class StateMachineConfiguration extends StateMachineConfigurerAdapter<String, String> {

    private final Action<String, String> sendMessageAction;
    private final Action<String, String> exceptionHandlerAction;
    private final Action<String, String> addProductAction;
    private final Action<String, String> listProductsAction;
    private final Action<String, String> requestUserAction;
    private final Action<String, String> shareListAction;

    public StateMachineConfiguration(Action<String, String> sendMessageAction, Action<String, String> exceptionHandlerAction, Action<String, String> addProductAction, Action<String, String> listProductsAction, Action<String, String> requestUserAction, Action<String, String> shareListAction) {
        this.sendMessageAction = sendMessageAction;
        this.exceptionHandlerAction = exceptionHandlerAction;
        this.addProductAction = addProductAction;
        this.listProductsAction = listProductsAction;
        this.requestUserAction = requestUserAction;
        this.shareListAction = shareListAction;
    }

    @Override
    public void configure(StateMachineStateConfigurer<String, String> states) throws Exception {
        states.withStates()
                .initial(StateMachineStates.READY)
                .states(Set.of(
                        StateMachineStates.READY,
                        StateMachineStates.PRODUCT_ADD,
                        StateMachineStates.SHARE_PENDING
                ));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<String, String> transitions) throws Exception {
        transitions.withExternal()
                .source(StateMachineStates.READY).target(StateMachineStates.PRODUCT_ADD)
                .event(StateMachineEvents.COMMAND_PRODUCT)
                .action(sendMessageAction, exceptionHandlerAction)
                .and()
                .withExternal()
                .source(StateMachineStates.PRODUCT_ADD).target(StateMachineStates.READY)
                .event(StateMachineEvents.TEXT_INPUT)
                .action(addProductAction, exceptionHandlerAction)
                .action(sendMessageAction, exceptionHandlerAction)
                .and()
                .withExternal()
                .source(StateMachineStates.READY).target(StateMachineStates.READY)
                .event(StateMachineEvents.COMMAND_LIST)
                .action(sendMessageAction, exceptionHandlerAction)
                .action(listProductsAction, exceptionHandlerAction)
                .and()
                .withExternal()
                .source(StateMachineStates.READY).target(StateMachineStates.SHARE_PENDING)
                .event(StateMachineEvents.COMMAND_SHARE)
                .action(requestUserAction, exceptionHandlerAction)
                .and()
                .withExternal()
                .source(StateMachineStates.SHARE_PENDING).target(StateMachineStates.READY)
                .event(StateMachineEvents.USER_INPUT)
                .action(shareListAction, exceptionHandlerAction)
                .action(sendMessageAction, exceptionHandlerAction);
    }

    @Bean
    public StateMachinePersist<String, String, String> stateMachinePersist(RedisConnectionFactory connectionFactory) {
        RedisStateMachineContextRepository<String, String> repository =
                new RedisStateMachineContextRepository<>(connectionFactory);
        return new RepositoryStateMachinePersist<>(repository);
    }

    @Bean
    public RedisStateMachinePersister<String, String> redisStateMachinePersister(
            StateMachinePersist<String, String, String> stateMachinePersist) {
        return new RedisStateMachinePersister<>(stateMachinePersist);
    }
}
