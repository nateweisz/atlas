mod codec;
mod client;
mod server;
mod session;

use std::iter::once_with;
use actix_web::{App, get, HttpRequest, HttpResponse, HttpServer, post, Responder, web};
use actix_web::dev::WebService;
use actix_web::http::Error;

#[actix_web::main]
async fn main() -> std::io::Result<()>{
    HttpServer::new(|| {
        App::new()
            .service(hello)
            .service(echo)
            .route("/wss/", web::get().to(websocket))
    })
        .bind(("127.0.0.1", 8080))?
        .run()
        .await
}

async fn websocket(req: HttpRequest, steam: web::Payload) -> Result<HttpResponse, Error> {
    let re
}

#[get("/")]
async fn hello() -> impl Responder {
    HttpResponse::Ok().body("Hello World!")
}

#[post("/echo")]
async fn echo(req_body: String) -> impl Responder {
    HttpResponse::Ok().body(req_body)
}



