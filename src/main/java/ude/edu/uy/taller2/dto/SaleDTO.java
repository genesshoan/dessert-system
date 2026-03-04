package ude.edu.uy.taller2.dto;

import ude.edu.uy.taller2.server.domain.SaleStatus;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO that represents a sale summary exposed by the logic layer.
 * Contains basic sale metadata (id, date, address and status).
 */
public class SaleDTO implements Serializable {
    private long id;
    private LocalDate date;
    private String address;
    private SaleStatus status;

    /**
     * Create a SaleDTO.
     *
     * @param id      sale identifier
     * @param date    sale date
     * @param address delivery/address for the sale
     * @param status  current sale status
     */
    public SaleDTO(long id, LocalDate date, String address, SaleStatus status) {
        this.id = id;
        this.date = date;
        this.address = address;
        this.status = status;
    }

    /** Returns the sale identifier. */
    public long getId() {
        return id;
    }

    /** Returns the sale date. */
    public LocalDate getDate() {
        return date;
    }

    /** Returns the sale delivery address. */
    public String getAddress() {
        return address;
    }

    /** Returns the current status of the sale. */
    public SaleStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return String.format("SaleDTO{id=%d, date=%s, address=%s, status=%s}",
                id, date == null ? "null" : date.toString(), address, status == null ? "null" : status.name());
    }
}
