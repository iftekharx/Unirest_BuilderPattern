package user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequest {

    private final String name;
    private final String email;
    private final String password;
    private final String role;
    private final String avatar;




}