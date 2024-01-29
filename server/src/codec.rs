use std::char::DecodeUtf16Error;
#[allow(dead_code)]
use std::io;
use actix_codec::{Decoder, Encoder};
use actix_web::web::{BufMut, BytesMut, Json};
use byteorder::{BigEndian, ByteOrder};
use serde::{Deserialize, Serialize};

// Client requests
#[derive(Serialize, Deserialize, Debug)]
#[rtype(result = "()")]
#[serde(tag = "cmd", content = "data")]
pub enum Request {
    Authenticate(String), // client & server secret
}

#[derive(Serialize, Deserialize, Debug)]
#[rtype(result = "()")]
#[serde(tag = "cmd", content = "data")]
pub enum Response {
    AuthenticationStatus(bool),
}

// Client -> Server (Codec)
pub struct Codec;

impl Decoder for Codec {
    type Item = Request;
    type Error = io::Error;

    fn decode(&mut self, src: &mut BytesMut) -> Result<Option<Self::Item>, Self::Error> {
        let size = {
            if src.len() < 2 {
                return Ok(None)
            }
            BigEndian::read_u16(src.as_ref()) as usize
        };
        
        if src.len() >= size + 2 {
            let _ = src.split_to(2);
            let buf = src.split_to(size);
            Ok(Some(serde_json::from_slice::<Request>(&buf)?))
        } else {  
            Ok(None)
        }
    }
}

impl Encoder<Response> for Codec {
    type Error = io::Error;

    fn encode(&mut self, item: Response, dst: &mut BytesMut) -> Result<(), Self::Error> {
        let msg = serde_json::to_string(&item).unwrap();
        let msg_ref: &[u8] = msg.as_ref();
        
        dst.reserve(msg_ref.len() + 2);
        dst.put_u16(msg_ref.len() as u16);
        dst.put(msg_ref);
        
        Ok(())
    }
}