package cn.lancet.socket.udp

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

/**
 * Created by Lancet at 2022/8/15 15:56
 */

private const val LISTEN_PORT = 30000

class UPDSearcher {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            println("UPDSearcher Started.")
//            系统自动分配端口
            val ds = DatagramSocket()

//            构建一份发送数据
            val requestData = "Hello World"
            val requestDataBytes = requestData.toByteArray()

            val requestPacket = DatagramPacket(requestDataBytes, requestDataBytes.size)
                .apply {
                    address = InetAddress.getLocalHost()
                    port = 20000
                }


            ds.send(requestPacket)

            val buf = ByteArray(512)
            var receivePack = DatagramPacket(buf,buf.size)
//            接收
            ds.receive(receivePack)
//            发送者的信息
            val ip = receivePack.address.hostAddress
            val port = receivePack.port
            val dataLen = receivePack.length

            val data = String(receivePack.data,0,dataLen)
            println("UPDSearcher receive from ip:${ip}   port: ${port}  data: ${data}")


            println("UPDSearcher Finished")
            ds.close()

        }

        @JvmStatic
        fun listen() {

        }

        fun sendBroadcast(){

        }

    }

}