package com.inzynierka2k24.apiserver.web.config;

import com.inzynierka2k24.apiserver.exception.apartment.ApartmentNotFoundException;
import com.inzynierka2k24.apiserver.exception.reservation.ReservationNotFoundException;
import com.inzynierka2k24.apiserver.exception.reservation.ReservationNotValidException;
import com.inzynierka2k24.apiserver.exception.user.UserAlreadyExistsException;
import com.inzynierka2k24.apiserver.exception.user.UserNotFoundException;
import com.inzynierka2k24.apiserver.web.request.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<ApiErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(new ApiErrorResponse(HttpStatus.CONFLICT, e.getMessage()));
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage()));
  }

  @ExceptionHandler(ApartmentNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleApartmentNotFoundException(
      ApartmentNotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage()));
  }

  @ExceptionHandler(ReservationNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleReservationNotFoundException(
      ReservationNotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage()));
  }

  @ExceptionHandler(ReservationNotValidException.class)
  public ResponseEntity<ErrorResponse> handleReservationNotValidException(
      ReservationNotValidException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage()));
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<ApiErrorResponse> handleRuntimeException(RuntimeException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ApiErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage()));
  }
}
