package charles09.alindao.com.mypaws.Model;

/**
 * Created by Pc-user on 18/02/2018.
 */

public class Claim {
    private String ClaimCode;
    private String ClaimPetCode;
    private String ClaimImageOne;
    private String ClaimImageTwo;
    private String ClaimImageStatus;
    private String ClaimOwnerCode;

    public Claim() {

    }

    public Claim(String claimCode, String claimPetCode, String claimImageOne, String claimImageTwo, String claimImageStatus, String claimOwnerCode) {
        ClaimCode = claimCode;
        ClaimPetCode = claimPetCode;
        ClaimImageOne = claimImageOne;
        ClaimImageTwo = claimImageTwo;
        ClaimImageStatus = claimImageStatus;
        ClaimOwnerCode = claimOwnerCode;
    }

    public String getClaimOwnerCode() {
        return ClaimOwnerCode;
    }

    public void setClaimOwnerCode(String claimOwnerCode) {
        ClaimOwnerCode = claimOwnerCode;
    }

    public String getClaimCode() {
        return ClaimCode;
    }

    public void setClaimCode(String claimCode) {
        ClaimCode = claimCode;
    }

    public String getClaimPetCode() {
        return ClaimPetCode;
    }

    public void setClaimPetCode(String claimPetCode) {
        ClaimPetCode = claimPetCode;
    }

    public String getClaimImageOne() {
        return ClaimImageOne;
    }

    public void setClaimImageOne(String claimImageOne) {
        ClaimImageOne = claimImageOne;
    }

    public String getClaimImageTwo() {
        return ClaimImageTwo;
    }

    public void setClaimImageTwo(String claimImageTwo) {
        ClaimImageTwo = claimImageTwo;
    }

    public String getClaimImageStatus() {
        return ClaimImageStatus;
    }

    public void setClaimImageStatus(String claimImageStatus) {
        ClaimImageStatus = claimImageStatus;
    }
}
