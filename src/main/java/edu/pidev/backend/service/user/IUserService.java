package edu.pidev.backend.service.user;


import edu.pidev.backend.common.PageResponse;
import edu.pidev.backend.common.user.UpdateProfileRequest;
import edu.pidev.backend.common.user.UserFilterCriteria;
import edu.pidev.backend.common.user.UserDTO;
import edu.pidev.backend.entity.User;
import org.springframework.data.domain.Sort;

public interface IUserService {

    User add(User user);

    User update(User user);

    PageResponse<UserDTO> getAll(int page, int size, String sort, Sort.Direction order, UserFilterCriteria filter);

    User get(Long id);

    User get(String email);

    void handleEnable(Long id);

    void handleLock(Long id);

    boolean emailExists(String email);

    User changeEmail(String oldEmail, String newEmail);

    User changePassword(Long userId,String oldPassword, String newPassword);

    boolean userExists(String username);

    void disableAccount(User user);
}
