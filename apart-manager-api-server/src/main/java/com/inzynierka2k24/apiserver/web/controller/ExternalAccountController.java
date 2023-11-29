package com.inzynierka2k24.apiserver.web.controller;

import com.inzynierka2k24.apiserver.exception.account.AccountNotFoundException;
import com.inzynierka2k24.apiserver.model.ExternalAccount;
import com.inzynierka2k24.apiserver.service.ExternalAccountService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/{userId}/external/account")
@RequiredArgsConstructor
public class ExternalAccountController {

  private final ExternalAccountService accountService;

  @GetMapping()
  public ResponseEntity<List<ExternalAccount>> getAll(@PathVariable long userId) {
    return ResponseEntity.ok(accountService.getAll(userId));
  }

  @GetMapping("/{accountId}")
  public ResponseEntity<ExternalAccount> get(
      @PathVariable long userId, @PathVariable long accountId) throws AccountNotFoundException {
    return ResponseEntity.ok(accountService.getById(userId, accountId));
  }

  @PostMapping()
  public ResponseEntity<String> add(
      @PathVariable long userId, @RequestBody ExternalAccount account) {
    accountService.add(userId, account);
    return ResponseEntity.status(HttpStatus.CREATED).body("Account created successfully");
  }

  @PutMapping()
  public ResponseEntity<String> edit(
      @PathVariable long userId, @RequestBody ExternalAccount account)
      throws AccountNotFoundException {
    accountService.update(userId, account);
    return ResponseEntity.ok("Account edited successfully");
  }

  @DeleteMapping("/{accountId}")
  public ResponseEntity<String> delete(@PathVariable long userId, @PathVariable long accountId)
      throws AccountNotFoundException {
    accountService.deleteById(userId, accountId);
    return ResponseEntity.ok("Account deleted successfully");
  }
}
