package com.sigma.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CodeCPV {
	
	@Id
	private String codeCpv;
	private String libelleCpv;
	
	public CodeCPV(String codeCpv, String libelleCpv) {
		super();
		this.codeCpv = codeCpv;
		this.libelleCpv = libelleCpv;
	}
	public CodeCPV() {
		super();
	}
	public String getCodeCpv() {
		return codeCpv;
	}
	public void setCodeCpv(String codeCpv) {
		this.codeCpv = codeCpv;
	}
	public String getLibelleCpv() {
		return libelleCpv;
	}
	public void setLibelleCpv(String libelleCpv) {
		this.libelleCpv = libelleCpv;
	}
	
	@Override
    public String toString() {
    	// TODO Auto-generated method stub
    	return codeCpv + " - " + libelleCpv;
    }

}
