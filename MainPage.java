package dbexp;

import java.sql.*;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Vector;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

public class MainPage extends javax.swing.JFrame {

       String user,pass,selnode;
       String db[];
       int countdb = 0;
       int counttb[];

       /** Creates new form MainPage
     * @param user
     * @param pass */
    public MainPage(String user,String pass) {
        this.user = user;
        this.pass = pass;
        initComponents();
        showDatabases();
        tree.addTreeSelectionListener((TreeSelectionEvent e) -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                    tree.getLastSelectedPathComponent();
            
            /* if nothing is selected */
            if (node == null) return;
            TreeNode[] n;
            /* retrieve the node that was selected */
            //Object nodeInfo = node.getUserObject();
            n = node.getPath();
            /* React to the node selection. */
            printTable(n);
            
        });
    }
   
    public void printTable(TreeNode[] n)
    {
        int i = 0,j;
        for(TreeNode k : n)
        {
            i++;
        }
        
        if(i == 3)
        {
            secondpanel.removeAll();
            secondpanel.add(tablepanel);
            secondpanel.repaint();
            secondpanel.revalidate();
                for(j=0;j<countdb;j++)
                {
                    if(db[j].equals(n[1].toString()))
                    {
                        System.out.println(db[j] + " " + n[2].toString());
                        findTable(db[j],n[2].toString());
                    }
                }
        }    
            
    }
    
    
    public void findTable(String dbname,String tabname)
    {
        int columncount = 0,i,rowcount = 0,j;
        String types[],columns[],rows[];
        Connection conn;
        Statement stmt;
        ResultSet rs;
        //DefaultTableModel model = (DefaultTableModel) table.getModel();
        DefaultTableModel model = new DefaultTableModel(0,0);
        try 
        {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/" + dbname,user,pass);
            stmt = conn.createStatement();
            rs = stmt.executeQuery("show columns from " + tabname);
            
            while(rs.next())
            {
                columncount++;
            }
            columns = new String[columncount];
            rows = new String[columncount];
            types = new String[columncount];
            rs.first();
            i=0;
            do
            {
                columns[i] = rs.getString(1);
                model.addColumn(columns[i]);
                types[i] = rs.getString(2);
                i++;
            }while(rs.next());
            
            rs = stmt.executeQuery("select * from "+ tabname);
            
            while(rs.next())
            {
                rowcount++;
            }
            if(rowcount!=0)
            {
                rs.first();
                j=0;
                do
                {
                    for(i=0;i<columncount;i++)
                    {
                        if(types[i].contains("int"))
                        {
                             rows[i] = (String.valueOf(rs.getInt(i+1)));
                        }
                        else if(types[i].contains("float"))
                        {
                            rows[i] = (String.valueOf(rs.getFloat(i+1)));
                        }
                    else{
                            rows[i] = (rs.getString(i+1));
                        }
                    }
                    model.insertRow(j, rows);
                    j++;
                    
                    
                    //row.removeAllElements();
                    //row.clear();
                }while(rs.next());
                            
            }
            else 
            {
                System.out.println("Null Set!");
            }
            table.setModel(model);
            conn.close();
            stmt.close();
            rs.close();
        }
        catch(Exception e)
        {
               e.printStackTrace();
        }
    
    }
    
    private void showDatabases()
    {
        int i=0,j;
        Connection conn = null,con1 = null;
        Statement stmt,stmt1;
        ResultSet rs,rs1;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/",user,pass);
            stmt = conn.createStatement();
            rs1 = stmt.executeQuery("show databases;");
            while(rs1.next())
            {
                i++;
            }
            countdb = i;
            System.out.println("DB count = "+countdb);
            counttb = new int[countdb];
            db = new String[countdb];
            rs = stmt.executeQuery("show databases;");
            DefaultMutableTreeNode Databases = new DefaultMutableTreeNode("Databases");
            DefaultMutableTreeNode[] Database = new DefaultMutableTreeNode[i];
            i=0;
            tree.setModel(new javax.swing.tree.DefaultTreeModel(Databases));
            
            DefaultTreeModel model = (DefaultTreeModel) tree.getModel(); 
            
            model.reload();
            while(rs.next())
            {
                j=0;
                db[i] = rs.getString(1);
                System.out.println(db[i]+" "+i);
                Database[i] = new DefaultMutableTreeNode(rs.getString(1));
                con1 = DriverManager.getConnection("jdbc:mysql://localhost/"+rs.getString(1),user,pass);
                stmt1 = con1.createStatement();
                rs1 = stmt1.executeQuery("show tables;");
                while(rs1.next())
                { 
                    j++;
                }
                counttb[i] = j;
                rs1.first();
                if(j!=0)
                {
                    DefaultMutableTreeNode[] tables = new DefaultMutableTreeNode[j];
                    j=0;
                    do
                    {
                        tables[j] = new DefaultMutableTreeNode(rs1.getString(1));
                        Database[i].add(tables[j]);
                        j++;
                    }while(rs1.next());
                }
                    System.out.println();
                    Databases.add(Database[i]);
                    i++;
                    con1.close();
                
            }
                System.out.println(i);
                conn.close();
            }
    catch(Exception e)
            {
            System.out.println("Error occured!");
            e.printStackTrace();
            }
        finally{
            if(conn!=null)
                try{
            conn.close();}
            catch(Exception e)
            {e.printStackTrace();}
            if(con1!=null)
             try{
                conn.close();
             }
            catch(Exception e)
            {e.printStackTrace();}
        }
    }
    
    
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tree = new javax.swing.JTree();
        secondpanel = new javax.swing.JPanel();
        emptypanel = new javax.swing.JPanel();
        tablepanel = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Databases");

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        tree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jScrollPane1.setViewportView(tree);

        secondpanel.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout emptypanelLayout = new javax.swing.GroupLayout(emptypanel);
        emptypanel.setLayout(emptypanelLayout);
        emptypanelLayout.setHorizontalGroup(
            emptypanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 639, Short.MAX_VALUE)
        );
        emptypanelLayout.setVerticalGroup(
            emptypanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 477, Short.MAX_VALUE)
        );

        secondpanel.add(emptypanel, "card3");

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tablepanel.setViewportView(table);

        secondpanel.add(tablepanel, "card4");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(secondpanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(secondpanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(MainPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        /* Create and display the form */
        /*java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainPage(user,pass).setVisible(true);
            }
        });*/
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel emptypanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel secondpanel;
    private javax.swing.JTable table;
    private javax.swing.JScrollPane tablepanel;
    private javax.swing.JTree tree;
    // End of variables declaration//GEN-END:variables

}
