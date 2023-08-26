package xyz.glabaystudios.discord.listeners;

import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import org.jetbrains.annotations.NotNull;
import xyz.glabaystudios.discord.selection.impl.BookShelfEdit;

import java.util.Objects;

public class SelectMenuListener extends ListenerAdapter {

    @Override
    public void onSelectMenuInteraction(@NotNull SelectMenuInteractionEvent event) {
        SelectMenu selectMenu = event.getSelectMenu();
        String trigger = event.getComponentId();
        if (Objects.isNull(selectMenu.getId())) {
            System.out.println("[SelectMenuOption] - (onSelectMenuInteraction) -> SelectMenu ID is null.");
            return;
        }

        switch(trigger) {
            case "EDIT_BOOKSHELF_SELECTION" -> new BookShelfEdit().onSelectionSubmission(event);

            default -> System.out.println("[BookShelfEdit] - () -> Unhandled trigger: ".concat(trigger));
        }
    }
}
