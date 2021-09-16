package edu.unm.dragonfly;

import com.esri.arcgisruntime.geometry.Point;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.unm.dragonfly.msgs.DDSARequest;
import edu.unm.dragonfly.msgs.DDSAWaypointsRequest;
import edu.unm.dragonfly.msgs.LatLon;
import edu.unm.dragonfly.msgs.LawnmowerRequest;
import edu.unm.dragonfly.msgs.LawnmowerWaypointsRequest;
import edu.unm.dragonfly.msgs.LawnmowerWaypointsResponse;
import edu.unm.dragonfly.msgs.MavrosState;
import edu.unm.dragonfly.msgs.NavSatFix;
import edu.unm.dragonfly.msgs.NavigationRequest;
import edu.unm.dragonfly.msgs.PoseStamped;
import edu.unm.dragonfly.msgs.Response;
import edu.unm.dragonfly.msgs.SetupRequest;
import edu.unm.dragonfly.msgs.SimpleRequest;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.SingleSubject;
import io.reactivex.subjects.Subject;
import ros.RosBridge;
import ros.RosListenDelegate;
import ros.msgs.std_msgs.PrimitiveMsg;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Drone {

    private final String name;
    private final Subject<MavrosState.SystemStatus> statusSubject = BehaviorSubject.createDefault(MavrosState.SystemStatus.MAV_STATE_UNINIT);
    private final RosBridge bridge;
    private final BehaviorSubject<NavSatFix> position = BehaviorSubject.create();
    private final BehaviorSubject<PoseStamped> localPosition = BehaviorSubject.create();
    private final PublishSubject<String> logSubject = PublishSubject.create();
    private final Observable<LatLonRelativeAltitude> relativeAltitudeObservable;

    public Drone(RosBridge bridge, String name) {
        this.bridge = bridge;
        this.name = name;

        relativeAltitudeObservable = Observable.combineLatest(position, localPosition,
                (navSatFix, poseStamped) -> new LatLonRelativeAltitude(navSatFix.latitude(),
                        navSatFix.longitude(),
                        poseStamped.pose().position().z()));
    }

    public void init() {

        bridge.subscribe("/mavros/global_position/global", "sensor_msgs/msg/NavSatFix",
                new JsonRosListenerDelegate<>(NavSatFix.class, position::onNext));

        bridge.subscribe("/mavros/local_position/pose", "geometry_msgs/msg/PoseStamped",
                new JsonRosListenerDelegate<>(PoseStamped.class, localPosition::onNext));

        bridge.subscribe("/log", "std_msgs/msg/String",
                new JsonRosListenerDelegate<PrimitiveMsg<String>>(PrimitiveMsg.class, value -> logSubject.onNext(value.data())));

        bridge.subscribe("/mavros/state", "mavros_msgs/msg/State",
                new JsonRosListenerDelegate<>(MavrosState.class, value -> statusSubject.onNext(value.systemStatus())));
    }

    public void lawnmower(List<Point> boundaryPoints, float stepLength, float altitude, int stacks, boolean walkBoundary, int walk, float waitTime, float distanceThreshold) {
        LawnmowerRequest request = LawnmowerRequest.builder()
//                .commandTime(Time.now())
                .boundary(boundaryPoints.stream().map(mapToLatLon()).collect(Collectors.toList()))
                .stepLength(stepLength)
                .walkBoundary(walkBoundary)
                .stacks(stacks)
                .altitude(altitude)
                .walk(walk)
                .waitTime(waitTime)
                .distanceThreshold(distanceThreshold)
                .build();

        bridge.call("/command/lawnmower", "dragonfly_messages/srv/Lawnmower", request, noop());
    }

    public Single<List<Point>> getLawnmowerWaypoints(List<Point> boundaryPoints, float stepLength, float altitude, int stacks, boolean walkBoundary, int walk, float waitTime) {
        LawnmowerWaypointsRequest request = LawnmowerWaypointsRequest.builder()
                .boundary(boundaryPoints.stream().map(mapToLatLon()).collect(Collectors.toList()))
                .stepLength(stepLength)
                .walkBoundary(walkBoundary)
                .stacks(stacks)
                .altitude(altitude)
                .walk(walk)
                .waitTime(waitTime)
                .build();

        SingleSubject<List<Point>> result = SingleSubject.create();

        bridge.call("/build/lawnmower", "dragonfly_messages/srv/LawnmowerWaypoints", request,
                new JsonRosListenerDelegate<>(LawnmowerWaypointsResponse.class,
                        value -> result.onSuccess(value.waypoints().stream().map(mapToPoint()).collect(Collectors.toList()))));

        return result;
    }

    private Function<Point, LatLon> mapToLatLon() {
        return input -> LatLon.builder()
                .latitude(input.getY())
                .longitude(input.getX())
                .relativeAltitude(input.getZ())
                .build();
    }

    private Function<LatLon, Point> mapToPoint() {
        return input -> new Point(input.longitude(), input.latitude(), input.relativeAltitude());
    }

    public void ddsa(float radius, float stepLength, float altitude, int loops, int stacks, int walk, float waitTime, float distanceThreshold) {
        DDSARequest request = DDSARequest.builder()
//                .commandTime(Time.now())
                .radius(radius)
                .stepLength(stepLength)
                .stacks(stacks)
                .altitude(altitude)
                .walk(walk)
                .waitTime(waitTime)
                .loops(loops)
                .distanceThreshold(distanceThreshold)
                .build();

        bridge.call("/command/ddsa", "dragonfly_messages/srv/DDSA", request, noop());

    }

    public Single<List<Point>> getDDSAWaypoints(float radius, float stepLength, float altitude, int loops, int stacks, int walk, float waitTime) {
        DDSAWaypointsRequest request = DDSAWaypointsRequest.builder()
                .radius(radius)
                .stepLength(stepLength)
                .stacks(stacks)
                .altitude(altitude)
                .walk(walk)
                .waitTime(waitTime)
                .loops(loops)
                .build();

        SingleSubject<List<Point>> result = SingleSubject.create();

        bridge.call("/build/ddsa", "dragonfly_messages/srv/DDSAWaypoints", request,
                new JsonRosListenerDelegate<>(LawnmowerWaypointsResponse.class,
                        value -> result.onSuccess(value.waypoints().stream().map(mapToPoint()).collect(Collectors.toList()))));

        return result;
    }

    public void navigate(List<Point> waypoints, float distanceThreshold) {
        NavigationRequest request = NavigationRequest.builder()
//                .commandTime(Time.now())
                .waypoints(waypoints.stream().map(mapToLatLon()).collect(Collectors.toList()))
                .waitTime(0)
                .distanceThreshold(distanceThreshold)
                .build();

        bridge.call("/command/navigate", "dragonfly_messages/srv/Navigation", request, noop());
    }

    private RosListenDelegate noop() {
        return new JsonRosListenerDelegate<>(Response.class, value -> {});
    }

    public void cancel() {

        bridge.call("/command/cancel", "dragonfly_messages/srv/Simple", SimpleRequest.create(), (data, stringRep) -> System.out.println("Cancel sent to " + name));
    }

    public Observable<MavrosState.SystemStatus> getStatus() {
        return statusSubject;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public Observable<String> getLog() {
        return logSubject;
    }

    public Observable<LatLonRelativeAltitude> getLatestPosition() {
        return relativeAltitudeObservable.take(1);
    }

    public Observable<LatLonRelativeAltitude> getPositions() {
        return relativeAltitudeObservable;
    }

    public void shutdown() {
        position.onComplete();
        localPosition.onComplete();
        logSubject.onComplete();
    }

    public void takeoff() {
        bridge.call("/command/takeoff", "dragonfly_messages/srv/Simple", SimpleRequest.create(),
                (data, stringRep) -> System.out.println("Takeoff sent to " + name));
    }

    public void land() {
        bridge.call("/command/land", "dragonfly_messages/srv/Simple", SimpleRequest.create(),
                (data, stringRep) -> System.out.println("Land sent to " + name));
    }

    public void rtl() {
        bridge.call("/command/rtl", "dragonfly_messages/srv/Simple", SimpleRequest.create(),
                (data, stringRep) -> System.out.println("RTL sent to " + name));
    }

    public void sendMission(ObjectNode missionDataHolder) {
        bridge.call("/command/mission", "dragonfly_messages/srv/Mission", missionDataHolder,
                (data, stringRep) -> System.out.println("Mission sent to " + name));
    }

    public void startMission() {
        bridge.call("/command/start_mission", "dragonfly_messages/srv/Simple", SimpleRequest.create(),
                (data, stringRep) -> System.out.println("Start mission sent to " + name));
    }

    public void setup(SetupRequest setupData) {
        bridge.call("/command/setup", "dragonfly_messages/srv/Setup", setupData,
                (data, stringRep) -> System.out.println("Setup sent to " + name));
    }

    public static class LatLonRelativeAltitude {
        private final double latitude;
        private final double longitude;
        private final double relativeAltitude;

        public LatLonRelativeAltitude(double latitude, double longitude, double relativeAltitude) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.relativeAltitude = relativeAltitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public double getRelativeAltitude() {
            return relativeAltitude;
        }
    }
}
