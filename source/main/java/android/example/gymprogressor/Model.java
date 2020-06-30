package android.example.gymprogressor;

public class Model {

    private int image;
    private String title;
    private String description;
    private String input;

    public Model(int image, String title, String description, String userInput) {
        this.image = image;
        this.title = title;
        this.description = description;

        if (userInput != null) {
            this.input = userInput;
        } else this.input = "";
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInput() {
        return input;
    }
}
