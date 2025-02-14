package it.unicam.cs.ids2425.users.model.actors.sellers;

import it.unicam.cs.ids2425.users.model.UserRole;

public final class EventPlanner extends GenericSeller {
    @Override
    public UserRole getRole() {
        return UserRole.EVENT_PLANNER;
    }
}
