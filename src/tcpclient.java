import java.net.*;
import java.io.*;
 
// 1. 本程式必須與 TcpServer.java 程式搭配執行，先執行 TcpServer 再執行本程式。
// 2. 本程式必須有一個參數，指定伺服器的 IP。
// 用法範例： java TcpClient 127.0.0.1
// 3. 接收
public class tcpclient {
    public static int port = 5500; // 設定傳送埠為 20。
 
    public static void main(String[] args) throws Exception {
        System.out.println("[Client start]");
        Socket client = new Socket("192.168.0.13", port);     // 根據 args[0] 的 TCP Socket.
        InputStream in = client.getInputStream();      // 取得輸入訊息的串流
        StringBuffer buf = new StringBuffer();        // 建立讀取字串。
        try {
            while (true) {            // 不斷讀取。
                int x = in.read();    // 讀取一個 byte。(read 傳回 -1 代表串流結束)
                if (x==-1) break;    // x = -1 代表串流結束，讀取完畢，用 break 跳開。
                byte b = (byte) x;    // 將 x 轉為 byte，放入變數 b.
                buf.append((char) b);// 假設傳送ASCII字元都是 ASCII。
            }
        } catch (Exception e) {
            in.close();                // 關閉輸入串流。
        }
        System.out.println(buf);                    // 印出接收到的訊息。
        client.close();                                // 關閉 TcpSocket.
    }
}