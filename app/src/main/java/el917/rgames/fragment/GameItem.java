package el917.rgames.fragment;

public class GameItem {
    public String getTitleGame() {
        return titleGame;
    }

    public String getTypeGame() {
        return typeGame;
    }

    public String getReleasedData() {
        return releasedData;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    private String titleGame;
    private String typeGame;
    private String releasedData;
    private String thumbnailUrl;

    public GameItem(String titleGame, String typeGame, String releasedData, String thumbnailUrl) {
        this.titleGame = titleGame;
        this.typeGame = typeGame;
        this.releasedData = releasedData;
        this.thumbnailUrl = thumbnailUrl;
    }

    @Override
    public String toString() {
        return "titleGame: " + titleGame
                + '\n' + "typeGame: " + typeGame
                + '\n' + "releasedData: " + releasedData
                + '\n' + "thumbnailUrl: " + thumbnailUrl;
    }
}
