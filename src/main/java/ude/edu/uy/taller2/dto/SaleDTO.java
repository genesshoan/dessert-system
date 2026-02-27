package ude.edu.uy.taller2.dto;

import ude.edu.uy.taller2.domain.SaleStatus;

import java.io.Serializable;
import java.time.LocalDate;

public class SaleDTO implements Serializable {
    private long id;
    private LocalDate date;
    private String address;
    private SaleStatus status;

    public SaleDTO(long id, LocalDate date, String address, SaleStatus status) {
        this.id = id;
        this.date = date;
        this.address = address;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getAddress() {
        return address;
    }

    public SaleStatus getStatus() {
        return status;
    }
}
