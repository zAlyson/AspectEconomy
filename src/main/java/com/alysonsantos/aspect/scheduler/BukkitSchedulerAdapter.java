package com.alysonsantos.aspect.scheduler;

import com.alysonsantos.aspect.EconomyPlugin;

import java.util.concurrent.Executor;

public class BukkitSchedulerAdapter extends AbstractJavaScheduler implements SchedulerAdapter {
    private final Executor sync;

    public BukkitSchedulerAdapter(EconomyPlugin bootstrap) {
        this.sync = r -> bootstrap.getServer().getScheduler().scheduleSyncDelayedTask(bootstrap, r);
    }

    @Override
    public Executor sync() {
        return this.sync;
    }

}
