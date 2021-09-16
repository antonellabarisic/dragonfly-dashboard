package edu.unm.dragonfly.msgs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import ros.msgs.std_msgs.Header;
import ros.msgs.std_msgs.Time;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author John Ericksen
 */
public class JacksonSerializationTest {

    private static final List<LatLon> TEST_LAT_LON_LIST = Collections.singletonList(LatLon.builder().latitude(1).longitude(2).relativeAltitude(3).build());
    private static final Header TEST_HEADER = Header.create(1, Time.create(2, 3), "frame1");

    @Test
    public void testMarshalling() throws IOException {
        testMarshalling(DDSARequest.class, DDSARequest.builder()
//                .commandTime(Time.create(9, 10))
                .altitude(1)
                .distanceThreshold(2)
                .loops(3)
                .radius(4)
                .stacks(5)
                .stepLength(6)
                .waitTime(7)
                .walk(8)
                .build());

        testMarshalling(DDSAWaypointsRequest.class, DDSAWaypointsRequest.builder()
                .altitude(1)
                .loops(2)
                .radius(3)
                .stacks(4)
                .stepLength(5)
                .waitTime(6)
                .walk(7)
                .build());

        testMarshalling(DDSAWaypointsResponse.class, DDSAWaypointsResponse.create(TEST_LAT_LON_LIST));

        testMarshalling(LatLon.class, LatLon.builder().latitude(4).longitude(5).relativeAltitude(6).build());

        testMarshalling(LawnmowerRequest.class, LawnmowerRequest.builder()
//                .commandTime(Time.create(9, 10))
                .altitude(1)
                .boundary(TEST_LAT_LON_LIST)
                .distanceThreshold(3)
                .stacks(4)
                .stepLength(5)
                .waitTime(6)
                .walk(7)
                .walkBoundary(true)
                .build());

        testMarshalling(LawnmowerWaypointsRequest.class, LawnmowerWaypointsRequest.builder()
                .altitude(1)
                .boundary(TEST_LAT_LON_LIST)
                .stacks(4)
                .stepLength(5)
                .waitTime(6)
                .walk(7)
                .walkBoundary(true)
                .build());

        testMarshalling(LawnmowerWaypointsResponse.class, LawnmowerWaypointsResponse.create(TEST_LAT_LON_LIST));

        testMarshalling(MavrosState.class, MavrosState.create(TEST_HEADER, true, false, true, false, "mode", MavrosState.SystemStatus.MAV_STATE_CALIBRATING));

        testMarshalling(NavigationRequest.class, NavigationRequest.builder()
//                .commandTime(Time.create(9, 10))
                .distanceThreshold(1)
                .waitTime(2)
                .waypoints(TEST_LAT_LON_LIST)
                .build());

        testMarshalling(NavSatFix.class, NavSatFix.create(TEST_HEADER, NavSatStatus.create(2, 3), 4, 5, 6, new double[]{7, 8}, 9));

        testMarshalling(NavSatStatus.class, NavSatStatus.create(1, 2));

        testMarshalling(Point.class, Point.create(1, 2, 3));

        testMarshalling(Pose.class, Pose.create(Point.create(1, 2, 3), Quaternion.create(1, 2, 3, 4)));

        testMarshalling(PoseStamped.class, PoseStamped.create(TEST_HEADER, Pose.create(Point.create(1, 2, 3), Quaternion.create(4, 5, 6, 7))));

        testMarshalling(Quaternion.class, Quaternion.create(1, 2, 3, 4));

        testMarshalling(Response.class, Response.create(1, "test"));
    }

    private <T> void testMarshalling(Class<T> clazz, T input) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        String serialized = mapper.writeValueAsString(input);
        Object output = mapper.readValue(serialized, clazz);

        assertEquals(input, output);
    }
}
