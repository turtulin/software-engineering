package it.unicam.cs.ids2425.users.model.actors.sellers;

import it.unicam.cs.ids2425.users.model.UserRole;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Producer extends GenericSeller {
    @Override
    public UserRole getRole() {
        return UserRole.PRODUCER;
    }
}
