package com.assign.exceptions;

import java.time.Instant;
import java.util.List;

public record NotificationError(int code, List<String> message, Instant timestamp) {
  
}
