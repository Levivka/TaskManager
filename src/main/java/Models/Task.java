package Models;

public class Task {

    protected String id;
    protected String title;
    protected String description;
    protected Enums.Priority priority;
    protected Enums.Status status;
    protected String[] comments;
    protected User owner;
    protected User executor;

    public Enums.Priority getPriority() {
        return priority;
    }

    public void setPriority(Enums.Priority priority) {
        this.priority = priority;
    }

    public Enums.Status getStatus() {
        return status;
    }

    public void setStatus(Enums.Status status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
