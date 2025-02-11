package it.unicam.cs.ids2425.utilities.statuses.statusconfigs;

public record StatusCodes(int code, String displayName) {
    public static StatusCodes DELETED = new StatusCodes(0, "Deleted");
    public static StatusCodes ACTIVE = new StatusCodes(1, "Active");
    public static StatusCodes INACTIVE = new StatusCodes(2, "Inactive");
    public static StatusCodes PUBLISHED = new StatusCodes(3, "Published");
    public static StatusCodes REJECTED = new StatusCodes(4, "Rejected");
    public static StatusCodes DRAFT = new StatusCodes(5, "Draft");
    public static StatusCodes PENDING = new StatusCodes(6, "Pending");
    public static StatusCodes PROCESSING = new StatusCodes(7, "Processing");
    public static StatusCodes SHIPPED = new StatusCodes(8, "Shipped");
    public static StatusCodes DELIVERED = new StatusCodes(9, "Delivered");
    public static StatusCodes CANCELLED = new StatusCodes(10, "Cancelled");
    public static StatusCodes REFUNDED = new StatusCodes(11, "Refunded");
    public static StatusCodes COMPLETED = new StatusCodes(12, "Completed");
    public static StatusCodes FAILED = new StatusCodes(13, "Failed");

}
