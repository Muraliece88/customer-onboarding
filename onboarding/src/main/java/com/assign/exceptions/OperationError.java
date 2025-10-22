package com.assign.exceptions;

import java.time.Instant;
import java.util.List;

public record OperationError(int code, List<String> reasons, Instant timestamp, String message) {
  
}
