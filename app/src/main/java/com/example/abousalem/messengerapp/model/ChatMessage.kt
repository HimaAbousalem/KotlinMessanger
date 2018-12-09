package com.example.abousalem.messengerapp.model

class ChatMessage (var id: String, val text: String, val fromId: String, val toId: String, val timestamp: Long){
    constructor(): this("","","","",-1)
}