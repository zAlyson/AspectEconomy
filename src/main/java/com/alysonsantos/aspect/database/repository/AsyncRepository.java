package com.alysonsantos.aspect.database.repository;

import com.alysonsantos.aspect.EconomyPlugin;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Consumer;

@AllArgsConstructor
public class AsyncRepository<ID, R> {

    private Repository<ID, R> repository;

    public void find(ID id, @NonNull Consumer<R> consumer) {
        makeFuture(() -> {
            consumer.accept(repository.find(id));
        });
    }

    public void insert(ID id, R r, Consumer<Integer> consumer) {
        makeFuture(() -> {
            Integer result = repository.insert(id, r);
            if (consumer != null) consumer.accept(result);
        });
    }

    public void update(ID id, R r) {
        makeFuture(() -> {
           repository.update(id, r);
        });
    }

    public void delete(ID id, Consumer<Integer> consumer) {
        makeFuture(() -> {
            Integer result = repository.delete(id);
            if (consumer != null) consumer.accept(result);
        });
    }

    private CompletableFuture<Void> makeFuture(Runnable runnable) {
        return CompletableFuture.runAsync(() -> {
            try {
                runnable.run();
            } catch (Exception e) {
                if (e instanceof RuntimeException) {
                    throw (RuntimeException) e;
                }
                throw new CompletionException(e);
            }
        }, EconomyPlugin.getPlugin().getSchedulerAdapter().async());
    }
}