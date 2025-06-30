package com.spboot.todo.mapper;

import com.spboot.todo.Dto.UserDto;
import com.spboot.todo.entity.User;

public class UserMapper {

    public static UserDto mapToUserDto(User user) {
        return new UserDto(
            user.getId(),
            user.getFullname(),
            user.getUsername(),
            user.getPassword(),
            user.getEmail()
        );
    }

    public static User mapToUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setFullname(userDto.getFullname());
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        return user;
    }
}
