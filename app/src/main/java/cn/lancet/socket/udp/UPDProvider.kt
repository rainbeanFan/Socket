package cn.lancet.socket.udp

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.util.UUID

/**
 * Created by Lancet at 2022/8/15 15:37
 */
class UPDProvider {


    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val sn = UUID.randomUUID().toString()
            val provider = Provider(sn)
            provider.start()

            System.`in`.read()
            provider.exit()

//            println("UDPProvider Started.")
////            指定一个端口进行数据接收
//            val ds = DatagramSocket(20000)
//
//            val buf = ByteArray(512)
//            var receivePack = DatagramPacket(buf, buf.size)
////            接收
//            ds.receive(receivePack)
////            发送者的信息
//            val ip = receivePack.address.hostAddress
//            val port = receivePack.port
//            val dataLen = receivePack.length
//
//            val data = String(receivePack.data, 0, dataLen)
//            println("UDPProvider receive from ip:${ip}   port: ${port}  data: ${data}")
////            根据收到的信息构建一份回送信息
//            val responseData = "Receive data with len:$dataLen"
//            val responseDataBytes = responseData.toByteArray()
//
//            val responsePacket = DatagramPacket(
//                responseDataBytes,
//                responseDataBytes.size, receivePack.address, receivePack.port
//            )
//
//            ds.send(responsePacket)
//
//            println("UPDProvider Finished")
//            ds.close()

        }

        internal class Provider(private val sn: String) : Thread() {

            var done = false
            private var ds: DatagramSocket? = null

            override fun run() {
                super.run()

                println("UDPProvider started")

                try {
                    ds = DatagramSocket(20000)

                    while (!done) {
                        val buf = ByteArray(1024)
                        val receivePack = DatagramPacket(buf, buf.size)

                        ds?.receive(receivePack)
                        //            发送者的信息
                        val ip = receivePack.address.hostAddress
                        val port = receivePack.port
                        val dataLen = receivePack.length

                        val data = String(receivePack.data, 0, dataLen)
                        println("UDPProvider receive from ip:${ip}   port: $port  data: $data")

                        val responsePort = MessageCreator.parsePort(data)
                        if (responsePort != -1) {
                            //            根据收到的信息构建一份回送信息
                            val responseData = MessageCreator.buildWithSN(sn)
                            val responseDataBytes = responseData.toByteArray()

                            val responsePacket = DatagramPacket(
                                responseDataBytes,
                                responseDataBytes.size, receivePack.address, responsePort
                            )

                            ds?.send(responsePacket)
                        }
                    }

                } catch (e: Exception) {

                } finally {
                    ds?.close()
                }


            }

            fun exit() {
                done = true
                ds?.close()
                ds = null
            }

        }

    }

}