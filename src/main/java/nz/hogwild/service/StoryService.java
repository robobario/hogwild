package nz.hogwild.service;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import nz.hogwild.model.Author;
import nz.hogwild.model.Entry;
import nz.hogwild.model.Story;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class StoryService {

    @Resource
    private SessionFactory sessionFactory;

    @Transactional
    public void addEntryToStory(long authorId, long storyId, String body){
        Session session = sessionFactory.getCurrentSession();
        Author author = (Author) session.get(Author.class, authorId);
        Story story = (Story) session.get(Story.class, storyId);
        Entry entry = new Entry();
        entry.setAuthor(author);
        entry.setStory(story);
        entry.setBody(body);
        story.addEntry(entry);
        session.save(story);
    }

    @Transactional
    public List<ApiEntry> getEntries(int storyId, Integer loggedInUserId){
        Session session = sessionFactory.getCurrentSession();
        Story story = (Story) session.get(Story.class, storyId);
        List<Entry> entries = story.getEntries();
        List<Integer> authorIds = getAuthorIds(story);
        List<Entry> discard = loggedInUserId == null ? getVisibleToAll(entries, authorIds) : getVisibleToUser(entries,loggedInUserId, authorIds);
        return toApi(discard, entries.subList(discard.size(), entries.size()));
    }

    private List<Entry> getVisibleToUser(List<Entry> entries, Integer loggedInUserId, List<Integer> authorIds) {
        int authorIndex = authorIds.indexOf(loggedInUserId);
        Integer previousUser = authorIds.get(authorIndex - 1 % authorIds.size());
        for (int i = entries.size() - 1; i >= 0 ; i--) {
            int id = entries.get(i).getId();
            if(entries.get(i).getId() == authorIndex || id == previousUser){
                return entries.subList(0, i);
            }
        }
        return ImmutableList.of();
    }

    private List<Entry> getVisibleToAll(List<Entry> entries, List<Integer> authorIds) {
        return entries.size() > authorIds.size() ? entries.subList(0, entries.size() - authorIds.size()) : ImmutableList.<Entry>of();
    }

    @Transactional
    public Author getUser(String email) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Author.class).add(Restrictions.eq("email", email));
        return (Author) criteria.uniqueResult();
    }

    private List<ApiEntry> toApi(List<Entry> discard, List<Entry> entries) {
        ImmutableList.Builder<ApiEntry> builder = ImmutableList.builder();
        for (Entry entry : discard) {
            ApiEntry element = new ApiEntry();
            element.setBody(entry.getBody());
            element.setCharacterName(entry.getAuthor().getCharacterName());
            element.setSpoiler(false);
            builder.add(element);
        }
        for (Entry entry : entries){
            ApiEntry element = new ApiEntry();
            element.setCharacterName(entry.getAuthor().getEmail());
            element.setSpoiler(true);
            builder.add(element);
        }
        return builder.build();
    }

    private List<Integer> getAuthorIds(Story story) {
        List<Author> author = story.getAuthors();
        List<Integer> ids = newArrayList();
        for (Author user : author) {
            ids.add(user.getId());
        }
        return ids;
    }

}
