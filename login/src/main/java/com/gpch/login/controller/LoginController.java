package com.gpch.login.controller;

import javax.validation.Valid;

import com.gpch.login.model.User;
import com.gpch.login.repository.UserRepository;
import com.gpch.login.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    UserRepository repository;

    @RequestMapping(value={"/", "/login"}, method = RequestMethod.GET)
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }


    @RequestMapping(value="/registration", method = RequestMethod.GET)
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
        } else {
            userService.saveUser(user);
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("registration");

        }
        return modelAndView;
    }

    @RequestMapping(value="/registrationteacher", method = RequestMethod.GET)
    public ModelAndView registrationteacher(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("registrationteacher");
        return modelAndView;
    }

    @RequestMapping(value = "/registrationteacher", method = RequestMethod.POST)
    public ModelAndView createNewUserTeacher(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registrationteacher");
        } else {
            userService.saveUser2(user);
            modelAndView.addObject("successMessage", "User Teacher has been registered successfully");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("registrationteacher");

        }
        return modelAndView;
    }



    @RequestMapping(value="/teacher/home", method = RequestMethod.GET)
    public ModelAndView home(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByName(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.getName() + " " + user.getLastName());
        modelAndView.addObject("adminMessage","Content Available Only for Users with Teacher Role");
        if(user.getRole() == 1){
            String result = "";
            for(User users : repository.findAll()){
                if(users.getRole() == 2){
                    result += "<div>" + users.toString() + "</div>";
                }
            }
            modelAndView.addObject("adminMessage",result);
        }
        modelAndView.setViewName("teacher/home");
        return modelAndView;
    }
}


