package com.modernfrontendshtmx.bookmarks;

public class CreateBookmarkFormData {
    private String name;
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Bookmark toBookmark(int id) {
        return new Bookmark(id, name, url);
    }
}
