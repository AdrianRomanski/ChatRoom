package adrianromanski.chat.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.net.UnknownHostException;


@RestController
public class ChatController {

    /**
     * Login Page
     */
    @GetMapping("/")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    /**
     * Chatroom Page
     */
    @GetMapping("/chatroom/{username}")
    public ModelAndView index(@PathVariable String username, HttpServletRequest request) throws UnknownHostException {
        return new ModelAndView("chat");
    }
}
