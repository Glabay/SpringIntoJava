package xyz.glabaystudios.discord.handlers.selection;

import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;

/**
 * @author Glabay
 * @project DisLibrary
 * @social Discord: Glabay | Website: www.GlabayStudios.xyz
 * @since 2023-08-19
 */
public interface SelectMenuHandler {
    void onSelectionSubmission(SelectMenuInteractionEvent event);
}
