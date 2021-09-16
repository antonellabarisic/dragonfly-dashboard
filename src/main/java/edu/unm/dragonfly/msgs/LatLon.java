package edu.unm.dragonfly.msgs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.auto.value.AutoValue;


/**
 * @author John Ericksen
 */
@AutoValue
@JsonDeserialize(builder = AutoValue_LatLon.Builder.class)
public abstract class LatLon {
    @JsonProperty
    public abstract double latitude();
    @JsonProperty
    public abstract double longitude();
    @JsonProperty("relative_altitude")
    public abstract double relativeAltitude();

    public static Builder builder() {
        return new AutoValue_LatLon.Builder();
    }

    @AutoValue.Builder
    @JsonPOJOBuilder(withPrefix = "")
    public abstract static class Builder {
        public abstract Builder latitude(double latitude);
        public abstract Builder longitude(double longitude);
        @JsonProperty("relative_altitude")
        public abstract Builder relativeAltitude(double relativeAltitude);
        public abstract LatLon build();
    }
}
