package demo.codingnomads.co.controllers;

import demo.codingnomads.co.services.TwoFactorAuthCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TwoFactorAuthenticationController {

    @Autowired
    TwoFactorAuthCodeService authService;

    //TODO see why .html required
    @GetMapping("/login")
    public ModelAndView getLoginPage(@RequestParam(value = "error", defaultValue = "false") boolean isError,
                                     @RequestBody(required = false) String errorMessage,
                                     @CookieValue(value = "MONSTER", required = false) String confirmRecentLoginCookie) {

        ModelAndView modelAndView =  new ModelAndView();

        //if already logged in redirect to home page
        if(authService.isAlreadyAuthenticated()) {
            modelAndView.setViewName("redirect:/recipes");
            return modelAndView;
        }

        //redirect to 2FA page if code is already assigned
        if(confirmRecentLoginCookie !=null && authService.isAwaitingCode(confirmRecentLoginCookie)) {
            modelAndView.setViewName("redirect:/2FA");
            return modelAndView;
        }

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

    @PostMapping("/2FA/authenticate")
    public ModelAndView redirectToLogin() {
       return new ModelAndView("redirect:/recipes");
    }

}
