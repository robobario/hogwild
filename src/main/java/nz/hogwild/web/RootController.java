package nz.hogwild.web;

import nz.hogwild.model.Author;
import nz.hogwild.service.ApiEntry;
import nz.hogwild.service.SessionStore;
import nz.hogwild.service.StoryService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller("/")
public class RootController {

    @javax.annotation.Resource
    private StoryService storyService;

    @javax.annotation.Resource
    private SessionStore sessionStore;


    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    Resource index(){
        return new ClassPathResource("hogwild.html");
    }

    @RequestMapping(value = "/app/story/{id}/", method = RequestMethod.GET)
    @ResponseBody
    List<ApiEntry> story(HttpServletRequest request, @PathVariable("id") int id){
        HttpSession session = request.getSession(true);
        String email = sessionStore.get(session.getId());
        Integer id1 = null;
        if(email != null) {
            Author author = storyService.getUser(email);
            id1 = author.getId();
        }
        return storyService.getEntries(id, id1);
    }
}