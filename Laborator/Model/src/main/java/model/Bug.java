package model;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public class Bug extends Entity<String> {

    String name;
    String description;
    String solutionDescription;
    StatusType statusType;
    String assignedDeveloper;

    public Bug(String name, String description) {
        this.name = name;
        this.description = description;
        this.statusType = StatusType.Free;
        this.solutionDescription = "";
        this.assignedDeveloper = "";
        setId(name);
    }

    public Bug() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StatusType getStatusType() {
        return statusType;
    }

    public void setStatusType(StatusType statusType) {
        this.statusType = statusType;
    }

    public String getSolutionDescription() {
        return solutionDescription;
    }

    public void setSolutionDescription(String solutionDescription) {
        this.solutionDescription = solutionDescription;
    }

    public String getAssignedDeveloper() {
        return assignedDeveloper;
    }

    public void setAssignedDeveloper(String assignedDeveloper) {
        this.assignedDeveloper = assignedDeveloper;
    }

    public String toString() {

        if (statusType == StatusType.Assigned)
            return name + " : " + description + " - " + assignedDeveloper;
        if (statusType == StatusType.Finished)
            return name + " : " + description + " - " + solutionDescription;
        return name + " : " + description + " - " + statusType;
    }
}
