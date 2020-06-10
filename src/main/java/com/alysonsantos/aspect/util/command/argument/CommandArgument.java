package com.alysonsantos.aspect.util.command.argument;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommandArgument<T> {

    private ArgumentType<T> type;
    public Class<T> getClassType() {
        return type.getClazz();
    }

    private boolean array;

    private T defaultValue;
    private boolean nullable;

}
