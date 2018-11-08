package bgu.spl.a2.sim.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JsonAction {
    @SerializedName("Action")
    @Expose
    private String action;
    @SerializedName("Department")
    @Expose
    private String department;
    @SerializedName("Course")
    @Expose
    private String course;
    @SerializedName("Space")
    @Expose
    private String space;
    @SerializedName("Prerequisites")
    @Expose
    private List<String> prerequisitesList = null;
    @SerializedName("Student")
    @Expose
    private String student;
    @SerializedName("Number")
    @Expose
    private String number;
    @SerializedName("Computer")
    @Expose
    private String computer;
    @SerializedName("Conditions")
    @Expose
    private List<String> conditionsList = null;
    @SerializedName("Grade")
    @Expose
    private List<String> gradesList = null;
    @SerializedName("Students")
    @Expose
    private List<String> studentsList = null;
    @SerializedName("Preferences")
    @Expose
    private List<String> preferencesList = null;

    //getter per field
    public String getSpace() {  return space;}

    public String getAction() { return action;}

    public String getDepartment(){  return department;}

    public String getStudent() {    return student;}

    public String getNumber() { return number;}

    public String getCourse() { return course;}

    public String getComputer() {   return computer;}

    public List<String> getPreferences() {  return preferencesList;}

    public List<String> getPrerequisites() {    return prerequisitesList;}

    public List<String> getGrade() {    return gradesList;}

    public List<String> getStudents() { return studentsList;}

    public List<String> getConditions() {   return conditionsList;}


    //setter per field
    public void setAction(String action) {  this.action = action;}

    public void setStudent(String student) {    this.student = student;}

    public void setDepartment(String department) {  this.department = department;}

    public void setCourse(String course) {  this.course = course;}

    public void setGrade(List<String> grade) {  this.gradesList = grade;}

    public void setNumber(String number) {  this.number = number;}

    public void setStudents(List<String> students) {    this.studentsList = students;}

    public void setComputer(String computer) {  this.computer = computer;}

    public void setConditions(List<String> conditions) {    this.conditionsList = conditions;}

    public void setSpace(String space) {    this.space = space;}

    public void setPrerequisites(List<String> prerequisites) {
        this.prerequisitesList = prerequisites;
    }

    public void setPreferences(List<String> preferences) {
        this.preferencesList = preferences;
    }
}