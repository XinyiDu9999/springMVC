package org.example.web;


import org.example.entity.User;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.concurrent.Callable;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(){
        return "room";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public String login(@RequestBody User user) {

        User ret = userService.getUserByUsernameAndPassword(user.getUsername(),user.getPassword());
//        if(ret == null) {
//            model.addAttribute("message", "not found this user");
//        }else{
//            model.addAttribute("message","the user is existed, having id " +
//                   ret.getId());
//        }
        if(ret == null){
            return "{\"userExisted\":false}";
        }else {
            return "{\"userExisted\":true}";
        }


    }
    @RequestMapping(value = "/message", method = RequestMethod.GET)
    public String getMessage(Model model){
        model.addAttribute("message", "today is good");
        return "message";

    }

    @GetMapping("/users")
    public Callable<List<User>> users() {
        return () -> {
            // 模拟3秒耗时:
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
            }
            return userService.getAllUsers();
        };
    }


}
