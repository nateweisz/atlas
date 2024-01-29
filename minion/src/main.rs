use futures_util::{SinkExt, StreamExt};
use tokio_tungstenite::connect_async;
use tokio_tungstenite::tungstenite::Message;

#[tokio::main]
async fn main() {
    let url: &str = "wss://localhost:9090";
    let secret_key = env!("SECRET");

    println!("Connecting to {}", url);
    let (ws_stream, _) = connect_async(url).await.expect("Failed to connect to websocket");
    println!("Connected!!!");
    
    let (mut write, mut read) = ws_stream.split();
    
    let msg = Message::Text("Aloha echo server".into());
    
    write.send(msg).await.expect("Failed to send message");
    
    if let Some(message) = read.next().await {
        let message = message.expect("Couldn't read message");
        println!("Received message: {}", message)
    }
}