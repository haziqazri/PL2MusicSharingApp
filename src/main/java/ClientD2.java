import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.List;

public class ClientD2 extends JFrame implements ActionListener {

    // Music related
    private String ID = "";
    private String[] genres = { "J-Pop", "J-Rock", "アニメ", "ポップス", "ロック", "クラシック", "ジャズ", "K-Pop", "演歌・歌謡曲" };
    public Music[] music = new Music[10000];
    private Music selectedMusic = null;
    private Music selectedMusics = null;
    private Music[] musicArray;
    private Music[] musicToShow;

    // Server related
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private ObjectInputStream ois;

    // Panel used in the frame
    private JPanel loginpanel;
    private JPanel accountpanel;
    private JPanel homepanel;
    private JPanel suggestpanel;
    private JPanel detailpanel;
    //private BufferedImage background;
    //private JPanel bg;

    // Screen transition uses
    private CardLayout layout;
    private JPanel panel;

    // Login panel component
    private JLabel loginlabel;
    private JLabel idlabel;
    private JLabel pwlabel;
    private JTextField idInput;
    private JPasswordField pwInput;
    private JButton loginbutton;
    private JButton registerbutton;

    // Create account component
    private JLabel accountlabel;
    private JLabel idAccount;
    private JLabel pwAccount;
    private JLabel pwAccount2;
    private JTextField idNewinput;
    private JPasswordField pwNewinput;
    private JPasswordField pwNewinput2;
    private JButton createbutton;
    private JButton backbutton;
    // private JTextField Q_sec_input;

    // Main lobby component
    // private JPanel musicPanel;
    private JLabel welcomelabel;
    private JButton rcmButton;
    private JLabel rcmLabel;
    private JButton filterButton;
    private JPanel musicPanel;
    List<Integer> selectedGenres = new ArrayList<>();
    private Music[] filteredMusic;
    private JScrollPane scrollPane;
    private JButton[] songButton;

    // おススメする曲の登録画面の要素
    private JTextField searchTextField;
    private JButton backButton;
    private JButton searchButton;
    private JButton[] musicButton;
    private JLabel[] thumbnailLabel;
    private boolean popup = false;
    // private boolean clicked = false;
    private static final int MAX_RESULTS = 5;
    private JPanel thumbnailPanel;

    // Image flag
    boolean searchFlag = false;
    boolean showFlag = false;
    boolean detailFlag = false;

    public ClientD2() {

        try {
            //socket = new Socket("LEEYOUNGMIN.asuscomm.com", 3838);
            socket = new Socket("localhost", 8080);
            System.out.println("Connected to server");
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ois = new ObjectInputStream(socket.getInputStream());



        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Information exchange error: " +
                    e.getMessage());
        }


        // フレームの設定
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1080, 720);
        setTitle("おすすめ曲の登録");
        setResizable(false);



        


        /*-----------------------------------------------------------------------------------------------------------------------------------------*/

        // ログイン
        // Font font13 = new Font("Serif", Font.PLAIN, 13);
        Font font20 = new Font("Gothic", Font.PLAIN, 20);
        // Font font30 = new Font("Serif", Font.PLAIN, 30);

        loginpanel = createScreen();
        //loginpanel = new JPanel();
        loginpanel.setLayout(null);
        loginpanel.setBackground(Color.WHITE);

        loginlabel = new JLabel("ログイン");
        loginlabel.setFont(new Font("Gothic", Font.PLAIN, 50));
        loginlabel.setBounds(420, 110, 400, 60);
        loginlabel.setForeground(Color.WHITE);
        loginpanel.add(loginlabel);

        idlabel = new JLabel("USER ID");
        idlabel.setFont(font20);
        idlabel.setBounds(350, 220, 150, 40);
        idlabel.setForeground(Color.WHITE);
        loginpanel.add(idlabel);

        idInput = new JTextField(16);
        idInput.setBounds(520, 220, 220, 40);
        // idInput.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        loginpanel.add(idInput);

        pwlabel = new JLabel("PASSWORD");
        pwlabel.setFont(font20);
        pwlabel.setForeground(Color.WHITE);
        pwlabel.setBounds(350, 280, 150, 40);
        loginpanel.add(pwlabel);

        pwInput = new JPasswordField(16);
        pwInput.setBounds(520, 280, 220, 40);
        // pwInput.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        loginpanel.add(pwInput);

        loginbutton = new JButton("ログイン");
        loginbutton.setFont(new Font("Gothic", Font.BOLD, 20));
        loginbutton.setBounds(280, 420, 500, 60);
        loginbutton.setBackground(Color.RED);
        loginbutton.setForeground(Color.WHITE);
        loginbutton.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.WHITE));
        loginbutton.setActionCommand("Login");
        loginbutton.addActionListener(this);
        loginpanel.add(loginbutton);

        registerbutton = new JButton("新規アカウント作成");
        registerbutton.setFont(new Font("Gothic", Font.BOLD, 20));
        registerbutton.setBounds(280, 490, 500, 60);
        registerbutton.setBackground(Color.GRAY);
        registerbutton.setForeground(Color.WHITE);
        registerbutton.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.WHITE));
        registerbutton.setActionCommand("NewAccountPage");
        registerbutton.addActionListener(this);
        loginpanel.add(registerbutton);

        /*-----------------------------------------------------------------------------------------------------------------------------------------*/

        // 新規アカウント作成
        accountpanel = createScreen();
        //accountpanel = new JPanel();
        accountpanel.setLayout(null);
        accountpanel.setBackground(Color.WHITE);

        accountlabel = new JLabel("新規登録");
        accountlabel.setFont(new Font("Gothic", Font.PLAIN, 50));
        accountlabel.setBounds(420, 90, 400, 60);
        accountlabel.setForeground(Color.WHITE);
        accountpanel.add(accountlabel);

        idAccount = new JLabel("USER ID");
        idAccount.setFont(font20);
        idAccount.setBounds(350, 220, 150, 40);
        idAccount.setForeground(Color.WHITE);
        accountpanel.add(idAccount);

        idNewinput = new JTextField(16);
        idNewinput.setBounds(520, 220, 220, 40);
        accountpanel.add(idNewinput);

        pwAccount = new JLabel("PASSWORD");
        pwAccount.setFont(font20);
        pwAccount.setForeground(Color.WHITE);
        pwAccount.setBounds(350, 280, 150, 40);
        accountpanel.add(pwAccount);

        pwNewinput = new JPasswordField(16);
        pwNewinput.setBounds(520, 280, 220, 40);
        accountpanel.add(pwNewinput);

        pwAccount2 = new JLabel("PASSWORD2");
        pwAccount2.setFont(font20);
        pwAccount2.setForeground(Color.WHITE);
        pwAccount2.setBounds(350, 340, 150, 40);
        accountpanel.add(pwAccount2);

        pwNewinput2 = new JPasswordField(16);
        pwNewinput2.setBounds(520, 340, 220, 40);
        accountpanel.add(pwNewinput2);

        createbutton = new JButton("確定");
        createbutton.setFont(new Font("Gothic", Font.BOLD, 20));
        createbutton.setBounds(280, 420, 500, 60);
        createbutton.setBackground(Color.RED);
        createbutton.setForeground(Color.WHITE);
        createbutton.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.WHITE));
        createbutton.setActionCommand("CreateAccount");
        createbutton.addActionListener(this);
        accountpanel.add(createbutton);

        backbutton = new JButton("戻る");
        backbutton.setFont(new Font("Gothic", Font.BOLD, 20));
        backbutton.setBounds(280, 490, 500, 60);
        backbutton.setBackground(Color.GRAY);
        backbutton.setForeground(Color.WHITE);
        backbutton.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.WHITE));
        backbutton.setActionCommand("BacktoLogin");
        backbutton.addActionListener(this);
        accountpanel.add(backbutton);

        /*-----------------------------------------------------------------------------------------------------------------------------------------*/

        // Main Lobby settings

        homepanel = createScreen();
        //homepanel = new JPanel();
        homepanel.setLayout(null);
        homepanel.setBackground(Color.WHITE);

        welcomelabel = new JLabel("ようこそ " + ID + " さん!");
        welcomelabel.setFont(new Font("Gothic", Font.BOLD, 30));
        welcomelabel.setForeground(Color.WHITE);
        welcomelabel.setBounds(100, 50, 400, 40);
        homepanel.add(welcomelabel);

        rcmButton = new JButton("\u271A");
        rcmButton.setBounds(100, 150, 50, 50);
        rcmButton.setFont(new Font("Gothic", Font.PLAIN, 15));
        rcmButton.setBackground(Color.RED);
        rcmButton.setForeground(Color.WHITE);
        rcmButton.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.WHITE));
        rcmButton.setActionCommand("Recommend");
        rcmButton.addActionListener(this);
        homepanel.add(rcmButton);

        rcmLabel = new JLabel("曲をおすすめする");
        rcmLabel.setFont(new Font("Gothic", Font.PLAIN, 20));
        rcmLabel.setForeground(Color.WHITE);
        rcmLabel.setBounds(160, 150, 200, 50);
        homepanel.add(rcmLabel);

        filterButton = new JButton("ジャンル選択");
        filterButton.setBounds(850, 150, 150, 50);
        filterButton.setFont(new Font("Gothic", Font.BOLD, 13));
        filterButton.setBackground(Color.RED);
        filterButton.setForeground(Color.WHITE);
        filterButton.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.WHITE));
        filterButton.setActionCommand("Filter");
        filterButton.addActionListener(this);
        homepanel.add(filterButton);

        /*-----------------------------------------------------------------------------------------------------------------------------------------*/

        // 登録画面の設定

        suggestpanel = createScreen();
        //suggestpanel = new JPanel();
        suggestpanel.setLayout(null);
        suggestpanel.setBackground(Color.WHITE);
        suggestpanel.setVisible(true);

        JLabel label = new JLabel("おすすめ曲の登録");
        label.setBounds(100, 50, 400, 40);
        Font labelFont = new Font(Font.SANS_SERIF, Font.BOLD, 30);
        label.setFont(labelFont);
        label.setForeground(Color.WHITE);
        suggestpanel.add(label);

        backButton = new JButton("戻る");
        backButton.setBounds(880, 50, 100, 40);
        backButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        backButton.setBackground(Color.RED);
        backButton.setForeground(Color.WHITE);
        backButton.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.WHITE));
        backButton.setActionCommand("BackMainLobby");
        backButton.addActionListener(this);
        suggestpanel.add(backButton);

        searchTextField = new JTextField("Search here...");
        searchTextField.setBounds(100, 150, 720, 40);
        suggestpanel.add(searchTextField);

        // Textfield をクリアする
        searchTextField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                searchTextField.setText("");
            }
        });

        searchTextField.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // searchMusicFromServer(ID);
                }
            }
        });

        searchButton = new JButton("\u2192");
        searchButton.setBounds(880, 150, 100, 40);
        searchButton.setBackground(Color.RED);
        searchButton.setForeground(Color.WHITE);
        searchButton.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.WHITE));
        searchButton.setActionCommand("Search");
        searchButton.addActionListener(this);
        suggestpanel.add(searchButton);

        musicButton = new JButton[MAX_RESULTS];
        thumbnailLabel = new JLabel[MAX_RESULTS];
        for (int i = 0; i < MAX_RESULTS; i++) {
            musicButton[i] = new JButton();
            musicButton[i].setBounds(100, 240 + i * 80, 880, 60);
            musicButton[i].setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLACK));
            musicButton[i].setBackground(Color.WHITE);
            musicButton[i].setLayout(new BorderLayout());

            thumbnailPanel = new JPanel();
            thumbnailPanel.setPreferredSize(new Dimension(50, 50));
            thumbnailLabel[i] = new JLabel();
            thumbnailLabel[i].setPreferredSize(new Dimension(50, 50));
            thumbnailPanel.add(thumbnailLabel[i]);
            musicButton[i].add(thumbnailPanel, BorderLayout.WEST);
            musicButton[i].setActionCommand("Suggest");
            musicButton[i].addActionListener(this);

            // Add mouse listener to the music button
            if (!popup) {
                musicButton[i].addMouseListener(new MouseAdapter() {

                    private int originalWidth;
                    private int originalHeight;
                    private int originalX;
                    private int originalY;

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        // マウス入ってからサイズチェンジ
                        JButton button = (JButton) e.getSource();

                        // if (!popup && clicked) {
                        originalWidth = button.getWidth();
                        originalHeight = button.getHeight();
                        originalX = button.getX();
                        originalY = button.getY();

                        button.setBounds(originalX - 5, originalY - 5, originalWidth + 10, originalHeight + 10);
                        // }

                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        // マウス出たらサイズチェンジ

                        JButton button = (JButton) e.getSource();
                        // if (!popup && clicked) {
                        button.setBounds(originalX, originalY, originalWidth, originalHeight);
                        // }
                    }

                });
            }

            suggestpanel.add(musicButton[i]);

        }

        /*-----------------------------------------------------------------------------------------------------------------------------------------*/

        detailpanel = createScreen();
       // detailpanel = new JPanel();
        detailpanel.setLayout(null);

        // 画面遷移
        panel = new JPanel();
        layout = new CardLayout();



        panel.setLayout(layout);
        panel.add(loginpanel, "Login");
        panel.add(accountpanel, "Account");
        panel.add(homepanel, "Home");
        panel.add(suggestpanel, "Suggest");
        panel.add(detailpanel, "Detail");
        layout.show(panel, "Login");

        

        add(panel);
        setVisible(true);

    }

    private static JPanel createScreen() {
        JPanel screen = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    BufferedImage image = ImageIO.read(new File("icon/background1.jpg"));
                    g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        

        return screen;
    }

    public void actionPerformed(ActionEvent e) {

        String cmd = e.getActionCommand();
        int musicNum = 0;
        switch (cmd) {
            case "Login":
                boolean logincheck = authentication(ID = idInput.getText(), String.valueOf(pwInput.getPassword()));
                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                if (logincheck) {
                    try {
                        Scanner input = new Scanner(socket.getInputStream());
                        welcomelabel.setText("ようこそ " + ID + " さん");
                        for (int i = 0; i < music.length; i++) {
                            music[i] = new Music();
                        }
                        musicNum = Integer.parseInt(input.nextLine());
                        System.out.println("musicNum: " + musicNum);
                        System.out.println("print music");
                        for (int i = musicNum - 1; i >= 0; i--) {
                            music[i] = (Music) ois.readObject();
                            System.out.println("music: \n" + ow.writeValueAsString(music[i]));
                        }

                        // ois.readObject();

                        System.out.println("received music");

                        createMusicPanel();
                        layout.show(panel, "Home");

                    } catch (Exception ex) {
                        System.out.println("Error getting music object :" + ex.getMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(panel, "IDまたはパスワードが間違っています。", "エラー", JOptionPane.ERROR_MESSAGE);
                    pwInput.setText("");
                    idInput.setText("");
                }

                break;
            case "NewAccountPage":

                layout.show(panel, "Account");
                break;
            case "CreateAccount":

                if (idNewinput.getText().equals("") || String.valueOf(pwNewinput.getPassword()).equals("")
                        || String.valueOf(pwNewinput2.getPassword()).equals("")) {

                    JOptionPane.showMessageDialog(panel, "IDまたはパスワードが入力されていません。", "エラー", JOptionPane.ERROR_MESSAGE);

                } else if (!String.valueOf(pwNewinput.getPassword())
                        .equals(String.valueOf(pwNewinput2.getPassword()))) {

                    JOptionPane.showMessageDialog(panel, "パスワードが一致しません。", "エラー", JOptionPane.ERROR_MESSAGE);

                } else {
                    if (setNewAccount(idNewinput.getText(), String.valueOf(pwNewinput.getPassword()))) {

                        JOptionPane.showMessageDialog(panel, "アカウントが作成されました。", "成功", JOptionPane.INFORMATION_MESSAGE);

                        layout.show(panel, "Login");
                    } else {
                        JOptionPane.showMessageDialog(panel, "アカウントの作成に失敗しました。", "エラー", JOptionPane.ERROR_MESSAGE);
                    }
                }
                idNewinput.setText("");
                pwNewinput.setText("");
                pwNewinput2.setText("");
                break;
            case "BacktoLogin":
                layout.show(panel, "Login");
                break;
            case "Filter":
                showFilterDialog();
                break;
            case "Recommend":
                layout.show(panel, "Suggest");
                break;
            case "BackMainLobby":
                layout.show(panel, "Home");
                for (int i = 0; i < 5; i++) {
                    musicButton[i].setText("");
                    musicButton[i].setIcon(null);
                    thumbnailLabel[i].setIcon(null);
                }

                break;
            case "Search":
                searchMusic(ID);
                break;
            case "Suggest":

                JButton mbutton = (JButton) e.getSource();

                int originalWidth = mbutton.getWidth();
                int originalHeight = mbutton.getHeight();
                int originalX = mbutton.getX();
                int originalY = mbutton.getY();
                mbutton.setBounds(originalX + 5, originalY + 5, originalWidth - 10, originalHeight - 10);

                int index = Arrays.asList(musicButton).indexOf(mbutton);
                selectedMusic = null;
                if (musicArray != null) {
                    selectedMusic = musicArray[index];
                }

                showDialogPanel(selectedMusic, index);

                break;

            case "Details":

                JButton sbutton = (JButton) e.getSource();
                int indexs = Arrays.asList(songButton).indexOf(sbutton);
                if (indexs >= 0 && indexs < music.length) {
                    selectedMusics = musicToShow[indexs];
                    createDetailPanel(selectedMusics);
                    layout.show(panel, "Detail");
                } else {
                    System.out.println("Invalid index: " + indexs);
                    return;
                }

                break;
            default:
                break;

        }

       

    }

    private void createMusicPanel() {

        showFlag = true;

        if (musicPanel != null) {

            homepanel.remove(scrollPane);
        }

        musicPanel = new JPanel();
        musicPanel.setLayout(new BoxLayout(musicPanel, BoxLayout.Y_AXIS));
        musicPanel.setBackground(Color.WHITE);
        // musicPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0,
        // Color.WHITE));
        // musicPanel.setBounds(100, 200, 900, 360);

        // musicPanel.removeAll();
        

        if (filteredMusic != null && filteredMusic.length != 0) {
            musicToShow = filteredMusic; // Use the filtered music
            System.out.println("filteredMusic ");
        } else {
            musicToShow = music;
            System.out.println("music ");// Use the original unfiltered music
        }

        int i = 0;
        if (musicToShow != null && musicToShow.length > 0) {
            songButton = new JButton[musicToShow.length];
            for (Music song : musicToShow) {

                if (song == null) {
                    continue;
                }

                songButton[i] = new JButton();
                songButton[i].setPreferredSize(new Dimension(800, 100));
                songButton[i].setBackground(Color.WHITE);
                songButton[i].setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
                songButton[i].setActionCommand("Details");
                songButton[i].addActionListener(this);

                JPanel buttonPanel = new JPanel(new BorderLayout());
                buttonPanel.setBackground(Color.WHITE);

                JPanel thumbnailpanel = new JPanel();
                thumbnailpanel.setLayout(new BorderLayout());
                thumbnailpanel.setPreferredSize(new Dimension(100, 100));
                thumbnailpanel.setBackground(Color.WHITE);

                JLabel songthumbnail = new JLabel();
                String songth = (song != null) ? song.getThumbnailURL() : "";

                songthumbnail.setIcon(createImageIcon(songth));
                // songthumbnail.setHorizontalAlignment(SwingConstants.CENTER);
                thumbnailpanel.add(songthumbnail, BorderLayout.WEST);

                JPanel labelpanel = new JPanel(new BorderLayout());
                labelpanel.setBackground(Color.WHITE);

                JLabel songname = new JLabel();
                String songt = (song != null) ? song.getTitle() : "";
                songname.setText("" + songt);
                songname.setHorizontalAlignment(SwingConstants.CENTER);
                songname.setFont(new Font("Serif", Font.BOLD, 20));
                labelpanel.add(songname, BorderLayout.CENTER);

                JLabel songgenre = new JLabel();
                int g = (song != null) ? song.getGenre() : -1;
                String songg = "";
                if (song != null && g >= 0 && g < genres.length) {
                    songg = genres[g];
                }
                songgenre.setText(songg + "          ");
                songgenre.setFont(new Font("Serif", Font.PLAIN, 15));
                labelpanel.add(songgenre, BorderLayout.EAST);

                buttonPanel.add(thumbnailpanel, BorderLayout.WEST);
                buttonPanel.add(labelpanel, BorderLayout.CENTER);
                songButton[i].setLayout(new BorderLayout());
                songButton[i].add(buttonPanel, BorderLayout.CENTER);

                musicPanel.add(songButton[i]);
                i++;

            }

        } else {
            JLabel noMusicLabel = new JLabel("No music available.");
            noMusicLabel.setFont(new Font("Serif", Font.PLAIN, 16));
            noMusicLabel.setHorizontalAlignment(SwingConstants.CENTER);
            musicPanel.add(noMusicLabel);
        }

        scrollPane = new JScrollPane(musicPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
        scrollBar.setUnitIncrement(20); // Set the scroll unit increment (small scroll amount)
        scrollBar.setBlockIncrement(20);
        scrollBar.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(100, musicPanel.getWidth()));
        scrollPane.setBounds(100, 240, 900, 360);
        // scrollPane.setBorder(BorderFactory.createEmptyBorder());
        // homepanel.add(musicPanel);
        homepanel.add(scrollPane);

        scrollPane.revalidate(); // Revalidate the home panel to update its layout
        scrollPane.repaint();

    }

    private void createDetailPanel(Music music) {

        if (detailpanel != null) {

            detailpanel.removeAll();
        }

        showFlag = false;
        detailFlag = true;
        // detailpanel = new JPanel();
        detailpanel.setLayout(null);
        detailpanel.setBackground(Color.WHITE);

        // 曲詳細画面の要素

        JLabel page_s = new JLabel("曲の詳細");
        page_s.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
        page_s.setBounds(100, 50, 400, 40);
        page_s.setForeground(Color.WHITE);
        detailpanel.add(page_s);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(null); // Set layout as null for the info panel
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBounds(400, 200, 550, 200);
        infoPanel.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK));

        JLabel thumbnail_s = new JLabel();
        thumbnail_s.setIcon(createImageIcon(music.getThumbnailURL()));
        thumbnail_s.setBounds(100, 150, 300, 300);
        thumbnail_s.setBackground(Color.WHITE);
        detailpanel.add(thumbnail_s);

        JLabel title_s = new JLabel("");
        String t = music.getTitle();
        System.out.print("Title :" + t);
        title_s.setText(t);
        title_s.setForeground(Color.WHITE);
        title_s.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.BLACK));
        title_s.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
        title_s.setBounds(100, 420, 800, 40);
        detailpanel.add(title_s);

        JLabel genre_s = new JLabel();
        int g = music.getGenre();
        String genre_string = "";
        switch (g) {
            case 0:
                genre_string = "J-Pop";
                break;
            case 1:
                genre_string = "J-Rock";
                break;
            case 2:
                genre_string = "アニメ";
                break;
            case 3:
                genre_string = "ポップス";
                break;
            case 4:
                genre_string = "ロック";
                break;
            case 5:
                genre_string = "クラシック";
                break;
            case 6:
                genre_string = "ジャズ";
                break;
            case 7:
                genre_string = "K-Pop";
                break;
            case 8:
                genre_string = "演歌・歌謡曲";
                break;
            default:
                break;

        }
        System.out.println("\nGenre :" + g + genre_string);
        genre_s.setText("ジャンル: " + genre_string);
        genre_s.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        // genre_s.setBackground(Color.RED);
        //genre_s.setForeground(Color.WHITE);
        genre_s.setBounds(20, 10, 500, 40);
        infoPanel.add(genre_s);

        JLabel link_s = new JLabel();
        link_s.setText("Youtube で聴く？ ->" + music.getURL());
        link_s.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        link_s.setBounds(20, 145, 500, 40);
        link_s.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.BLACK));
        infoPanel.add(link_s);

        link_s.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                openWebPage(music.getURL());
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                link_s.setForeground(Color.BLUE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                link_s.setForeground(Color.BLACK);
            }
        });

        JLabel explain_s = new JLabel();
        String c = music.getMsg();
        explain_s.setText("紹介文: " + c);
        explain_s.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        explain_s.setBounds(20, 55, 500, 40);
        infoPanel.add(explain_s);

        JLabel recommender_s = new JLabel();
        String r = music.getRecommender();
        recommender_s.setText("おすすめ者: " + r);
        recommender_s.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        recommender_s.setBounds(20, 105, 400, 40);
        infoPanel.add(recommender_s);

        JLabel like_count = new JLabel();
        int like = music.getLike();
        like_count.setText("いいね数 :" + like);
        like_count.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        like_count.setBounds(200, 105, 500, 40);
        infoPanel.add(like_count);

        JButton like_s = new JButton();
        Icon icon = new ImageIcon("icon/like.png");
        Icon icon1 = new ImageIcon("icon/like(1).png");
        like_s.setIcon(icon);
        //like_s.setBackground(detailpanel.getBackground());
        like_s.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.WHITE));
        like_s.setBorder(null);
        like_s.setContentAreaFilled(false);
        like_s.setBounds(462, 500, 150, 150);
        detailpanel.add(like_s);

        // add mouse listener to like_s
        like_s.addMouseListener(new MouseAdapter() {

            boolean clicked = false;

            @Override
            public void mouseClicked(MouseEvent e) {
                if (!clicked) {
                    sendlike(music.getTitle());
                    JOptionPane.showMessageDialog(panel, "この曲をお気に入りに追加しました", "", JOptionPane.INFORMATION_MESSAGE);
                    clicked = true;
                } else {
                    // optionpane to tell that you have liked this song
                    JOptionPane.showMessageDialog(panel, "この曲はすでにお気に入りに追加されています。", "", JOptionPane.INFORMATION_MESSAGE);
                }

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                like_s.setIcon(icon1);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                like_s.setIcon(icon);
            }
        });

        JButton back_s = new JButton("戻る");
        back_s.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        back_s.setBounds(880, 50, 100, 40);
        back_s.setBackground(Color.RED);
        back_s.setForeground(Color.WHITE);
        back_s.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.WHITE));
        back_s.setActionCommand("BackMainLobby");
        back_s.addActionListener(this);
        detailpanel.add(back_s);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            System.out.println("music: \n" + ow.writeValueAsString(music));

        } catch (JsonProcessingException e) {

            e.printStackTrace();
        }

        detailpanel.add(infoPanel);
        panel.add(detailpanel, "Detail");
        showFlag = true;
        detailFlag = false;
    }

    private void openWebPage(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void applyGenreFilter(List<Integer> genres, JDialog dialog) {
        List<Music> filteredList = new ArrayList<>();
    
        if (music != null) {
            for (Music song : music) {
                if (song != null && genres.contains(song.getGenre())) {
                    filteredList.add(song);
                }
            }
        }
    
        if (filteredList.isEmpty()) {
            filteredMusic = null; // Reset the filtered music
            JOptionPane.showMessageDialog(dialog, "このジャンルでおススメされている曲はありません");
        } else {
            filteredMusic = filteredList.toArray(new Music[0]);
        }
    
        System.out.println("Filtered list: " + filteredList);
        System.out.println("Filtered music array: " + Arrays.toString(filteredMusic));
    
        createMusicPanel();
    }
    private void showFilterDialog() {

        for (Component component : homepanel.getComponents()) {
            component.setEnabled(false);
            // scrollPane.setEnabled(false);
        }

        JDialog dialog = new JDialog();
        dialog.setUndecorated(true);
        dialog.setLayout(null);
        Border border = BorderFactory.createLineBorder(Color.GRAY, 1);
        dialog.getRootPane().setBorder(border);

        dialog.setSize(400, 300);

        Window window = SwingUtilities.getWindowAncestor(homepanel);
        window.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                dialog.setLocationRelativeTo(homepanel);
            }
        });

        JLabel messageLabel = new JLabel("聞きたいジャンルは？");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        messageLabel.setBounds(50, 10, 300, 45);
        dialog.add(messageLabel);

        Box genreBox = Box.createVerticalBox();
        genreBox.setBounds(50, 100, 300, 110);

        // Add genre checkboxes
        int rows = 3;
        int columns = 3;
        JRadioButton[] radioButton = new JRadioButton[9];
        for (int i = 0; i < rows; i++) {
            Box rowBox = Box.createHorizontalBox();

            for (int j = 0; j < columns; j++) {
                int index = i * columns + j;
                if (index < genres.length) {
                    radioButton[index] = new JRadioButton(genres[index]);
                    Font genreFont = new Font(Font.SANS_SERIF, 10, 15);
                    radioButton[index].setFont(genreFont);
                    rowBox.add(radioButton[index]);
                }
            }

            genreBox.add(rowBox);
        }

        dialog.add(genreBox);

        JButton okButton = new JButton("確定");
        okButton.setBounds(110, 250, 90, 30);
        okButton.setBackground(Color.RED);
        okButton.setForeground(Color.WHITE);
        okButton.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.WHITE));
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                selectedGenres = new ArrayList<>();
                //selectedGenre = -1;
                for (int i = 0; i < genres.length; i++) {


                    if (radioButton[i].isSelected()) {

                        if (selectedGenres == null) {
                            //selectedGenres = new ArrayList<>(); // Initialize selectedGenres as an ArrayList
                        }
                        selectedGenres.add(i);
                    }
                }
                //System.out.println("Selected genre index: " + selectedGenre);

                //applyGenreFilter(selectedGenres, dialog);

               if (selectedGenres.isEmpty()) {
                    filteredMusic = null; // Reset the filtered music
                } else {
                    applyGenreFilter(selectedGenres, dialog);
                }
                dialog.dispose();
                for (Component component : homepanel.getComponents()) {
                    component.setEnabled(true);
                    // scrollPane.setEnabled(true);
                }
                createMusicPanel();

            }
        });
        dialog.add(okButton);

        JButton cancelButton = new JButton("取り消し");
        cancelButton.setBounds(210, 250, 90, 30);
        cancelButton.setBackground(Color.WHITE);
        cancelButton.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.WHITE));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                for (Component component : homepanel.getComponents()) {
                    component.setEnabled(true);
                    // scrollPane.setEnabled(true);
                }
            }
        });
        dialog.add(cancelButton);

        dialog.setAlwaysOnTop(true);
        dialog.setLocationRelativeTo(homepanel);
        dialog.setVisible(true);

    }

    private void showDialogPanel(Music music, int num) {

        popup = true;
        // int genrenum = 0;

        String[] options = { "確定", "取り消し" };
        for (Component component : suggestpanel.getComponents()) {
            component.setEnabled(false);
        }

        JDialog dialog = new JDialog();
        dialog.setUndecorated(true);
        dialog.setLayout(null);
        dialog.setSize(350, 400);
        Border border = BorderFactory.createLineBorder(Color.GRAY, 1);
        dialog.getRootPane().setBorder(border);

        Window window = SwingUtilities.getWindowAncestor(suggestpanel);
        window.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentMoved(ComponentEvent e) {
                dialog.setLocationRelativeTo(suggestpanel);
            }
        });

        JLabel messageLabel = new JLabel("この曲をおすすめしますか？");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        messageLabel.setBounds(20, 10, 300, 45);
        dialog.add(messageLabel);

        // Create an array to hold the genre checkboxes

        Box genreBox = Box.createVerticalBox();
        genreBox.setBounds(20, 70, 310, 110);
        Border genreBorder = BorderFactory.createLineBorder(Color.GRAY, 1);
        genreBox.setBorder(
                BorderFactory.createTitledBorder(genreBorder, "ジャンルを選択してください", TitledBorder.CENTER, TitledBorder.TOP));

        // Add genre checkboxes
        int rows = 3;
        int columns = 3;
        ButtonGroup buttonGroup = new ButtonGroup();
        JRadioButton[] genreRadioButtons = new JRadioButton[genres.length];

        for (int i = 0; i < genres.length; i++) {
            JRadioButton radioButton = new JRadioButton(genres[i]);
            Font genreFont = new Font(Font.SANS_SERIF, 10, 15);
            radioButton.setFont(genreFont);
            genreRadioButtons[i] = radioButton;
            buttonGroup.add(radioButton);
        }

        int radioButtonIndex = 0;
        for (int i = 0; i < rows; i++) {
            Box rowBox = Box.createHorizontalBox();

            for (int j = 0; j < columns; j++) {
                if (radioButtonIndex < genres.length) {
                    rowBox.add(genreRadioButtons[radioButtonIndex]);
                    radioButtonIndex++;
                }
            }

            genreBox.add(rowBox);
        }

        dialog.add(genreBox);

        JTextArea explanation = new JTextArea("紹介文を書いてください...");
        explanation.setForeground(Color.GRAY);
        explanation.setBounds(20, 200, 310, 120);
        explanation.setLineWrap(true);
        explanation.setWrapStyleWord(true);
        explanation.setAlignmentY(JTextArea.TOP_ALIGNMENT);
        explanation.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {

                explanation.setText("");
                explanation.setForeground(Color.BLACK);

            }

            @Override
            public void focusLost(FocusEvent e) {
                if (explanation.getText().isEmpty()) {
                    explanation.setForeground(Color.LIGHT_GRAY);
                    explanation.setText("紹介文を書いてください");
                }
            }
        });
        dialog.add(explanation);

        JPanel buttonPanel = new JPanel(null);
        buttonPanel.setBounds(70, 340, 210, 30);

        int buttonWidth = 90;
        int buttonHeight = 30;
        int buttonSpacing = 10;
        int x = 10;
        int y = (buttonPanel.getHeight() - buttonHeight) / 2;

        for (int i = 0; i < options.length; i++) {
            JButton button = new JButton(options[i]);
            button.setBounds(x + (buttonWidth + buttonSpacing) * i, y, buttonWidth, buttonHeight);
            if (options[i].equals("確定")) {
                button.setBackground(Color.RED);
                button.setForeground(Color.WHITE);
                button.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.WHITE));
            } else {
                button.setBackground(Color.WHITE);
                button.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.WHITE));
            }
            buttonPanel.add(button);

            button.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {

                    String selectedGenre = "";

                    // Handle button click
                    if (button.getText().equals("確定")) {
                        // ’確定’の場合

                        for (JRadioButton radioButton : genreRadioButtons) {
                            if (radioButton.isSelected()) {
                                selectedGenre = radioButton.getText();
                                break;
                            }
                        }

                        int genrenum = -1;
                        switch (selectedGenre) {
                            case "J-Pop":
                                genrenum = 0;
                                break;
                            case "J-Rock":
                                genrenum = 1;
                                break;
                            case "アニメ":
                                genrenum = 2;
                                break;
                            case "ポップス":
                                genrenum = 3;
                                break;
                            case "ロック":
                                genrenum = 4;
                                break;
                            case "クラシック":
                                genrenum = 5;
                                break;
                            case "ジャズ":
                                genrenum = 6;
                                break;
                            case "K-Pop":
                                genrenum = 7;
                                break;
                            case "演歌・歌謡曲":
                                genrenum = 8;
                                break;
                        }

                        String note = explanation.getText();
                        if (note.equals("紹介文を書いてください...")) {
                            note = "";
                        }
                        out.println("---c-o-n-f-i-r-m---");

                        if (registerMusic(music, ID, num, genrenum, note)) {

                            // Create option pane show success
                            
                            // Send 'like' to server
                            sendlike(music.getTitle());
                        } else {
                            // Create option pane show failure
                           // JOptionPane.showMessageDialog(dialog, "おススメする曲の登録に失敗しました。", "登録失敗",
                            //        JOptionPane.ERROR_MESSAGE);
                        }

                        JOptionPane.showMessageDialog(dialog, "おススメする曲の登録が完了しました。", "登録完了",JOptionPane.INFORMATION_MESSAGE);


                    } else {
                        // "取り消し" の場合
                        out.println("false");
                    }
                    popup = false;
                    for (Component component : suggestpanel.getComponents()) {
                        component.setEnabled(true);
                    }
                    dialog.dispose();
                }

            });
        }

        dialog.add(buttonPanel);
        dialog.setAlwaysOnTop(true);
        dialog.setLocationRelativeTo(suggestpanel);
        dialog.setVisible(true);
    }

    private boolean authentication(String id, String pw) {

        String response = "";

        try {
            out.println("1");
            out.println(id);
            System.out.println("Send id login: " + id);

            out.println(pw);
            System.out.println("Send pw login: " + pw);

            response = in.readLine();
        } catch (IOException e) {
            System.out.println("Login info exchange error: " + e.getMessage());
        }

        return response.equals("Success");

    }

    private boolean setNewAccount(String id, String pw) {

        String response = "";

        try {
            out.println("2");
            out.println(id);
            System.out.println("Send id register: " + id);
            out.println(pw);
            System.out.println("Send pw register: " + pw);
            response = in.readLine();
            System.out.println("Response from server: " + response);
        } catch (IOException e) {
            System.out.println("Register account info exchange error: " + e.getMessage());
        }

        return response.equals("Success");

    }

    private boolean registerMusic(Music music, String name, int num, int genre, String notes) {
        // Send music object, genre and notes to server
        // out.println("Register");
        out.println(num);
        System.out.println("Send music title to server :" + num);
        out.println(name);

        out.println(genre);
        System.out.println("Send music genre to server :" + genre);
        out.println(notes);
        // System.out.println("Send music genre " + genre + " and notes " + notes + " to
        // server);

        String response = "";
        try {
            response = in.readLine();
            System.out.print(response);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return response.equals("Success");

    }

    private void sendlike(String title) {
        out.println("4");
        out.println(title);

    }

    private ImageIcon createImageIcon(String thumbnailURL) {
        if (thumbnailURL != null && !thumbnailURL.isEmpty()) {

            String modifiedURL = thumbnailURL.replace("default.jpg", "hqdefault.jpg");
            try {
                URL url = new URL(modifiedURL);
                Image image = ImageIO.read(url);

                if (showFlag) {
                    return new ImageIcon(image.getScaledInstance(90, 70, Image.SCALE_SMOOTH));
                } else if (searchFlag) {
                    return new ImageIcon(image.getScaledInstance(60, 40, Image.SCALE_SMOOTH));
                } else if (detailFlag) {
                    return new ImageIcon(image.getScaledInstance(250, 180, Image.SCALE_SMOOTH));
                }

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("URLからのサムの読み込みエラー: " + e.getMessage());
            }
        }
        return null;

    }

    private void searchMusic(String name) {
        try {

            showFlag = false;
            searchFlag = true;

            // clicked = true;

            String searchTerm = searchTextField.getText();
            musicArray = new Music[5];
            out.println("3");
            out.println(searchTerm);
            System.out.println("Send search request");
            // out.println(name);
            // System.out.println("Send user id");

            // String response = in.readLine();
            // System.out.println("サーバーからの反応: " + response);
            // Clear previous search results
            for (int i = 0; i < 5; i++) {
                musicButton[i].setText("");
                musicButton[i].setIcon(null);
            }
            ois = new ObjectInputStream(socket.getInputStream());
            for (int i = 0; i < 5; i++) {

                Object obj = ois.readObject();
                System.out.println("Received object");
                if (obj instanceof Music) {
                    musicArray[i] = (Music) obj; // Assign the received Music object to the array
                } else {
                    System.out.println("Received object is not of type Music");
                }
            }

            int i = 0;
            for (Music music : musicArray) {
                String musicInfo = music.getTitle();
                String thumbnailurl = music.getThumbnailURL();

                if (musicInfo != null && thumbnailurl != null) {
                    musicButton[i].setText(musicInfo);
                    ImageIcon imageIcon = createImageIcon(thumbnailurl);
                    thumbnailLabel[i].setIcon(imageIcon);
                    i++;
                } else {
                    break; // Break loop if no more search results
                }

            }
            searchFlag = false;

        } catch (Exception e) {
            System.out.println("Search error: " + e.getMessage());
        }
    }

    



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            /*
             * try {
             * UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
             * } catch (Exception e) {
             * System.out.println(e.getMessage());
             * }
             */

            ClientD2 client = new ClientD2();
            client.setVisible(true);

        });
    }

}


