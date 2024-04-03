package com.ductrongdatn.besellphone.Controllers;

import com.ductrongdatn.besellphone.Components.LocalizationUtils;
import com.ductrongdatn.besellphone.DTO.UserDTO;
import com.ductrongdatn.besellphone.DTO.UserLoginDTO;
import com.ductrongdatn.besellphone.Models.Token;
import com.ductrongdatn.besellphone.Models.User;
import com.ductrongdatn.besellphone.Responses.user.LoginResponse;
import com.ductrongdatn.besellphone.Services.IUserService;
import com.ductrongdatn.besellphone.Utils.MessageKeys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;
    private final LocalizationUtils localizationUtils;
    @PostMapping("/register")
    public ResponseEntity<?> createUser(
            @Valid @RequestBody UserDTO userDTO,
            BindingResult result
    ) {
        try{
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            if(!userDTO.getPassword().equals(userDTO.getRetypePassword())){
                return ResponseEntity.badRequest().body("Password does not match");
            }
            userService.createUser(userDTO);
            return ResponseEntity.ok("Register successfully");
        }  catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseEntity<ResponseObject> login(
            @Valid @RequestBody UserLoginDTO userLoginDTO,
            HttpServletRequest request
    ) throws Exception {
        // Kiểm tra thông tin đăng nhập và sinh token
        String token = userService.login(
                userLoginDTO.getPhoneNumber(),
                userLoginDTO.getPassword(),
                userLoginDTO.getRoleId() == null ? 1 : userLoginDTO.getRoleId()
        );
        String userAgent = request.getHeader("User-Agent");
        User userDetail = userService.getUserDetailsFromToken(token);
        Token jwtToken = tokenService.addToken(userDetail, token, isMobileDevice(userAgent));

        LoginResponse loginResponse = LoginResponse.builder()
                .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_SUCCESSFULLY))
                .token(jwtToken.getToken())
                .tokenType(jwtToken.getTokenType())
                .refreshToken(jwtToken.getRefreshToken())
                .username(userDetail.getUsername())
                .roles(userDetail.getAuthorities().stream().map(item -> item.getAuthority()).toList())
                .id(userDetail.getId())
                .build();
        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Login successfully")
                .data(loginResponse)
                .status(HttpStatus.OK)
                .build());
    }
}
