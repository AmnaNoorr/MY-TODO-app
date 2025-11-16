package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TaskV5Filtered implements Serializable {
    private static final long serialVersionUID = 5L;
    private String title, description, priority, category;
    private LocalDate deadline;
    private boolean completed;

    public TaskV5Filtered(String title, String description, String priority, String category, LocalDate deadline) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.category = category;
        this.deadline = deadline;
    }

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public String getDeadlineFormatted() {
        if (deadline == null) return "No Deadline";
        String dayOfWeek = deadline.getDayOfWeek().toString().substring(0, 1)
                + deadline.getDayOfWeek().toString().substring(1).toLowerCase();
        return deadline.format(DateTimeFormatter.ofPattern("dd MMM yyyy")) + " (" + dayOfWeek + ")";
    }
}
