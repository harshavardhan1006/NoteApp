import java.io.Serializable;

public class Note implements Serializable {
    private String id;
    private String title;
    private String content;

    public Note(String id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }

    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }

    @Override
    public String toString() {
        return title;
    }
}
