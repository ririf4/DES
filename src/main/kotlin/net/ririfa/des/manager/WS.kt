package net.ririfa.des.manager

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.*
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.codec.http.HttpServerCodec
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler
import net.ririfa.des.Logger

class WS(
	private val port: Int,
	private var channel: Channel? = null,
	private var bossGroup: EventLoopGroup? = null,
	private var workerGroup: EventLoopGroup? = null
) {
	fun start() {
		bossGroup = NioEventLoopGroup(1)
		workerGroup = NioEventLoopGroup()

		val bootStrap = ServerBootstrap()
			.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel::class.java)
			.childHandler(object : ChannelInitializer<SocketChannel>() {
				override fun initChannel(ch: SocketChannel) {
					ch.pipeline().apply {

						addLast(HttpServerCodec())

						addLast(HttpObjectAggregator(524288))

						addLast(WebSocketServerProtocolHandler("/ws"))

						addLast(object : SimpleChannelInboundHandler<BinaryWebSocketFrame>() {
							override fun messageReceived(
								ctx: ChannelHandlerContext,
								frame: BinaryWebSocketFrame
							) {
								val byteBuf = frame.content()
								val bytes = ByteArray(byteBuf.readableBytes())
								byteBuf.readBytes(bytes)

								val jsonString = String(bytes)
								//TODO
							}

						})
					}
				}
			})

		channel = bootStrap.bind(port).sync().channel()
		Logger.info(TODO())
	}

	data class WSMessage(val type: String, val data: String)

	fun stop() {
		channel?.close()?.sync()
		bossGroup?.shutdownGracefully()
		workerGroup?.shutdownGracefully()

	}
}
