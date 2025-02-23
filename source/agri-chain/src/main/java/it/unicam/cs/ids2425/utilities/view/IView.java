package it.unicam.cs.ids2425.utilities.view;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.function.Supplier;

public interface IView {
    default <T> ResponseEntity<ViewResponse<T>> genericCall(Supplier<T> action, HttpStatus successStatus, HttpStatus failureStatus) {
        try {
            T result = action.get();
            if (result == null) {
                throw new IllegalArgumentException("No result found");
            }
            return ResponseEntity.status(successStatus).body(ViewResponse.<T>builder().status(successStatus).data(result).build());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.status(failureStatus).body(ViewResponse.<T>builder().status(failureStatus).message(e.getClass().getSimpleName() + " " + e.getMessage()).build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ViewResponse.<T>builder().status(HttpStatus.BAD_REQUEST).message(e.getClass().getSimpleName() + " " + e.getMessage()).build());
        }
    }

    default <T> ResponseEntity<ViewResponse<T>> genericCall(Supplier<T> action, HttpStatus successStatus) {
        return genericCall(action, successStatus, HttpStatus.BAD_REQUEST);
    }

    default <T> ResponseEntity<ViewResponse<T>> genericCall(Supplier<T> action) {
        return genericCall(action, HttpStatus.OK, HttpStatus.BAD_REQUEST);
    }
}
