package milo.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

public class TaskListTest {

    @Test
    public void add_tasksAndGetSize_expectedTasksStoredInOrder() {
        TaskList tasks = new TaskList();
        Task first = new ToDo("first");
        Task second = new ToDo("second");

        tasks.add(first);
        tasks.add(second);

        assertEquals(2, tasks.size());
        assertEquals(first, tasks.get(0));
        assertEquals(second, tasks.get(1));
    }

    @Test
    public void removeThenClear_tasks_expectedTasksRemovedAndListEmptied() {
        TaskList tasks = new TaskList(List.of(new ToDo("first"), new ToDo("second")));

        tasks.remove(0);
        assertEquals(1, tasks.size());
        assertEquals("[T] [ ] second", tasks.get(0).toString());

        tasks.clear();
        assertEquals(0, tasks.size());
    }

    @Test
    public void asList_existingTasks_expectedReadOnlySnapshot() {
        TaskList tasks = new TaskList();
        tasks.add(new ToDo("first"));

        assertEquals(1, tasks.asList().size());
        assertThrows(UnsupportedOperationException.class, () -> tasks.asList().clear());
    }
}
