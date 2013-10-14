package com.ojt;

public class CompetitionDescriptor {

    private String date = "";

    private String name = "";

    private String location = "";

    private String fightTime = "2 min";

    private CompetitionCategory category = CompetitionCategory.UNKNOWN_CATEGORY;

    public String getDate() {
        return date;
    }

    public void setDate(final String date) {
        this.date = date;
    }

    public String getCompetitionName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(final String location) {
        this.location = location;
    }

    public String getFightTime() {
        return fightTime;
    }

    public void setFightTime(final String fightTime) {
        this.fightTime = fightTime;
    }

    public void setCategory(final CompetitionCategory category) {
        this.category = category;
    }

    public CompetitionCategory getCategory() {
        return category;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof CompetitionDescriptor) {
            return name.equals(((CompetitionDescriptor) obj).getCompetitionName())
                    && location.equals(((CompetitionDescriptor) obj).getLocation())
                    && date.equals(((CompetitionDescriptor) obj).getDate())
                    && category.equals(((CompetitionDescriptor) obj).getCategory());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
