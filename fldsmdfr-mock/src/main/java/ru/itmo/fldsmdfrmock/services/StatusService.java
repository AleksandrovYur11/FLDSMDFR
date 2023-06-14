package ru.itmo.fldsmdfrmock.services;

import org.springframework.stereotype.Service;

@Service
public class StatusService {

    private static String status = "ok";

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        StatusService.status = status;
    }
}
