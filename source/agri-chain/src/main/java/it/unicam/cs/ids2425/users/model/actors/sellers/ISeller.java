package it.unicam.cs.ids2425.users.model.actors.sellers;

import it.unicam.cs.ids2425.users.model.IUser;

public sealed interface ISeller extends IUser permits GenericSeller {
}
