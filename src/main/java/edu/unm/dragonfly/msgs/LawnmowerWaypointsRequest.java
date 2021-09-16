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
@JsonDeserialize(builder = AutoValue_LawnmowerWaypointsRequest.Builder.class)
public abstract class LawnmowerWaypointsRequest {
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
    @JsonProperty("wait_time")
    public abstract double waitTime();

    public static Builder builder() {
        return new AutoValue_LawnmowerWaypointsRequest.Builder();
    }

    @AutoValue.Builder
    @JsonPOJOBuilder(withPrefix = "")
    public abstract static class Builder {
        public abstract Builder boundary(List<LatLon> boundary);
        @JsonProperty("step_length")
        public abstract Builder stepLength(double stepLength);
        @JsonProperty("walk_boundary")
        public abstract Builder walkBoundary(boolean walkBoundary);
        public abstract Builder walk(int walk);
        public abstract Builder stacks(int stacks);
        public abstract Builder altitude(double altitude);
        @JsonProperty("wait_time")
        public abstract Builder waitTime(double waitTime);
        public abstract LawnmowerWaypointsRequest build();
    }
}