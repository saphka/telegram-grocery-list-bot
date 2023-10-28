package org.saphka.telegram.grocery.bot.service;

import org.saphka.telegram.grocery.bot.config.StateMachineEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.EntityType;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;

@Service
public class TelegramUpdateProcessor {
    private static final Logger log = LoggerFactory.getLogger(TelegramUpdateProcessor.class);
    private final StateMachineFactory<String, String> stateMachineFactory;
    private final StateMachinePersister<String, String, String> stateMachinePersister;

    public TelegramUpdateProcessor(StateMachineFactory<String, String> stateMachineFactory, StateMachinePersister<String, String, String> stateMachinePersister) {
        this.stateMachineFactory = stateMachineFactory;
        this.stateMachinePersister = stateMachinePersister;
    }

    public void processUpdate(Update update) {
        if (!update.hasMessage()) {
            return;
        }
        var message = update.getMessage();

        String machineId = message.getChatId().toString();
        var stateMachine = restoreStateMachine(machineId);

        var result = stateMachine.sendEvent(Mono.just(createMessage(message, update)))
                .blockFirst();
        log.debug("Transition result is {}. Machine state is {}", result, stateMachine.getState());

        persistStateMachine(stateMachine, machineId);
    }

    private org.springframework.messaging.Message<String> createMessage(Message message, Update update) {
        var command = extractCommand(message);
        var event = command.map(s -> StateMachineEvents.COMMAND_PREFIX + s).orElseGet(() -> {
            if (message.getUserShared() != null) {
                return StateMachineEvents.USER_INPUT;
            }
            return StateMachineEvents.TEXT_INPUT;
        });
        var headers = new MessageHeaders(Map.of(
                "message", message,
                "update", update
        ));
        return new GenericMessage<>(event, headers);
    }

    private Optional<String> extractCommand(Message message) {
        return Optional.ofNullable(message.getEntities())
                .flatMap(list -> list
                        .stream()
                        .filter(e -> EntityType.BOTCOMMAND.equals(e.getType()))
                        .findFirst())
                .map(MessageEntity::getText).map(String::toUpperCase)
                .map(com -> com.substring(1));
    }

    private void persistStateMachine(StateMachine<String, String> stateMachine, String machineId) {
        try {
            stateMachinePersister.persist(stateMachine, machineId);
        } catch (Exception e) {
            log.error("Error persisting state machine for chat {}", machineId, e);
        }
    }

    private StateMachine<String, String> restoreStateMachine(String machineId) {
        var stateMachine = stateMachineFactory.getStateMachine(machineId);
        try {
            stateMachinePersister.restore(stateMachine, machineId);
        } catch (Exception e) {
            log.error("Error restoring state machine for chat {}", machineId, e);
        }
        stateMachine.startReactively().block();
        return stateMachine;
    }
}
