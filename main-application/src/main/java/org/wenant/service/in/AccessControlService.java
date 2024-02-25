package org.wenant.service.in;

import org.springframework.stereotype.Service;
import org.wenant.domain.dto.MeterReadingDto;
import org.wenant.domain.dto.MeterTypeCatalogDto;
import org.wenant.domain.dto.ReadingDto;
import org.wenant.domain.dto.UserDto;
import org.wenant.service.UserService;

import java.time.YearMonth;
import java.util.List;

@Service
public class AccessControlService {
    private final JwtService jwtService;
    private final MeterReadingService meterReadingService;
    private final MeterTypeCatalogService meterTypeCatalogService;
    private final UserService userService;

    public AccessControlService(JwtService jwtService, MeterReadingService meterReadingService, MeterTypeCatalogService meterTypeCatalogService, UserService userService) {
        this.jwtService = jwtService;
        this.meterReadingService = meterReadingService;
        this.meterTypeCatalogService = meterTypeCatalogService;
        this.userService = userService;
    }

    public MeterReadingService.MeterReadingStatus addNewReading(ReadingDto readingDto, String authorizationHeader) {
        if (jwtService.isValidToken(authorizationHeader)) {
            String username = jwtService.getUsernameFromAuthorizationHeader(authorizationHeader);
            return meterReadingService.addNewReading(readingDto, username);
        } else {
            return MeterReadingService.MeterReadingStatus.INVALID_TOKEN;
        }
    }

    public List<MeterReadingDto> getAllMeterReadings(String authorizationHeader, String usernameFromPath) {
        String username = jwtService.getUsernameFromAuthorizationHeader(authorizationHeader);

        if (!jwtService.isValidToken(authorizationHeader)) {
            throw new IllegalArgumentException("Invalid token");
        } else if (jwtService.userIsAdmin(authorizationHeader)) {
            if (usernameFromPath == null) {
                throw new IllegalArgumentException("Missing username in path for admin request");
            } else {
                return meterReadingService.getAllForUser(usernameFromPath);
            }
        } else {
            return meterReadingService.getAllForUser(username);
        }

    }

    public List<MeterReadingDto> getLatestMeterReadings(String authorizationHeader, String usernameFromPath) {
        String username = jwtService.getUsernameFromAuthorizationHeader(authorizationHeader);

        if (!jwtService.isValidToken(authorizationHeader)) {
            throw new IllegalArgumentException("Invalid token");
        } else if (jwtService.userIsAdmin(authorizationHeader)) {
            if (usernameFromPath == null) {
                throw new IllegalArgumentException("Missing username in path for admin request");
            } else {
                return meterReadingService.getLatestMeterReadings(usernameFromPath);
            }
        } else {
            return meterReadingService.getLatestMeterReadings(username);
        }

    }

    public List<MeterReadingDto> getByUserAndDate(String authorizationHeader, String usernameFromPath, YearMonth of) {
        String username = jwtService.getUsernameFromAuthorizationHeader(authorizationHeader);

        if (!jwtService.isValidToken(authorizationHeader)) {
            throw new IllegalArgumentException("Invalid token");
        } else if (jwtService.userIsAdmin(authorizationHeader)) {
            if (usernameFromPath == null) {
                throw new IllegalArgumentException("Missing username in path for admin request");
            } else {
                return meterReadingService.getByUserAndDate(usernameFromPath, of);
            }
        } else {
            return meterReadingService.getByUserAndDate(username, of);
        }

    }

    public void addMeterType(MeterTypeCatalogDto meterTypeCatalogDto, String authorizationHeader) {

        if (!jwtService.userIsAdmin(authorizationHeader)) {
            throw new IllegalArgumentException("User is not an administrator");
        } else {
            String meterType = meterTypeCatalogDto.getMeterType();

            if (meterType != null && meterType.length() >= 3) {
                meterTypeCatalogService.addMeterType(meterType);
            } else {
                throw new IllegalArgumentException("Invalid meter type");
            }
        }
    }

    public List<UserDto> getAllUsers(String authorizationHeader) {

        if (!jwtService.userIsAdmin(authorizationHeader)) {
            throw new IllegalArgumentException("User is not an administrator");
        } else {
            return userService.getAllUsers();

        }
    }
}

