package ir.component.core.dao.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * @author Mohammad Yasin Kaji
 */
@Entity
@Table(name = "userProfile")
public class User extends BaseEntityInfo<Integer> {

    private String firstName;
    private String lastName;
    private String tel;
    private String email;
    private String userName;
    private String password;
    private int userType;
    @Transient
    private UserType ut;
    private String businessTitle;
    private Date registerDate;
    private String registrationNumber;
    private Double annualMembershipFee;
    private Date membershipEndDate;
    private AdvertiserState advertiserState;

    public User() {
    }

    public User(String firstName, String lastName, String tel, String email, String userName, String password, int userType, UserType ut, String businessTitle, Date registerDate, String registrationNumber, Double annualMembershipFee, Date membershipEndDate, AdvertiserState advertiserState) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.tel = tel;
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.userType = userType;
        this.ut = ut;
        this.businessTitle = businessTitle;
        this.registerDate = registerDate;
        this.registrationNumber = registrationNumber;
        this.annualMembershipFee = annualMembershipFee;
        this.membershipEndDate = membershipEndDate;
        this.advertiserState = advertiserState;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Double getAnnualMembershipFee() {
        return annualMembershipFee;
    }

    public void setAnnualMembershipFee(Double annualMembershipFee) {
        this.annualMembershipFee = annualMembershipFee;
    }

    public Date getMembershipEndDate() {
        return membershipEndDate;
    }

    public void setMembershipEndDate(Date membershipEndDate) {
        this.membershipEndDate = membershipEndDate;
    }

    public AdvertiserState getAdvertiserState() {
        return advertiserState;
    }

    public void setAdvertiserState(AdvertiserState advertiserState) {
        this.advertiserState = advertiserState;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getBusinessTitle() {
        return businessTitle;
    }

    public void setBusinessTitle(String businessTitle) {
        this.businessTitle = businessTitle;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public UserType getUt() {
        return ut;
    }

    public void setUt(UserType ut) {
        this.userType = ut.ordinal();
        this.ut = ut;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return firstName + " " + lastName;
    }
}
