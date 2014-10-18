package nz.hogwild.web;

import com.google.common.collect.ImmutableList;
import nz.hogwild.model.Entry;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller("/")
public class RootController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    Resource index(){
        return new ClassPathResource("hogwild.html");
    }

    @RequestMapping(value = "/app/story", method = RequestMethod.GET)
    @ResponseBody
    List<Entry> story(HttpServletRequest request){
        HttpSession session = request.getSession(true);
        return ImmutableList.of();
    }
}
