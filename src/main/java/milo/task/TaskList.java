package milo.task;

import java.util.ArrayList;
import java.util.List;

public class TaskList {
    private ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>(100);
    }

    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    public void add(Task task) {
        tasks.add(task);
    }

    public Task get(int index) {
        return tasks.get(index);
    }

    public void remove(int index) {
        tasks.remove(index);
    }

    public void clear() {
        tasks.clear();
    }

    public int size() {
        return tasks.size();
    }

    /**
     * Returns the tasks whose descriptions contain the keyword, ignoring case.
     *
     * @param keyword text to search for
     * @return matching tasks in their original order
     */
    public TaskList find(String keyword) {
        TaskList searchResults = new TaskList();

        for (Task task : this.tasks) {
            if (task.containsKeyword(keyword)) {
                searchResults.add(task);
            }
        }

        return searchResults;
    }

    public List<Task> asList() {
        return List.copyOf(tasks);
    }
}
