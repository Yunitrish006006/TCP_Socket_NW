import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ClientEntity {
    public int port;
    public String serverIP;



    public ClientEntity(int port,String serverIP) {
        this.port = port;
        this.serverIP = serverIP;
    }

    public String receive() throws IOException {
        Socket socket = new Socket(serverIP, port);     // 根據 args[0] 的 TCP Socket.
        InputStream in = socket.getInputStream();      // 取得輸入訊息的串流
        StringBuffer buffer = new StringBuffer();        // 建立讀取字串。
        try {
            while (true) {            // 不斷讀取。
                int x = in.read();    // 讀取一個 byte。(read 傳回 -1 代表串流結束)
                if (x==-1) break;    // x = -1 代表串流結束，讀取完畢，用 break 跳開。
                byte b = (byte) x;    // 將 x 轉為 byte，放入變數 b.
                buffer.append((char) b);// 假設傳送ASCII字元都是 ASCII。
            }
        } catch (Exception e) {
            in.close();                // 關閉輸入串流。
        }
        socket.close();
        return buffer.toString();
    }

    public static void main(String[] args) throws Exception {
        System.out.println("[Client start]");
        ClientEntity client = new ClientEntity(5555,"localhost");
        System.out.println("receive: " + client.receive());
    }
}
