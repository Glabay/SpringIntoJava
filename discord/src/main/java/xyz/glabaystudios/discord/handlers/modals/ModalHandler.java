package xyz.glabaystudios.discord.handlers.modals;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

public interface ModalHandler {
    void handleModalSubmission(ModalInteractionEvent event);
}
