package softuni.smileShop.web;


import org.modelmapper.ModelMapper;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import softuni.smileShop.model.binding.UserLoginBindingModel;
import softuni.smileShop.model.binding.UserRegisterBindingModel;
import softuni.smileShop.model.service.UserServiceModel;
import softuni.smileShop.service.UserService;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;


    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @ModelAttribute("userRegisterBindingModel")
    public UserRegisterBindingModel createBindingModel() {
        return new UserRegisterBindingModel();
    }

    @GetMapping("/login")
    public String login(Model model) {
        if (!model.containsAttribute("userLoginBindingModel")) {
            model.addAttribute("userLoginBindingModel", new UserLoginBindingModel());
            model.addAttribute("notFound", false);
        }
        return "login";
    }


    @PostMapping("/login")
    public String failedLogin(@ModelAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY)
                                      String username, RedirectAttributes attributes) {

        attributes.addFlashAttribute("bad_credentials", true);
        attributes.addFlashAttribute("username", username);

        return "redirect:login";
    }


    @GetMapping("/register")
    public String register(Model model) {
        if (!model.containsAttribute("userRegisterBindingModel")) {
            model.addAttribute("userRegisterBindingModel", new UserRegisterBindingModel());
        }
        return "register";
    }


    @PostMapping("/register")
    public String registerAndLoginUser(@Valid UserRegisterBindingModel userRegisterBindingModel,
                                       BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        //@Valid валидира валидации който ние сме сложили, а байнингрезулта показва грешките който ние сме задали,
        // редиректатррибют казва да редиректне къде да ни върне и

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userRegisterBindingModel", userRegisterBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegisterBindingModel", bindingResult);

            return "redirect:/register";
        }

        if (userService.userExists(userRegisterBindingModel.getUsername())) {
            redirectAttributes.addFlashAttribute("registrationBindingModel", userRegisterBindingModel);
            redirectAttributes.addFlashAttribute("userExistsError", true);

            return "redirect:/register";
        }

        UserServiceModel userServiceModel = modelMapper
                .map(userRegisterBindingModel, UserServiceModel.class);

        this.userService.registerUser(userServiceModel);

        return "redirect:/home";


    }

}
