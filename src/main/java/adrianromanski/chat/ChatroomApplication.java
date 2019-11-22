package adrianromanski.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.net.UnknownHostException;

@RestController
@SpringBootApplication
public class ChatroomApplication {


        public static void main(String[] args) {
            SpringApplication.run(ChatroomApplication.class, args);
        }

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
        public ModelAndView index(String username, HttpServletRequest request) throws UnknownHostException {
            return new ModelAndView("chat");
        }
    }

