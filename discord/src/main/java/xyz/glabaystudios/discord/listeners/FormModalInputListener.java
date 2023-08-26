package xyz.glabaystudios.discord.listeners;

import xyz.glabaystudios.discord.handlers.impl.RegistrationForm;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class FormModalInputListener extends ListenerAdapter {

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        Member disMem = event.getMember();
        if (Objects.isNull(disMem)) return;
        String form = event.getModalId();

        if (Objects.equals(form, "REGISTRATION_FORM")) new RegistrationForm().handleModalSubmission(event);
    }
}
