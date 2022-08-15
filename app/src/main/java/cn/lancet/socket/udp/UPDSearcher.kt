package cn.lancet.socket.udp

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.util.concurrent.CountDownLatch

/**
 * Created by Lancet at 2022/8/15 15:56
 */

private const val LISTEN_PORT = 30000

class UPDSearcher {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {

            val listener = listen()
            sendBroadcast()

            System.`in`.read()

            val devices = listener.getDevicesInfoAndClose()

            devices.forEach {
                println("Devices: ${it.toString()}")
            }

            println("UDPSearcher finished.")

        }

        @JvmStatic
        fun listen():Listener {
            println("UDPSearcher start listen.")
            val countDownLatch = CountDownLatch(1)
            val listener = Listener(LISTEN_PORT,countDownLatch)
            listener.start()
            countDownLatch.await()
            return listener
        }

        private fun sendBroadcast(){
            println("UDPSearcher started")

            val ds = DatagramSocket()

            val requestData = MessageCreator.buildWithPort(LISTEN_PORT)
            val requestDataBytes = requestData.toByteArray()

            val requestPacket = DatagramPacket(requestDataBytes, requestDataBytes.size)
                .apply{
                    address = InetAddress.getByName("255.255.255.255")
                    port = 20000
                }
            ds.send(requestPacket)
            ds.close()
            println("UPDSearcher sendBroadcast Finished")
        }


        class DeviceInfo(val port: Int, val ip: String, val sn: String) {
            override fun toString(): String {
                return "DeviceInfo(port=$port, ip='$ip', sn='$sn')"
            }
        }


        class Listener(private val listenPort: Int,private val countDownLatch: CountDownLatch):Thread() {

            private val devices = mutableListOf<DeviceInfo>()
            private var done = false
            private var ds:DatagramSocket?=null

            override fun run() {
                super.run()

                countDownLatch.countDown()
                ds = DatagramSocket(listenPort)

                try {
                    while(!done){
                        val buf = ByteArray(512)
                        val receivePack = DatagramPacket(buf, buf.size)
            //            接收
                        ds?.receive(receivePack)
            //            发送者的信息
                        val ip = receivePack.address.hostAddress
                        val port = receivePack.port
                        val dataLen = receivePack.length

                        val data = String(receivePack.data, 0, dataLen)
                        println("UPDSearcher receive from ip:${ip}   port: $port  data: $data")

                        val sn = MessageCreator.parseSN(data)
                        if (sn!=null){
                            val deviceInfo = DeviceInfo(port, ip, sn)
                            devices.add(deviceInfo)
                        }

                    }
                }catch (_: Exception) {

                }finally {
                    close()
                }
                println("UPDSearcher listen finished.")
            }

            private fun close(){
                ds?.close()
                ds = null
            }

            fun getDevicesInfoAndClose():MutableList<DeviceInfo>{
                done = true
                close()
                return devices
            }


        }

    }

}