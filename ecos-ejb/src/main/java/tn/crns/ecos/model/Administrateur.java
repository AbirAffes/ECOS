package tn.crns.ecos.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "ID")
public class Administrateur extends Utilisateur implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "AUTO_ET")
	private Boolean auto_et;
	@Column(name = "AUTO_EN")
	private Boolean auto_en;
	@Column(name = "AUTO_EX")
	private Boolean auto_ex;
	@Column(name = "AUTO_ADMIN")
	private Boolean auto_admin;

	public Administrateur(Long cin, String pwd, Boolean auto_et,
			Boolean auto_en, Boolean auto_ex, Boolean auto_admin) {
		super(cin, pwd);
		this.auto_admin = auto_admin;
		this.auto_en = auto_en;
		this.auto_et = auto_et;
		this.auto_ex = auto_ex;
	}

	public Administrateur() {
		super();
	}

	public Boolean getAuto_et() {
		return auto_et;
	}

	public void setAuto_et(Boolean auto_et) {
		this.auto_et = auto_et;
	}

	public Boolean getAuto_en() {
		return auto_en;
	}

	public void setAuto_en(Boolean auto_en) {
		this.auto_en = auto_en;
	}

	public Boolean getAuto_ex() {
		return auto_ex;
	}

	public void setAuto_ex(Boolean auto_ex) {
		this.auto_ex = auto_ex;
	}

	public Boolean getAuto_admin() {
		return auto_admin;
	}

	public void setAuto_admin(Boolean auto_admin) {
		this.auto_admin = auto_admin;
	}



}
