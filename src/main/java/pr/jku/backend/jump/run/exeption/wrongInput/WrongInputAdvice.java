package pr.jku.backend.jump.run.exeption.wrongInput;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class WrongInputAdvice {
    @ResponseBody
    @ExceptionHandler(WrongInputException.class)
    @ResponseStatus
    String wrongInputHandler(WrongInputException ex) {
        return ex.getMessage();
    }
}
