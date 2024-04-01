package DontNeg;

import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class CommandListener {
    public CommandListener(){
        Bot bot = Bot.bot;
        TwitchClient twitchClient = bot.twitchClient;
        twitchClient.getEventManager().onEvent(ChannelMessageEvent.class, event -> {
            String msg = event.getMessage();
            if(msg.startsWith("add(")&&event.getUser().getName().equals(Dotenv.configure().load().get("CHANNEL"))){
                try{
                    Files.write(Paths.get("commands.txt"), (
                        msg.substring(msg.indexOf("(")+1,msg.indexOf(","))
                        + " " +
                        msg.substring(msg.indexOf(",")+1,msg.indexOf(")")) + "\n"
                        ).getBytes(),
                        StandardOpenOption.APPEND);
                } catch (IOException ignored) {}
            }
            try {
                BufferedReader reader = new BufferedReader(new FileReader("commands.txt"));
                String line = reader.readLine();
                while (line != null) {
                    if(msg.equals(line.substring(0,line.indexOf(" ")))){
                        twitchClient.getChat().sendMessage(Dotenv.configure().load().get("CHANNEL"),line.substring(line.indexOf(" ")));
                    }
                    line = reader.readLine();

                }
                reader.close();
            } catch (IOException ignored) {}
        });
    }
    public void start(){
        Bot bot = Bot.bot;
        TwitchClient twitchClient = bot.twitchClient;
        twitchClient.getChat().joinChannel(Dotenv.configure().load().get("CHANNEL"));
        twitchClient.getChat().sendMessage(Dotenv.configure().load().get("CHANNEL"),"Started");
    }
}
