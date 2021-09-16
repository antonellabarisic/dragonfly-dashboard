package edu.unm.dragonfly.msgs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.auto.value.AutoValue;

/**
 * @author John Ericksen
 */
@AutoValue
@JsonDeserialize(builder = AutoValue_DDSARequest.Builder.class)
public abstract class DDSARequest {
//    @JsonProperty("command_time")
//    public abstract Time commandTime();
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
    @JsonProperty
    public abstract double waitTime();
    @JsonProperty("distance_threshold")
    public abstract double distanceThreshold();

    public static Builder builder() {
        return new AutoValue_DDSARequest.Builder();
    }

    @AutoValue.Builder
    @JsonPOJOBuilder(withPrefix = "")
    public abstract static class Builder {
//        @JsonProperty("command_time")
//        public abstract Builder commandTime(Time time);
        public abstract Builder radius(double radius);
        @JsonProperty("step_length")
        public abstract Builder stepLength(double stepLength);
        public abstract Builder walk(int walk);
        public abstract Builder stacks(int stacks);
        public abstract Builder loops(int loops);
        public abstract Builder altitude(double altitude);
        public abstract Builder waitTime(double waitTime);
        @JsonProperty("distance_threshold")
        public abstract Builder distanceThreshold(double distanceThreshold);
        public abstract DDSARequest build();
    }
}