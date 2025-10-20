package Business.Profiles;

import Business.CourseSchedule.CourseOffer;

public class FacultyAssignment {
    private FacultyProfile facultyProfile;
    private CourseOffer courseOffer;

    public FacultyAssignment(FacultyProfile facultyProfile, CourseOffer courseOffer) {
        this.facultyProfile = facultyProfile;
        this.courseOffer = courseOffer;
    }

    public FacultyProfile getFacultyProfile() {
        return facultyProfile;
    }
}
