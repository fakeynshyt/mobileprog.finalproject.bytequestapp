package com.example.finalprojectjava.styles;

public class SubjectItem {
    private String subjectTitle;
    private String subjectDesc;
    private int subjectImage;

    public SubjectItem(String subjectTitle, String subjectDesc, int subjectImage) {
        this.subjectTitle = subjectTitle;
        this.subjectDesc = subjectDesc;
        this.subjectImage = subjectImage;
    }

    public String getSubjectTitle() {
        return subjectTitle;
    }

    public void setSubjectTitle(String subjectTitle) {
        this.subjectTitle = subjectTitle;
    }

    public String getSubjectDesc() {
        return subjectDesc;
    }

    public void setSubjectDesc(String subjectDesc) {
        this.subjectDesc = subjectDesc;
    }

    public int getSubjectImage() {
        return subjectImage;
    }

    public void setSubjectImage(int subjectImage) {
        this.subjectImage = subjectImage;
    }
}
