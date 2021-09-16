package edu.unm.dragonfly.msgs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.auto.value.AutoValue;

/**
 * @author John Ericksen
 */
@AutoValue
@JsonDeserialize(builder = AutoValue_DDSAWaypointsRequest.Builder.class)
public abstract class DDSAWaypointsRequest {
    @JsonProperty
    public abstract double radius();
    @JsonProperty("step_length")
    public abstract double stepLength();
    @JsonProperty
    public abstract int walk();
    @JsonProperty
    public abstract int stacks();
    @JsonProperty
    public abstract int loops();
    @JsonProperty
    public abstract double altitude();
    @JsonProperty("wait_time")
    public abstract double waitTime();

    public static Builder builder() {
        return new AutoValue_DDSAWaypointsRequest.Builder();
    }

    @AutoValue.Builder
    @JsonPOJOBuilder(withPrefix = "")
    public abstract static class Builder {
        public abstract Builder radius(double radius);
        @JsonProperty("step_length")
        public abstract Builder stepLength(double stepLength);
        public abstract Builder walk(int walk);
        public abstract Builder stacks(int stacks);
        public abstract Builder loops(int loops);
        public abstract Builder altitude(double altitude);
        @JsonProperty("wait_time")
        public abstract Builder waitTime(double waitTime);
        public abstract DDSAWaypointsRequest build();
    }
}