package nz.hogwild.service;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import nz.hogwild.model.Entry;
import nz.hogwild.model.Story;
import nz.hogwild.model.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class StoryService {

    @Resource
    private SessionFactory sessionFactory;

    @Transactional
    public void addEntryToStory(long authorId, long storyId, String body){
        Session session = sessionFactory.getCurrentSession();
        User user = (User) session.get(User.class, authorId);
        Story story = (Story) session.get(Story.class, storyId);
        Entry entry = new Entry();
        entry.setAuthor(user);
        entry.setStory(story);
        entry.setBody(body);
        story.addEntry(entry);
        session.save(story);
    }

    @Transactional
    public List<ApiEntry> getEntries(long storyId, Integer loggedInUserId){
        Session session = sessionFactory.getCurrentSession();
        Story story = (Story) session.get(Story.class, storyId);
        List<Entry> entries = story.getEntries();
        List<Integer> authorIds = getAuthorIds(story);
        Integer position = loggedInUserId == null ? 0 : authorIds.get(loggedInUserId);
        List<Entry> discard = ListSpoilerDiscarder.discard(entries, authorIds.size(), position);
        return toApi(discard, entries.subList(discard.size(), entries.size()));
    }

    @Transactional
    public User getUser(String email) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(User.class).add(Restrictions.eq("email", email));
        return (User) criteria.uniqueResult();
    }

    private List<ApiEntry> toApi(List<Entry> discard, List<Entry> entries) {
        ImmutableList.Builder<ApiEntry> builder = ImmutableList.builder();
        for (Entry entry : discard) {
            ApiEntry element = new ApiEntry();
            element.setBody(entry.getBody());
            element.setAuthorEmail(entry.getAuthor().getEmail());
            element.setSpoiler(false);
            builder.add(element);
        }
        for (Entry entry : entries){
            ApiEntry element = new ApiEntry();
            element.setAuthorEmail(entry.getAuthor().getEmail());
            element.setSpoiler(true);
            builder.add(element);
        }
        return builder.build();
    }

    private List<Integer> getAuthorIds(Story story) {
        List<User> author = story.getAuthors();
        List<Integer> ids = newArrayList();
        for (User user : author) {
            ids.add(user.getId());
        }
        return ids;
    }

}
