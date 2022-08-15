package cn.lancet.socket.socket

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintStream
import java.net.ServerSocket
import java.net.Socket

/**
 * Created by Lancet at 2022/8/15 13:29
 */
class Server {

    companion object{

        @JvmStatic
        fun main(args: Array<String>) {
            val server = ServerSocket(2000)

            println("服务器准备就绪~")
            println("服务器信息："+server.inetAddress+" P:"+server.localPort)

//            等待客户端连接
            val client = server.accept()
//            客户端构建异步线程并启动
            ClientHandler(client).start()

        }


        internal class ClientHandler(private val socket: Socket):Thread() {

            private var flag = true

            override fun run() {
                super.run()
                println("新客户端信息：${socket.inetAddress} P: ${socket.port}")
                try {
//                    用于数据输出，服务器回送数据使用
                    val printStream = PrintStream(socket.getOutputStream())
//                    得到输入流
                    val socketInput = BufferedReader(InputStreamReader(socket.getInputStream()))

                    do {
                        val str = socketInput.readLine()
                        if ("bye".equals(str,true)){
                            flag = false
                            printStream.println("bye")
                        }else{
                            println(str)
                            printStream.println("回送：${str.length}")
                        }

                    }while (flag)

                    printStream.close()
                    socketInput.close()

                }catch (e:Exception){
                    println("连接异常断开")
                }finally {
                    try {
                        socket.close()
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }

                println("客户端已关闭：${socket.inetAddress} P: ${socket.port}")


            }


        }

    }



}