/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.clienteleilao;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.nashorn.internal.codegen.CompilerConstants;

/**
 *
 * @author aluno
 */
public class TCPClient extends Thread {

    //String ip = "10.151.34.132";
    Socket sock;
    Produto produto;

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public TCPClient() {

    }

    public void Conectar(String ip, String porta) {

        try {
            Socket sock;
            InetAddress srvAddr = null;
            int port = Integer.parseInt(porta);

            /* Gets parameters and check */
            if (!ip.equals("localhost")) {
                try {
                    srvAddr = InetAddress.getByName(ip);
                } catch (UnknownHostException e) {
                    System.out.print("\nEndereco servidor: " + e.getMessage());
                    System.exit(1);
                }
                // port = Integer.parseInt("65530");
                if ((port < 1) && (port > 65535)) {
                    System.out.print("\nPorta invalida!!!\n\tFaixa: 1 - 65535");
                    System.exit(1);
                }
            } else {
                System.out.print("\nErro!!!\n\tUse: TCPClient <ip-servidor> <port-servidor>");
                System.exit(1);
            }

            System.out.print("\nConectando a " + srvAddr.toString() + " na porta " + port + "... ");
            sock = new Socket(srvAddr, port);
            System.out.print("[OK]");
            // this.sock = sock;
            this.sock = sock;
        } catch (IOException e) {
            System.out.print("\n\tConexao: " + e.getMessage());
        }

    }

    public static void enviarMensagem(String outMsg, Socket sock) throws IOException {

        DataOutputStream out = new DataOutputStream(sock.getOutputStream());

        //  Scanner input = new Scanner(System.in);
        // while (true) {
        System.out.print("\nMensagem: ");
        //outMsg = input.nextLine();

        /* Check message */
        if ("<close>".equals(outMsg)) {
            /* Connection close */
            sock.close();

            /* Close system */
            System.exit(0);
        }

        /* Send message to server */
        out.writeUTF(outMsg);

        /* Receive message from server */
    }

    public void run() {
        try {

            receberMensagem(sock);
        } catch (IOException ex) {
            Logger.getLogger(TCPClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void receberMensagem(Socket sock) throws IOException {
        DataInputStream in = new DataInputStream(sock.getInputStream());
        String data;
      
        while (true) {

            /* mensagem servidor */
            data = in.readUTF();
            this.produto.setNome(data);
            System.out.print("\n[Resposta] " + data);
            

        }

    }


}
