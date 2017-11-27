package org.xeter.flyertracker.android.flyer.list;

public class Flyer {

    private final long id;
    private final String title;
    private final String description;
    private final String imagePath;

    public Flyer() {
        this(0, null, null, null);
    }

    public Flyer(long id, Flyer flyer) {
        this(id, flyer.getTitle(), flyer.getDescription(), flyer.getImagePath());
    }

    public Flyer(long id, String newTitle, String newDescription, String imagePath) {
        this.id = id;
        this.title = newTitle;
        this.description = newDescription;
        this.imagePath = imagePath;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImagePath() {
        return imagePath;
    }
}
