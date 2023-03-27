package ru.example.jobbot.config;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.example.jobbot.bot.keyboard.button.Button;
import ru.example.jobbot.bot.keyboard.button.handler.ButtonHandler;

import javax.sql.DataSource;
import java.util.*;

@Slf4j
@Configuration
public class AppConfig {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean("jasyptStringEncryptor")
    public StringEncryptor stringEncryptor(Environment environment) {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        encryptor.setPassword(Objects.requireNonNull(environment.getProperty("jasypt.encryptor.password")));
        encryptor.setPoolSize(1);
        return encryptor;
    }

    @Bean
    public TelegramBotsApi telegramBotsApi() {
        try {
            return new TelegramBotsApi(DefaultBotSession.class);
        } catch (TelegramApiException e) {
            log.error("Ошибка при создании экземпляра TelegramBotsApi: {}", e.getMessage());
            throw new IllegalStateException("Не удалось создать экземпляр TelegramBotsApi", e);
        }
    }

    @Bean(name = "buttonHandlersMap")
    public Map<String, ButtonHandler> buttonHandlersMap(List<Button> buttons, List<ButtonHandler> buttonHandlers) {
        Map<String, ButtonHandler> buttonHandlersMap = new HashMap<>();
        for (Button button : buttons) {
            buttonHandlersMap.put(button.getCallBackText(), buttonHandlers.
                    stream().
                    filter(handler -> handler.getButtonHandlerName().equals(button.getCallBackText()))
                    .findAny().orElseThrow());
        }
        return buttonHandlersMap;
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames(
                "i18n/bot/commands/commands_name_messages",
                "i18n/bot/commands/commands_message_text_messages"
        );
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setDefaultLocale(new Locale("ru", "RU"));
        return messageSource;
    }
}