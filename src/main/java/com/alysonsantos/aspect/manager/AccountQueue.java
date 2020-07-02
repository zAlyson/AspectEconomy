package com.alysonsantos.aspect.manager;

import com.alysonsantos.aspect.models.Account;
import com.alysonsantos.aspect.util.cache.CacheQueue;

public class AccountQueue extends CacheQueue<Account> {

    public AccountQueue() {
        super();
    }

    public void removeItem(Account account) {
        removeItem(it -> it.getName().equals(account.getName()));
    }
}
