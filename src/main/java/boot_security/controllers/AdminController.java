package boot_security.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import boot_security.models.User;
import boot_security.services.RoleService;
import boot_security.services.UserService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    private final UserService userService;
    private final RoleService roleService;
    
    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }
    
    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users";
    }
    
    @GetMapping("/new")
    public String newUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "admin/user-form";
    }
    
    @PostMapping
    public String createUser(@ModelAttribute("user") User user, 
                            @RequestParam("roleIds") List<Long> roleIds) {
        user.setRoles(roleService.getRolesByIds(roleIds));
        userService.saveUser(user);
        return "redirect:/admin";
    }
    
    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable("id") Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "admin/user-form";
    }
    
    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") User user, 
                            @RequestParam("roleIds") List<Long> roleIds) {
        user.setRoles(roleService.getRolesByIds(roleIds));
        userService.updateUser(user);
        return "redirect:/admin";
    }
    
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
