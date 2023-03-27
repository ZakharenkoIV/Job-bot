package ru.example.jobbot.service.cache;

import org.springframework.stereotype.Service;
import ru.example.jobbot.bot.AccessLevel;
import ru.example.jobbot.entity.TelegramUser;
import ru.example.jobbot.repository.UserRepository;

import java.util.*;

@Service
public class HashSetCacheService implements CacheService {

    private final HashSet<Long> telegramIds;
    private final HashSet<Long> chatIds;
    private final List<TelegramUser> users;
    private final Map<Long, TelegramUser> registrationList;

    /* Map<telegramUserId, Map<memberName, Map<commandName, messageId>>> */
    private final Map<Long, Map<String, Map<String, Integer>>> commandArchive;


    public HashSetCacheService(UserRepository userRepository) {
        this.telegramIds = new HashSet<>(userRepository.getAllTelegramIds());
        this.chatIds = new HashSet<>();
        this.users = userRepository.findAll();
        this.registrationList = new HashMap<>();
        this.commandArchive = new HashMap<>();
    }

    @Override
    public void addTelegramId(Long telegramId) {
        telegramIds.add(telegramId);
    }

    @Override
    public boolean isTelegramIdExist(Long telegramId) {
        return telegramIds.contains(telegramId);
    }

    public List<TelegramUser> getUsers() {
        return users;
    }

    public HashSet<Long> getChatIds() {
        return chatIds;
    }

    public boolean isChatIdExist(Long chatId) {
        return chatIds.contains(chatId);
    }

    @Override
    public void addChatId(Long chatId) {
        this.chatIds.add(chatId);
    }

    @Override
    public void removeChatId(Long chatId) {
        this.chatIds.remove(chatId);
    }

    @Override
    public void addRegistrationMap(TelegramUser user) {
        registrationList.put(user.getTelegramId(), user);
    }

    @Override
    public TelegramUser removeTelegramUserFromRegistrationMap(Long telegramUserId) {
        return registrationList.remove(telegramUserId);
    }

    @Override
    public void removeRegistrationMap(Long telegramUserId) {
        registrationList.remove(telegramUserId);
    }

    public Optional<Integer> getSentMessageId(Long chatId, String accessLevel, String commandName) {
        String memberName = accessLevel.contains(AccessLevel.ADMIN) ? AccessLevel.ADMIN : "user";
        if (commandArchive.containsKey(chatId)) {
            Map<String, Map<String, Integer>> commandMembers = commandArchive.get(chatId);
            if (commandMembers.containsKey(memberName)) {
                Map<String, Integer> member = commandMembers.get(memberName);
                return Optional.ofNullable(member.get(commandName));
            }
        }
        return Optional.empty();
    }

    public void putSentMessageId(Long chatId, String accessLevel, String commandName, Integer messageId) {
        String memberName = accessLevel.contains(AccessLevel.ADMIN) ? AccessLevel.ADMIN : "user";
        if (!commandArchive.containsKey(chatId)) {
            commandArchive.put(chatId, new HashMap<>());
        }
        Map<String, Map<String, Integer>> commandMembers = commandArchive.get(chatId);
        if (!commandMembers.containsKey(memberName)) {
            commandMembers.put(memberName, new HashMap<>());
        }
        Map<String, Integer> member = commandMembers.get(memberName);
        member.put(commandName, messageId);
    }

    public boolean containsCommandMassage(Long telegramUserId, String accessLevel, String commandName) {
        String memberName = accessLevel.contains(AccessLevel.ADMIN) ? AccessLevel.ADMIN : "user";
        if (commandArchive.containsKey(telegramUserId)) {
            if (commandArchive.get(telegramUserId).containsKey(memberName)) {
                return commandArchive.get(telegramUserId).get(memberName).containsKey(commandName);
            }
        }
        return false;
    }

    @Override
    public TelegramUser findRegisteringUser(Long newTelegramUserId) {
        return registrationList.get(newTelegramUserId);
    }

    @Override
    public void deleteChatByTelegramId(Long chatId) {
        chatIds.remove(chatId);
    }
}
