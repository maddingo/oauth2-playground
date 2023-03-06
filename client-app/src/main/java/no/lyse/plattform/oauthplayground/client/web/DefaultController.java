package no.lyse.plattform.oauthplayground.client.web;

import org.springframework.web.bind.annotation.GetMapping;

public class DefaultController {
    @GetMapping("/")
    public String root() {
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }

}
