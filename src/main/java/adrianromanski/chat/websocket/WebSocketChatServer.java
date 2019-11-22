package adrianromanski.chat.websocket;

import adrianromanski.chat.decoders.MessageDecoder;
import adrianromanski.chat.decoders.MessageEncoder;
import adrianromanski.chat.model.Message;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket Server
 *
 * @see ServerEndpoint WebSocket Client
 * @see Session   WebSocket Session
 */

@Component
@ServerEndpoint(
        value="/chatroom/{username}",
        decoders = MessageDecoder.class,
        encoders = MessageEncoder.class
)

public class WebSocketChatServer {

    private static final String USERNAME_PATH_PARAM = "username";
    private static final String DEFAULT_USERNAME = "unknown";

    enum TYPE {
        SPEAK("SPEAK"),
        WELCOME("WELCOME"),
        GOODBYE("GOODBYE");

        private final String name;

        TYPE(String type) {
            this.name = type;
        }
    }

    /**
     * All chat sessions.
     */
    private static Map<String, Session> onlineSessions = new ConcurrentHashMap<>();

    private static void sendMessageToAll(Message msg) {
        onlineSessions.forEach((username, session) -> {
            try {
                session.getBasicRemote().sendObject(msg);
            } catch (IOException | EncodeException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Open connection, 1) add session, 2) add user.
     */
    @OnOpen
    public void onOpen(Session session) {
        onlineSessions.put(getUsername(session), session);
        final Message message = new Message();
        message.setOnlineCount(onlineSessions.size());
        message.setUsername(getUsername(session));
        message.setType(TYPE.WELCOME.name);
        sendMessageToAll(message);
    }

    /**
     * Send message, 1) get username and session, 2) send message to all.
     */
    @OnMessage
    public void onMessage(Session session, Message message) {
        message.setUsername(getUsername(session));
        message.setType(TYPE.SPEAK.name);
        sendMessageToAll(message);
    }

    /**
     * Close connection, 1) remove session, 2) update user.
     */
    @OnClose
    public void onClose(Session session) {
        onlineSessions.remove(getUsername(session));
        final Message message = new Message();
        message.setOnlineCount(onlineSessions.size());
        message.setUsername(getUsername(session));
        message.setType(TYPE.GOODBYE.name);
        sendMessageToAll(message);
    }

    /**
     * Print exception.
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    /**
     * Get username from session.
     */
    private String getUsername(Session session) {
        return session.getPathParameters()
                .getOrDefault(USERNAME_PATH_PARAM, DEFAULT_USERNAME);
    }
}
