package org.dailykit.logs;

import timber.log.Timber;

public class DevelopmentTree extends Timber.DebugTree {

    @Override
    protected String createStackElementTag(StackTraceElement element) {
        return String.format("%s %s | %s",
                element.getLineNumber(),
                super.createStackElementTag(element),
                element.getMethodName());
    }

}
