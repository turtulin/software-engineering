package it.unicam.cs.ids2425.infrastructure;

import it.unicam.cs.ids2425.authentication.controller.TokenController;
import it.unicam.cs.ids2425.user.controller.actor.SingleEntityController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


@Component
public class PermissionInterceptor implements HandlerInterceptor {

    private final TokenController tokenController;
    private final SingleEntityController singleEntityController;

    @Autowired
    public PermissionInterceptor(TokenController tokenController, SingleEntityController singleEntityController) {
        this.tokenController = tokenController;
        this.singleEntityController = singleEntityController;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        var authorization = request.getHeader("Authorization");
        if (authorization != null) {
            assignUser(request, authorization);
        } else {
            request.setAttribute("user", singleEntityController.getGuestUser());
        }
        return true;
    }

    private void assignUser(HttpServletRequest request, @NonNull String authorization) {
        var user = tokenController.check(authorization);
        if (user == null) {
            request.setAttribute("user", singleEntityController.getGuestUser());
            return;
        }
        request.setAttribute("user", user);
    }
}
