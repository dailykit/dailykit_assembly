package org.dailykit.logs;

import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import timber.log.Timber;

public class ReleaseTree extends Timber.Tree{

    private static final int MAX_LOG_LENGTH = 4000;

    @Override
    protected boolean isLoggable(@Nullable String tag, int priority) {
        if(priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO){
            return false;
        }
        return true;
    }

    @Override
    protected void log(int priority, @Nullable String tag, @NotNull String message, @Nullable Throwable t) {
        if(isLoggable(priority)){
            if(message.length() < MAX_LOG_LENGTH){
                if(priority == Log.ASSERT){
                    Log.wtf(tag,message);
                }
                else{
                    Log.println(priority,tag,message);
                }
                return;
            }

            for(int i=0,length = message.length();i<length;i++){
                int newline = message.indexOf('\n',i);
                newline = newline != -1?newline: length;
                do {
                    int end = Math.min(newline,i+MAX_LOG_LENGTH);
                    String part = message.substring(i,end);
                    if(priority == Log.ASSERT){
                        Log.wtf(tag,part);
                    }
                    else {
                        Log.println(priority,tag,part);
                    }
                    i=end;
                } while(i<newline);
            }
        }
    }
}
