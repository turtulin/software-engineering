package it.unicam.cs.ids2425.utilities.views;

import it.unicam.cs.ids2425.utilities.wrappers.responses.ResponseStatus;
import it.unicam.cs.ids2425.utilities.wrappers.responses.ViewResponse;

import java.util.function.Supplier;

public interface IView {
    default <T> ViewResponse<T> genericCall(Supplier<T> action, ResponseStatus successStatus, String successMessage, ResponseStatus failureStatus) {
        try {
            T result = action.get();
            if (result == null) {
                throw new IllegalArgumentException("No result found");
            }

            ViewResponse.ViewResponseBuilder<T> builder = ViewResponse.<T>builder()
                    .status(successStatus)
                    .data(result);

            if (successMessage != null) {
                builder.message(successMessage);
            }
            return builder.build();
        } catch (IllegalArgumentException e) {
            return ViewResponse.<T>builder()
                    .status(failureStatus)
                    .message(e.getMessage())
                    .build();
        } catch (Exception e) {
            return ViewResponse.<T>builder()
                    .status(ResponseStatus.BAD_REQUEST)
                    .message(e.getMessage())
                    .build();
        }
    }

    default <T> ViewResponse<T> genericCall(Supplier<T> action, ResponseStatus successStatus, ResponseStatus failureStatus) {
        return genericCall(action, successStatus, null, failureStatus);
    }

    default <T> ViewResponse<T> genericCall(Supplier<T> action, ResponseStatus successStatus, String successMessage) {
        return genericCall(action, successStatus, successMessage, ResponseStatus.BAD_REQUEST);
    }

    default <T> ViewResponse<T> genericCall(Supplier<T> action, ResponseStatus successStatus) {
        return genericCall(action, successStatus, null, ResponseStatus.BAD_REQUEST);
    }

    default <T> ViewResponse<T> genericCall(Supplier<T> action) {
        return genericCall(action, ResponseStatus.OK, null, ResponseStatus.BAD_REQUEST);
    }

    default <T> ViewResponse<T> genericCall(Supplier<T> action, String successMessage) {
        return genericCall(action, ResponseStatus.OK, successMessage, ResponseStatus.BAD_REQUEST);
    }
}
