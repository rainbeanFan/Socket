package cn.lancet.socket.udp

/**
 * Created by Lancet at 2022/8/15 16:20
 */

private const val SN_HEADER = "收到暗号，我是（SN）"
private const val PORT_HEADER = "这是暗号，请回电端口（Port）"


class MessageCreator {

    companion object {
        @JvmStatic
        fun buildWithPort(port: Int) = PORT_HEADER+port

        @JvmStatic
        fun parsePort(data:String):Int{
            if (data.startsWith(PORT_HEADER)){
                return Integer.parseInt(data.substring(PORT_HEADER.length))
            }
            return -1
        }

        @JvmStatic
        fun buildWithSN(sn:String) = SN_HEADER + sn

        @JvmStatic
        fun parseSN(data:String):String? {
            if (data.startsWith(SN_HEADER)){
                return data.substring(SN_HEADER.length)
            }
            return null
        }

    }


}