package xyz.glabaystudios.discord.handlers.commands.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import xyz.glabaystudios.discord.handlers.commands.DisSlash;
import xyz.glabaystudios.network.GlabayStudiosNetwork;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import xyz.glabaystudios.network.dto.BookDTO;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class AddBook implements DisSlash, GlabayStudiosNetwork {
    @Override
    public void handleSlashCommand(SlashCommandInteractionEvent event) {
        Member disMem = event.getMember();
        if (Objects.isNull(disMem)) return;
        var options = event.getOptions();
        if (options.isEmpty()) return;
        var isbn = options.get(0).getAsString();

        try (CloseableHttpClient httpClient = getHttpClient()) {
            var response = submitHttpPostWithReply(BASE_API_ENDPOINT.concat("/v1/book/add/isbn/").concat(isbn), httpClient);
            if (Objects.nonNull(response)) {
                var reply = EntityUtils.toString(response.getEntity(), "UTF-8");
                ObjectMapper mapper = new ObjectMapper();
                var book = mapper.readValue(reply, BookDTO.class);
                if (Objects.nonNull(book))
                    sendCreatedEmbed(event, book);
                else
                    event.reply("Something went wrong...").queue();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendCreatedEmbed(SlashCommandInteractionEvent event, BookDTO book) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(new Color(64, 0, 128));
        builder.setAuthor("DisLibrary");
        builder.setTitle(book.getTitle());
        builder.setDescription(book.getDescription());

        builder.addField("Author", book.getAuthor(), true);
        builder.addField("Publisher", book.getPublisher(), true);
        builder.addField("published", book.getPublishedDate(), true);

        builder.addField("ISBN 10", String.valueOf(book.getISBN10()), true);
        builder.addField("ISBN 13", String.valueOf(book.getISBN13()), true);
        builder.addField("Shelf ID", String.valueOf(book.getShelfId()), true);

        builder.setFooter("Powered by: Glabay-Studios");

        event.replyEmbeds(builder.build()).queue();

    }
}
