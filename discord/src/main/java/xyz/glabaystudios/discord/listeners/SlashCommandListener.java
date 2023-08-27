package xyz.glabaystudios.discord.listeners;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import xyz.glabaystudios.discord.handlers.commands.impl.AddBook;
import xyz.glabaystudios.discord.handlers.commands.impl.BookShelf;
import xyz.glabaystudios.discord.handlers.commands.impl.Register;

import java.util.Objects;

public class SlashCommandListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        Member disMem = event.getMember();
        if (Objects.isNull(disMem)) return;
        String commandName = event.getInteraction().getName();

        if (commandName.equalsIgnoreCase("register")) new Register().handleSlashCommand(event);
        if (commandName.equalsIgnoreCase("add-book")) new AddBook().handleSlashCommand(event);
        if (commandName.equalsIgnoreCase("bookshelf")) new BookShelf().handleSlashCommand(event);

    }
}
