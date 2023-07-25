import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.Thumbnail;
import com.google.api.services.youtube.model.SearchResult;

import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Search {

    public static Account[] account = new Account[10000]; // アカウントデータ管理
    public static Music[] music = new Music[10000]; // Musicデータ管理

    public static Music[] temp = new Music[5];
    public static int num_account = 0; //
    public static int num_music = 0;
    static ServerSocket ServerS;

    static ServerSocket test;
    static Socket testS;
    // static Scanner input;
    static DataOutputStream output;
    static BufferedReader ins;

    public static void main(String[] args) {
        try {
            for (int i = 0; i < account.length; i++) {
                account[i] = new Account();
            }
            for (int i = 0; i < music.length; i++) {
                music[i] = new Music();
            }
            ServerS = new ServerSocket(8080, 10);
            ObjectMapper mapper = new ObjectMapper();
            FileInputStream fis = new FileInputStream("data/Account.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bra = new BufferedReader(isr);
            num_account = Integer.parseInt(bra.readLine());
            if (num_account != 0) {
                List<Account> objectList = Arrays.asList(mapper.readValue(new File("data/Account.json"), Account[].class));
                int i = 0;
                for (Account accountt : objectList) {
                    account[i].setID(accountt.getID());
                    account[i].setPW(accountt.getPW());
                    account[i].setGenre(accountt.getGenre());
                    // System.out.println(account[i].getID());
                    // System.out.println(account[i].getPW());
                    // System.out.println(Arrays.toString(account[i].getGenre()));
                    i++;
                }
            }
            bra.close();
            FileInputStream fism = new FileInputStream("data/Music.txt");
            InputStreamReader isrm = new InputStreamReader(fism);
            BufferedReader brm = new BufferedReader(isrm);
            num_music = Integer.parseInt(brm.readLine());
            if (num_music != 0) {
                List<Music> objectListMusic = Arrays.asList(mapper.readValue(new File("data/Music.json"), Music[].class));
                int i = 0;
                for (Music musicc : objectListMusic) {
                    music[i].setTitle(musicc.getTitle());
                    music[i].setURL(musicc.getURL());
                    music[i].setThumbnailURL(musicc.getThumbnailURL());
                    music[i].setMsg(musicc.getMsg());
                    music[i].setLike(musicc.getLike());
                    music[i].setRecommender(musicc.getRecommender());
                    music[i].setGenre(musicc.getGenre());
                    i++;
                }
            }
            brm.close();

            new stopThread().start();
            while (true) {
                System.out.println("サーバーは稼働しています。");
                Socket socket = ServerS.accept();
                new ServThread(socket).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class ServThread extends Thread {
    /** Global instance properties filename. */
    private static final String PROPERTIES_FILENAME = "youtube.properties";

    /** Global instance of the HTTP transport. */
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();

    /**
     * Global instance of the max number of videos we want returned (50 = upper
     * limit per page).
     */
    private static final long NUMBER_OF_VIDEOS_RETURNED = 5;

    /** Global instance of Youtube object to make all API requests. */
    private static YouTube youtube;
    private Socket socket;
    OutputStream os;
    static ObjectOutputStream oos;

    public ServThread(Socket socket) {
        this.socket = socket;
        System.out.println("接続されました" + socket.getRemoteSocketAddress());
    }

    public void run() {
        Scanner input = null;
        PrintWriter out = null;
        BufferedReader ins = null;
        int this_acc = 0;
        String this_id;
        String this_pw;
        Account this_ac;

        Properties properties = new Properties();
        try {
            InputStream in = Search.class.getResourceAsStream("/" + PROPERTIES_FILENAME);
            properties.load(in);

        } catch (IOException e) {
            System.err.println("There was an error reading " + PROPERTIES_FILENAME + ": " + e.getCause()
                    + " : " + e.getMessage());
            System.exit(1);
        }

        try {
            input = new Scanner(socket.getInputStream());
            ins = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

            while (true) {
                System.out.println("selecting menu");
                int cmd = Integer.parseInt(ins.readLine());
                switch (cmd) { // switch文の判定番号は後から変える
                    case 1:// ログイン
                        System.out.println("Log in");
                        //input.nextLine();
                        this_id = ins.readLine();
                        this_pw = ins.readLine();
                        // System.out.println("ID:"+this_id+" PW:"+this_pw);
                        int flaga = 0;
                        for (int i = 0; i < Search.num_account; i++) {
                            if ((Search.account[i].getID().equals(this_id)
                                    && (Search.account[i].getPW().equals(this_pw)))) {
                                System.out.println("ok");
                                out.println("Success");
                                flaga = 1;
                                this_ac = Search.account[i];
                                System.out.println(
                                        "ID:" + Search.account[i].getID() + " PW:" + Search.account[i].getPW());
                                this_acc = i;
                            }
                        }
                        if (flaga == 0) {
                            out.println("0");
                        } else if (flaga == 1) {
                            out.println(Search.num_music);
                            System.out.println(Search.num_music);
                            Thread.sleep(500);
                            for (int i = 0; i < Search.num_music; i++) {
                                System.out.println(ow.writeValueAsString(Search.music[i]));
                                oos.writeObject(Search.music[i]);
                                oos.flush();
                                // Thread.sleep(500);
                            }
                        }
                        break;
                    case 2:// 新規アカウント作成
                           // System.out.println(input.nextLine());
                           // System.out.println(ins.readLine());
                        System.out.println("Sign up");
                        if (Search.num_account < 100) {
                            boolean flag = false;
                            String idNew = ins.readLine();
                            Search.account[Search.num_account].setID(idNew);
                            Search.account[Search.num_account].setPW(ins.readLine());

                            for (int i = 0; i < Search.num_account; i++) {
                                if (Search.account[i].getID().equals(idNew)) {
                                    flag = true;
                                }
                            }

                            // 表示
                            System.out.println("ID:" + Search.account[Search.num_account].getID() + " PW:"
                                    + Search.account[Search.num_account].getPW());
                            if (flag) {
                                System.out.println("flag: true");
                                out.println("0");
                            } else {
                                System.out.println("flag: false");
                                Search.num_account++;
                                out.println("Success");
                            }

                        } else {
                            out.println("0");
                        }

                        break;
                    case 3: // 曲の追加
                        System.out.println("Add music");
                        int musicIndex = -1;
                        int musicGenre;
                        String musicRecommender = null;
                        String musicMessage = null;
                        Music dmusic = null;

                        // APIによる実装
                        try {

                            /*
                             * The YouTube object is used to make all API requests. The last argument is
                             * required, but
                             * because we don't need anything initialized when the HttpRequest is
                             * initialized, we override
                             * the interface and provide a no-op function.
                             */
                            youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
                                public void initialize(HttpRequest request) {
                                }
                            }).setApplicationName("youtube-cmdline-search-sample").build();

                            YouTube.Search.List search = youtube.search().list("id,snippet");
                            /*
                             * It is important to set your API key from the Google Developer Console for
                             * non-authenticated requests (found under the Credentials tab at this link:
                             * console.developers.google.com/). This is good practice and increased your
                             * quota.
                             */
                            String apiKey = properties.getProperty("youtube.apikey");
                            search.setKey(apiKey);
                            while (true) {
                                // Get query term from user.

                                String queryTerm = getInputQuery(ins);

                                if (queryTerm.equals("---c-o-n-f-i-r-m---")) {
                                    break;
                                }
                                search.setQ(queryTerm);
                                /*
                                 * We are only searching for videos (not playlists or channels). If we were
                                 * searching for
                                 * more, we would add them as a string like this: "video,playlist,channel".
                                 */
                                search.setType("video");
                               search.setVideoCategoryId("10");// only musics
                                /*
                                 * This method reduces the info returned to only the fields we need and makes
                                 * calls more
                                 * efficient.
                                 */
                                search.setFields(
                                        "items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
                                search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
                                SearchListResponse searchResponse = search.execute();

                                List<SearchResult> searchResultList = searchResponse.getItems();
                                if (searchResultList != null) {
                                    System.out.println("searching...");
                                    getInfo(searchResultList.iterator(), queryTerm, socket);
                                }
                                // System.out.println("while");
                            }

                        } catch (GoogleJsonResponseException e) {
                            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                                    + e.getDetails().getMessage());
                        } catch (IOException e) {
                            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                        System.out.println("going to add music");
                        // input.nextLine();
                        /*
                         * musicIndex = Integer.parseInt(input.nextLine());
                         * musicRecommender = input.nextLine();
                         * musicGenre = Integer.parseInt(input.nextLine());
                         * musicMessage = input.nextLine();
                         */

                        musicIndex = Integer.parseInt(ins.readLine());
                        musicRecommender = ins.readLine();
                        musicGenre = Integer.parseInt(ins.readLine());
                        musicMessage = ins.readLine();

                        System.out.println("musicIndex:" + musicIndex + " musicGenre:" + musicGenre);
                        // musicMessage:"+musicMessage);
                        // System.out.println(Search.temp[musicIndex]);
                        dmusic = Search.temp[musicIndex];

                        Thread.sleep(500);
                        out.println("Success");
                        dmusic.setRecommender(musicRecommender);
                        dmusic.setGenre(musicGenre);
                        dmusic.setMsg(musicMessage);
                        dmusic.setLike(0);
                        add_Music(dmusic);

                        break;
                    case 4:// 曲のいいね更新
                           // String hmusicID = input.nextLine();
                        System.out.println("update like");
                        String hmusicID = ins.readLine();
                        for (int l = 0; l < Search.num_music; l++) {
                            if (Search.music[l].getTitle().equals(hmusicID)) {
                                Search.music[l].likeCount();
                            }
                        }

                        break;
                    /*
                     * case 5:// パスワード再設定
                     * input.nextLine();
                     * Search.account[this_acc].setPW(input.nextLine());
                     * out.println("Success");
                     * System.out.println(
                     * "ID:" + Search.account[this_acc].getID() + " PW:" +
                     * Search.account[this_acc].getPW());
                     * break;
                     */
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchElementException e1) {
            out.close();
            // input.close();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private static String getInputQuery(BufferedReader in) throws IOException {
        String term;

        term = in.readLine();
        
        
        

        if (term == null || term.length() < 1) {
            // If nothing is entered, defaults to "YouTube Developers Live."
            term = "YouTube Developers Live";

           
        }else if(term.equals("3")) {
            term = in.readLine();
        }

        System.out.println(term);
        return term;
        
    }

    private static void getInfo(Iterator<SearchResult> iteratorSearchResults, String query, Socket s) {
        try {
            ObjectOutputStream OOS = new ObjectOutputStream(s.getOutputStream());

            if (!iteratorSearchResults.hasNext()) {
                System.out.println(" There aren't any results for your query.");
            }
            // output.println("SUCCESS");
            System.out.println("sending...");
            int i = 0;
            while (iteratorSearchResults.hasNext()) {

                SearchResult singleVideo = iteratorSearchResults.next();
                ResourceId rId = singleVideo.getId();

                // Double checks the kind is video.
                if (rId.getKind().equals("youtube#video")) {
                    Thumbnail thumbnail = (Thumbnail) singleVideo.getSnippet().getThumbnails().get("default");
                    Music musicList = new Music(singleVideo.getSnippet().getTitle(), rId.getVideoId(),
                            thumbnail.getUrl());
                    Search.temp[i++] = musicList;
                    OOS.writeObject(musicList);
                    OOS.flush();
                }
            }
            System.out.println("end");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

     public static void add_Music(Music amusic) {
        if (Search.num_music < 10000) {
            Search.music[Search.num_music] = amusic;
            Search.num_music++;
            Musicgoodcount(Search.num_music - 1);
            Music_sort();
        } else {
            for (int j = 1; j <= Search.num_music; j++) {
                Search.music[Search.num_music - j] = Search.music[Search.num_music - j - 1];
            }
            Search.music[0] = amusic;
            Musicgoodcount(0);
            Music_sort();
        }
    }

    public static void Music_sort() {
        Music music_swap;
        for (int i = 0; i < Search.num_music; i++) {
            for (int j = i + 1; j < Search.num_music; j++) {
                if (Search.music[j].getLike() > Search.music[i].getLike()) {
                    music_swap = Search.music[i];
                    Search.music[i] = Search.music[j];
                    Search.music[j] = music_swap;
                }
            }
        }
    }

    public static void Musicgoodcount(int music_num) {
        int max = 0;
        Search.music[music_num].likeCount();
        max = Search.music[music_num].getLike();
        for (int i = 0; i < Search.num_music; i++) {
            if (Search.music[i].getTitle().equals(Search.music[music_num].getTitle())) {
                if (Search.music[i].getLike() >= Search.music[music_num].getLike()) {
                    max = Search.music[i].getLike();
                }
            }
        }
        max++;
        Search.music[music_num].setLike(max);
        for (int i = 0; i < Search.num_music; i++) {
            if (Search.music[i].getTitle().equals(Search.music[music_num].getTitle())) {
                Search.music[i].setLike(max);
            }
        }
    }
}

class Account implements Serializable {
    private String ID;
    private String PW;

    int[] Genre = new int[9];

    Account() {
        // ジャンル分け
        Genre[0] = 0;
        Genre[1] = 0;
        Genre[2] = 0;
        Genre[3] = 0;
        Genre[4] = 0;
        Genre[5] = 0;
        Genre[6] = 0;
        Genre[7] = 0;
        Genre[8] = 0;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setPW(String PW) {
        this.PW = PW;
    }

    public String getID() {
        return ID;
    }

    public String getPW() {
        return PW;
    }

    public void record(int result) {
        if (Genre[result] != -1) {
            Genre[result]++;
        }
    }

    public void setGenre(int[] Genre) {
        this.Genre = Genre;
    }

    public int[] getGenre() {
        return Genre;
    }
}

class stopThread extends Thread {
    public void run() {
        try {
            Scanner input = new Scanner(System.in);
            String str;
            while (true) {
                str = input.nextLine();
                if (str.equals("exit") || str.equals("stop")) {
                    System.out.println(str);
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.enable(SerializationFeature.INDENT_OUTPUT); // Enable pretty-printing

                    FileWriter fileat = new FileWriter("Account.txt");
                    PrintWriter pwat = new PrintWriter(new BufferedWriter(fileat));
                    pwat.println(Search.num_account);
                    mapper.writeValue(new File("Account.json"), Search.account);
                    pwat.close();
                    System.out.println("Account.json / Account.txt Saved");

                    FileWriter filemt = new FileWriter("Music.txt");
                    PrintWriter pwmt = new PrintWriter(new BufferedWriter(filemt));
                    pwmt.println(Search.num_music);
                    mapper.writeValue(new File("Music.json"), Search.music);
                    pwmt.close();
                    System.out.println("Music.json / Music.txt Saved");
                    System.exit(0);
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}