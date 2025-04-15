import "dotenv/config"
import express from 'express'; 
import connect from "./db.js";
import router from "./router.js";
import runConsumer from "./consumer.js";
import { Server } from "socket.io";
import {createServer} from "http"
import pubsub from "./pubsub.js";
const app = express();
connect();
runConsumer().catch((e)=>{
    console.log(e);
})
const httpServer = createServer(app)
const io = new Server(httpServer,{
cors:{
    origin:"*",
    methods:["GET","POST"],
    allowedHeaders:["Content-Type"],
    credentials:true,
    transports:["websocket","polling"]}
})
io.on("connection",(socket)=>{
    console.log("Socket connection",socket.id)
    socket.on("join",(room_id)=>{
        console.log("join",room_id)
        socket.join(room_id)
    })
  

    pubsub.on("window_in",(window)=>{
        console.log("window_in",window)
        io.to(window.shortUrl).emit("update",{clicks:window.totalClicks})
    })
})
app.use("/api/v1/analytics",router)
httpServer.listen(8000,()=>{
    console.log(
        "Server running fucking great"
    )
})