package com.example.spring1.controllers;

import com.example.spring1.exceptions.helper.ValidationError;
import com.example.spring1.exceptions.DepartmentNotFoundException;
import com.example.spring1.exceptions.UserNotFoundException;
import com.example.spring1.exceptions.UserPutException;
import com.example.spring1.model.Department;
import com.example.spring1.model.User;
import com.example.spring1.service.DepartmentService;
import com.example.spring1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController extends UserExceptionHandler{

    private final UserService userService;
    private final DepartmentService departmentService;

    @Autowired
    public UserController(UserService userService, DepartmentService departmentService) {
        this.userService = userService;
        this.departmentService = departmentService;
    }

    @GetMapping()
    public ResponseEntity<List<User>> getAllUsers(@RequestParam(name = "department", required = false) Long id)
            throws UserNotFoundException, DepartmentNotFoundException {
        if(id != null)
            return new ResponseEntity<>(userService.findAll(id), HttpStatus.OK);
        else
            return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<User> addNewUser(@Valid @RequestBody User user) throws DepartmentNotFoundException {
        return ResponseEntity.ok(userService.addUser(user));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) throws UserNotFoundException {
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public  ResponseEntity<User> putUserById(@PathVariable Long id, @RequestBody User newUser) throws UserNotFoundException, UserPutException, DepartmentNotFoundException {
        return new ResponseEntity<>( userService.putUser(newUser, id), HttpStatus.OK);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<User> patchUserById(@PathVariable Long id, @RequestBody User newUser) throws UserNotFoundException, DepartmentNotFoundException {
        return new ResponseEntity<>(userService.patchUser(newUser, id), HttpStatus.OK);
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) throws UserNotFoundException {
        return new ResponseEntity<>(userService.deleteUser(id), HttpStatus.OK);
    }


    /**
     * Перехватываем ошибки валидации @valid
     * @param exception
     * @param request
     * @return Вовращаем объект ValidationError с перечислением ошибок валидации
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ValidationError> handleMethodArgNotValid(MethodArgumentNotValidException exception, HttpServletRequest request){
        ValidationError error = new ValidationError(HttpStatus.BAD_REQUEST,request.getServletPath());
        BindingResult bindingResult = exception.getBindingResult();

        Map<String, String> validationErrors = new HashMap<>();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        error.setValidationErrors(validationErrors);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
