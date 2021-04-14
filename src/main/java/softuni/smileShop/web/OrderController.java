package softuni.smileShop.web;


import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import softuni.smileShop.model.binding.OrderBindingModel;
import softuni.smileShop.model.binding.ProductAddBindingModel;
import softuni.smileShop.model.service.OrderServiceModel;
import softuni.smileShop.model.service.ProductServiceModel;
import softuni.smileShop.model.view.ProductDetailsViewModel;
import softuni.smileShop.service.OrderService;
import softuni.smileShop.service.ProductService;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final ProductService productService;
    private final ModelMapper modelMapper;


    public OrderController(OrderService orderService, ProductService productService, ModelMapper modelMapper) {
        this.orderService = orderService;
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("product/{id}")
    public String orderProduct(@PathVariable String id, Model model){

        ProductServiceModel productServiceModel = productService.findProductById(id);

        ProductDetailsViewModel productDetailsViewModel = modelMapper
                .map(productServiceModel, ProductDetailsViewModel.class);

        model.addAttribute("product", productDetailsViewModel);

        return "redirect:/orders/product";

    }

    @PostMapping("/add")
    public String addProduct(@Valid OrderBindingModel orderBindingModel,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserDetails principal) throws IOException {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("orderBindingModel", orderBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.orderBindingModel", bindingResult);

            return "redirect:/add";
        }

        OrderServiceModel orderServiceModel = modelMapper.map(orderBindingModel, OrderServiceModel.class);
        orderServiceModel.setUser(principal.getUsername());
        orderServiceModel.setUsername(orderBindingModel.getNameCustomer());
        orderServiceModel.setProductId(orderBindingModel.getProductId());
        orderServiceModel.setDate(LocalDate.parse(orderBindingModel.getDate()));

        this.orderService.createOrder(orderServiceModel);


        return "redirect:/orders/all";
    }
}
