package nz.hogwild.service;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.util.List;

import static nz.hogwild.service.ListSpoilerDiscarder.discard;
import static org.junit.Assert.*;

public class ListSpoilerDiscarderTest {

    @Test
    public void testDiscard() throws Exception {
        List<Integer> discard = discard(ImmutableList.of(1), 2, 0);
        assertEquals(ImmutableList.of(1),discard);
    }

    @Test
    public void testDiscardSpoilers() throws Exception {
        List<Integer> discard = discard(ImmutableList.of(1, 2), 2, 0);
        assertEquals(ImmutableList.of(1),discard);
    }

    @Test
    public void testFinalaAuthorSeesAll() throws Exception {
        List<Integer> discard = discard(ImmutableList.of(1, 2), 2, 1);
        assertEquals(ImmutableList.of(1,2),discard);
    }


    @Test
    public void testFinalaAuthorSeesOne() throws Exception {
        List<Integer> discard = discard(ImmutableList.of(1, 2, 3), 3, 1);
        assertEquals(ImmutableList.of(1,2),discard);
    }

    @Test
    public void testMultipleCycles() throws Exception {
        List<Integer> discard = discard(ImmutableList.of(1, 2, 3, 4, 5), 3, 2);
        assertEquals(ImmutableList.of(1,2, 3),discard);
    }

    @Test
    public void testMultipleCyclesThing() throws Exception {
        List<Integer> discard = discard(ImmutableList.of(1, 2, 3, 4, 5), 3, 1);
        assertEquals(ImmutableList.of(1,2, 3,4,5),discard);
    }

    @Test
    public void testMultipleCyclesThing2() throws Exception {
        List<Integer> discard = discard(ImmutableList.of(1, 2, 3, 4, 5), 3, 0);
        assertEquals(ImmutableList.of(1,2, 3,4),discard);
    }
}