package dvd.search.app;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import java.sql.*;

public class DVDSearchApp extends JFrame {
    
    JTextField txtTitle;
    JButton butSearch;
    JList list;
    JScrollPane scrollResults;
    ArrayList <String> dvdList;
    public Connection cn;
    public Statement st;
    ResultSet data = null;
    
    final String mainClass = DVDSearchApp.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    final String mainDir = mainClass.substring(0, mainClass.indexOf("build"));
    
    public DVDSearchApp() {
        DefaultListModel dlm = new DefaultListModel();
        list = new JList(dlm);
        dvdList = new ArrayList();
        txtTitle = new JTextField();
        txtTitle.setBounds(30, 30, 150, 25);
        butSearch = new JButton("Title Search");
        butSearch.setBounds(200, 30, 120, 25);
        scrollResults = new JScrollPane(list);
        scrollResults.setBounds(30, 85, 290, 150);
        
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/prac_soft?zeroDateTimeBehavior=convertToNull","root","ExamplePas");
            st=cn.createStatement();
            JOptionPane.showMessageDialog(null,"Connected");
        }        
        catch(HeadlessException | ClassNotFoundException | SQLException e)
        {
            JOptionPane.showMessageDialog(null,"Not Connected");
        }
        
        try
        {            
            String sql= "SELECT title FROM practical_software";
            data = st.executeQuery(sql);
            
            while (data.next()) {
            String info = data.getString("title");
            dvdList.add(info);
            }                       
            JOptionPane.showMessageDialog(null,"Got Data");
        }
        catch(HeadlessException | SQLException e)
        {
            JOptionPane.showMessageDialog(null,"Not Connected");
        }
        
        setTitle("DVD Search App");
        setLayout(null);
        
        butSearch.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    dlm.removeAllElements();                    
                    dvdList.forEach((curVal) -> {
                        String tempVal = curVal.toLowerCase();
                        if (tempVal.contains(txtTitle.getText().toLowerCase())) {
                            dlm.addElement(curVal);
                        }
                    });
                } catch (Exception e) {
                    txtTitle.setText("Something Went Wrong!");
                    System.out.println(e);
                }
            }
        });
        
        add(txtTitle);
        add(butSearch);
        add(scrollResults);
                
        setSize(360, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        DVDSearchApp dvd = new DVDSearchApp();
        dvd.setVisible(true);
    }
}
