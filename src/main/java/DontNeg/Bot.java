package DontNeg;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.api.service.IEventHandler;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import io.github.cdimascio.dotenv.Dotenv;

public class Bot {
    static Bot bot;
    TwitchClient twitchClient;

    public Bot(OAuth2Credential credential, Class<? extends IEventHandler> eventHandler) {
        twitchClient = TwitchClientBuilder
                .builder()
                .withEnableHelix(true)
                .withEnableChat(true)
                .withChatAccount(credential)
                .withDefaultEventHandler(eventHandler)
                .build();
    }

    public static void main(String[] args) {
        OAuth2Credential credential = new OAuth2Credential("twitch", Dotenv.configure().load().get("OAUTH"));
        bot = new Bot(credential, SimpleEventHandler.class);
        CommandListener commandListener = new CommandListener();
        commandListener.start();
    }
}