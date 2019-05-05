package com.testcomapny.vmsmicroservice.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Visitor.
 */
@Entity
@Table(name = "visitor")
public class Visitor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "fname", nullable = false)
    private String fname;

    @NotNull
    @Column(name = "lname", nullable = false)
    private String lname;

    @NotNull
    @Column(name = "mobile", nullable = false)
    private String mobile;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "companyname")
    private String companyname;

    @NotNull
    @Column(name = "checkin", nullable = false)
    private LocalDate checkin;

    @NotNull
    @Column(name = "checkout", nullable = false)
    private LocalDate checkout;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public Visitor fname(String fname) {
        this.fname = fname;
        return this;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public Visitor lname(String lname) {
        this.lname = lname;
        return this;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getMobile() {
        return mobile;
    }

    public Visitor mobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public Visitor email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompanyname() {
        return companyname;
    }

    public Visitor companyname(String companyname) {
        this.companyname = companyname;
        return this;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public LocalDate getCheckin() {
        return checkin;
    }

    public Visitor checkin(LocalDate checkin) {
        this.checkin = checkin;
        return this;
    }

    public void setCheckin(LocalDate checkin) {
        this.checkin = checkin;
    }

    public LocalDate getCheckout() {
        return checkout;
    }

    public Visitor checkout(LocalDate checkout) {
        this.checkout = checkout;
        return this;
    }

    public void setCheckout(LocalDate checkout) {
        this.checkout = checkout;
    }

    public Employee getEmployee() {
        return employee;
    }

    public Visitor employee(Employee employee) {
        this.employee = employee;
        return this;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Visitor visitor = (Visitor) o;
        if (visitor.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), visitor.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Visitor{" +
            "id=" + getId() +
            ", fname='" + getFname() + "'" +
            ", lname='" + getLname() + "'" +
            ", mobile='" + getMobile() + "'" +
            ", email='" + getEmail() + "'" +
            ", companyname='" + getCompanyname() + "'" +
            ", checkin='" + getCheckin() + "'" +
            ", checkout='" + getCheckout() + "'" +
            "}";
    }
}
