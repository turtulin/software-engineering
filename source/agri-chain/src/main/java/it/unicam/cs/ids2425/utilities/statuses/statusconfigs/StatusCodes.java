package it.unicam.cs.ids2425.utilities.statuses.statusconfigs;

public record StatusCodes(int code, String displayName) {
    public static StatusCodes DELETED = new StatusCodes(0, "Deleted");
    public static StatusCodes ACTIVE = new StatusCodes(1, "Active");
    public static StatusCodes INACTIVE = new StatusCodes(2, "Inactive");
    public static StatusCodes PENDING = new StatusCodes(4, "Pending");

    // public static StatusCodes BANNED = new StatusCodes(6, "Banned");
    // public static StatusCodes PUBLISHED = new StatusCodes(5, "Published");
    // public static StatusCodes DRAFT = new StatusCodes(3, "Draft");
}
