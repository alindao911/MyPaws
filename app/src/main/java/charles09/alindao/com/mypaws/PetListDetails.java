package charles09.alindao.com.mypaws;

/**
 * Created by Pc-user on 02/02/2018.
 */

public class PetListDetails {
    private String name;
    private int image;

    public PetListDetails() {

    }

    public PetListDetails(String name, int image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
