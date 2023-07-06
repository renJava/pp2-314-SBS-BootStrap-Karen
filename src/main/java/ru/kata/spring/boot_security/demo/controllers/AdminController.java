package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private RoleService roleService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public String printUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user-admin";
    }

    @GetMapping("/user-create")
    public String createUserForm(Model model) {
        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("user", new User());
        return "user-create";
    }

    static final String REDIRECT_ADMIN = "redirect:/admin";

    @PostMapping
//    @Transactional  //Корректное добавление пользователя
    public String addUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user-create";
        }
        userService.saveUser(user);
        return REDIRECT_ADMIN;
    }

    @DeleteMapping("/user-delete/{id}")
    public String deleteUserFromTable(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return REDIRECT_ADMIN;
    }

    @GetMapping("/user-update/{id}")
    public String updateUserForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("user", userService.findById(id));
        return "user-update";
    }

    @PatchMapping("/user-update")
//    @Transactional    //Корректное обновление пользователя
    public String updateUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user-update";
        }

        userService.saveUser(user);
        return REDIRECT_ADMIN;
    }

}
