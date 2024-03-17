package com.ductrongdatn.besellphone.Services;

import com.ductrongdatn.besellphone.DTO.UserDTO;
import com.ductrongdatn.besellphone.Exceptions.DataNotFoundException;
import com.ductrongdatn.besellphone.Models.User;

public interface IUserService {
    User createUser(UserDTO userDTO) throws DataNotFoundException;
    String login(String phoneNumber, String password);
}