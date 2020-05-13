package com.sigma.model;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class CodeAPE {

    @Id
    private String codeApe;
    private String libelleApe;
    // private int codeDivision;
    // private String libelleDivision;

    // public CodeAPE(String codeApe, String libelleApe, int codeDivision, String libelleDivision) {
    public CodeAPE(String codeApe, String libelleApe) {
        super();
        this.codeApe = codeApe;
        this.libelleApe = libelleApe;
        // this.codeDivision = codeDivision;
        // this.libelleDivision = libelleDivision;
    }

    public CodeAPE() {
        super();
    }

    public String getCodeApe() {
        return codeApe;
    }

    public void setCodeApe(String codeApe) {
        this.codeApe = codeApe;
    }

    public String getLibelleApe() {
        return libelleApe;
    }

    public void setLibelleApe(String libelleApe) {
        this.libelleApe = libelleApe;
    }
    
    @Override
    public String toString() {
    	// TODO Auto-generated method stub
    	return codeApe + " - " + libelleApe;
    }

    // public int getCodeDivision() {
    //     return codeDivision;
    // }
    //
    // public void setCodeDivision(int codeDivision) {
    //     this.codeDivision = codeDivision;
    // }
    //
    // public String getLibelleDivision() {
    //     return libelleDivision;
    // }
    //
    // public void setLibelleDivision(String libelleDivision) {
    //     this.libelleDivision = libelleDivision;
    // }
}
