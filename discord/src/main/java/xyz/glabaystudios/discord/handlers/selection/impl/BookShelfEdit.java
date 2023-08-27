package xyz.glabaystudios.discord.handlers.selection.impl;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import xyz.glabaystudios.discord.handlers.selection.SelectMenuHandler;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import xyz.glabaystudios.network.dto.BookDTO;
import xyz.glabaystudios.network.impl.BookShelfNetwork;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Glabay
 * @project DisLibrary
 * @social Discord: Glabay | Website: www.GlabayStudios.xyz
 * @since 2023-08-19
 */
public class BookShelfEdit implements SelectMenuHandler, BookShelfNetwork {
    @Override
    public void onSelectionSubmission(SelectMenuInteractionEvent event) {
        Member member = event.getMember();
        if (Objects.isNull(member)) return;
        SelectMenu selectMenu = event.getSelectMenu();
        if (Objects.isNull(selectMenu.getId())) {
            System.out.println("[BookShelfEdit] - (onSelectionSubmission) -> SelectMenu ID is null.");
            return;
        }
        var value = event.getValues().get(0); // EDIT_SHELF_1|0|0|0|0
        var strData = value.replace("EDIT_SHELF_", "").trim().split("\\|");// 1|0|0|0|0
        var shelfId = Long.parseLong(strData[0]);
        var editName = toBoolean(strData[1].charAt(0));
        var editDescribe = toBoolean(strData[2].charAt(0));
        var editAddBook = toBoolean(strData[3].charAt(0));
        var editRemoveBook = toBoolean(strData[4].charAt(0));
        // create an input form with the applicable options, and also add a selection modal for the book to add, and or to remove

        var inputs = new ArrayList<TextInput>();
        if (editName) {
            var shelfName = TextInput.create("SHELF_NAME", "Please enter a new name for your shelf", TextInputStyle.SHORT)
                    .setRequired(true)
                    .build();
            inputs.add(shelfName);
        }
        if (editDescribe) {
            var shelfDescription = TextInput.create("SHELF_DESCRIPTION", "Please enter a new description for your shelf", TextInputStyle.SHORT)
                    .setRequired(true)
                    .build();
            inputs.add(shelfDescription);
        }
        if (!inputs.isEmpty()) {
            var inputActionRow = inputs.stream().map(ActionRow::of).collect(Collectors.toCollection(ArrayList::new));

            var modal = Modal.create("UPDATE_SHELF", "Please provide some information to update.")
                    .addActionRows(inputActionRow)
                    .build();
            event.replyModal(modal).queue();
        }

        if (editAddBook) {
            // fetch a list of books, not on a shelf
            var books = getBooksForUserNotOnShelf(member.getIdLong());
            if (Objects.nonNull(books)) {
                var options = books.stream().map(dto -> SelectOption.of(dto.getTitle(), "ADD_TO_SHELF_".concat(String.valueOf(shelfId)))).collect(Collectors.toCollection(ArrayList::new));
                var addBook = SelectMenu.create("EDIT_ADD_BOOK")
                        .addOptions(options)
                        .build();
                event.getHook().sendMessage("Select a Book to add to this shelf").addActionRow(addBook).setEphemeral(true).queue();
            }

        }
        if (editRemoveBook) {
            // fetch a list of books on the provided shelf
            var books = getBooksForUserOnShelf(member.getIdLong(), shelfId);
            if (Objects.nonNull(books)) {
                var options = books.stream().map(dto -> SelectOption.of(dto.getTitle(), "REMOVE_FROM_SHELF_".concat(String.valueOf(shelfId)))).collect(Collectors.toCollection(ArrayList::new));
                var removeBook = SelectMenu.create("EDIT_REMOVE_BOOK")
                        .addOptions(options)
                        .build();
                event.getHook().sendMessage("Select a Book to remove from this shelf").addActionRow(removeBook).setEphemeral(true).queue();
            }

        }
    }

    private Boolean toBoolean(char c) {
        return c == '1' ? Boolean.TRUE : Boolean.FALSE;
    }
}
