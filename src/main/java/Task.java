public class Task {
    private static int counter = 0;
    private int taskNum;
    private String description;
    private boolean isDone;

    public Task(String description) {
        counter++;
        this.taskNum = counter;
        this.description = description;
        this.isDone = false;
    }

    public void markAsDone() {
        this.isDone = true;
    }

    public void markAsUndone() {
        this.isDone = false;
    }

    @Override
    public String toString() {
        String marker = isDone ? "X" : " ";
        return String.format("%d.[%s] %s", taskNum, marker, description);
    }

}
