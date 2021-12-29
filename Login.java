
package ChuongTrinhTracNghiem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author TH
 */
public class Login extends javax.swing.JFrame {
    Socket socket ;
    // Cac phuong thuc
    
    public Login() throws IOException {
        initComponents();
        
        this.username.setText("");
        this.password.setText("");
        this.socket = new Socket("127.0.0.1",9999);
    }

   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labTitle = new javax.swing.JLabel();
        labUser = new javax.swing.JLabel();
        username = new javax.swing.JTextField();
        labPass = new javax.swing.JLabel();
        btnLogin = new javax.swing.JButton();
        btnQMK = new javax.swing.JButton();
        labHD = new javax.swing.JLabel();
        password = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Đăng nhập");
        setBounds(new java.awt.Rectangle(450, 230, 400, 247));
        setResizable(false);
        setSize(new java.awt.Dimension(400, 247));

        labTitle.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        labTitle.setForeground(new java.awt.Color(255, 0, 0));
        labTitle.setText("ĐĂNG NHẬP");

        labUser.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        labUser.setText("Username:");

        labPass.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        labPass.setText("Password:");

        btnLogin.setBackground(new java.awt.Color(0, 0, 204));
        btnLogin.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnLogin.setForeground(new java.awt.Color(255, 255, 255));
        btnLogin.setText("Đăng nhập");
        btnLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLoginMouseClicked(evt);
            }
        });
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });

        btnQMK.setBackground(new java.awt.Color(0, 0, 204));
        btnQMK.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnQMK.setForeground(new java.awt.Color(255, 255, 255));
        btnQMK.setText("Quên mật khẩu");
        btnQMK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQMKActionPerformed(evt);
            }
        });

        labHD.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        labHD.setForeground(new java.awt.Color(153, 153, 153));
        labHD.setText("Mặc định: Username: admin, Password: 1234");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labPass)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                                .addComponent(btnLogin)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnQMK))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(password))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labUser)
                        .addGap(28, 28, 28)
                        .addComponent(username)))
                .addGap(36, 36, 36))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(labTitle)
                        .addGap(138, 138, 138))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(labHD, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(labTitle)
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labUser))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(labPass))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, 30, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLogin)
                    .addComponent(btnQMK))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addComponent(labHD, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
//       try 
//       {
//           if (username.getText().equalsIgnoreCase("admin") && password.getText().equalsIgnoreCase("1234"))
//           {
//               JOptionPane.showMessageDialog(null, "Đăng nhập thành công");
//               this.setVisible(false);
//               
//               Main x = new Main();
//               x.setVisible(true);
//               
//           }
//           else
//           {
//               JOptionPane.showMessageDialog(null, "Đăng nhập không thành công");
//           }
//       }
//       catch (Exception e)
//       {
//           JOptionPane.showMessageDialog(null, "Đăng nhập không thành công");
//       }
    }//GEN-LAST:event_btnLoginActionPerformed

    private void btnQMKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQMKActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(null, "Liên hệ quản đốc để giải quyết");
    }//GEN-LAST:event_btnQMKActionPerformed

    private void btnLoginMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLoginMouseClicked
        // TODO add your handling code here:
        String username = this.username.getText();
        String password = this.password.getText();
        String accountMessage = createAccountMessage(username, password);
        try {
            sendAccountMessage("1", socket);
            sendAccountMessage(accountMessage, socket);
            InputStream istream = socket.getInputStream();
            BufferedReader receiveRead = new BufferedReader(new InputStreamReader(istream), 1024);
            getMessageFromServer(receiveRead, socket);
        } catch (IOException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_btnLoginMouseClicked
    
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
            if(receiveMessage.equals("Login successfully: Welcome to THITRACNGHIEM")){
                dispose();
                Menu menu = new Menu();
                menu.startLayout(socket);
                
            }
        }
     
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Login().setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLogin;
    private javax.swing.JButton btnQMK;
    private javax.swing.JLabel labHD;
    private javax.swing.JLabel labPass;
    private javax.swing.JLabel labTitle;
    private javax.swing.JLabel labUser;
    private javax.swing.JPasswordField password;
    private javax.swing.JTextField username;
    // End of variables declaration//GEN-END:variables
}
