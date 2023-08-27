package xyz.glabaystudios.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import xyz.glabaystudios.discord.listeners.SlashCommandListener;
import xyz.glabaystudios.discord.listeners.FormModalInputListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Discord {

    private static Discord discordBot;
    private static JDA api;
    private static JDABuilder jdaBuilder;

    public static Discord getDiscordBot() {
        if (Objects.isNull(discordBot)) discordBot = new Discord();
        return discordBot;
    }

    public JDA getDiscordBotApi() {
        if (Objects.isNull(api)) init();
        return api;
    }

    private String getToken() {
        return "MTEyNDA3MTY1ODAzMDg5MTA2OA.G-3MFk.68KOoTs3s3gdI7iGk7kB2biV18nGkEMyoVYSM0"; // Token
    }

    public static void main(String[] args) {
        getDiscordBot().init();
    }


    private JDABuilder getJdaBuilder() {
        if (Objects.isNull(jdaBuilder))
            jdaBuilder = JDABuilder.createDefault(getToken(),
                    // Bot intents
                    GatewayIntent.GUILD_MEMBERS,
                    GatewayIntent.MESSAGE_CONTENT,
                    GatewayIntent.GUILD_EMOJIS_AND_STICKERS,
                    GatewayIntent.DIRECT_MESSAGES)

                    .disableCache(
                            CacheFlag.MEMBER_OVERRIDES,
                            CacheFlag.VOICE_STATE,
                            CacheFlag.ONLINE_STATUS);
        return jdaBuilder;
    }

    private void init() {
        System.out.println("Connecting to discord....");
        try {
            api = getJdaBuilder().build();

            // add the listeners
            getDiscordBotApi().addEventListener(new SlashCommandListener(), new FormModalInputListener());

            // create a list of "Slash Commands"
            List<CommandData> commands = new ArrayList<>();
            commands.add(Commands.slash("register", "Register a new account."));

            commands.add(Commands.slash("add-book", "Provides a way to add a book to your library.")
                    .addOptions(new OptionData(OptionType.INTEGER, "isbn", "This would be a 10, or 13-digit number unique to the book.", true)));

            commands.add(Commands.slash("bookshelf", "Manage you selves of books and novels.")
                    .addSubcommands(
                            new SubcommandData("create", "Create a new shelf")
                                    .addOptions(
                                            new OptionData(OptionType.STRING, "name", "Name of your bookshelf"),
                                            new OptionData(OptionType.STRING, "description", "Something to describe the theme of books in this shelf")
                                    ),
                            new SubcommandData("edit", "Edit many aspects of your selected bookshelf")
                                    .addOptions(
                                            new OptionData(OptionType.BOOLEAN, "name-change", "Name of your bookshelf"),
                                            new OptionData(OptionType.BOOLEAN, "re-describe", "Something to describe the theme of books in this shelf"),
                                            new OptionData(OptionType.BOOLEAN, "add-book", "Add a book from your library to a shelf"),
                                            new OptionData(OptionType.BOOLEAN, "remove-book", "Remove a book from a shelf")
                                    )
                    ));

            // send the commands to Discord
            getDiscordBotApi().updateCommands().addCommands(commands).queue();

            api.awaitReady();

            System.out.println(getDiscordBotApi().getInviteUrl(Permission.ADMINISTRATOR));
        } catch (Exception e) {
            System.err.println("[Discord] - Error loading discord bot.");
        }
    }

    public static void shutdownBot() {
        api.shutdown();
    }
}
