package com.example.spring1.controllers;

import com.example.spring1.exceptions.DepartmentNotFoundException;
import com.example.spring1.exceptions.UserNotFoundException;
import com.example.spring1.model.Department;
import com.example.spring1.model.User;
import com.example.spring1.service.DepartmentService;
import com.example.spring1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "departments")
public class DepartmentController extends UserExceptionHandler {

    /**
     * Добавляем отделы в бд после создания бинов
     */
    @PostConstruct
    public void initDepartments() {
        List<String> listDep = Arrays.asList("firstDepartmentName", "secondDepartmentName", "thirdDepartmentName");

        for (String s : listDep) {
            Department department1 = new Department();
            department1.setName(s);
            if (departmentService.findByName(department1.getName()) == null)
                departmentService.save(department1);
        }
    }

    private final UserService userService;
    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(UserService userService, DepartmentService departmentService) {
        this.userService = userService;
        this.departmentService = departmentService;
    }

    @GetMapping()
    public ResponseEntity<List<Department>> getDepartmentList() {
        return new ResponseEntity<>(departmentService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/users")
    public ResponseEntity<List<User>> getAllUsersInDepartments(
            @PathVariable(name = "id") Long id) throws DepartmentNotFoundException {
        return new ResponseEntity<>(userService.findByDepartment(id), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Department> getDepartmentById(
            @PathVariable(name = "id") Long id) throws DepartmentNotFoundException {
        return new ResponseEntity<>(departmentService.findById(id), HttpStatus.OK);
    }

    @PostMapping(value = "/{dep_id}/users/{user_id}")
    public ResponseEntity<User> addUserToDepartment(
            @PathVariable(name = "dep_id") Long depId,
            @PathVariable(name = "user_id") Long userId ) throws UserNotFoundException, DepartmentNotFoundException {
        User user = departmentService.setUserToDep(userId, depId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{dep_id}/users/{user_id}")
    public ResponseEntity<User> deleteUserFromDepartment(@PathVariable(name = "dep_id") Long depId, @PathVariable(name = "user_id") Long userId)
            throws UserNotFoundException, DepartmentNotFoundException {
        User user = departmentService.deleteUserFromDepartment(userId, depId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
