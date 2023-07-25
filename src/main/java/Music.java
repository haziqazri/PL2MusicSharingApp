import java.io.Serializable;

public class Music implements Serializable {
	private String title;
	private String link;
	private String tlink;
	private String msg;
	private int like;
	private String recommender;
	private int genre;

	Music(String title, String link, String tlink)
	{
		this.title = title;
		if(link.startsWith("https://www.youtube.com/watch?v="))
			this.link = link;
		else
			this.link = "https://www.youtube.com/watch?v="+link;
		this.tlink = tlink;
		msg = "";
		like = 0;
		recommender = "";
		genre = 0;
	}
	Music()
	{}

	public String getTitle() {
		return title;
	}
	public void setTitle(String musicn) {
		title = musicn;
	}
	
	public String getURL() {
		return link;
	}
	public void setURL(String mlink) {
		link = mlink;
	}
	
	public String getThumbnailURL() {
		return tlink;
	}
	public void setThumbnailURL(String musici) {
		tlink = musici;
	}
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String musicm) {
		msg = musicm;
	}
	
	public void likeCount() {
		like++;
	}
	
	public int getLike() {
		return like;
	}
	public void setLike(int num) {
		like = num;
	}
	
	public String getRecommender() {
		return recommender;
	}
	public void setRecommender(String musicr) {
		recommender = musicr;
	}
	
	public int getGenre() {
		return genre;
	}
	public void setGenre(int jnum) {
		genre = jnum;
	}
}

