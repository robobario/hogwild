package nz.hogwild.web;

import com.google.common.base.Objects;
import nz.hogwild.model.Author;
import nz.hogwild.service.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
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

    @RequestMapping(value = "/app/story", method = RequestMethod.GET)
    @ResponseBody
    public ApiStory story(HttpServletRequest request){
        HttpSession session = request.getSession(true);
        String email = sessionStore.get(session.getId());
        Integer authorId = null;
        if(email != null) {
            Author author = storyService.getUser(email);
            authorId = author.getId();
        }
        List<ApiEntry> entries = storyService.getEntries(1, authorId);
        Integer nextAuthor = storyService.getNextAuthor(1);
        return new ApiStory(!(authorId == null) && Objects.equal(nextAuthor,authorId), entries);
    }


    @RequestMapping(value = "/app/story", method = RequestMethod.POST)
    public void addEntry(@RequestBody AddEntry entry, HttpServletRequest request,HttpServletResponse response){
        HttpSession session = request.getSession(true);
        String email = sessionStore.get(session.getId());
        Integer authorId = null;
        if(email != null) {
            Author author = storyService.getUser(email);
            authorId = author.getId();
        }
        if(authorId != null){
            storyService.addEntryToStory(authorId,1, entry.getBody());
        }
        try {
            response.sendRedirect("/");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
