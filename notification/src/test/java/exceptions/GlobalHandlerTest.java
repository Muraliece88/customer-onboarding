package exceptions;

import com.assign.exceptions.GlobalHandler;
import com.assign.exceptions.NotificationError;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalHandlerTest {
  @InjectMocks
  private GlobalHandler globalHandler;

  @Test
  void testHandleGenericException() throws NoSuchAlgorithmException {
    Exception exception = mock(Exception.class);
    HttpServletRequest request = mock(HttpServletRequest.class);
    ResponseEntity<NotificationError> errorResponseEntity = globalHandler.handleException(exception, request);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, errorResponseEntity.getStatusCode());
  }
  @Test
  void testHandleMethodArgumentNotValidExceptionException() throws NoSuchAlgorithmException {
    MethodArgumentNotValidException methodNotValidException = mock(MethodArgumentNotValidException.class);
    BindingResult bindingResult = mock( BindingResult.class);
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(methodNotValidException.getBindingResult()).thenReturn(bindingResult);
    ResponseEntity<NotificationError> errorResponseEntity = globalHandler.handleException(methodNotValidException,request);
    assertEquals(HttpStatus.BAD_REQUEST, errorResponseEntity.getStatusCode());

  }

}