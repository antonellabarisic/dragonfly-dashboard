package edu.unm.dragonfly.msgs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.auto.value.AutoValue;

import java.util.List;

/**
 * @author John Ericksen
 */
@AutoValue
@JsonDeserialize(builder = AutoValue_LawnmowerRequest.Builder.class)
public abstract class LawnmowerRequest {
//    @JsonProperty("command_time")
//    public abstract Time commandTime();
    @JsonProperty
    public abstract List<LatLon> boundary();
    @JsonProperty("step_length")
    public abstract double stepLength();
    @JsonProperty("walk_boundary")
    public abstract boolean walkBoundary();
    @JsonProperty
    public abstract int walk();
    @JsonProperty
    public abstract int stacks();
    @JsonProperty
    public abstract double altitude();
    @JsonProperty
    public abstract double waitTime();
    @JsonProperty("distance_threshold")
    public abstract double distanceThreshold();

    public static Builder builder() {
        return new AutoValue_LawnmowerRequest.Builder();
    }

    @AutoValue.Builder
    @JsonPOJOBuilder(withPrefix = "")
    public abstract static class Builder {
//        @JsonProperty("command_time")
//        public abstract Builder commandTime(Time time);
        public abstract Builder boundary(List<LatLon> boundary);
        @JsonProperty("step_length")
        public abstract Builder stepLength(double length);
        @JsonProperty("walk_boundary")
        public abstract Builder walkBoundary(boolean walkBoundary);
        public abstract Builder walk(int walk);
        public abstract Builder stacks(int stacks);
        public abstract Builder altitude(double altitude);
        public abstract Builder waitTime(double time);
        @JsonProperty("distance_threshold")
        public abstract Builder distanceThreshold(double threshold);
        public abstract LawnmowerRequest build();
    }
}
