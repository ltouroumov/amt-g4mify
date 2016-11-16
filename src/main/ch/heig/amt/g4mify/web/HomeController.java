package ch.heig.amt.g4mify.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author ldavid
 * @created 11/9/16
 */
@Controller
public class HomeController {

    @RequestMapping("/")
    public String index(Model model) {
        return "home/index";
    }

}
