package com.example.spring1.service;

import com.example.spring1.exceptions.DepartmentNotFoundException;
import com.example.spring1.exceptions.UserNotFoundException;
import com.example.spring1.model.Department;
import com.example.spring1.model.User;
import com.example.spring1.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    @Lazy
    private UserService userService;


    public Department save(Department department){
        return this.departmentRepository.save(department);
    }

    public Department findById(Long id) throws DepartmentNotFoundException {
        return departmentRepository.findById(id)
                .orElseThrow( () -> new DepartmentNotFoundException("department with id=" + id + "not found"));
    }

    public Department findByName(String name){
       return departmentRepository.findByName(name).orElse(null);
    }

    /**
     * Меняем пользователю отдел
     * @param userId идентификатор пользователя
     * @param depId идентификатор отдела
     * @return измененный объект пользователя
     * @throws DepartmentNotFoundException отдел с заданным идентификатором не найден
     * @throws UserNotFoundException пользователь не найден
     */
    public User setUserToDep(Long userId, Long depId) throws DepartmentNotFoundException, UserNotFoundException {
        Department department = this.findById(depId);
        User user = userService.findById(userId);
        user.setDepartment(department);
        return userService.save(user);

    }


    /**
     * Удалить пользователя из отдела
     * @param userId идентификатор пользователя
     * @param depId идентификатор отдела
     * @return измененный объект пользователя
     * @throws DepartmentNotFoundException отдел с заданным идентификатором не найден
     * @throws UserNotFoundException пользователь не найден
     */
    public User deleteUserFromDepartment(Long userId, Long depId) throws DepartmentNotFoundException, UserNotFoundException {
        Department department = this.findById(depId);
        User user = userService.findById(userId);
        user.setDepartment(null);
        return userService.save(user);
    }

    public List<Department> findAll() {
        return this.departmentRepository.findAll();
    }
}
