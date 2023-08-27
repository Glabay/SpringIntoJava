package xyz.glabaystudios.discord.handlers.commands.impl;

import xyz.glabaystudios.discord.handlers.commands.DisSlash;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;

import java.util.Objects;

public class Register implements DisSlash {
    @Override
    public void handleSlashCommand(SlashCommandInteractionEvent event) {
        Member disMem = event.getMember();
        if (Objects.isNull(disMem)) return;

        TextInput displayName = TextInput.create("DISPLAY_NAME_INPUT", "Enter a username you wish to display.", TextInputStyle.SHORT)
                .setRequiredRange(1, 18)
                .setPlaceholder("EpicUsernameHere")
                .setRequired(true)
                .build();

        TextInput email = TextInput.create("EMAIL_INPUT", "Enter an email for recovery.", TextInputStyle.SHORT)
                .setMinLength(6)
                .setPlaceholder("email@mail.tld")
                .setRequired(true)
                .build();

        TextInput firstName = TextInput.create("FIRST_NAME_INPUT", "Tell us your first name.", TextInputStyle.SHORT)
                .setPlaceholder("First")
                .setRequired(true)
                .build();

        TextInput lastName = TextInput.create("LAST_NAME_INPUT", "Tell us your last name.", TextInputStyle.SHORT)
                .setPlaceholder("Last")
                .setRequired(true)
                .build();

        TextInput password = TextInput.create("PASSWORD_INPUT", "Please enter a password.", TextInputStyle.SHORT)
                .setPlaceholder("something secure")
                .setRequired(true)
                .build();

        Modal registrationForm = Modal.create("REGISTRATION_FORM", "Please fill out our registration")
                .addActionRow(displayName)
                .addActionRow(email)
                .addActionRow(firstName)
                .addActionRow(lastName)
                .addActionRow(password)
                .build();

        event.replyModal(registrationForm).queue();
    }
}
