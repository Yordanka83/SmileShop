package softuni.smileShop;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import softuni.smileShop.service.UserService;

@Component
public class SmileApplicationInit implements CommandLineRunner {

    private final UserService userService;

    public SmileApplicationInit(UserService userService) {
        this.userService = userService;
    }


    @Override
    public void run(String... args) throws Exception {


    }
}
