package charles09.alindao.com.mypaws.Model;

/**
 * Created by Pc-user on 18/02/2018.
 */

public class Pet {
    private String PetName;
    private String PetPhoto;
    private String PetOwnerCode;
    private String PetCode;
    private String PetStatus;

    public Pet() {

    }

    public Pet(String petName, String petPhoto, String petOwnerCode, String petCode, String petStatus) {
        PetName = petName;
        PetPhoto = petPhoto;
        PetOwnerCode = petOwnerCode;
        PetCode = petCode;
        PetStatus = petStatus;
    }

    public String getPetName() {
        return PetName;
    }

    public void setPetName(String petName) {
        PetName = petName;
    }

    public String getPetPhoto() {
        return PetPhoto;
    }

    public void setPetPhoto(String petPhoto) {
        PetPhoto = petPhoto;
    }

    public String getPetOwnerCode() {
        return PetOwnerCode;
    }

    public void setPetOwnerCode(String petOwnerCode) {
        PetOwnerCode = petOwnerCode;
    }

    public String getPetCode() {
        return PetCode;
    }

    public void setPetCode(String petCode) {
        PetCode = petCode;
    }

    public String getPetStatus() {
        return PetStatus;
    }

    public void setPetStatus(String petStatus) {
        PetStatus = petStatus;
    }
}
