package demo.codingnomads.co.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TwoFactorAuthenticationController {

    //TODO see why .html required
    @GetMapping("/login")
    public ModelAndView getLoginPage(@RequestParam(value = "error", required = false) boolean isError,
                                     @RequestBody(required = false) String errorMessage) {

        ModelAndView modelAndView =  new ModelAndView();

        //no redirect required. return login page
        modelAndView.setViewName("login.html");

        //add error message if present
        if(isError) {
            modelAndView.addObject("isError",true);
            if(null == errorMessage) {
                errorMessage = "Something went wrong :/";
            }

            modelAndView.addObject("errorMessage", errorMessage);

        }else{
            modelAndView.addObject("isError",false);
        }

        return modelAndView;
    }

    @GetMapping("/2FA")
    public Object getTwoFactorAuthPage() {
        return new ModelAndView("two-factor-auth-page.html");
    }
}
