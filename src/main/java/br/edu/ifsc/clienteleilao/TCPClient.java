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

/**
 *
 * @author aluno
 */
public class TCPClient extends Thread {

    //String ip = "10.151.34.132";
    Socket sock;
    Produto produto;
    String mensagemProduto;
    String mensagemProspota;

    public TCPClient() {

    }

    public void Conectar(String ip, String porta) {

        try {
            Socket sock;
            InetAddress srvAddr = null;
            int port = Integer.parseInt(porta);

            /* Gets parameters and check */
            if (true) {
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

            this.sock = sock;
        } catch (IOException e) {
            System.out.print("\n\tConexao: " + e.getMessage());
        }

    }

    public void enviarMensagem(String outMsg, Socket sock) throws IOException {

        DataOutputStream out = new DataOutputStream(sock.getOutputStream());

        //  Scanner input = new Scanner(System.in);
        // while (true) {
        System.out.print("\nMensagem [enviar msg]: ");
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

            receberMensagem(this.sock);

            System.out.println("Socket run: " + sock.toString());
        } catch (IOException ex) {
            Logger.getLogger(TCPClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void receberMensagem(Socket sock) throws IOException {
        DataInputStream in = new DataInputStream(sock.getInputStream());
        String data;
        Produto produto;
        int i = 0;
        System.out.println("Sock: " + sock.toString());
        while (true) {
            i = i++;

            System.out.println("i+ " + i);
            /* mensagem servidor */
            data = in.readUTF();
            if (data.equals("ACK")) {

                mensagemProspota = ("Proposta Recebida no servidor");
                System.out.println(mensagemProspota);
            } else {

                System.out.print("\n[Resposta] " + data);
                String[] array = data.split(";");
                System.out.println("\nNome: " + array[0] + " Preço: " + array[1] + " Caracteristica: " + array[2]);
                produto = new Produto(array[0], Double.parseDouble(array[1]), array[2]);

                this.produto = produto;
                mensagemProduto = ("Produto: " +this.produto.getNome()+ "      Valor: "+ this.produto.getPrecoInicial());
                System.out.println("Produto: " + this.produto.getNome());

            }
        }

    }

    public Produto receberProduto() throws IOException {
        DataInputStream in = new DataInputStream(sock.getInputStream());
        String data;
        Produto produto;

        while (true) {

            /* mensagem servidor */
            data = in.readUTF();

            System.out.print("\n[Resposta] " + data);
            String[] array = data.split(";");
            System.out.println("\nNome: " + array[0] + " Preço: " + array[1] + " Caracteristica: " + array[2]);
            produto = new Produto(array[0], Double.parseDouble(array[1]), array[2]);

            this.produto = produto;
             mensagemProduto = ("Produto: " +this.produto.getNome()+ "     Valor: "+ this.produto.getPrecoInicial());
            System.out.println("Produto: " + this.produto.getNome());

            return produto;
        }
    }

}
