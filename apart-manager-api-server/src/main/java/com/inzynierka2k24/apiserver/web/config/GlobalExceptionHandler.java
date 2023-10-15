package com.inzynierka2k24.apiserver.web.config;

import com.inzynierka2k24.apiserver.exception.apartment.ApartmentNotFoundException;
import com.inzynierka2k24.apiserver.exception.reservation.ReservationNotFoundException;
import com.inzynierka2k24.apiserver.exception.reservation.ReservationNotValidException;
import com.inzynierka2k24.apiserver.exception.user.UserAlreadyExistsException;
import com.inzynierka2k24.apiserver.exception.user.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(
      UserAlreadyExistsException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponseException(HttpStatus.BAD_REQUEST, e));
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ErrorResponseException(HttpStatus.NOT_FOUND, e));
  }

  @ExceptionHandler(ApartmentNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleApartmentNotFoundException(
      ApartmentNotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ErrorResponseException(HttpStatus.NOT_FOUND, e));
  }

  @ExceptionHandler(ReservationNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleReservationNotFoundException(
      ReservationNotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ErrorResponseException(HttpStatus.NOT_FOUND, e));
  }

  @ExceptionHandler(ReservationNotValidException.class)
  public ResponseEntity<ErrorResponse> handleReservationNotValidException(
      ReservationNotValidException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponseException(HttpStatus.BAD_REQUEST, e));
  }
}
