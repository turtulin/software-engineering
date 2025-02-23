package it.unicam.cs.ids2425.utilities.statuscode;

public interface IStatusCode {
    int getCode();

    default boolean equals(IStatusCode statusCode) {
        return this.getCode() == statusCode.getCode();
    }
}
