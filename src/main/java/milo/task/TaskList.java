package milo.task;

import java.util.ArrayList;
import java.util.List;

/** Owns Milo's tasks and provides basic collection operations. */
public class TaskList {
    /** Mutable collection of tasks in their display order. */
    private ArrayList<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() {
        this.tasks = new ArrayList<>(100);
    }

    /** Creates a task list initialized with a copy of the supplied tasks. */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /** Adds a task to the end of this list. */
    public void add(Task task) {
        tasks.add(task);
    }

    /** Returns the task at the zero-based index. */
    public Task get(int index) {
        return tasks.get(index);
    }

    /** Removes the task at the zero-based index. */
    public void remove(int index) {
        tasks.remove(index);
    }

    /** Removes all tasks from this list. */
    public void clear() {
        tasks.clear();
    }

    /** Returns the number of tasks in this list. */
    public int size() {
        return tasks.size();
    }

    /** Returns an immutable snapshot of the tasks for reading. */
    public List<Task> asList() {
        return List.copyOf(tasks);
    }
}
