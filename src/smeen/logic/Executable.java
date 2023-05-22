package smeen.logic;

import smeen.logic.SmeenContext;

/**
 * Represent a class that can be executed, given a context.
 */
public interface Executable {

    /**
     * @param shouldBreak whether the outer code block list (that is part of loop) should break out of the loop
     */
    record Result(boolean shouldBreak, boolean shouldStop) {
    }

    /**
     * Execute this object with the given context.
     *
     * @param context a context to be used.
     * @return the execution result
     */
    Result execute(SmeenContext context);

}
