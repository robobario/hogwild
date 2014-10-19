package nz.hogwild.service;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.List;

public class ListSpoilerDiscarder {

    public static <T> List<T> discard(List<T> toHide, int numAuthors, int authorIndex){
        int lastIndex = toHide.size() - 1;
        int lastAuthor = lastIndex % numAuthors;
        if(lastAuthor == authorIndex){
            return toHide;
        }else{
            int modifier = lastAuthor > authorIndex ? 0 : numAuthors;
            return limit(toHide, numAuthors, authorIndex, lastIndex, modifier);
        }
    }

    private static <T> List<T> limit(List<T> toHide, int numAuthors, int authorIndex, int lastIndex, int numAuthors1) {
        int b = (lastIndex - (lastIndex % numAuthors)) - numAuthors1 + authorIndex;
        return ImmutableList.copyOf(Iterables.limit(toHide, Math.max(0, b + 1))) ;
    }
}
