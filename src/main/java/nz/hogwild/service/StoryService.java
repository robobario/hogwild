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
    public void addEntryToStory(int authorId, int storyId, String body){
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
        try {
            Session session = sessionFactory.getCurrentSession();
            Story story = (Story) session.get(Story.class, storyId);
            List<Entry> entries = story.getEntries();
            List<Integer> authorIds = getAuthorIds(story);
            List<Entry> discard = loggedInUserId == null ? getVisibleToAll(entries, authorIds) : getVisibleToUser(entries, loggedInUserId, authorIds);
            return toApi(discard, entries.subList(discard.size(), entries.size()));
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public Integer getNextAuthor(int storyId){
        try{
        Session session = sessionFactory.getCurrentSession();
        Story story = (Story) session.get(Story.class, storyId);
        List<Entry> entries = story.getEntries();
        if(entries.isEmpty()){
            return null;
        }else{
            Entry entry = entries.get(entries.size() - 1);
            int lastAuthor = entry.getAuthor().getId();
            List<Integer> authorIds = getAuthorIds(story);
            Integer lastAuthorIndex = authorIds.indexOf(lastAuthor);
            Integer nextAuthorIndex = (lastAuthorIndex + 1) % authorIds.size();
            return authorIds.get(nextAuthorIndex);
        }}catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public Author getUser(String email) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Author.class).add(Restrictions.eq("email", email));
        return (Author) criteria.uniqueResult();
    }

    public static List<Entry> getVisibleToUser(List<Entry> entries, Integer loggedInUserId, List<Integer> authorIds) {
        int authorIndex = authorIds.indexOf(loggedInUserId);
        int previousUserIndex = authorIndex - 1;
        if(previousUserIndex == -1){
            previousUserIndex = authorIds.size() - 1;
        }
        int previousUser = authorIds.get(previousUserIndex);
        for (int i = (entries.size() - 1); i >= 0 ; i--) {
            int id = entries.get(i).getAuthor().getId();
            if(id == loggedInUserId || id == previousUser){
                return entries.subList(0, i + 1);
            }
        }
        return ImmutableList.of();
    }

    private List<Entry> getVisibleToAll(List<Entry> entries, List<Integer> authorIds) {
        return entries.size() > authorIds.size() ? entries.subList(0, entries.size() - authorIds.size()) : ImmutableList.<Entry>of();
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
            element.setCharacterName(entry.getAuthor().getCharacterName());
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
