package org.saphka.telegram.grocery.bot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bot")
public record BotProperties(String token, String username, String path) {
}
