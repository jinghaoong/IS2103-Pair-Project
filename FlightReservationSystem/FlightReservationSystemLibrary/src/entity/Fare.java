/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author jinghao
 */
@Entity
public class Fare implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fareId;
    @Column(nullable = false, length = 16, unique = true)
    private String fareBasisCode;
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal fareAmount;
    
    @ManyToOne(optional = false)
    private CabinClassConfiguration cabinClassConfig;

    public Fare() {
    }

    public Fare(String fareBasisCode, BigDecimal fareAmount, CabinClassConfiguration cabinClassConfig) {
        this.fareBasisCode = fareBasisCode;
        this.fareAmount = fareAmount;
        this.cabinClassConfig = cabinClassConfig;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fareId != null ? fareId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the fareId fields are not set
        if (!(object instanceof Fare)) {
            return false;
        }
        Fare other = (Fare) object;
        if ((this.fareId == null && other.fareId != null) || (this.fareId != null && !this.fareId.equals(other.fareId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Fare[ id=" + fareId + " ]";
    }
    
    public Long getFareId() {
        return fareId;
    }

    public void setFareId(Long fareId) {
        this.fareId = fareId;
    }

    public String getFareBasisCode() {
        return fareBasisCode;
    }

    public void setFareBasisCode(String fareBasisCode) {
        this.fareBasisCode = fareBasisCode;
    }

    public BigDecimal getFareAmount() {
        return fareAmount;
    }

    public void setFareAmount(BigDecimal fareAmount) {
        this.fareAmount = fareAmount;
    }

    public CabinClassConfiguration getCabinClassConfig() {
        return cabinClassConfig;
    }

    public void setCabinClassConfig(CabinClassConfiguration cabinClassConfig) {
        this.cabinClassConfig = cabinClassConfig;
    }
    
}
