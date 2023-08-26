package xyz.glabaystudios.discord.commands.impl;

import xyz.glabaystudios.discord.commands.DisSlash;
import xyz.glabaystudios.network.BookShelfNetwork;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import xyz.glabaystudios.network.dto.BookShelfDTO;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BookShelf implements DisSlash, BookShelfNetwork {
    @Override
    public void handleSlashCommand(SlashCommandInteractionEvent event) {
        Member disMem = event.getMember();
        if (Objects.isNull(disMem)) return;
        var subCommandName = event.getSubcommandName();
        if (Objects.isNull(subCommandName)) return;
        event.deferReply().queue();
        Map<String, OptionMapping> optionMappingMap;
        switch(subCommandName) {
            case "create" -> {
                optionMappingMap = new HashMap<>();
                event.getOptions().forEach(option -> optionMappingMap.put(option.getName(), option));
                createNewBookshelf(event.getHook(), optionMappingMap);
            }
            case "edit" -> {
                optionMappingMap = new HashMap<>();
                event.getOptions().forEach(option -> optionMappingMap.put(option.getName(), option));
                editBookshelf(disMem.getIdLong(), event.getHook(), optionMappingMap);
            }
            default -> somethingWentWrong(event.getHook());
        }
    }

    private void editBookshelf(long discordUserId, InteractionHook hook, Map<String, OptionMapping> optionMappingMap) {
        if (Objects.isNull(hook)) {
            somethingWentWrong(null);
            return;
        }
        String str = "";
        if (Objects.equals("name-change", optionMappingMap.get("name-change").getName())) {
            var toggle = optionMappingMap.get("name-change").getAsBoolean();
            str += "|".concat(toggle ? "1" : "0");
        }
        if (Objects.equals("re-describe", optionMappingMap.get("re-describe").getName())) {
            var toggle = optionMappingMap.get("re-describe").getAsBoolean();
            str += "|".concat(toggle ? "1" : "0");
        }
        if (Objects.equals("add-book", optionMappingMap.get("add-book").getName())) {
            var toggle = optionMappingMap.get("add-book").getAsBoolean();
            str += "|".concat(toggle ? "1" : "0");
        }
        if (Objects.equals("remove-book", optionMappingMap.get("remove-book").getName())) {
            var toggle = optionMappingMap.get("remove-book").getAsBoolean();
            str += "|".concat(toggle ? "1" : "0");
        }
        var shelfDTOList = getBookshelvesForUser(discordUserId);
        String finalStr = str;
        var shelves = shelfDTOList.stream()
                .map(shelf -> SelectOption.of(shelf.getShelfName(), "EDIT_SHELF_".concat(shelf.getShelfId().toString()).concat(finalStr)))
                .toList();
        var selectMenu = SelectMenu.create("EDIT_BOOKSHELF_SELECTION")
                .addOptions(shelves)
                .build();
        hook.sendMessage("Please select a shelf to edit").addActionRow(selectMenu).queue();
    }

    private void createNewBookshelf(InteractionHook hook, Map<String, OptionMapping> optionMappingMap) {
        if (Objects.isNull(hook)) {
            somethingWentWrong(null);
            return;
        }
        var dto = new BookShelfDTO();
        var option = optionMappingMap.get("name");
        if (Objects.nonNull(option)) dto.setShelfName(option.getAsString());
        var option2 = optionMappingMap.get("description");
        if (Objects.nonNull(option2)) dto.setShelfDescription(option2.getAsString());
        var discordUserId = Objects.requireNonNull(hook.getInteraction().getMember()).getIdLong();
        dto.setOwnerDiscordId(discordUserId);
        var reply = postCreateNewBookShelf(dto);
        hook.sendMessage(reply).queue();
    }

    private void somethingWentWrong(InteractionHook hook) {
        if (Objects.isNull(hook)) {
            System.err.println("something went sideways...");
            return;
        }
        hook.sendMessage("Something went sideways in the code, please contact a developer...").setEphemeral(true).queue();
    }
}
