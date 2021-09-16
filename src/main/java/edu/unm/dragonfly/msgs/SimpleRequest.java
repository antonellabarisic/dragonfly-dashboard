package edu.unm.dragonfly.msgs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.auto.value.AutoValue;

/**
 * @author John Ericksen
 */
@AutoValue
public abstract class SimpleRequest {
//    @JsonProperty("command_time")
//    public abstract Time commandTime();

//    @JsonCreator
//    public static SimpleRequest create(@JsonProperty("command_time") Time startTime) {
//        return new AutoValue_SimpleRequest(startTime);
//    }

    @JsonCreator
    public static SimpleRequest create() {
        return new AutoValue_SimpleRequest();
    }

//    public static SimpleRequest now() {
//        return create(Time.now());
//    }
}
