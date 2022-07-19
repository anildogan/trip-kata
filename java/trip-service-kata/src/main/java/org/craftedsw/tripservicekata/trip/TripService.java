package org.craftedsw.tripservicekata.trip;

import org.craftedsw.tripservicekata.exception.UserNotLoggedInException;
import org.craftedsw.tripservicekata.user.User;
import org.craftedsw.tripservicekata.user.UserSession;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TripService {

    private final UserSession userSession;
    private final TripDAO tripDao;

    public TripService(UserSession userSession, TripDAO tripDao) {
        this.userSession = userSession;
        this.tripDao = tripDao;
    }

    public List<Trip> getTripsByUser(User user) throws UserNotLoggedInException {
        final User loggedUser = getLoggedUser().orElseThrow(UserNotLoggedInException::new);

        return isFriend(user, loggedUser) ? getTrips(user) : Collections.emptyList();
    }

    private boolean isFriend(User user, User loggedUser) {
        return user.getFriends().stream().anyMatch(user1 -> user1.equals(loggedUser));
    }

    protected List<Trip> getTrips(User user) {
        return tripDao.findTripsBy(user);
    }

    protected Optional<User> getLoggedUser() {
        return Optional.of(userSession.getLoggedUser());
    }

}
