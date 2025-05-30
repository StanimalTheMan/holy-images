package com.jiggycode.model;

import java.util.List;

public class ImageAnalysis {
    private String description;
    private List<FaceAnalysis> faces;

    public List<FaceAnalysis> getFaces() {
        return faces;
    }

    public void setFaces(List<FaceAnalysis> faces) {
        this.faces = faces;
    }

    public ImageAnalysis() {}

    public ImageAnalysis(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static class FaceAnalysis {
        private String expression;
        private String ageGroup;
        private String gender;

        // Getters and setters...


        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getAgeGroup() {
            return ageGroup;
        }

        public void setAgeGroup(String ageGroup) {
            this.ageGroup = ageGroup;
        }

        public String getExpression() {
            return expression;
        }

        public void setExpression(String expression) {
            this.expression = expression;
        }
    }
}
