package nz.hogwild.service;

import com.google.common.collect.ImmutableList;
import nz.hogwild.model.Author;
import nz.hogwild.model.Entry;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class StoryServiceTest {

    @Test
    public void test(){
        Entry element = entryForAuthorId(1);
        ImmutableList<Entry> of = ImmutableList.of(element);
        List<Entry> visibleToUser = StoryService.getVisibleToUser(of, 1, ImmutableList.of(1, 2, 4, 3));
        assertEquals(of, visibleToUser);
    }


    @Test
    public void test2(){
        Entry element = entryForAuthorId(1);
        Entry element2 = entryForAuthorId(2);
        ImmutableList<Entry> of = ImmutableList.of(element,element2);
        List<Entry> visibleToUser = StoryService.getVisibleToUser(of, 1, ImmutableList.of(1, 2, 4, 3));
        assertEquals(ImmutableList.of(element), visibleToUser);
    }


    @Test
    public void test3(){
        Entry element = entryForAuthorId(1);
        Entry element2 = entryForAuthorId(2);
        ImmutableList<Entry> of = ImmutableList.of(element,element2);
        List<Entry> visibleToUser = StoryService.getVisibleToUser(of, 2, ImmutableList.of(1, 2, 4, 3));
        assertEquals(ImmutableList.of(element,element2), visibleToUser);
    }


    @Test
    public void test4(){
        Entry element = entryForAuthorId(1);
        Entry element2 = entryForAuthorId(2);
        ImmutableList<Entry> of = ImmutableList.of(element,element2);
        List<Entry> visibleToUser = StoryService.getVisibleToUser(of, 4, ImmutableList.of(1, 2, 4, 3));
        assertEquals(ImmutableList.of(element,element2), visibleToUser);
    }

    @Test
    public void test5(){
        Entry element = entryForAuthorId(1);
        Entry element2 = entryForAuthorId(2);
        ImmutableList<Entry> of = ImmutableList.of(element,element2);
        List<Entry> visibleToUser = StoryService.getVisibleToUser(of, 3, ImmutableList.of(1, 2, 4, 3));
        assertEquals(ImmutableList.<Entry>of(), visibleToUser);
    }


    @Test
    public void test6(){
        Entry element = entryForAuthorId(1);
        Entry element2 = entryForAuthorId(2);
        Entry element3 = entryForAuthorId(4);
        Entry element4 = entryForAuthorId(3);
        Entry element5 = entryForAuthorId(1);
        ImmutableList<Entry> of = ImmutableList.of(element,element2,element3,element4,element5);
        List<Entry> visibleToUser = StoryService.getVisibleToUser(of, 4, ImmutableList.of(1, 2, 4,3));
        assertEquals(ImmutableList.of(element, element2, element3), visibleToUser);
    }


    private Entry entryForAuthorId(int id) {
        Entry element = new Entry();
        Author author = new Author();
        author.setId(id);
        element.setAuthor(author);
        return element;
    }


}