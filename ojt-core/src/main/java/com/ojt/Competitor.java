package com.ojt;

import java.util.Date;

/**
 * Compétiteur
 * @since 2009-04-19 (DwarfConan) : Ajout du numéro de licence
 */
public class Competitor {

	/** Numéro de licence FFJDA */
	private String licenseCode;

	private String name;

	private String firstName;

	private Club club;

	private CompetitorCategory category = CompetitorCategory.UNKNOWN;

	private Float weight;
	
	private Date birthDate;
	
	private CompetitorSex sex = CompetitorSex.UNKNOWN;
	
	private GradeBelt gradeBelt;

	/**
	 * 
	 */
	public Competitor() {
		super();
	}

	/**
	 * Constructeur se basant sur le code de la license.
	 * @param licenseCode
	 */
	public Competitor(final String licenseCode) {
		super();

		this.licenseCode = licenseCode;
	}

	/**
	 * @param name Le nom du compétiteur
	 * @param club Le club d'appartenance
	 * @param weight Le poids
	 */
	public Competitor(final String name, final String firstName, final Club club,
			final float weight) {
		this(name, firstName, club, CompetitorCategory.UNKNOWN, weight);
	}
	
	public Competitor(final String name, final String firstName, final Club club,
			final Date birthDate, final float weight) {
		this(name, firstName, club, weight);
		this.birthDate = birthDate;
	}
	
	public Competitor(final String name, final String firstName, final Club club,
			final CompetitorCategory competitorCategory, final float weight) {
		super();

		this.name = name;
		this.firstName = firstName;
		this.club = club;
		category  = competitorCategory;
		this.weight = weight;
	}

	public String getNameAndWeight() {
		return getDisplayName() + " (" + weight + ")";
	}

	public String getDisplayName() {
		return name + " " + firstName.toLowerCase();
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public void setSex(CompetitorSex sex) {
		this.sex = sex;
	}
	
	public CompetitorSex getSex() {
		return sex;
	}
	
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public Club getClub() {
		return club;
	}

	public void setClub(final Club club) {
		this.club = club;
	}

	public CompetitorCategory getCategory() {
		return category;
	}

	public void setCategory(final CompetitorCategory category) {
		this.category = category;
	}

	public Float getWeight() {
		return weight;
	}

	public float getWeightAsFloat() {
		if (weight == null) {
			return -1;
		}
		return weight.floatValue();
	}

	public void setWeight(final Float weight) {
		this.weight = weight;
	}

	/**
	 * @return Numéro de licence FFJDA
	 */
	public String getLicenseCode() {
		return licenseCode;
	}

	/**
	 * @param licenseCode Numéro de licence FFJDA
	 */
	public void setLicenseCode(final String licenseCode) {
		this.licenseCode = licenseCode;
	}

	public GradeBelt getGradeBelt() {
		return gradeBelt;
	}
	
	public void setGradeBelt(GradeBelt gradeBelt) {
		this.gradeBelt = gradeBelt;
	}
	// -------------------------------------------------------------------------
	// 
	// -------------------------------------------------------------------------
	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append(getDisplayName());
		sb.append(" (").append(club);
		sb.append(", ").append(weight).append("kg)");
		return sb.toString();
	}

	@Override
	public boolean equals(final Object obj) {
		// return (obj instanceof Competitor) && (hashCode() == ((Competitor)
		// obj).hashCode());
		return (obj instanceof Competitor)
				&& (getDisplayName().equalsIgnoreCase(((Competitor) obj).getDisplayName()));
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}
}
