package event.ticket.booking.identity;

import event.ticket.booking.shared.consts.UserRole;

public interface UserContract {
    record RegisterReq(
            String username,
            String email,
            String password,
            UserRole role
    ) {}

    record LoginReq(
            String email,
            String password
    ) {}

    record LoginRes(
            String token,
            String email,
            String username
    ) {}
}
