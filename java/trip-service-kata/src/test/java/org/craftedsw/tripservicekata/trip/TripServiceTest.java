package org.craftedsw.tripservicekata.trip;


import org.craftedsw.tripservicekata.exception.UserNotLoggedInException;
import org.craftedsw.tripservicekata.user.User;
import org.craftedsw.tripservicekata.user.UserSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TripServiceTest {

    @Mock
    private TripDAO tripDAO;

    @Mock
    private UserSession userSession;

    private TripService newTripService;

    @BeforeEach
    void setUp() {
        newTripService = new TestableTripService(tripDAO, userSession);
    }

    class TestableTripService extends TripService {

        public TestableTripService(TripDAO tripDao, UserSession userSession) {
            super(userSession, tripDao);
        }
    }

    @Test
    void it_should_throw_not_logged_in_exception_when_user_not_found(){
        //given
        User user = new User();
        when(userSession.getLoggedUser()).thenReturn(null);

        //when
        final UserNotLoggedInException userNotLoggedInException
                = assertThrows(UserNotLoggedInException.class, () -> newTripService.getTripsByUser(user));

        //then
        assertNotNull(userNotLoggedInException);
    }

    @Test
    void it_should_get_trips_when_user_has_friends(){
        //given
        User user = new User();
        User loggedUser = new User();
        final List<Trip> trips = Collections.singletonList(new Trip());

        user.addFriend(loggedUser);
        when(userSession.getLoggedUser()).thenReturn(loggedUser);
        when(tripDAO.findTripsBy(user)).thenReturn(trips);

        //when
        final List<Trip> assertedTrips = newTripService.getTripsByUser(user);

        //then
        assertEquals(trips, assertedTrips);
    }

    @Test
    void it_should_get_empty_trips_when_user_has_no_friends(){
        //given
        User user = new User();
        User user1 = new User();
        User loggedUser = new User();

        user.addFriend(user1);
        when(userSession.getLoggedUser()).thenReturn(loggedUser);

        //when
        final List<Trip> assertedTrips = newTripService.getTripsByUser(user);

        //then
        assertEquals(Collections.emptyList(), assertedTrips);
    }

    @Test
    void it_should_return_empty_list_when_user_has_no_friends(){
        //given
        User user = new User();
        User loggedUser = new User();

        when(userSession.getLoggedUser()).thenReturn(loggedUser);

        //when
        final List<Trip> assertedTrips = newTripService.getTripsByUser(user);

        //then
        assertEquals(Collections.emptyList(), assertedTrips);
    }

}
