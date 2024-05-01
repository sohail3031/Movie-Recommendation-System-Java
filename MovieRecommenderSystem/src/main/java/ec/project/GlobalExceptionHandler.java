package ec.project;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle specific exceptions
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleResourceNotFoundException() {
        ModelAndView modelAndView = new ModelAndView("error/404");
        modelAndView.addObject("message", "The resource you are trying to access is not found.");
        return modelAndView;
    }

    // Handle generic exceptions
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleException(Exception e) {
        ModelAndView modelAndView = new ModelAndView("error/500");
        modelAndView.addObject("message", "An unexpected error occurred: " + e.getMessage());
        return modelAndView;
    }
}

// Custom exception class
class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
