/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ChuongTrinhTracNghiem;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author ADMIN
 */
public class Main extends javax.swing.JFrame {
    Socket socket;
    ArrayList<CauHoiTracNghiem> list = new ArrayList<>();
    /**
     * Creates new form Main
     */

    List<Integer> list2 = new ArrayList<>();
    List<diemso> list3 = new ArrayList<>();
    int kt = 2;
    int ktkt2 = 1;
    boolean kttime = true;

//    public Main(Socket socket) {
//        this.socket = socket;
//        initComponents();
//        
//    }
   

    private String createAccountMessage(String name, String password){
        return name+ "_" + password;
    }


    private void sendAccountMessage(String str, Socket sock) throws IOException {
        OutputStream ostream = sock.getOutputStream();
        PrintWriter pwrite = new PrintWriter(ostream, true);
        pwrite.println(str);       // sending to server
        pwrite.flush();            // flush the data
    }

    private static String removeNonAscii(String s){
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<s.length(); ++i){
            if(s.charAt(i) < 128){
                sb.append(s.charAt(i));
            }
        }
        return sb.toString();
    }

    private static String replaceUnreadable(String s){

        return s.replaceAll("\\P{Print}", "");
    }

    private void getMessageFromServer(BufferedReader receiveRead, Socket socket) throws IOException {
        String receiveMessage;
        receiveMessage = String.valueOf(receiveRead.readLine());
        receiveMessage = removeNonAscii(receiveMessage);
        receiveMessage = replaceUnreadable(receiveMessage);
        if(!Objects.equals(receiveMessage, "0")) //receive from server
        {
            System.out.print("from server: ");
            System.out.println(receiveMessage); // displaying at DOS prompt
            
            getQuestion(receiveMessage,list);
            XFile.writeObject("test.txt", list);
            System.out.println("OK");
            
        }     
    }
    
    private void getQuestion(String receiveMessage, ArrayList<CauHoiTracNghiem> list){
        String[] listQuestions = receiveMessage.split("@");
        System.out.print(listQuestions.length);
        System.out.println();
        for (int i = 0;i < listQuestions.length;i++){
            String[] str = listQuestions[i].split("_");
            CauHoiTracNghiem question = new CauHoiTracNghiem();
            int index =  Integer.parseInt(str[0]);
            question.setId(index);
            question.setCauHoi(str[1]);
            question.setCauA(str[2]);
            question.setCauB(str[3]);
            question.setCauC(str[4]);
            question.setCauD(str[5]);
            question.setDA(str[6]);
            list.add(question);
            list2.add(question.getId());
        }
    }

    
    public Main(){};
   
     
    public Main(Socket socket) throws IOException {
        String s = "";
        this.socket = socket;
        initComponents();
        InputStream istream = socket.getInputStream();
        BufferedReader receiveRead = new BufferedReader(new InputStreamReader(istream), 1024);
        getMessageFromServer(receiveRead, socket);
        
        thoiGian(txtTime);
        setLocationRelativeTo(null);
        JPanel container = (JPanel) jPanel1.getComponent(1);
        JPanel name = (JPanel) container.getComponent(2);
        kt = Integer.parseInt(name.getName());
        hienThiCauHoi(list2.get(0));
//        thoiGian();
//        list = (ArrayList<CauHoiTracNghiem>)  XFile.readObject("test.txt");
//        Random rd = new Random();
//        while (true) {
//            int value = rd.nextInt((35 - 1) + 1) + 1;
//            if (list2.contains(value) == false) {
//                list2.add(value);
//                s = s + value + "\n";
//            }
//            if (list2.size() == 20) {
//                break;
//            }
//
//        }
    }

    
    public void thoiGian(JLabel lb) {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                int phut = 20, giay = -1;
                while (kttime == true) {
                    try {
                        if (giay == -1) {
                            giay = 59;
                            phut--;
                        }
                        String tg = null;
                        if (giay <= 9) {
                            tg = phut + ":0" + giay;
                        } else {
                            tg = phut + ":" + giay;
                        }
                        lb.setText(tg);
                        if (phut == 0 && giay == 0) {
                            JOptionPane.showMessageDialog(rootPane, "Bạn đã hết thời gian lm bài");
                            ChonDA();
                            hienthi();
                            break;
                        }
                        giay--;
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        break;
                    }
                }
            }

        }
        );
        t1.start();       
    }

    public void hienThiCauHoi(int i) {
        int vitri = 20;
        for (CauHoiTracNghiem u : list) {
            if (i == u.id) {
              //  JOptionPane.showMessageDialog(this, u.DA);
                if (u.cauHoi.length() <= 64) {
                    jspCauHoi.setBounds(20, vitri, 560, 25);
                    jtaCauHoi.setText(u.cauHoi);
                    vitri = vitri + 40;
                } else {
                    float f = u.cauHoi.length() / 64;
                    int m = (((int) Math.floor(f)) + 1);
                    int kq = m * 20 + 20;
                    vitri = kq + 30;
                    jspCauHoi.setSize(560, kq);
                    jtaCauHoi.setText(u.cauHoi);
                }
                //CauA
                if (u.cauA.length() <= 79) {
                    jspCauTL1.setBounds(20, vitri, 560, 25);
                    jtaCauTL1.setText(u.cauA);
                    vitri = vitri + 10 + 25;
                } else {
                    float f = u.cauA.length() / 79;
                    int m = (((int) Math.floor(f)) + 1);
                    int kq = m * 20 + 20;
                    jspCauTL1.setBounds(20, vitri, 560, kq);
                    vitri = vitri + kq;
                    jtaCauTL1.setText(u.cauA);
                }
                //CauB
                if (u.cauB.length() <= 79) {
                    jspCauTL2.setBounds(20, vitri, 560, 25);
                    jtaCauTL2.setText(u.cauB);
                    vitri = vitri + 10 + 25;
                } else {
                    float f = u.cauB.length() / 79;
                    int m = (((int) Math.floor(f)) + 1);
                    int kq = m * 20 + 20;
                    jspCauTL2.setBounds(20, vitri, 560, kq);
                    vitri = vitri + kq;
                    jtaCauTL2.setText(u.cauB);
                }
                //CauC
                if (u.cauC == null) {
                    jtaCauTL3.setText("");
                } else {
                    if (u.cauC.length() <= 79) {
                        jspCauTL3.setBounds(20, vitri, 560, 40);
                        jtaCauTL3.setText(u.cauC);
                        vitri = vitri + 10 + 25;
                    } else {
                        float f = u.cauC.length() / 79;
                        int m = (((int) Math.floor(f)) + 1);
                        int kq = m * 20 + 20;
                        jspCauTL3.setBounds(20, vitri, 560, kq);
                        vitri = vitri + kq;
                        jtaCauTL3.setText(u.cauC);
                    }
                }
                //CauD
                if (u.cauD == null) {
                    jtaCauTL4.setText("");
                } else {
                    if (u.cauD.length() <= 79) {
                        jspCauTL4.setBounds(20, vitri, 560, 25);
                        jtaCauTL4.setText(u.cauD);
                        vitri = vitri + 10 + 25;
                    } else {
                        float f = u.cauD.length() / 79;
                        int m = (((int) Math.floor(f)) + 1);
                        int kq = m * 20 + 20;
                        jspCauTL4.setBounds(20, vitri, 560, kq);
                        vitri = vitri + kq + 10;
                        jtaCauTL4.setText(u.cauD);
                    }
                }
            }

        }
    }

    //Tính Điểm
    public void ChonDA() {
        int diem = 0;
        int n = 0;
        JPanel container = (JPanel) jPanel1.getComponent(1);
        for (int i = 2; i <= 20; i++) {
            String s = "";
            JPanel name = (JPanel) container.getComponent(i);
            for (int u = 0; u <= 8; u++) {
                if (u % 2 != 0) {
                    JCheckBox chk = (JCheckBox) name.getComponent(u);
                    if (chk.isSelected() == true) {
                        s = s + chk.getName();
                    }
                }
            }

            for (CauHoiTracNghiem h : list) {
                if (list2.get(n) == h.id) {
                    if (s.equals(h.DA) == true) {
                        diem++;
                    }
                }
            }
            n++;
        }
        diemso ds = new diemso();
        ds.cauDung = diem;
        ds.cauSai = 20 - diem;
        list3.add(ds);
        XFile.writeObject("diemso.txt", list3);
    }

    public void hienthi() {
        KetQua kq = new KetQua();
        kq.setVisible(true);
        this.setVisible(false);
    }

    public void Hover(int i) {
        JPanel container = (JPanel) jPanel1.getComponent(1);
        JPanel name = (JPanel) container.getComponent(i);
        name.setBackground(new Color(255, 0, 0));
        JPanel nameFirst = (JPanel) name.getComponent(0);
        nameFirst.setBackground(new Color(255, 157, 202));
        JLabel chk = (JLabel) nameFirst.getComponent(0);
        chk.setBorder(new EtchedBorder());
        for (int u = 2; u <= 8; u++) {
            if (u % 2 == 0) {
                JLabel chk1 = (JLabel) name.getComponent(u);
                chk1.setForeground(new Color(255, 204, 0));
            }
        }
    }

    public void ExitHover(int i) {
        JPanel container = (JPanel) jPanel1.getComponent(1);
        JPanel name = (JPanel) container.getComponent(i);
        name.setBackground(new Color(255, 255, 255));
        JPanel nameFirst = (JPanel) name.getComponent(0);
        nameFirst.setBackground(new Color(204, 204, 204));
        JLabel chk = (JLabel) nameFirst.getComponent(0);
        chk.setBorder(new BevelBorder(0));
        for (int u = 2; u <= 8; u++) {
            if (u % 2 == 0) {
                JLabel chk1 = (JLabel) name.getComponent(u);
                chk1.setForeground(new Color(0, 0, 0));
            }
        }
    }
   
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        txtTime = new javax.swing.JLabel();
        jpnCau1 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jlbHover1_1 = new javax.swing.JLabel();
        jCheckBox2 = new javax.swing.JCheckBox();
        jlbHover1_2 = new javax.swing.JLabel();
        jCheckBox3 = new javax.swing.JCheckBox();
        jlbHover1_3 = new javax.swing.JLabel();
        jCheckBox4 = new javax.swing.JCheckBox();
        jlbHover1_4 = new javax.swing.JLabel();
        jpnCau2 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jCheckBox5 = new javax.swing.JCheckBox();
        jLabel11 = new javax.swing.JLabel();
        jCheckBox6 = new javax.swing.JCheckBox();
        jLabel12 = new javax.swing.JLabel();
        jCheckBox7 = new javax.swing.JCheckBox();
        jLabel13 = new javax.swing.JLabel();
        jCheckBox8 = new javax.swing.JCheckBox();
        jLabel14 = new javax.swing.JLabel();
        jpnCau3 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jCheckBox13 = new javax.swing.JCheckBox();
        jLabel22 = new javax.swing.JLabel();
        jCheckBox14 = new javax.swing.JCheckBox();
        jLabel23 = new javax.swing.JLabel();
        jCheckBox15 = new javax.swing.JCheckBox();
        jLabel24 = new javax.swing.JLabel();
        jCheckBox16 = new javax.swing.JCheckBox();
        jLabel25 = new javax.swing.JLabel();
        jpnCau4 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jCheckBox17 = new javax.swing.JCheckBox();
        jLabel27 = new javax.swing.JLabel();
        jCheckBox18 = new javax.swing.JCheckBox();
        jLabel28 = new javax.swing.JLabel();
        jCheckBox19 = new javax.swing.JCheckBox();
        jLabel29 = new javax.swing.JLabel();
        jCheckBox20 = new javax.swing.JCheckBox();
        jLabel30 = new javax.swing.JLabel();
        jpnCau5 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jCheckBox21 = new javax.swing.JCheckBox();
        jLabel32 = new javax.swing.JLabel();
        jCheckBox22 = new javax.swing.JCheckBox();
        jLabel33 = new javax.swing.JLabel();
        jCheckBox23 = new javax.swing.JCheckBox();
        jLabel34 = new javax.swing.JLabel();
        jCheckBox24 = new javax.swing.JCheckBox();
        jLabel35 = new javax.swing.JLabel();
        jpnCau6 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        jCheckBox25 = new javax.swing.JCheckBox();
        jLabel37 = new javax.swing.JLabel();
        jCheckBox26 = new javax.swing.JCheckBox();
        jLabel38 = new javax.swing.JLabel();
        jCheckBox27 = new javax.swing.JCheckBox();
        jLabel39 = new javax.swing.JLabel();
        jCheckBox28 = new javax.swing.JCheckBox();
        jLabel40 = new javax.swing.JLabel();
        jpnCau7 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        jCheckBox29 = new javax.swing.JCheckBox();
        jLabel42 = new javax.swing.JLabel();
        jCheckBox30 = new javax.swing.JCheckBox();
        jLabel43 = new javax.swing.JLabel();
        jCheckBox31 = new javax.swing.JCheckBox();
        jLabel44 = new javax.swing.JLabel();
        jCheckBox32 = new javax.swing.JCheckBox();
        jLabel45 = new javax.swing.JLabel();
        jpnCau8 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        jCheckBox33 = new javax.swing.JCheckBox();
        jLabel47 = new javax.swing.JLabel();
        jCheckBox34 = new javax.swing.JCheckBox();
        jLabel48 = new javax.swing.JLabel();
        jCheckBox35 = new javax.swing.JCheckBox();
        jLabel49 = new javax.swing.JLabel();
        jCheckBox36 = new javax.swing.JCheckBox();
        jLabel50 = new javax.swing.JLabel();
        jpnCau9 = new javax.swing.JPanel();
        jPanel28 = new javax.swing.JPanel();
        jLabel51 = new javax.swing.JLabel();
        jCheckBox37 = new javax.swing.JCheckBox();
        jLabel52 = new javax.swing.JLabel();
        jCheckBox38 = new javax.swing.JCheckBox();
        jLabel53 = new javax.swing.JLabel();
        jCheckBox39 = new javax.swing.JCheckBox();
        jLabel54 = new javax.swing.JLabel();
        jCheckBox40 = new javax.swing.JCheckBox();
        jLabel55 = new javax.swing.JLabel();
        jpnCau10 = new javax.swing.JPanel();
        jPanel30 = new javax.swing.JPanel();
        jLabel56 = new javax.swing.JLabel();
        jCheckBox41 = new javax.swing.JCheckBox();
        jLabel57 = new javax.swing.JLabel();
        jCheckBox42 = new javax.swing.JCheckBox();
        jLabel58 = new javax.swing.JLabel();
        jCheckBox43 = new javax.swing.JCheckBox();
        jLabel59 = new javax.swing.JLabel();
        jCheckBox44 = new javax.swing.JCheckBox();
        jLabel60 = new javax.swing.JLabel();
        jpnCau11 = new javax.swing.JPanel();
        jPanel50 = new javax.swing.JPanel();
        jLabel106 = new javax.swing.JLabel();
        jCheckBox81 = new javax.swing.JCheckBox();
        jLabel107 = new javax.swing.JLabel();
        jCheckBox82 = new javax.swing.JCheckBox();
        jLabel108 = new javax.swing.JLabel();
        jCheckBox83 = new javax.swing.JCheckBox();
        jLabel109 = new javax.swing.JLabel();
        jCheckBox84 = new javax.swing.JCheckBox();
        jLabel110 = new javax.swing.JLabel();
        jpnCau12 = new javax.swing.JPanel();
        jPanel32 = new javax.swing.JPanel();
        jLabel61 = new javax.swing.JLabel();
        jCheckBox45 = new javax.swing.JCheckBox();
        jLabel62 = new javax.swing.JLabel();
        jCheckBox46 = new javax.swing.JCheckBox();
        jLabel63 = new javax.swing.JLabel();
        jCheckBox47 = new javax.swing.JCheckBox();
        jLabel64 = new javax.swing.JLabel();
        jCheckBox48 = new javax.swing.JCheckBox();
        jLabel65 = new javax.swing.JLabel();
        jpnCau13 = new javax.swing.JPanel();
        jPanel34 = new javax.swing.JPanel();
        jLabel66 = new javax.swing.JLabel();
        jCheckBox49 = new javax.swing.JCheckBox();
        jLabel67 = new javax.swing.JLabel();
        jCheckBox50 = new javax.swing.JCheckBox();
        jLabel68 = new javax.swing.JLabel();
        jCheckBox51 = new javax.swing.JCheckBox();
        jLabel69 = new javax.swing.JLabel();
        jCheckBox52 = new javax.swing.JCheckBox();
        jLabel70 = new javax.swing.JLabel();
        jpnCau14 = new javax.swing.JPanel();
        jPanel36 = new javax.swing.JPanel();
        jLabel71 = new javax.swing.JLabel();
        jCheckBox53 = new javax.swing.JCheckBox();
        jLabel72 = new javax.swing.JLabel();
        jCheckBox54 = new javax.swing.JCheckBox();
        jLabel73 = new javax.swing.JLabel();
        jCheckBox55 = new javax.swing.JCheckBox();
        jLabel74 = new javax.swing.JLabel();
        jCheckBox56 = new javax.swing.JCheckBox();
        jLabel75 = new javax.swing.JLabel();
        jpnCau15 = new javax.swing.JPanel();
        jPanel38 = new javax.swing.JPanel();
        jLabel76 = new javax.swing.JLabel();
        jCheckBox57 = new javax.swing.JCheckBox();
        jLabel77 = new javax.swing.JLabel();
        jCheckBox58 = new javax.swing.JCheckBox();
        jLabel78 = new javax.swing.JLabel();
        jCheckBox59 = new javax.swing.JCheckBox();
        jLabel79 = new javax.swing.JLabel();
        jCheckBox60 = new javax.swing.JCheckBox();
        jLabel80 = new javax.swing.JLabel();
        jpnCau16 = new javax.swing.JPanel();
        jPanel40 = new javax.swing.JPanel();
        jLabel81 = new javax.swing.JLabel();
        jCheckBox61 = new javax.swing.JCheckBox();
        jLabel82 = new javax.swing.JLabel();
        jCheckBox62 = new javax.swing.JCheckBox();
        jLabel83 = new javax.swing.JLabel();
        jCheckBox63 = new javax.swing.JCheckBox();
        jLabel84 = new javax.swing.JLabel();
        jCheckBox64 = new javax.swing.JCheckBox();
        jLabel85 = new javax.swing.JLabel();
        jpnCau17 = new javax.swing.JPanel();
        jPanel42 = new javax.swing.JPanel();
        jLabel86 = new javax.swing.JLabel();
        jCheckBox65 = new javax.swing.JCheckBox();
        jLabel87 = new javax.swing.JLabel();
        jCheckBox66 = new javax.swing.JCheckBox();
        jLabel88 = new javax.swing.JLabel();
        jCheckBox67 = new javax.swing.JCheckBox();
        jLabel89 = new javax.swing.JLabel();
        jCheckBox68 = new javax.swing.JCheckBox();
        jLabel90 = new javax.swing.JLabel();
        jpnCau18 = new javax.swing.JPanel();
        jPanel44 = new javax.swing.JPanel();
        jLabel91 = new javax.swing.JLabel();
        jCheckBox69 = new javax.swing.JCheckBox();
        jLabel92 = new javax.swing.JLabel();
        jCheckBox70 = new javax.swing.JCheckBox();
        jLabel93 = new javax.swing.JLabel();
        jCheckBox71 = new javax.swing.JCheckBox();
        jLabel94 = new javax.swing.JLabel();
        jCheckBox72 = new javax.swing.JCheckBox();
        jLabel95 = new javax.swing.JLabel();
        jpnCau19 = new javax.swing.JPanel();
        jPanel46 = new javax.swing.JPanel();
        jLabel96 = new javax.swing.JLabel();
        jCheckBox73 = new javax.swing.JCheckBox();
        jLabel97 = new javax.swing.JLabel();
        jCheckBox74 = new javax.swing.JCheckBox();
        jLabel98 = new javax.swing.JLabel();
        jCheckBox75 = new javax.swing.JCheckBox();
        jLabel99 = new javax.swing.JLabel();
        jCheckBox76 = new javax.swing.JCheckBox();
        jLabel100 = new javax.swing.JLabel();
        jpnCau20 = new javax.swing.JPanel();
        jPanel48 = new javax.swing.JPanel();
        jLabel101 = new javax.swing.JLabel();
        jCheckBox77 = new javax.swing.JCheckBox();
        jLabel102 = new javax.swing.JLabel();
        jCheckBox78 = new javax.swing.JCheckBox();
        jLabel103 = new javax.swing.JLabel();
        jCheckBox79 = new javax.swing.JCheckBox();
        jLabel104 = new javax.swing.JLabel();
        jCheckBox80 = new javax.swing.JCheckBox();
        jLabel105 = new javax.swing.JLabel();
        btnKQ = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jspCauHoi = new javax.swing.JScrollPane();
        jtaCauHoi = new javax.swing.JTextPane();
        jspCauTL1 = new javax.swing.JScrollPane();
        jtaCauTL1 = new javax.swing.JTextPane();
        jspCauTL2 = new javax.swing.JScrollPane();
        jtaCauTL2 = new javax.swing.JTextPane();
        jspCauTL3 = new javax.swing.JScrollPane();
        jtaCauTL3 = new javax.swing.JTextPane();
        jspCauTL4 = new javax.swing.JScrollPane();
        jtaCauTL4 = new javax.swing.JTextPane();
        jPanel12 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.gray, null));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 0, 0));
        jLabel1.setText("Ứng dụng thi trắc nghiệm trực tuyến");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 12, 350, -1));

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(153, 153, 153), null));
        jPanel2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel2.setName("jp1"); // NOI18N
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(0, 0, 0));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 153, 0));
        jLabel3.setText("Thời gian còn lại");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 2, -1, 35));

        jPanel5.setBackground(new java.awt.Color(0, 0, 0));

        txtTime.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        txtTime.setForeground(new java.awt.Color(255, 255, 0));
        txtTime.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTime.setText("20:00");
        txtTime.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
                txtTimeAncestorMoved(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(txtTime, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtTime, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(202, 2, 188, 35));

        jpnCau1.setBackground(new java.awt.Color(255, 255, 255));
        jpnCau1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jpnCau1.setName("2"); // NOI18N
        jpnCau1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jpnCau1MouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jpnCau1MouseReleased(evt);
            }
        });
        jpnCau1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel8.setBackground(new java.awt.Color(204, 204, 204));
        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel5.setBackground(new java.awt.Color(204, 204, 204));
        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("1");
        jLabel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel5.setPreferredSize(new java.awt.Dimension(40, 40));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
        );

        jpnCau1.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 40, 40));

        buttonGroup1.add(jCheckBox1);
        jCheckBox1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox1.setAlignmentY(2.0F);
        jCheckBox1.setContentAreaFilled(false);
        jCheckBox1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox1.setName("A"); // NOI18N
        jCheckBox1.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau1.add(jCheckBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(45, 15, 20, 20));

        jlbHover1_1.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jlbHover1_1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlbHover1_1.setText("1");
        jlbHover1_1.setName("1_1"); // NOI18N
        jpnCau1.add(jlbHover1_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 4, 15, 10));

        buttonGroup1.add(jCheckBox2);
        jCheckBox2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox2.setAlignmentY(2.0F);
        jCheckBox2.setContentAreaFilled(false);
        jCheckBox2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox2.setName("B"); // NOI18N
        jCheckBox2.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau1.add(jCheckBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 15, 20, 20));

        jlbHover1_2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jlbHover1_2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlbHover1_2.setText("2");
        jpnCau1.add(jlbHover1_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(72, 2, 15, 12));

        buttonGroup1.add(jCheckBox3);
        jCheckBox3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox3.setAlignmentY(2.0F);
        jCheckBox3.setContentAreaFilled(false);
        jCheckBox3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox3.setName("C"); // NOI18N
        jCheckBox3.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau1.add(jCheckBox3, new org.netbeans.lib.awtextra.AbsoluteConstraints(95, 15, 20, 20));

        jlbHover1_3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jlbHover1_3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlbHover1_3.setText("3");
        jpnCau1.add(jlbHover1_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(97, 2, 15, 12));

        buttonGroup1.add(jCheckBox4);
        jCheckBox4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox4.setAlignmentY(2.0F);
        jCheckBox4.setContentAreaFilled(false);
        jCheckBox4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox4.setName("D"); // NOI18N
        jCheckBox4.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau1.add(jCheckBox4, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 15, 20, 20));

        jlbHover1_4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jlbHover1_4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlbHover1_4.setText("4");
        jpnCau1.add(jlbHover1_4, new org.netbeans.lib.awtextra.AbsoluteConstraints(123, 2, 15, 12));

        jPanel2.add(jpnCau1, new org.netbeans.lib.awtextra.AbsoluteConstraints(42, 44, 148, 41));

        jpnCau2.setBackground(new java.awt.Color(255, 255, 255));
        jpnCau2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jpnCau2.setName("3"); // NOI18N
        jpnCau2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jpnCau2MouseClicked(evt);
            }
        });
        jpnCau2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel10.setBackground(new java.awt.Color(204, 204, 204));
        jPanel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel10.setBackground(new java.awt.Color(204, 204, 204));
        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("2");
        jLabel10.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel10.setPreferredSize(new java.awt.Dimension(40, 40));

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
        );

        jpnCau2.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 40, 40));

        jCheckBox5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox5.setAlignmentY(2.0F);
        jCheckBox5.setContentAreaFilled(false);
        jCheckBox5.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox5.setName("A"); // NOI18N
        jCheckBox5.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau2.add(jCheckBox5, new org.netbeans.lib.awtextra.AbsoluteConstraints(45, 15, 20, 20));

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("1");
        jpnCau2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 4, 15, 10));

        jCheckBox6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox6.setAlignmentY(2.0F);
        jCheckBox6.setContentAreaFilled(false);
        jCheckBox6.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox6.setName("B"); // NOI18N
        jCheckBox6.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau2.add(jCheckBox6, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 15, 20, 20));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("2");
        jpnCau2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(72, 2, 15, 12));

        jCheckBox7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox7.setAlignmentY(2.0F);
        jCheckBox7.setContentAreaFilled(false);
        jCheckBox7.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox7.setName("C"); // NOI18N
        jCheckBox7.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau2.add(jCheckBox7, new org.netbeans.lib.awtextra.AbsoluteConstraints(95, 15, 20, 20));

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("3");
        jpnCau2.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(97, 2, 15, 12));

        jCheckBox8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox8.setAlignmentY(2.0F);
        jCheckBox8.setContentAreaFilled(false);
        jCheckBox8.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox8.setName("D"); // NOI18N
        jCheckBox8.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau2.add(jCheckBox8, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 15, 20, 20));

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("4");
        jpnCau2.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(123, 2, 15, 12));

        jPanel2.add(jpnCau2, new org.netbeans.lib.awtextra.AbsoluteConstraints(42, 103, 148, 41));

        jpnCau3.setBackground(new java.awt.Color(255, 255, 255));
        jpnCau3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jpnCau3.setName("4"); // NOI18N
        jpnCau3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jpnCau3MouseClicked(evt);
            }
        });
        jpnCau3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel16.setBackground(new java.awt.Color(204, 204, 204));
        jPanel16.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel21.setBackground(new java.awt.Color(204, 204, 204));
        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("3");
        jLabel21.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel21.setPreferredSize(new java.awt.Dimension(40, 40));

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
        );

        jpnCau3.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 40, 40));

        jCheckBox13.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox13.setAlignmentY(2.0F);
        jCheckBox13.setContentAreaFilled(false);
        jCheckBox13.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox13.setName("A"); // NOI18N
        jCheckBox13.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau3.add(jCheckBox13, new org.netbeans.lib.awtextra.AbsoluteConstraints(45, 15, 20, 20));

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("1");
        jpnCau3.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 4, 15, 10));

        jCheckBox14.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox14.setAlignmentY(2.0F);
        jCheckBox14.setContentAreaFilled(false);
        jCheckBox14.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox14.setName("B"); // NOI18N
        jCheckBox14.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau3.add(jCheckBox14, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 15, 20, 20));

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("2");
        jpnCau3.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(72, 2, 15, 12));

        jCheckBox15.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox15.setAlignmentY(2.0F);
        jCheckBox15.setContentAreaFilled(false);
        jCheckBox15.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox15.setName("C"); // NOI18N
        jCheckBox15.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau3.add(jCheckBox15, new org.netbeans.lib.awtextra.AbsoluteConstraints(95, 15, 20, 20));

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("3");
        jpnCau3.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(97, 2, 15, 12));

        jCheckBox16.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox16.setAlignmentY(2.0F);
        jCheckBox16.setContentAreaFilled(false);
        jCheckBox16.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox16.setName("D"); // NOI18N
        jCheckBox16.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau3.add(jCheckBox16, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 15, 20, 20));

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("4");
        jpnCau3.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(123, 2, 15, 12));

        jPanel2.add(jpnCau3, new org.netbeans.lib.awtextra.AbsoluteConstraints(42, 162, 148, 41));

        jpnCau4.setBackground(new java.awt.Color(255, 255, 255));
        jpnCau4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jpnCau4.setName("5"); // NOI18N
        jpnCau4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jpnCau4MouseClicked(evt);
            }
        });
        jpnCau4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel18.setBackground(new java.awt.Color(204, 204, 204));
        jPanel18.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel26.setBackground(new java.awt.Color(204, 204, 204));
        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setText("4");
        jLabel26.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel26.setPreferredSize(new java.awt.Dimension(40, 40));

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
        );

        jpnCau4.add(jPanel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 40, 40));

        jCheckBox17.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox17.setAlignmentY(2.0F);
        jCheckBox17.setContentAreaFilled(false);
        jCheckBox17.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox17.setName("A"); // NOI18N
        jCheckBox17.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau4.add(jCheckBox17, new org.netbeans.lib.awtextra.AbsoluteConstraints(45, 15, 20, 20));

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel27.setText("1");
        jpnCau4.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 4, 15, 10));

        jCheckBox18.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox18.setAlignmentY(2.0F);
        jCheckBox18.setContentAreaFilled(false);
        jCheckBox18.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox18.setName("B"); // NOI18N
        jCheckBox18.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau4.add(jCheckBox18, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 15, 20, 20));

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setText("2");
        jpnCau4.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(72, 2, 15, 12));

        jCheckBox19.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox19.setAlignmentY(2.0F);
        jCheckBox19.setContentAreaFilled(false);
        jCheckBox19.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox19.setName("C"); // NOI18N
        jCheckBox19.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau4.add(jCheckBox19, new org.netbeans.lib.awtextra.AbsoluteConstraints(95, 15, 20, 20));

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel29.setText("3");
        jpnCau4.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(97, 2, 15, 12));

        jCheckBox20.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox20.setAlignmentY(2.0F);
        jCheckBox20.setContentAreaFilled(false);
        jCheckBox20.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox20.setName("D"); // NOI18N
        jCheckBox20.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau4.add(jCheckBox20, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 15, 20, 20));

        jLabel30.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel30.setText("4");
        jpnCau4.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(123, 2, 15, 12));

        jPanel2.add(jpnCau4, new org.netbeans.lib.awtextra.AbsoluteConstraints(42, 221, 148, 41));

        jpnCau5.setBackground(new java.awt.Color(255, 255, 255));
        jpnCau5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jpnCau5.setName("6"); // NOI18N
        jpnCau5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jpnCau5MouseClicked(evt);
            }
        });
        jpnCau5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel20.setBackground(new java.awt.Color(204, 204, 204));
        jPanel20.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel31.setBackground(new java.awt.Color(204, 204, 204));
        jLabel31.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel31.setText("5");
        jLabel31.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel31.setPreferredSize(new java.awt.Dimension(40, 40));

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
        );

        jpnCau5.add(jPanel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 40, 40));

        jCheckBox21.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox21.setAlignmentY(2.0F);
        jCheckBox21.setContentAreaFilled(false);
        jCheckBox21.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox21.setName("A"); // NOI18N
        jCheckBox21.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau5.add(jCheckBox21, new org.netbeans.lib.awtextra.AbsoluteConstraints(45, 15, 20, 20));

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel32.setText("1");
        jpnCau5.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 4, 15, 10));

        jCheckBox22.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox22.setAlignmentY(2.0F);
        jCheckBox22.setContentAreaFilled(false);
        jCheckBox22.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox22.setName("B"); // NOI18N
        jCheckBox22.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau5.add(jCheckBox22, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 15, 20, 20));

        jLabel33.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel33.setText("2");
        jpnCau5.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(72, 2, 15, 12));

        jCheckBox23.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox23.setAlignmentY(2.0F);
        jCheckBox23.setContentAreaFilled(false);
        jCheckBox23.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox23.setName("C"); // NOI18N
        jCheckBox23.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau5.add(jCheckBox23, new org.netbeans.lib.awtextra.AbsoluteConstraints(95, 15, 20, 20));

        jLabel34.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel34.setText("3");
        jpnCau5.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(97, 2, 15, 12));

        jCheckBox24.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox24.setAlignmentY(2.0F);
        jCheckBox24.setContentAreaFilled(false);
        jCheckBox24.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox24.setName("D"); // NOI18N
        jCheckBox24.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau5.add(jCheckBox24, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 15, 20, 20));

        jLabel35.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel35.setText("4");
        jpnCau5.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(123, 2, 15, 12));

        jPanel2.add(jpnCau5, new org.netbeans.lib.awtextra.AbsoluteConstraints(42, 280, 148, 41));

        jpnCau6.setBackground(new java.awt.Color(255, 255, 255));
        jpnCau6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jpnCau6.setName("7"); // NOI18N
        jpnCau6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jpnCau6MouseClicked(evt);
            }
        });
        jpnCau6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel22.setBackground(new java.awt.Color(204, 204, 204));
        jPanel22.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel36.setBackground(new java.awt.Color(204, 204, 204));
        jLabel36.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel36.setText("6");
        jLabel36.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel36.setPreferredSize(new java.awt.Dimension(40, 40));

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
        );

        jpnCau6.add(jPanel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 40, 40));

        jCheckBox25.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox25.setAlignmentY(2.0F);
        jCheckBox25.setContentAreaFilled(false);
        jCheckBox25.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox25.setName("A"); // NOI18N
        jCheckBox25.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau6.add(jCheckBox25, new org.netbeans.lib.awtextra.AbsoluteConstraints(45, 15, 20, 20));

        jLabel37.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel37.setText("1");
        jpnCau6.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 4, 15, 10));

        jCheckBox26.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox26.setAlignmentY(2.0F);
        jCheckBox26.setContentAreaFilled(false);
        jCheckBox26.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox26.setName("B"); // NOI18N
        jCheckBox26.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau6.add(jCheckBox26, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 15, 20, 20));

        jLabel38.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel38.setText("2");
        jpnCau6.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(72, 2, 15, 12));

        jCheckBox27.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox27.setAlignmentY(2.0F);
        jCheckBox27.setContentAreaFilled(false);
        jCheckBox27.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox27.setName("C"); // NOI18N
        jCheckBox27.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau6.add(jCheckBox27, new org.netbeans.lib.awtextra.AbsoluteConstraints(95, 15, 20, 20));

        jLabel39.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel39.setText("3");
        jpnCau6.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(97, 2, 15, 12));

        jCheckBox28.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox28.setAlignmentY(2.0F);
        jCheckBox28.setContentAreaFilled(false);
        jCheckBox28.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox28.setName("D"); // NOI18N
        jCheckBox28.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau6.add(jCheckBox28, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 15, 20, 20));

        jLabel40.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel40.setText("4");
        jpnCau6.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(123, 2, 15, 12));

        jPanel2.add(jpnCau6, new org.netbeans.lib.awtextra.AbsoluteConstraints(42, 339, 148, 41));

        jpnCau7.setBackground(new java.awt.Color(255, 255, 255));
        jpnCau7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jpnCau7.setName("8"); // NOI18N
        jpnCau7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jpnCau7MouseClicked(evt);
            }
        });
        jpnCau7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel24.setBackground(new java.awt.Color(204, 204, 204));
        jPanel24.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel41.setBackground(new java.awt.Color(204, 204, 204));
        jLabel41.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel41.setText("7");
        jLabel41.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel41.setPreferredSize(new java.awt.Dimension(40, 40));

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel24Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel41, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
        );

        jpnCau7.add(jPanel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 40, 40));

        jCheckBox29.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox29.setAlignmentY(2.0F);
        jCheckBox29.setContentAreaFilled(false);
        jCheckBox29.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox29.setName("A"); // NOI18N
        jCheckBox29.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau7.add(jCheckBox29, new org.netbeans.lib.awtextra.AbsoluteConstraints(45, 15, 20, 20));

        jLabel42.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel42.setText("1");
        jpnCau7.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 4, 15, 10));

        jCheckBox30.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox30.setAlignmentY(2.0F);
        jCheckBox30.setContentAreaFilled(false);
        jCheckBox30.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox30.setName("B"); // NOI18N
        jCheckBox30.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau7.add(jCheckBox30, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 15, 20, 20));

        jLabel43.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel43.setText("2");
        jpnCau7.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(72, 2, 15, 12));

        jCheckBox31.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox31.setAlignmentY(2.0F);
        jCheckBox31.setContentAreaFilled(false);
        jCheckBox31.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox31.setName("C"); // NOI18N
        jCheckBox31.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau7.add(jCheckBox31, new org.netbeans.lib.awtextra.AbsoluteConstraints(95, 15, 20, 20));

        jLabel44.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel44.setText("3");
        jpnCau7.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(97, 2, 15, 12));

        jCheckBox32.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox32.setAlignmentY(2.0F);
        jCheckBox32.setContentAreaFilled(false);
        jCheckBox32.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox32.setName("D"); // NOI18N
        jCheckBox32.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau7.add(jCheckBox32, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 15, 20, 20));

        jLabel45.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel45.setText("4");
        jpnCau7.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(123, 2, 15, 12));

        jPanel2.add(jpnCau7, new org.netbeans.lib.awtextra.AbsoluteConstraints(42, 398, 148, 41));

        jpnCau8.setBackground(new java.awt.Color(255, 255, 255));
        jpnCau8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jpnCau8.setName("9"); // NOI18N
        jpnCau8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jpnCau8MouseClicked(evt);
            }
        });
        jpnCau8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel26.setBackground(new java.awt.Color(204, 204, 204));
        jPanel26.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel46.setBackground(new java.awt.Color(204, 204, 204));
        jLabel46.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel46.setText("8");
        jLabel46.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel46.setPreferredSize(new java.awt.Dimension(40, 40));

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel46, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
        );

        jpnCau8.add(jPanel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 40, 40));

        jCheckBox33.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox33.setAlignmentY(2.0F);
        jCheckBox33.setContentAreaFilled(false);
        jCheckBox33.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox33.setName("A"); // NOI18N
        jCheckBox33.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau8.add(jCheckBox33, new org.netbeans.lib.awtextra.AbsoluteConstraints(45, 15, 20, 20));

        jLabel47.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel47.setText("1");
        jpnCau8.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 4, 15, 10));

        jCheckBox34.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox34.setAlignmentY(2.0F);
        jCheckBox34.setContentAreaFilled(false);
        jCheckBox34.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox34.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox34.setName("B"); // NOI18N
        jCheckBox34.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau8.add(jCheckBox34, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 15, 20, 20));

        jLabel48.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel48.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel48.setText("2");
        jpnCau8.add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(72, 2, 15, 12));

        jCheckBox35.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox35.setAlignmentY(2.0F);
        jCheckBox35.setContentAreaFilled(false);
        jCheckBox35.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox35.setName("C"); // NOI18N
        jCheckBox35.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau8.add(jCheckBox35, new org.netbeans.lib.awtextra.AbsoluteConstraints(95, 15, 20, 20));

        jLabel49.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel49.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel49.setText("3");
        jpnCau8.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(97, 2, 15, 12));

        jCheckBox36.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox36.setAlignmentY(2.0F);
        jCheckBox36.setContentAreaFilled(false);
        jCheckBox36.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox36.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox36.setName("D"); // NOI18N
        jCheckBox36.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau8.add(jCheckBox36, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 15, 20, 20));

        jLabel50.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel50.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel50.setText("4");
        jpnCau8.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(123, 2, 15, 12));

        jPanel2.add(jpnCau8, new org.netbeans.lib.awtextra.AbsoluteConstraints(42, 457, 148, 41));

        jpnCau9.setBackground(new java.awt.Color(255, 255, 255));
        jpnCau9.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jpnCau9.setName("10"); // NOI18N
        jpnCau9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jpnCau9MouseClicked(evt);
            }
        });
        jpnCau9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel28.setBackground(new java.awt.Color(204, 204, 204));
        jPanel28.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel51.setBackground(new java.awt.Color(204, 204, 204));
        jLabel51.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel51.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel51.setText("9");
        jLabel51.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel51.setPreferredSize(new java.awt.Dimension(40, 40));

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel28Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel51, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel51, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
        );

        jpnCau9.add(jPanel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 40, 40));

        jCheckBox37.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox37.setAlignmentY(2.0F);
        jCheckBox37.setContentAreaFilled(false);
        jCheckBox37.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox37.setName("A"); // NOI18N
        jCheckBox37.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau9.add(jCheckBox37, new org.netbeans.lib.awtextra.AbsoluteConstraints(45, 15, 20, 20));

        jLabel52.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel52.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel52.setText("1");
        jpnCau9.add(jLabel52, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 4, 15, 10));

        jCheckBox38.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox38.setAlignmentY(2.0F);
        jCheckBox38.setContentAreaFilled(false);
        jCheckBox38.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox38.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox38.setName("B"); // NOI18N
        jCheckBox38.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau9.add(jCheckBox38, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 15, 20, 20));

        jLabel53.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel53.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel53.setText("2");
        jpnCau9.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(72, 2, 15, 12));

        jCheckBox39.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox39.setAlignmentY(2.0F);
        jCheckBox39.setContentAreaFilled(false);
        jCheckBox39.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox39.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox39.setName("C"); // NOI18N
        jCheckBox39.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau9.add(jCheckBox39, new org.netbeans.lib.awtextra.AbsoluteConstraints(95, 15, 20, 20));

        jLabel54.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel54.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel54.setText("3");
        jpnCau9.add(jLabel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(97, 2, 15, 12));

        jCheckBox40.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox40.setAlignmentY(2.0F);
        jCheckBox40.setContentAreaFilled(false);
        jCheckBox40.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox40.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox40.setName("D"); // NOI18N
        jCheckBox40.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau9.add(jCheckBox40, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 15, 20, 20));

        jLabel55.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel55.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel55.setText("4");
        jpnCau9.add(jLabel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(123, 2, 15, 12));

        jPanel2.add(jpnCau9, new org.netbeans.lib.awtextra.AbsoluteConstraints(42, 516, 148, 41));

        jpnCau10.setBackground(new java.awt.Color(255, 255, 255));
        jpnCau10.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jpnCau10.setName("11"); // NOI18N
        jpnCau10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jpnCau10MouseClicked(evt);
            }
        });
        jpnCau10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel30.setBackground(new java.awt.Color(204, 204, 204));
        jPanel30.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel56.setBackground(new java.awt.Color(204, 204, 204));
        jLabel56.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel56.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel56.setText("10");
        jLabel56.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel56.setPreferredSize(new java.awt.Dimension(40, 40));

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel30Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel56, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
        );

        jpnCau10.add(jPanel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 40, 40));

        jCheckBox41.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox41.setAlignmentY(2.0F);
        jCheckBox41.setContentAreaFilled(false);
        jCheckBox41.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox41.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox41.setName("A"); // NOI18N
        jCheckBox41.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau10.add(jCheckBox41, new org.netbeans.lib.awtextra.AbsoluteConstraints(45, 15, 20, 20));

        jLabel57.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel57.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel57.setText("1");
        jpnCau10.add(jLabel57, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 4, 15, 10));

        jCheckBox42.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox42.setAlignmentY(2.0F);
        jCheckBox42.setContentAreaFilled(false);
        jCheckBox42.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox42.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox42.setName("B"); // NOI18N
        jCheckBox42.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau10.add(jCheckBox42, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 15, 20, 20));

        jLabel58.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel58.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel58.setText("2");
        jpnCau10.add(jLabel58, new org.netbeans.lib.awtextra.AbsoluteConstraints(72, 2, 15, 12));

        jCheckBox43.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox43.setAlignmentY(2.0F);
        jCheckBox43.setContentAreaFilled(false);
        jCheckBox43.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox43.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox43.setName("C"); // NOI18N
        jCheckBox43.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau10.add(jCheckBox43, new org.netbeans.lib.awtextra.AbsoluteConstraints(95, 15, 20, 20));

        jLabel59.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel59.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel59.setText("3");
        jpnCau10.add(jLabel59, new org.netbeans.lib.awtextra.AbsoluteConstraints(97, 2, 15, 12));

        jCheckBox44.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox44.setAlignmentY(2.0F);
        jCheckBox44.setContentAreaFilled(false);
        jCheckBox44.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox44.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox44.setName("D"); // NOI18N
        jCheckBox44.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau10.add(jCheckBox44, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 15, 20, 20));

        jLabel60.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel60.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel60.setText("4");
        jpnCau10.add(jLabel60, new org.netbeans.lib.awtextra.AbsoluteConstraints(123, 2, 15, 12));

        jPanel2.add(jpnCau10, new org.netbeans.lib.awtextra.AbsoluteConstraints(42, 575, 148, 41));

        jpnCau11.setBackground(new java.awt.Color(255, 255, 255));
        jpnCau11.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jpnCau11.setName("12"); // NOI18N
        jpnCau11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jpnCau11MouseClicked(evt);
            }
        });
        jpnCau11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel50.setBackground(new java.awt.Color(204, 204, 204));
        jPanel50.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel106.setBackground(new java.awt.Color(204, 204, 204));
        jLabel106.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel106.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel106.setText("11");
        jLabel106.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel106.setPreferredSize(new java.awt.Dimension(40, 40));

        javax.swing.GroupLayout jPanel50Layout = new javax.swing.GroupLayout(jPanel50);
        jPanel50.setLayout(jPanel50Layout);
        jPanel50Layout.setHorizontalGroup(
            jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel50Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel106, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel50Layout.setVerticalGroup(
            jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel106, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
        );

        jpnCau11.add(jPanel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 40, 40));

        jCheckBox81.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox81.setAlignmentY(2.0F);
        jCheckBox81.setContentAreaFilled(false);
        jCheckBox81.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox81.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox81.setName("A"); // NOI18N
        jCheckBox81.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau11.add(jCheckBox81, new org.netbeans.lib.awtextra.AbsoluteConstraints(45, 15, 20, 20));

        jLabel107.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel107.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel107.setText("1");
        jpnCau11.add(jLabel107, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 4, 15, 10));

        jCheckBox82.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox82.setAlignmentY(2.0F);
        jCheckBox82.setContentAreaFilled(false);
        jCheckBox82.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox82.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox82.setName("B"); // NOI18N
        jCheckBox82.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau11.add(jCheckBox82, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 15, 20, 20));

        jLabel108.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel108.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel108.setText("2");
        jpnCau11.add(jLabel108, new org.netbeans.lib.awtextra.AbsoluteConstraints(72, 2, 15, 12));

        jCheckBox83.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox83.setAlignmentY(2.0F);
        jCheckBox83.setContentAreaFilled(false);
        jCheckBox83.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox83.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox83.setName("C"); // NOI18N
        jCheckBox83.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau11.add(jCheckBox83, new org.netbeans.lib.awtextra.AbsoluteConstraints(95, 15, 20, 20));

        jLabel109.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel109.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel109.setText("3");
        jpnCau11.add(jLabel109, new org.netbeans.lib.awtextra.AbsoluteConstraints(97, 2, 15, 12));

        jCheckBox84.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox84.setAlignmentY(2.0F);
        jCheckBox84.setContentAreaFilled(false);
        jCheckBox84.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox84.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox84.setName("D"); // NOI18N
        jCheckBox84.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau11.add(jCheckBox84, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 15, 20, 20));

        jLabel110.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel110.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel110.setText("4");
        jpnCau11.add(jLabel110, new org.netbeans.lib.awtextra.AbsoluteConstraints(123, 2, 15, 12));

        jPanel2.add(jpnCau11, new org.netbeans.lib.awtextra.AbsoluteConstraints(202, 44, 148, 41));

        jpnCau12.setBackground(new java.awt.Color(255, 255, 255));
        jpnCau12.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jpnCau12.setName("13"); // NOI18N
        jpnCau12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jpnCau12MouseClicked(evt);
            }
        });
        jpnCau12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel32.setBackground(new java.awt.Color(204, 204, 204));
        jPanel32.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel61.setBackground(new java.awt.Color(204, 204, 204));
        jLabel61.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel61.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel61.setText("12");
        jLabel61.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel61.setPreferredSize(new java.awt.Dimension(40, 40));

        javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel32Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel61, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel61, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
        );

        jpnCau12.add(jPanel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 40, 40));

        jCheckBox45.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox45.setAlignmentY(2.0F);
        jCheckBox45.setContentAreaFilled(false);
        jCheckBox45.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox45.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox45.setName("A"); // NOI18N
        jCheckBox45.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau12.add(jCheckBox45, new org.netbeans.lib.awtextra.AbsoluteConstraints(45, 15, 20, 20));

        jLabel62.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel62.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel62.setText("1");
        jpnCau12.add(jLabel62, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 4, 15, 10));

        jCheckBox46.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox46.setAlignmentY(2.0F);
        jCheckBox46.setContentAreaFilled(false);
        jCheckBox46.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox46.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox46.setName("B"); // NOI18N
        jCheckBox46.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau12.add(jCheckBox46, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 15, 20, 20));

        jLabel63.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel63.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel63.setText("2");
        jpnCau12.add(jLabel63, new org.netbeans.lib.awtextra.AbsoluteConstraints(72, 2, 15, 12));

        jCheckBox47.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox47.setAlignmentY(2.0F);
        jCheckBox47.setContentAreaFilled(false);
        jCheckBox47.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox47.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox47.setName("C"); // NOI18N
        jCheckBox47.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau12.add(jCheckBox47, new org.netbeans.lib.awtextra.AbsoluteConstraints(95, 15, 20, 20));

        jLabel64.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel64.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel64.setText("3");
        jpnCau12.add(jLabel64, new org.netbeans.lib.awtextra.AbsoluteConstraints(97, 2, 15, 12));

        jCheckBox48.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox48.setAlignmentY(2.0F);
        jCheckBox48.setContentAreaFilled(false);
        jCheckBox48.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox48.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox48.setName("D"); // NOI18N
        jCheckBox48.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau12.add(jCheckBox48, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 15, 20, 20));

        jLabel65.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel65.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel65.setText("4");
        jpnCau12.add(jLabel65, new org.netbeans.lib.awtextra.AbsoluteConstraints(123, 2, 15, 12));

        jPanel2.add(jpnCau12, new org.netbeans.lib.awtextra.AbsoluteConstraints(202, 103, 148, 41));

        jpnCau13.setBackground(new java.awt.Color(255, 255, 255));
        jpnCau13.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jpnCau13.setName("14"); // NOI18N
        jpnCau13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jpnCau13MouseClicked(evt);
            }
        });
        jpnCau13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel34.setBackground(new java.awt.Color(204, 204, 204));
        jPanel34.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel66.setBackground(new java.awt.Color(204, 204, 204));
        jLabel66.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel66.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel66.setText("13");
        jLabel66.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel66.setPreferredSize(new java.awt.Dimension(40, 40));

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel34Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel66, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel66, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
        );

        jpnCau13.add(jPanel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 40, 40));

        jCheckBox49.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox49.setAlignmentY(2.0F);
        jCheckBox49.setContentAreaFilled(false);
        jCheckBox49.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox49.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox49.setName("A"); // NOI18N
        jCheckBox49.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau13.add(jCheckBox49, new org.netbeans.lib.awtextra.AbsoluteConstraints(45, 15, 20, 20));

        jLabel67.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel67.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel67.setText("1");
        jpnCau13.add(jLabel67, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 4, 15, 10));

        jCheckBox50.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox50.setAlignmentY(2.0F);
        jCheckBox50.setContentAreaFilled(false);
        jCheckBox50.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox50.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox50.setName("B"); // NOI18N
        jCheckBox50.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau13.add(jCheckBox50, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 15, 20, 20));

        jLabel68.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel68.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel68.setText("2");
        jpnCau13.add(jLabel68, new org.netbeans.lib.awtextra.AbsoluteConstraints(72, 2, 15, 12));

        jCheckBox51.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox51.setAlignmentY(2.0F);
        jCheckBox51.setContentAreaFilled(false);
        jCheckBox51.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox51.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox51.setName("C"); // NOI18N
        jCheckBox51.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau13.add(jCheckBox51, new org.netbeans.lib.awtextra.AbsoluteConstraints(95, 15, 20, 20));

        jLabel69.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel69.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel69.setText("3");
        jpnCau13.add(jLabel69, new org.netbeans.lib.awtextra.AbsoluteConstraints(97, 2, 15, 12));

        jCheckBox52.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox52.setAlignmentY(2.0F);
        jCheckBox52.setContentAreaFilled(false);
        jCheckBox52.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox52.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox52.setName("D"); // NOI18N
        jCheckBox52.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau13.add(jCheckBox52, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 15, 20, 20));

        jLabel70.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel70.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel70.setText("4");
        jpnCau13.add(jLabel70, new org.netbeans.lib.awtextra.AbsoluteConstraints(123, 2, 15, 12));

        jPanel2.add(jpnCau13, new org.netbeans.lib.awtextra.AbsoluteConstraints(202, 162, 148, 41));

        jpnCau14.setBackground(new java.awt.Color(255, 255, 255));
        jpnCau14.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jpnCau14.setName("15"); // NOI18N
        jpnCau14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jpnCau14MouseClicked(evt);
            }
        });
        jpnCau14.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel36.setBackground(new java.awt.Color(204, 204, 204));
        jPanel36.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel71.setBackground(new java.awt.Color(204, 204, 204));
        jLabel71.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel71.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel71.setText("14");
        jLabel71.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel71.setPreferredSize(new java.awt.Dimension(40, 40));

        javax.swing.GroupLayout jPanel36Layout = new javax.swing.GroupLayout(jPanel36);
        jPanel36.setLayout(jPanel36Layout);
        jPanel36Layout.setHorizontalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel36Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel71, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel36Layout.setVerticalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel71, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
        );

        jpnCau14.add(jPanel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 40, 40));

        jCheckBox53.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox53.setAlignmentY(2.0F);
        jCheckBox53.setContentAreaFilled(false);
        jCheckBox53.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox53.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox53.setName("A"); // NOI18N
        jCheckBox53.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau14.add(jCheckBox53, new org.netbeans.lib.awtextra.AbsoluteConstraints(45, 15, 20, 20));

        jLabel72.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel72.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel72.setText("1");
        jpnCau14.add(jLabel72, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 4, 15, 10));

        jCheckBox54.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox54.setAlignmentY(2.0F);
        jCheckBox54.setContentAreaFilled(false);
        jCheckBox54.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox54.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox54.setName("B"); // NOI18N
        jCheckBox54.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau14.add(jCheckBox54, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 15, 20, 20));

        jLabel73.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel73.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel73.setText("2");
        jpnCau14.add(jLabel73, new org.netbeans.lib.awtextra.AbsoluteConstraints(72, 2, 15, 12));

        jCheckBox55.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox55.setAlignmentY(2.0F);
        jCheckBox55.setContentAreaFilled(false);
        jCheckBox55.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox55.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox55.setName("C"); // NOI18N
        jCheckBox55.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau14.add(jCheckBox55, new org.netbeans.lib.awtextra.AbsoluteConstraints(95, 15, 20, 20));

        jLabel74.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel74.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel74.setText("3");
        jpnCau14.add(jLabel74, new org.netbeans.lib.awtextra.AbsoluteConstraints(97, 2, 15, 12));

        jCheckBox56.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox56.setAlignmentY(2.0F);
        jCheckBox56.setContentAreaFilled(false);
        jCheckBox56.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox56.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox56.setName("D"); // NOI18N
        jCheckBox56.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau14.add(jCheckBox56, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 15, 20, 20));

        jLabel75.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel75.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel75.setText("4");
        jpnCau14.add(jLabel75, new org.netbeans.lib.awtextra.AbsoluteConstraints(123, 2, 15, 12));

        jPanel2.add(jpnCau14, new org.netbeans.lib.awtextra.AbsoluteConstraints(202, 221, 148, 41));

        jpnCau15.setBackground(new java.awt.Color(255, 255, 255));
        jpnCau15.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jpnCau15.setName("16"); // NOI18N
        jpnCau15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jpnCau15MouseClicked(evt);
            }
        });
        jpnCau15.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel38.setBackground(new java.awt.Color(204, 204, 204));
        jPanel38.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel76.setBackground(new java.awt.Color(204, 204, 204));
        jLabel76.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel76.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel76.setText("15");
        jLabel76.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel76.setPreferredSize(new java.awt.Dimension(40, 40));

        javax.swing.GroupLayout jPanel38Layout = new javax.swing.GroupLayout(jPanel38);
        jPanel38.setLayout(jPanel38Layout);
        jPanel38Layout.setHorizontalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel38Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel76, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel38Layout.setVerticalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel76, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
        );

        jpnCau15.add(jPanel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 40, 40));

        jCheckBox57.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox57.setAlignmentY(2.0F);
        jCheckBox57.setContentAreaFilled(false);
        jCheckBox57.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox57.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox57.setName("A"); // NOI18N
        jCheckBox57.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau15.add(jCheckBox57, new org.netbeans.lib.awtextra.AbsoluteConstraints(45, 15, 20, 20));

        jLabel77.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel77.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel77.setText("1");
        jpnCau15.add(jLabel77, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 4, 15, 10));

        jCheckBox58.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox58.setAlignmentY(2.0F);
        jCheckBox58.setContentAreaFilled(false);
        jCheckBox58.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox58.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox58.setName("B"); // NOI18N
        jCheckBox58.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau15.add(jCheckBox58, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 15, 20, 20));

        jLabel78.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel78.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel78.setText("2");
        jpnCau15.add(jLabel78, new org.netbeans.lib.awtextra.AbsoluteConstraints(72, 2, 15, 12));

        jCheckBox59.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox59.setAlignmentY(2.0F);
        jCheckBox59.setContentAreaFilled(false);
        jCheckBox59.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox59.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox59.setName("C"); // NOI18N
        jCheckBox59.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau15.add(jCheckBox59, new org.netbeans.lib.awtextra.AbsoluteConstraints(95, 15, 20, 20));

        jLabel79.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel79.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel79.setText("3");
        jpnCau15.add(jLabel79, new org.netbeans.lib.awtextra.AbsoluteConstraints(97, 2, 15, 12));

        jCheckBox60.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox60.setAlignmentY(2.0F);
        jCheckBox60.setContentAreaFilled(false);
        jCheckBox60.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox60.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox60.setName("D"); // NOI18N
        jCheckBox60.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau15.add(jCheckBox60, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 15, 20, 20));

        jLabel80.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel80.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel80.setText("4");
        jpnCau15.add(jLabel80, new org.netbeans.lib.awtextra.AbsoluteConstraints(123, 2, 15, 12));

        jPanel2.add(jpnCau15, new org.netbeans.lib.awtextra.AbsoluteConstraints(202, 280, 148, 41));

        jpnCau16.setBackground(new java.awt.Color(255, 255, 255));
        jpnCau16.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jpnCau16.setName("17"); // NOI18N
        jpnCau16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jpnCau16MouseClicked(evt);
            }
        });
        jpnCau16.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel40.setBackground(new java.awt.Color(204, 204, 204));
        jPanel40.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel81.setBackground(new java.awt.Color(204, 204, 204));
        jLabel81.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel81.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel81.setText("16");
        jLabel81.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel81.setPreferredSize(new java.awt.Dimension(40, 40));

        javax.swing.GroupLayout jPanel40Layout = new javax.swing.GroupLayout(jPanel40);
        jPanel40.setLayout(jPanel40Layout);
        jPanel40Layout.setHorizontalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel40Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel81, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel40Layout.setVerticalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel81, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
        );

        jpnCau16.add(jPanel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 40, 40));

        jCheckBox61.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox61.setAlignmentY(2.0F);
        jCheckBox61.setContentAreaFilled(false);
        jCheckBox61.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox61.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox61.setName("A"); // NOI18N
        jCheckBox61.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau16.add(jCheckBox61, new org.netbeans.lib.awtextra.AbsoluteConstraints(45, 15, 20, 20));

        jLabel82.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel82.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel82.setText("1");
        jpnCau16.add(jLabel82, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 4, 15, 10));

        jCheckBox62.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox62.setAlignmentY(2.0F);
        jCheckBox62.setContentAreaFilled(false);
        jCheckBox62.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox62.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox62.setName("B"); // NOI18N
        jCheckBox62.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau16.add(jCheckBox62, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 15, 20, 20));

        jLabel83.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel83.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel83.setText("2");
        jpnCau16.add(jLabel83, new org.netbeans.lib.awtextra.AbsoluteConstraints(72, 2, 15, 12));

        jCheckBox63.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox63.setAlignmentY(2.0F);
        jCheckBox63.setContentAreaFilled(false);
        jCheckBox63.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox63.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox63.setName("C"); // NOI18N
        jCheckBox63.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau16.add(jCheckBox63, new org.netbeans.lib.awtextra.AbsoluteConstraints(95, 15, 20, 20));

        jLabel84.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel84.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel84.setText("3");
        jpnCau16.add(jLabel84, new org.netbeans.lib.awtextra.AbsoluteConstraints(97, 2, 15, 12));

        jCheckBox64.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox64.setAlignmentY(2.0F);
        jCheckBox64.setContentAreaFilled(false);
        jCheckBox64.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox64.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox64.setName("D"); // NOI18N
        jCheckBox64.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau16.add(jCheckBox64, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 15, 20, 20));

        jLabel85.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel85.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel85.setText("4");
        jpnCau16.add(jLabel85, new org.netbeans.lib.awtextra.AbsoluteConstraints(123, 2, 15, 12));

        jPanel2.add(jpnCau16, new org.netbeans.lib.awtextra.AbsoluteConstraints(202, 339, 148, 41));

        jpnCau17.setBackground(new java.awt.Color(255, 255, 255));
        jpnCau17.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jpnCau17.setName("18"); // NOI18N
        jpnCau17.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jpnCau17MouseClicked(evt);
            }
        });
        jpnCau17.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel42.setBackground(new java.awt.Color(204, 204, 204));
        jPanel42.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel86.setBackground(new java.awt.Color(204, 204, 204));
        jLabel86.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel86.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel86.setText("17");
        jLabel86.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel86.setPreferredSize(new java.awt.Dimension(40, 40));

        javax.swing.GroupLayout jPanel42Layout = new javax.swing.GroupLayout(jPanel42);
        jPanel42.setLayout(jPanel42Layout);
        jPanel42Layout.setHorizontalGroup(
            jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel42Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel86, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel42Layout.setVerticalGroup(
            jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel86, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
        );

        jpnCau17.add(jPanel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 40, 40));

        jCheckBox65.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox65.setAlignmentY(2.0F);
        jCheckBox65.setContentAreaFilled(false);
        jCheckBox65.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox65.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox65.setName("A"); // NOI18N
        jCheckBox65.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau17.add(jCheckBox65, new org.netbeans.lib.awtextra.AbsoluteConstraints(45, 15, 20, 20));

        jLabel87.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel87.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel87.setText("1");
        jpnCau17.add(jLabel87, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 4, 15, 10));

        jCheckBox66.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox66.setAlignmentY(2.0F);
        jCheckBox66.setContentAreaFilled(false);
        jCheckBox66.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox66.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox66.setName("B"); // NOI18N
        jCheckBox66.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau17.add(jCheckBox66, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 15, 20, 20));

        jLabel88.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel88.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel88.setText("2");
        jpnCau17.add(jLabel88, new org.netbeans.lib.awtextra.AbsoluteConstraints(72, 2, 15, 12));

        jCheckBox67.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox67.setAlignmentY(2.0F);
        jCheckBox67.setContentAreaFilled(false);
        jCheckBox67.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox67.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox67.setName("C"); // NOI18N
        jCheckBox67.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau17.add(jCheckBox67, new org.netbeans.lib.awtextra.AbsoluteConstraints(95, 15, 20, 20));

        jLabel89.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel89.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel89.setText("3");
        jpnCau17.add(jLabel89, new org.netbeans.lib.awtextra.AbsoluteConstraints(97, 2, 15, 12));

        jCheckBox68.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox68.setAlignmentY(2.0F);
        jCheckBox68.setContentAreaFilled(false);
        jCheckBox68.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox68.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox68.setName("D"); // NOI18N
        jCheckBox68.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau17.add(jCheckBox68, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 15, 20, 20));

        jLabel90.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel90.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel90.setText("4");
        jpnCau17.add(jLabel90, new org.netbeans.lib.awtextra.AbsoluteConstraints(123, 2, 15, 12));

        jPanel2.add(jpnCau17, new org.netbeans.lib.awtextra.AbsoluteConstraints(202, 398, 148, 41));

        jpnCau18.setBackground(new java.awt.Color(255, 255, 255));
        jpnCau18.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jpnCau18.setName("19"); // NOI18N
        jpnCau18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jpnCau18MouseClicked(evt);
            }
        });
        jpnCau18.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel44.setBackground(new java.awt.Color(204, 204, 204));
        jPanel44.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel91.setBackground(new java.awt.Color(204, 204, 204));
        jLabel91.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel91.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel91.setText("18");
        jLabel91.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel91.setPreferredSize(new java.awt.Dimension(40, 40));

        javax.swing.GroupLayout jPanel44Layout = new javax.swing.GroupLayout(jPanel44);
        jPanel44.setLayout(jPanel44Layout);
        jPanel44Layout.setHorizontalGroup(
            jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel44Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel91, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel44Layout.setVerticalGroup(
            jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel91, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
        );

        jpnCau18.add(jPanel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 40, 40));

        jCheckBox69.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox69.setAlignmentY(2.0F);
        jCheckBox69.setContentAreaFilled(false);
        jCheckBox69.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox69.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox69.setName("A"); // NOI18N
        jCheckBox69.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau18.add(jCheckBox69, new org.netbeans.lib.awtextra.AbsoluteConstraints(45, 15, 20, 20));

        jLabel92.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel92.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel92.setText("1");
        jpnCau18.add(jLabel92, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 4, 15, 10));

        jCheckBox70.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox70.setAlignmentY(2.0F);
        jCheckBox70.setContentAreaFilled(false);
        jCheckBox70.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox70.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox70.setName("B"); // NOI18N
        jCheckBox70.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau18.add(jCheckBox70, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 15, 20, 20));

        jLabel93.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel93.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel93.setText("2");
        jpnCau18.add(jLabel93, new org.netbeans.lib.awtextra.AbsoluteConstraints(72, 2, 15, 12));

        jCheckBox71.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox71.setAlignmentY(2.0F);
        jCheckBox71.setContentAreaFilled(false);
        jCheckBox71.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox71.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox71.setName("C"); // NOI18N
        jCheckBox71.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau18.add(jCheckBox71, new org.netbeans.lib.awtextra.AbsoluteConstraints(95, 15, 20, 20));

        jLabel94.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel94.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel94.setText("3");
        jpnCau18.add(jLabel94, new org.netbeans.lib.awtextra.AbsoluteConstraints(97, 2, 15, 12));

        jCheckBox72.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox72.setAlignmentY(2.0F);
        jCheckBox72.setContentAreaFilled(false);
        jCheckBox72.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox72.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox72.setName("D"); // NOI18N
        jCheckBox72.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau18.add(jCheckBox72, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 15, 20, 20));

        jLabel95.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel95.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel95.setText("4");
        jpnCau18.add(jLabel95, new org.netbeans.lib.awtextra.AbsoluteConstraints(123, 2, 15, 12));

        jPanel2.add(jpnCau18, new org.netbeans.lib.awtextra.AbsoluteConstraints(202, 457, 148, 41));

        jpnCau19.setBackground(new java.awt.Color(255, 255, 255));
        jpnCau19.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jpnCau19.setName("20"); // NOI18N
        jpnCau19.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jpnCau19MouseClicked(evt);
            }
        });
        jpnCau19.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel46.setBackground(new java.awt.Color(204, 204, 204));
        jPanel46.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel96.setBackground(new java.awt.Color(204, 204, 204));
        jLabel96.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel96.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel96.setText("19");
        jLabel96.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel96.setPreferredSize(new java.awt.Dimension(40, 40));

        javax.swing.GroupLayout jPanel46Layout = new javax.swing.GroupLayout(jPanel46);
        jPanel46.setLayout(jPanel46Layout);
        jPanel46Layout.setHorizontalGroup(
            jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel46Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel96, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel46Layout.setVerticalGroup(
            jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel96, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
        );

        jpnCau19.add(jPanel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 40, 40));

        jCheckBox73.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox73.setAlignmentY(2.0F);
        jCheckBox73.setContentAreaFilled(false);
        jCheckBox73.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox73.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox73.setName("A"); // NOI18N
        jCheckBox73.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau19.add(jCheckBox73, new org.netbeans.lib.awtextra.AbsoluteConstraints(45, 15, 20, 20));

        jLabel97.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel97.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel97.setText("1");
        jpnCau19.add(jLabel97, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 4, 15, 10));

        jCheckBox74.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox74.setAlignmentY(2.0F);
        jCheckBox74.setContentAreaFilled(false);
        jCheckBox74.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox74.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox74.setName("B"); // NOI18N
        jCheckBox74.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau19.add(jCheckBox74, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 15, 20, 20));

        jLabel98.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel98.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel98.setText("2");
        jpnCau19.add(jLabel98, new org.netbeans.lib.awtextra.AbsoluteConstraints(72, 2, 15, 12));

        jCheckBox75.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox75.setAlignmentY(2.0F);
        jCheckBox75.setContentAreaFilled(false);
        jCheckBox75.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox75.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox75.setName("C"); // NOI18N
        jCheckBox75.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau19.add(jCheckBox75, new org.netbeans.lib.awtextra.AbsoluteConstraints(95, 15, 20, 20));

        jLabel99.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel99.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel99.setText("3");
        jpnCau19.add(jLabel99, new org.netbeans.lib.awtextra.AbsoluteConstraints(97, 2, 15, 12));

        jCheckBox76.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox76.setAlignmentY(2.0F);
        jCheckBox76.setContentAreaFilled(false);
        jCheckBox76.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox76.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox76.setName("D"); // NOI18N
        jCheckBox76.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau19.add(jCheckBox76, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 15, 20, 20));

        jLabel100.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel100.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel100.setText("4");
        jpnCau19.add(jLabel100, new org.netbeans.lib.awtextra.AbsoluteConstraints(123, 2, 15, 12));

        jPanel2.add(jpnCau19, new org.netbeans.lib.awtextra.AbsoluteConstraints(202, 516, 148, 41));

        jpnCau20.setBackground(new java.awt.Color(255, 255, 255));
        jpnCau20.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jpnCau20.setName("21"); // NOI18N
        jpnCau20.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jpnCau20MouseClicked(evt);
            }
        });
        jpnCau20.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel48.setBackground(new java.awt.Color(204, 204, 204));
        jPanel48.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel101.setBackground(new java.awt.Color(204, 204, 204));
        jLabel101.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel101.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel101.setText("20");
        jLabel101.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel101.setPreferredSize(new java.awt.Dimension(40, 40));

        javax.swing.GroupLayout jPanel48Layout = new javax.swing.GroupLayout(jPanel48);
        jPanel48.setLayout(jPanel48Layout);
        jPanel48Layout.setHorizontalGroup(
            jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel48Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel101, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel48Layout.setVerticalGroup(
            jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel101, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
        );

        jpnCau20.add(jPanel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 40, 40));

        jCheckBox77.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox77.setAlignmentY(2.0F);
        jCheckBox77.setContentAreaFilled(false);
        jCheckBox77.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox77.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox77.setName("A"); // NOI18N
        jCheckBox77.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau20.add(jCheckBox77, new org.netbeans.lib.awtextra.AbsoluteConstraints(45, 15, 20, 20));

        jLabel102.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel102.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel102.setText("1");
        jpnCau20.add(jLabel102, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 4, 15, 10));

        jCheckBox78.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox78.setAlignmentY(2.0F);
        jCheckBox78.setContentAreaFilled(false);
        jCheckBox78.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox78.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox78.setName("B"); // NOI18N
        jCheckBox78.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau20.add(jCheckBox78, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 15, 20, 20));

        jLabel103.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel103.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel103.setText("2");
        jpnCau20.add(jLabel103, new org.netbeans.lib.awtextra.AbsoluteConstraints(72, 2, 15, 12));

        jCheckBox79.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox79.setAlignmentY(2.0F);
        jCheckBox79.setContentAreaFilled(false);
        jCheckBox79.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox79.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox79.setName("C"); // NOI18N
        jCheckBox79.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau20.add(jCheckBox79, new org.netbeans.lib.awtextra.AbsoluteConstraints(95, 15, 20, 20));

        jLabel104.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel104.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel104.setText("3");
        jpnCau20.add(jLabel104, new org.netbeans.lib.awtextra.AbsoluteConstraints(97, 2, 15, 12));

        jCheckBox80.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCheckBox80.setAlignmentY(2.0F);
        jCheckBox80.setContentAreaFilled(false);
        jCheckBox80.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jCheckBox80.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox80.setName("D"); // NOI18N
        jCheckBox80.setPreferredSize(new java.awt.Dimension(30, 25));
        jpnCau20.add(jCheckBox80, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 15, 20, 20));

        jLabel105.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel105.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel105.setText("4");
        jpnCau20.add(jLabel105, new org.netbeans.lib.awtextra.AbsoluteConstraints(123, 2, 15, 12));

        jPanel2.add(jpnCau20, new org.netbeans.lib.awtextra.AbsoluteConstraints(202, 575, 148, 41));

        btnKQ.setBackground(new java.awt.Color(204, 204, 204));
        btnKQ.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnKQ.setForeground(new java.awt.Color(255, 0, 0));
        btnKQ.setText("KẾT THÚC");
        btnKQ.setBorderPainted(false);
        btnKQ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKQActionPerformed(evt);
            }
        });
        jPanel2.add(btnKQ, new org.netbeans.lib.awtextra.AbsoluteConstraints(44, 670, 307, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 0, 390, 800));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(158, 18, 18));
        jLabel2.setText("Mã Đề:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 10, -1, 20));

        jTextField1.setEditable(false);
        jTextField1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField1.setText("10");
        jTextField1.setDisabledTextColor(new java.awt.Color(255, 255, 255));
        jPanel1.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 10, 60, -1));

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(153, 153, 153), null));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel3.setEnabled(false);
        jPanel3.setRequestFocusEnabled(false);
        jPanel3.setVerifyInputWhenFocusTarget(false);
        jPanel3.setLayout(null);

        jspCauHoi.setBorder(null);
        jspCauHoi.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        jtaCauHoi.setEditable(false);
        jtaCauHoi.setBorder(null);
        jtaCauHoi.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jspCauHoi.setViewportView(jtaCauHoi);

        jPanel3.add(jspCauHoi);
        jspCauHoi.setBounds(20, 20, 560, 20);

        jspCauTL1.setBorder(null);
        jspCauTL1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jspCauTL1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jspCauTL1.setEnabled(false);

        jtaCauTL1.setEditable(false);
        jtaCauTL1.setBorder(null);
        jtaCauTL1.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        jspCauTL1.setViewportView(jtaCauTL1);

        jPanel3.add(jspCauTL1);
        jspCauTL1.setBounds(302, 7, 3, 21);

        jspCauTL2.setBorder(null);
        jspCauTL2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jspCauTL2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        jtaCauTL2.setEditable(false);
        jtaCauTL2.setBorder(null);
        jtaCauTL2.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        jspCauTL2.setViewportView(jtaCauTL2);

        jPanel3.add(jspCauTL2);
        jspCauTL2.setBounds(307, 7, 3, 21);

        jspCauTL3.setBorder(null);
        jspCauTL3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jspCauTL3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        jtaCauTL3.setEditable(false);
        jtaCauTL3.setBorder(null);
        jtaCauTL3.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        jspCauTL3.setViewportView(jtaCauTL3);

        jPanel3.add(jspCauTL3);
        jspCauTL3.setBounds(312, 7, 3, 21);

        jspCauTL4.setBorder(null);
        jspCauTL4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jspCauTL4.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        jtaCauTL4.setEditable(false);
        jtaCauTL4.setBorder(null);
        jtaCauTL4.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        jspCauTL4.setViewportView(jtaCauTL4);

        jPanel3.add(jspCauTL4);
        jspCauTL4.setBounds(317, 7, 3, 21);

        jPanel6.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 2, 594, 560));

        jPanel12.setBackground(new java.awt.Color(153, 153, 255));
        jPanel12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(153, 153, 153), null, null));
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel15.setForeground(new java.awt.Color(255, 0, 0));
        jLabel15.setText("Chọn ý đúng nhất từ 1 -->4 ");
        jPanel11.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 20, 370, 40));

        jLabel16.setForeground(new java.awt.Color(255, 0, 0));
        jLabel16.setText("Chú ý: Không sử dụng tài liệu dưới bất kỳ hình thức nào");
        jPanel11.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 380, 40));

        jScrollPane1.setBorder(null);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane1.setOpaque(false);

        jTextArea1.setEditable(false);
        jTextArea1.setBackground(new java.awt.Color(153, 153, 255));
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setText("\t             Đại học Bách Khoa Hà Nội");
        jTextArea1.setWrapStyleWord(true);
        jTextArea1.setBorder(null);
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 142, Short.MAX_VALUE))
            .addComponent(jScrollPane1)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel6.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(3, 560, 594, 196));

        jPanel1.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 600, 760));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 990, 800));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnKQActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKQActionPerformed
        // TODO add your handling code here:
        int i = JOptionPane.showConfirmDialog(null, "Bạn muốn nộp bài ?", "Chọn", JOptionPane.YES_NO_OPTION);
        if (ktkt2 == 1) {
            if (i == 0) {
                kttime = false;
                ChonDA();
                hienthi();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Bạn phải chọn hết");
        }


    }//GEN-LAST:event_btnKQActionPerformed

    private void jpnCau1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpnCau1MouseClicked
        // TODO add your handling code here:
        ExitHover(kt);
        JPanel container = (JPanel) jPanel1.getComponent(1);
        JPanel name = (JPanel) container.getComponent(2);
        kt = Integer.parseInt(name.getName());
        hienThiCauHoi(list2.get(0));
        Hover(kt);
    }//GEN-LAST:event_jpnCau1MouseClicked

    private void jpnCau2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpnCau2MouseClicked
        // TODO add your handling code here:
        ExitHover(kt);
        JPanel container = (JPanel) jPanel1.getComponent(1);
        JPanel name = (JPanel) container.getComponent(3);
        kt = Integer.parseInt(name.getName());
        hienThiCauHoi(list2.get(1));
        Hover(kt);
    }//GEN-LAST:event_jpnCau2MouseClicked

    private void jpnCau3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpnCau3MouseClicked
        // TODO add your handling code here:
        ExitHover(kt);
        JPanel container = (JPanel) jPanel1.getComponent(1);
        JPanel name = (JPanel) container.getComponent(4);
        kt = Integer.parseInt(name.getName());
        hienThiCauHoi(list2.get(2));
        Hover(kt);
    }//GEN-LAST:event_jpnCau3MouseClicked

    private void jpnCau4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpnCau4MouseClicked
        // TODO add your handling code here:
        ExitHover(kt);
        JPanel container = (JPanel) jPanel1.getComponent(1);
        JPanel name = (JPanel) container.getComponent(5);
        kt = Integer.parseInt(name.getName());
        hienThiCauHoi(list2.get(3));
        Hover(kt);
    }//GEN-LAST:event_jpnCau4MouseClicked

    private void jpnCau5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpnCau5MouseClicked
        // TODO add your handling code here:
        ExitHover(kt);
        JPanel container = (JPanel) jPanel1.getComponent(1);
        JPanel name = (JPanel) container.getComponent(6);
        kt = Integer.parseInt(name.getName());
        hienThiCauHoi(list2.get(4));
        Hover(kt);
    }//GEN-LAST:event_jpnCau5MouseClicked

    private void jpnCau6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpnCau6MouseClicked
        // TODO add your handling code here:
        ExitHover(kt);
        JPanel container = (JPanel) jPanel1.getComponent(1);
        JPanel name = (JPanel) container.getComponent(7);
        kt = Integer.parseInt(name.getName());
        hienThiCauHoi(list2.get(5));
        Hover(kt);
    }//GEN-LAST:event_jpnCau6MouseClicked

    private void jpnCau7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpnCau7MouseClicked
        // TODO add your handling code here:
        ExitHover(kt);
        JPanel container = (JPanel) jPanel1.getComponent(1);
        JPanel name = (JPanel) container.getComponent(8);
        kt = Integer.parseInt(name.getName());
        hienThiCauHoi(list2.get(6));
        Hover(kt);
    }//GEN-LAST:event_jpnCau7MouseClicked

    private void jpnCau8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpnCau8MouseClicked
        // TODO add your handling code here:
        ExitHover(kt);
        JPanel container = (JPanel) jPanel1.getComponent(1);
        JPanel name = (JPanel) container.getComponent(9);
        kt = Integer.parseInt(name.getName());
        hienThiCauHoi(list2.get(7));
        Hover(kt);
    }//GEN-LAST:event_jpnCau8MouseClicked

    private void jpnCau9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpnCau9MouseClicked
        // TODO add your handling code here:
        ExitHover(kt);
        JPanel container = (JPanel) jPanel1.getComponent(1);
        JPanel name = (JPanel) container.getComponent(10);
        kt = Integer.parseInt(name.getName());
        hienThiCauHoi(list2.get(8));
        Hover(kt);
    }//GEN-LAST:event_jpnCau9MouseClicked

    private void jpnCau10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpnCau10MouseClicked
        // TODO add your handling code here:
        ExitHover(kt);
        JPanel container = (JPanel) jPanel1.getComponent(1);
        JPanel name = (JPanel) container.getComponent(11);
        kt = Integer.parseInt(name.getName());
        hienThiCauHoi(list2.get(9));
        Hover(kt);
    }//GEN-LAST:event_jpnCau10MouseClicked

    private void jpnCau11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpnCau11MouseClicked
        // TODO add your handling code here:
        ExitHover(kt);
        JPanel container = (JPanel) jPanel1.getComponent(1);
        JPanel name = (JPanel) container.getComponent(12);
        kt = Integer.parseInt(name.getName());
        hienThiCauHoi(list2.get(10));
        Hover(kt);
    }//GEN-LAST:event_jpnCau11MouseClicked

    private void jpnCau12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpnCau12MouseClicked
        // TODO add your handling code here:
        ExitHover(kt);
        JPanel container = (JPanel) jPanel1.getComponent(1);
        JPanel name = (JPanel) container.getComponent(13);
        kt = Integer.parseInt(name.getName());
        hienThiCauHoi(list2.get(11));
        Hover(kt);
    }//GEN-LAST:event_jpnCau12MouseClicked

    private void jpnCau13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpnCau13MouseClicked
        // TODO add your handling code here:
        ExitHover(kt);
        JPanel container = (JPanel) jPanel1.getComponent(1);
        JPanel name = (JPanel) container.getComponent(14);
        kt = Integer.parseInt(name.getName());
        hienThiCauHoi(list2.get(12));
        Hover(kt);
    }//GEN-LAST:event_jpnCau13MouseClicked

    private void jpnCau14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpnCau14MouseClicked
        // TODO add your handling code here:
        ExitHover(kt);
        JPanel container = (JPanel) jPanel1.getComponent(1);
        JPanel name = (JPanel) container.getComponent(15);
        kt = Integer.parseInt(name.getName());
        hienThiCauHoi(list2.get(13));
        Hover(kt);
    }//GEN-LAST:event_jpnCau14MouseClicked

    private void jpnCau15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpnCau15MouseClicked
        // TODO add your handling code here:
        ExitHover(kt);
        JPanel container = (JPanel) jPanel1.getComponent(1);
        JPanel name = (JPanel) container.getComponent(16);
        kt = Integer.parseInt(name.getName());
        hienThiCauHoi(list2.get(14));
        Hover(kt);
    }//GEN-LAST:event_jpnCau15MouseClicked

    private void jpnCau16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpnCau16MouseClicked
        // TODO add your handling code here:
        ExitHover(kt);
        JPanel container = (JPanel) jPanel1.getComponent(1);
        JPanel name = (JPanel) container.getComponent(17);
        kt = Integer.parseInt(name.getName());
        hienThiCauHoi(list2.get(15));
        Hover(kt);
    }//GEN-LAST:event_jpnCau16MouseClicked

    private void jpnCau17MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpnCau17MouseClicked
        // TODO add your handling code here:
        ExitHover(kt);
        JPanel container = (JPanel) jPanel1.getComponent(1);
        JPanel name = (JPanel) container.getComponent(18);
        kt = Integer.parseInt(name.getName());
        hienThiCauHoi(list2.get(16));
        Hover(kt);
    }//GEN-LAST:event_jpnCau17MouseClicked

    private void jpnCau18MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpnCau18MouseClicked
        // TODO add your handling code here:
        ExitHover(kt);
        JPanel container = (JPanel) jPanel1.getComponent(1);
        JPanel name = (JPanel) container.getComponent(19);
        kt = Integer.parseInt(name.getName());
        hienThiCauHoi(list2.get(17));
        Hover(kt);
    }//GEN-LAST:event_jpnCau18MouseClicked

    private void jpnCau19MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpnCau19MouseClicked
        // TODO add your handling code here:
        ExitHover(kt);
        JPanel container = (JPanel) jPanel1.getComponent(1);
        JPanel name = (JPanel) container.getComponent(20);
        kt = Integer.parseInt(name.getName());
        hienThiCauHoi(list2.get(18));
        Hover(kt);
    }//GEN-LAST:event_jpnCau19MouseClicked

    private void jpnCau20MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpnCau20MouseClicked
        // TODO add your handling code here:
        ExitHover(kt);
        JPanel container = (JPanel) jPanel1.getComponent(1);
        JPanel name = (JPanel) container.getComponent(21);
        kt = Integer.parseInt(name.getName());
        hienThiCauHoi(list2.get(19));
        Hover(kt);
    }//GEN-LAST:event_jpnCau20MouseClicked

    private void txtTimeAncestorMoved(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_txtTimeAncestorMoved
        // TODO add your handling code here:
        //thoiGian(txtTime);

    }//GEN-LAST:event_txtTimeAncestorMoved

    private void jpnCau1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpnCau1MouseReleased
        // TODO add your handling code here:
        ExitHover(kt);
        JPanel container = (JPanel) jPanel1.getComponent(1);
        JPanel name = (JPanel) container.getComponent(2);
        kt = Integer.parseInt(name.getName());
        hienThiCauHoi(list2.get(0));
        Hover(kt);
    }//GEN-LAST:event_jpnCau1MouseReleased
    
    
     
    /**
     * @param args the command line arguments
     */
    public static void startLayout(Socket socket) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Main(socket).setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnKQ;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox13;
    private javax.swing.JCheckBox jCheckBox14;
    private javax.swing.JCheckBox jCheckBox15;
    private javax.swing.JCheckBox jCheckBox16;
    private javax.swing.JCheckBox jCheckBox17;
    private javax.swing.JCheckBox jCheckBox18;
    private javax.swing.JCheckBox jCheckBox19;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox20;
    private javax.swing.JCheckBox jCheckBox21;
    private javax.swing.JCheckBox jCheckBox22;
    private javax.swing.JCheckBox jCheckBox23;
    private javax.swing.JCheckBox jCheckBox24;
    private javax.swing.JCheckBox jCheckBox25;
    private javax.swing.JCheckBox jCheckBox26;
    private javax.swing.JCheckBox jCheckBox27;
    private javax.swing.JCheckBox jCheckBox28;
    private javax.swing.JCheckBox jCheckBox29;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox30;
    private javax.swing.JCheckBox jCheckBox31;
    private javax.swing.JCheckBox jCheckBox32;
    private javax.swing.JCheckBox jCheckBox33;
    private javax.swing.JCheckBox jCheckBox34;
    private javax.swing.JCheckBox jCheckBox35;
    private javax.swing.JCheckBox jCheckBox36;
    private javax.swing.JCheckBox jCheckBox37;
    private javax.swing.JCheckBox jCheckBox38;
    private javax.swing.JCheckBox jCheckBox39;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox40;
    private javax.swing.JCheckBox jCheckBox41;
    private javax.swing.JCheckBox jCheckBox42;
    private javax.swing.JCheckBox jCheckBox43;
    private javax.swing.JCheckBox jCheckBox44;
    private javax.swing.JCheckBox jCheckBox45;
    private javax.swing.JCheckBox jCheckBox46;
    private javax.swing.JCheckBox jCheckBox47;
    private javax.swing.JCheckBox jCheckBox48;
    private javax.swing.JCheckBox jCheckBox49;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBox jCheckBox50;
    private javax.swing.JCheckBox jCheckBox51;
    private javax.swing.JCheckBox jCheckBox52;
    private javax.swing.JCheckBox jCheckBox53;
    private javax.swing.JCheckBox jCheckBox54;
    private javax.swing.JCheckBox jCheckBox55;
    private javax.swing.JCheckBox jCheckBox56;
    private javax.swing.JCheckBox jCheckBox57;
    private javax.swing.JCheckBox jCheckBox58;
    private javax.swing.JCheckBox jCheckBox59;
    private javax.swing.JCheckBox jCheckBox6;
    private javax.swing.JCheckBox jCheckBox60;
    private javax.swing.JCheckBox jCheckBox61;
    private javax.swing.JCheckBox jCheckBox62;
    private javax.swing.JCheckBox jCheckBox63;
    private javax.swing.JCheckBox jCheckBox64;
    private javax.swing.JCheckBox jCheckBox65;
    private javax.swing.JCheckBox jCheckBox66;
    private javax.swing.JCheckBox jCheckBox67;
    private javax.swing.JCheckBox jCheckBox68;
    private javax.swing.JCheckBox jCheckBox69;
    private javax.swing.JCheckBox jCheckBox7;
    private javax.swing.JCheckBox jCheckBox70;
    private javax.swing.JCheckBox jCheckBox71;
    private javax.swing.JCheckBox jCheckBox72;
    private javax.swing.JCheckBox jCheckBox73;
    private javax.swing.JCheckBox jCheckBox74;
    private javax.swing.JCheckBox jCheckBox75;
    private javax.swing.JCheckBox jCheckBox76;
    private javax.swing.JCheckBox jCheckBox77;
    private javax.swing.JCheckBox jCheckBox78;
    private javax.swing.JCheckBox jCheckBox79;
    private javax.swing.JCheckBox jCheckBox8;
    private javax.swing.JCheckBox jCheckBox80;
    private javax.swing.JCheckBox jCheckBox81;
    private javax.swing.JCheckBox jCheckBox82;
    private javax.swing.JCheckBox jCheckBox83;
    private javax.swing.JCheckBox jCheckBox84;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel103;
    private javax.swing.JLabel jLabel104;
    private javax.swing.JLabel jLabel105;
    private javax.swing.JLabel jLabel106;
    private javax.swing.JLabel jLabel107;
    private javax.swing.JLabel jLabel108;
    private javax.swing.JLabel jLabel109;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel110;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel44;
    private javax.swing.JPanel jPanel46;
    private javax.swing.JPanel jPanel48;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel50;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel jlbHover1_1;
    private javax.swing.JLabel jlbHover1_2;
    private javax.swing.JLabel jlbHover1_3;
    private javax.swing.JLabel jlbHover1_4;
    private javax.swing.JPanel jpnCau1;
    private javax.swing.JPanel jpnCau10;
    private javax.swing.JPanel jpnCau11;
    private javax.swing.JPanel jpnCau12;
    private javax.swing.JPanel jpnCau13;
    private javax.swing.JPanel jpnCau14;
    private javax.swing.JPanel jpnCau15;
    private javax.swing.JPanel jpnCau16;
    private javax.swing.JPanel jpnCau17;
    private javax.swing.JPanel jpnCau18;
    private javax.swing.JPanel jpnCau19;
    private javax.swing.JPanel jpnCau2;
    private javax.swing.JPanel jpnCau20;
    private javax.swing.JPanel jpnCau3;
    private javax.swing.JPanel jpnCau4;
    private javax.swing.JPanel jpnCau5;
    private javax.swing.JPanel jpnCau6;
    private javax.swing.JPanel jpnCau7;
    private javax.swing.JPanel jpnCau8;
    private javax.swing.JPanel jpnCau9;
    private javax.swing.JScrollPane jspCauHoi;
    private javax.swing.JScrollPane jspCauTL1;
    private javax.swing.JScrollPane jspCauTL2;
    private javax.swing.JScrollPane jspCauTL3;
    private javax.swing.JScrollPane jspCauTL4;
    private javax.swing.JTextPane jtaCauHoi;
    private javax.swing.JTextPane jtaCauTL1;
    private javax.swing.JTextPane jtaCauTL2;
    private javax.swing.JTextPane jtaCauTL3;
    private javax.swing.JTextPane jtaCauTL4;
    private javax.swing.JLabel txtTime;
    // End of variables declaration//GEN-END:variables
}
