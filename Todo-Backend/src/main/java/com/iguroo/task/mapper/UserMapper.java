
package com.iguroo.task.mapper;

import java.util.Set;

import com.iguroo.task.dto.UserDto;
import com.iguroo.task.entity.Role;
import com.iguroo.task.entity.User;

public class UserMapper {

    public static User mapToEntity(UserDto userDto, Set<Role> roles) {
        User user = new User();
        user.setId(userDto.getId());
        user.setFullname(userDto.getFullname());
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setRoles(roles);
        return user;
    }
    
    public static UserDto mapToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFullname(user.getFullname());
        userDto.setUsername(user.getUsername());
        userDto.setPassword(user.getPassword());
        userDto.setEmail(user.getEmail());
        return userDto;
    }
	}
