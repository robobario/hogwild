package nz.hogwild.service;

import java.util.List;

public class ApiStory {
    private boolean isMyTurn;
    private List<ApiEntry> entries;

    public ApiStory(boolean isMyTurn, List<ApiEntry> entries) {
        this.isMyTurn = isMyTurn;
        this.entries = entries;
    }

    public boolean isMyTurn() {
        return isMyTurn;
    }

    public List<ApiEntry> getEntries() {
        return entries;
    }
}
