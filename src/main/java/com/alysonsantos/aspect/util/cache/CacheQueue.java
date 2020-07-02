package com.alysonsantos.aspect.util.cache;

import com.alysonsantos.aspect.EconomyPlugin;
import com.alysonsantos.aspect.scheduler.SchedulerTask;
import lombok.Setter;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class CacheQueue<T> {

    private final LinkedBlockingQueue<T> queue = new LinkedBlockingQueue<>();

    @Setter
    private Consumer<T> removalAction;
    private transient SchedulerTask updateTask;

    public CacheQueue() {
        updateTask = createTask();
    }

    public boolean stopQueue() {
        if (updateTask == null) return false;
        updateTask.cancel();
        updateTask = null;
        return true;
    }

    public boolean startQueue() {
        if (updateTask != null) return false;
        updateTask = createTask();
        return true;
    }

    public void addItem(T item) {
        if (!queue.contains(item))
            queue.add(item);
    }

    public void removeItem(Predicate<T> predicate) {
        queue.removeIf(predicate);
    }

    public void updateAll() {
        while (!queue.isEmpty()) {
            update();
        }
    }

    private void update() {
        T item = queue.poll();
        if (item == null) return;

        if (removalAction == null) return;
        removalAction.accept(item);
    }

    private SchedulerTask createTask() {
        return EconomyPlugin.getPlugin().getSchedulerAdapter().asyncRepeating(
                this::updateAll,
                30,
                TimeUnit.SECONDS
        );
    }
}
