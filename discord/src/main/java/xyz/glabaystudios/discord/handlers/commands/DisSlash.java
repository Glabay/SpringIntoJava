package xyz.glabaystudios.discord.handlers.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface DisSlash {
    void handleSlashCommand(SlashCommandInteractionEvent event);
}
