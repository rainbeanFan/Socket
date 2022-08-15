package cn.lancet.socket.socket

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintStream
import java.net.Inet4Address
import java.net.InetSocketAddress
import java.net.Socket

/**
 * Created by Lancet at 2022/8/15 13:10
 */
class Client {

    companion object{
        @JvmStatic
        fun main(args: Array<String>){
            val socket = Socket()
            socket.apply {
                soTimeout = 3000
                connect(InetSocketAddress(Inet4Address.getLocalHost(),2000),3000)
            }

            println("已发起服务器连接，并进入后续流程~")
            println("客户端信息："+socket.localAddress+" P:"+socket.localPort)
            println("服务端信息："+socket.inetAddress+" P:"+socket.port)

            try {
                send(socket)
            }catch (e:Exception){
                println("异常关闭")
            }

            socket.close()
            println("客户端已退出~")


        }

        @JvmStatic
        private fun send(client: Socket){
//            构建键盘输入流
            val inputStream = System.`in`
            val input = BufferedReader(InputStreamReader(inputStream))
//            得到Socket输出流，并转换为打印流
            val outputStream = client.getOutputStream()
            val socketPrintStream = PrintStream(outputStream)
//            得到Socket输入流，变转换为BufferedReader
            val clientInPut = client.getInputStream()
            val socketBufferedReader = BufferedReader(InputStreamReader(clientInPut))

            var flag = true
            do {
//            键盘读取一行
                val str = input.readLine()
//            发送到服务端
                socketPrintStream.println(str)
//            从服务端读取一行
                val echo = socketBufferedReader.readLine()
                if ("bye".equals(echo,true)) {
                    flag = false
                }else{
                    println(echo)
                }
            }while (flag)

//            资源释放
            socketPrintStream.close()
            socketBufferedReader.close()

        }

    }



}