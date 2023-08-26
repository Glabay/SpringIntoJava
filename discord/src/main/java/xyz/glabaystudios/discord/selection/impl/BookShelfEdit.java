package xyz.glabaystudios.discord.selection.impl;

import xyz.glabaystudios.discord.selection.SelectMenuHandler;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;

import java.util.Objects;

/**
 * @author Glabay
 * @project DisLibrary
 * @social Discord: Glabay | Website: www.GlabayStudios.xyz
 * @since 2023-08-19
 */
public class BookShelfEdit implements SelectMenuHandler {
    @Override
    public void onSelectionSubmission(SelectMenuInteractionEvent event) {
        SelectMenu selectMenu = event.getSelectMenu();
        if (Objects.isNull(selectMenu.getId())) {
            System.out.println("[BookShelfEdit] - (onSelectionSubmission) -> SelectMenu ID is null.");
            return;
        }
        var value = event.getValues().get(0);
        var strData = value.replace("EDIT_SHELF_", "").trim();
        var shelfId = strData.split("\\|")[0];
        var editOptions = strData.split("\\|")[1];
        // create an input form with the applicable options, and also add a selection modal for the book to add, and or to remove
    }
}
