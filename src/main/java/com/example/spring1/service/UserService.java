package com.example.spring1.service;

import com.example.spring1.exceptions.DepartmentNotFoundException;
import com.example.spring1.exceptions.UserNotFoundException;
import com.example.spring1.exceptions.UserPutException;
import com.example.spring1.model.Department;
import com.example.spring1.model.User;
import com.example.spring1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final DepartmentService departmentService;

    @Autowired
    public UserService(UserRepository userRepository, DepartmentService departmentService) {
        this.userRepository = userRepository;
        this.departmentService = departmentService;
    }


    /**
     * Поиск пользователя по идентификатору
     * @param id идентификатор
     * @return пользователь с заданным ид
     * @throws UserNotFoundException пользователь с заданнм ид не найден
     */
    public User findById(Long id) throws UserNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("user with id=" + id + " not found"));
    }

    /**
     * Получить список всех пользователей
     * @return список пользователей
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }


    /**
     * Добавить пользователя в бд
     * @param user объект пользователя
     * @return созданный пользователь
     * @throws DepartmentNotFoundException департамент с заданным id не найден
     */
    public User addUser(User user) throws DepartmentNotFoundException {
        if (user.getDepartment() != null) {
            Department dep = departmentService.findById(user.getDepartment().getId());
            user.setDepartment(dep);
        }
        user = userRepository.save(user);
        return user;
    }

    /**
     * Полное иззменение пользователя
     * @param newUser новый объект пользователя
     * @param id айди пользователя которого будем изменять
     * @return  измененный объект пользователя
     * @throws UserNotFoundException пользователь не найден
     * @throws UserPutException одно из полей пустое(недопустимо)
     * @throws DepartmentNotFoundException департамент с заданным айди не найден
     */
    public User putUser(User newUser, Long id) throws UserNotFoundException, UserPutException, DepartmentNotFoundException {
        // в методе put все поля должны быть заданы
        boolean isOneFielsEmpty = Stream.of(newUser.getFirstName(), newUser.getLastName(), newUser.getPhoneNumber())
                .anyMatch(s -> s == null || s.isEmpty());

        if (isOneFielsEmpty)
            throw new UserPutException("one fields is empty");

        User user = this.findById(id);
        user.setFirstName(newUser.getFirstName());
        user.setLastName(newUser.getLastName());
        user.setPhoneNumber(newUser.getPhoneNumber());

        if (newUser.getDepartment() != null) {
            Department dep = departmentService.findById(newUser.getDepartment().getId());
            user.setDepartment(dep);
        }

        User save = userRepository.save(user);
        return save;
    }

    /**
     * Частичное изменение полей
     * @param newUser Новый объект
     * @param id айди по которому изменяем пользователя
     * @return измененный объект пользователя
     * @throws UserNotFoundException пользователь с заданным айди не найдент
     * @throws DepartmentNotFoundException не найден заданный отдел
     */
    public User patchUser(User newUser, Long id) throws UserNotFoundException, DepartmentNotFoundException {
        User user = this.findById(id);

        if (newUser.getFirstName() != null && !newUser.getFirstName().isEmpty())
            user.setFirstName(newUser.getFirstName());

        if (newUser.getLastName() != null && !newUser.getLastName().isEmpty())
            user.setLastName(newUser.getFirstName());

        if (newUser.getPhoneNumber() != null && !newUser.getPhoneNumber().isEmpty())
            user.setPhoneNumber(newUser.getPhoneNumber());

        if (newUser.getDepartment() != null) {
            Department dep = departmentService.findById(newUser.getDepartment().getId());
            user.setDepartment(dep);
        }

        return userRepository.save(user);
    }

    /**
     * Удаление пользователя
     * @param id айди удаляемого пользователя
     * @return объект до удаления
     * @throws UserNotFoundException  Пользователь с заданным id не найден
     */
    public User deleteUser(Long id) throws UserNotFoundException {
        User byId = findById(id);
        userRepository.delete(byId);
        return byId;
    }

    /**
     * Сохранить пользователя
     * @param user сохраняемый пользователь
     * @return объект user извлеченный из бд после сохранения
     */
    public User save (User user){
        return userRepository.save(user);
    }

    public List<User> findByDepartment(Long id) {
        List<User> allByDepartmentId = userRepository.findAllByDepartmentId(id);
        return allByDepartmentId;
    }

    public List<User> findAll(Long id) throws DepartmentNotFoundException {
        Department byId = departmentService.findById(id);
        if(byId != null)
            return userRepository.findAllByDepartmentId(id);
        return null;
    }
}
