package Client;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

class TransferFileClient {

    Socket ClientSoc;
    DataInputStream din;
    DataOutputStream dout;
    BufferedReader br;

    TransferFileClient(Socket soc) {
        try {
            ClientSoc = soc;
            din = new DataInputStream(ClientSoc.getInputStream());
            dout = new DataOutputStream(ClientSoc.getOutputStream());
            br = new BufferedReader(new InputStreamReader(System.in));
        }
        catch (Exception ex) {
            System.out.println("Error: " + ex);
        }
    }

    void SendFile(String filename) throws Exception {

        String dirPath = System.getProperty("user.dir");
        File f = new File(dirPath + "/src/Storage/" + filename);

        if (!f.exists()) {
            System.out.println("File not Exists...");
            dout.writeUTF("File not found");
            return;
        }
        dout.writeUTF(filename);
        String msgFromServer = din.readUTF();
        if (msgFromServer.compareTo("File Already Exists") == 0) {

            final JPanel panel = new JPanel();
            JFrame frame = new JFrame("File Already Exists!");
            int option = JOptionPane.showConfirmDialog(frame, "File Already Exists! \nDo you want to overwrite it. Sure?");

            if (option == 0) {
                dout.writeUTF("Y");
            } else {
                dout.writeUTF("N");
                return;
            }
        }
        FileInputStream fin = new FileInputStream(f);
        int ch;
        do {
            ch = fin.read();
            dout.writeUTF(String.valueOf(ch));
        } while (ch != -1);

        fin.close();
        System.out.println(din.readUTF());
    }

    void ReceiveFile(String filename) throws Exception {

        System.out.println("receive file btn clicked...");

        System.out.println(filename);

        dout.writeUTF(filename);
        String msgFromServer = din.readUTF();

        JFrame frame=new JFrame();

        if (msgFromServer.compareTo("File Not Found")==0) {
            JOptionPane.showMessageDialog(frame,"File not found on Server.");
            return;
        }
        else if (msgFromServer.compareTo("READY")==0) {

            System.out.println("Receiving File ...");
            String dirPath = System.getProperty("user.dir");
            File f = new File(dirPath + "/src/Storage/" + filename);

            if (f.exists()) {

                final JPanel panel = new JPanel();
                JFrame frame1 = new JFrame("File Already Exists!");
                int Option = JOptionPane.showConfirmDialog(frame1, "File Already Exists! \nDo you want to overwrite it. Sure?");

                if (Option == 1) {
                    dout.flush();
                    return;
                }
            }
            FileOutputStream fout = new FileOutputStream(f);
            int ch;
            String temp;
            do {
                temp = din.readUTF();
                ch = Integer.parseInt(temp);
                if (ch != -1) {
                    fout.write(ch);
                }
            } while (ch != -1);
            fout.close();
            System.out.println(din.readUTF());
        }
        else {
        }
    }

}

