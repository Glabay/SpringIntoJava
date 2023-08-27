package xyz.glabaystudios.discord.handlers.modals.impl;

import xyz.glabaystudios.discord.handlers.modals.ModalHandler;
import xyz.glabaystudios.network.impl.RegistrationNetwork;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;
import xyz.glabaystudios.network.dto.RegistrationFormDTO;

import java.util.List;
import java.util.Objects;

public class RegistrationForm implements ModalHandler, RegistrationNetwork {
    @Override
    public void handleModalSubmission(ModalInteractionEvent event) {
        Member member = event.getMember();
        if (Objects.isNull(member)) return;

        List<ModalMapping> fields = event.getInteraction().getValues();
        String username = fields.get(0).getAsString();
        String email = fields.get(1).getAsString();
        String firstName = fields.get(2).getAsString();
        String lastName = fields.get(3).getAsString();
        String password = fields.get(4).getAsString();

        var dto = new RegistrationFormDTO();
            dto.setUsername(username);
            dto.setEmail(email);
            dto.setFirstName(firstName);
            dto.setLastName(lastName);
            dto.setPassword(password);
            dto.setDiscordUserId(member.getIdLong());

        if (checkIfUserExists(dto)) {
            event.reply("Sorry, it seems as if an account with that name already exists...").setEphemeral(true).queue();
            return;
        }
        event.reply(createNewAccount(dto)).setEphemeral(true).queue();
    }
}
