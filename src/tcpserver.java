import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
 
// 1. 本程式必須與 TcpClient.java 程式搭配執行，先執行本程式再執行 UdpClient。
// 2. 執行方法 : java TcpServer
// 3. 輸入
public class tcpserver {
    public static int port = 5500; // 連接埠
 
    public static void main(String[] args) throws Exception {
        System.out.println("[Server start]");
//        ServerSocket ss = new ServerSocket(port);     // 建立 TCP 伺服器。
//        while (true){                                // 不斷的接收處理輸入訊息。
//            Socket sc = ss.accept();                // 接收輸入訊息。當有人要跟你建立socket,就有accept動作
//            OutputStream os = sc.getOutputStream();    // 取得輸出串流。
//            Scanner scanner = new Scanner(System.in);  // 直接取得使用者的輸入字串
//            String st = scanner.next();
//            os.write(st.getBytes(StandardCharsets.UTF_8));// 送訊息到 Client 端。
//            System.out.printf("你輸入的是:" + st);          // 標準輸出
//            os.close();                                // 關閉輸出串流。
//            sc.close();                                // 關閉 TCP 伺服器。
//        }
        ServerSocket ss = new ServerSocket(port);     // 建立 TCP 伺服器。
        while(true){
            Socket sc = ss.accept();
            System.out.println("Connected");
            InputStream in = sc.getInputStream();      // 取得輸入訊息的串流
            StringBuffer buf = new StringBuffer();        // 建立讀取字串。
            try {
                while (true) {            // 不斷讀取。
                    int x = in.read();    // 讀取一個 byte。(read 傳回 -1 代表串流結束)
                    if (x==-1) break;    // x = -1 代表串流結束，讀取完畢，用 break 跳開。
                    byte b = (byte) x;    // 將 x 轉為 byte，放入變數 b.
                    buf.append((char) b);// 假設傳送ASCII字元都是 ASCII。
                }
                System.out.println("!!");
            } catch (Exception e) {
                in.close();                // 關閉輸入串流。
            }
            System.out.println(buf);                    // 印出接收到的訊息。
        }

    }
}