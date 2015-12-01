package ow.micropos.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ow.micropos.server.exception.InternalServerErrorException;
import ow.micropos.server.exception.InvalidParameterException;
import ow.micropos.server.exception.UserPermissionException;
import ow.micropos.server.model.Permission;
import ow.micropos.server.model.people.Employee;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Service
public class AuthService {

    @Autowired PeopleService eService;

    public static final String HEADER = "MicroPOS-Pin";

    /**
     * **************************************************************************
     * *
     * Simple Authorization Service that must be called manually per controller  *
     * mapping or service method. Spring Security was a bit overkill for pin #   *
     * authorization (could not figure out how to customize enough to skip       *
     * username/password authentication). HandlerInterceptors were also an       *
     * option, but were difficult to customize per controller mapping or service *
     * method. This means a pin must be provided with each request (in most      *
     * cases, via request parameter ?pin=####). We prefer authorizing within     *
     * the controller to avoid having to forward pin numbers as parameters to    *
     * services. Also, we can choose particular service methods depending on     *
     * employee authorities (roles/positions)                                    *
     * *
     * **************************************************************************
     */

    @Transactional(readOnly = true)
    public Employee authorize(HttpServletRequest request, Permission... permissions) {

        short pin;

        try {
            pin = Short.parseShort(request.getHeader(HEADER));
        } catch (Exception e) {
            System.out.println("Defaulting to Guest");
            pin = 0;
        }

        Employee employee = eService.getEmployee(pin);
        authorize(employee, permissions);
        return employee;

    }

    @Transactional(readOnly = true)
    public void authorize(Employee employee, Permission... permissions) {
        if (permissions != null) {
            if (!employee.hasPermissions(permissions)) {
                throw new UserPermissionException("Requires permissions : " + Arrays.toString(permissions));
            }
        }
    }

}
