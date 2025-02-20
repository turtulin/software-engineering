package it.unicam.cs.ids2425.users.model.actors.sellers;

import it.unicam.cs.ids2425.users.model.IStockUser;

public sealed interface ISeller extends IStockUser permits GenericSeller {
}
